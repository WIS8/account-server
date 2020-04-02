package cn.wis.account.component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.wis.account.aop.Rule;
import cn.wis.account.mapper.AuthorityMapper;
import cn.wis.account.model.dto.ApiConfirmResult;
import cn.wis.account.model.dto.ApiDto;
import cn.wis.account.model.dto.ApplicationDto;
import cn.wis.account.model.dto.ProviderDto;
import cn.wis.account.model.entity.Server;
import cn.wis.account.model.enums.RuleListEnum;
import cn.wis.account.model.request.feign.ApiConfirmRequest;
import cn.wis.account.model.request.feign.ApiMap;
import cn.wis.account.model.request.feign.ApplicationConfirmRequest;
import cn.wis.account.model.request.feign.ProviderConfirmRequest;
import cn.wis.account.model.result.Result;
import cn.wis.account.model.table.Authority;
import cn.wis.account.service.ApiService;
import cn.wis.account.service.ApplicationService;
import cn.wis.account.service.ParameterService;
import cn.wis.account.service.ProviderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccountInitializer implements ApplicationRunner {

	@Value("${spring.application.name}")
	private String appName;

	@Value("${server.plugin.name}")
	private String pluginName;

	@Value("${server.port}")
	private String port;

	@Resource
	private ApplicationContext context;

	@Resource
	private AuthorityMapper authMapper;

	@Resource
	private ApplicationService appService;

	@Resource
	private ProviderService providerService;

	@Resource
	private ParameterService paramService;

	@Resource
	private ApiService apiService;

	@Resource
	private AuthorityRule authRule;

	@Resource
	private ParameterPublisher paramPublisher;

	private Server server;

	public Server getServer() {
		return server;
	}

	public String makeMethodKey(Method method) {
		return makeMethodKey(method.getDeclaringClass().getName(), method.getName());
	}

	public String getUrl(HttpServletRequest http) {
		try {
			return http.getRemoteAddr().replace(":", "");
		} catch (Exception e) {
			return server.getUrl();
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			server = new Server();
			initialize();
		} catch (Exception e) {
			log.error("Cannot initialize server for: {}\n"
					+ "System exit with (status = -1)", e);
			System.exit(-1);
		}
	}

	private void initialize() {
		doFirstWhileIntialize();
		registerLocalProvider();
		synchronizeRulesOfApi();
		fetchAndDispatchParam();
	}

	private void doFirstWhileIntialize() {
		server.setUrl(makeUrl(checkLocalIpv4Address(), port));
		ApplicationDto dto = appService.confirm(new ApplicationConfirmRequest(appName, pluginName));
		server.setAppId(dto.getId());
		log.info("Confirm app [{}] successfully", appName);
		if (dto.getPluginName().equals(pluginName) == false) {
			log.info("New plugin-name [{}] found", dto.getPluginName());
		}
	}

	private void registerLocalProvider() {
		ProviderDto dto = providerService.confirm(new ProviderConfirmRequest(server.getUrl(), server.getAppId()));
		server.setProviderId(dto.getId());
		server.setIdentifier(dto.getIdentifier());
		log.info("Confirm url [{}] successfully", server.getUrl());
	}

	private String checkLocalIpv4Address() {
		Set<String> addresses = NetUtil.localIpv4s();
		if (CollectionUtil.isEmpty(addresses)) {
			throw new RuntimeException("Cannot find any Ipv4 address from the server");
		}
		addresses = addresses.parallelStream()
				.filter(address -> address.endsWith(".1") == false)
				.collect(Collectors.toSet());
		if (addresses.size() == 1) {
			return addresses.stream().findFirst().get();
		} else {
			throw new RuntimeException("Incorrect count of useful Ipv4 address on server: " + addresses.size());
		}
	}

	private String makeUrl(String ipv4, String port) {
		return "http://" + ipv4 + ":" + port;
	}

	private void synchronizeRulesOfApi() {
		List<ApiDto> dtos = parseApiAndAddRule();
		ApiConfirmResult result = apiService.confirm(new ApiConfirmRequest(dtos, server.getAppId()));
		log.info("Confirm api successfully with create[{}], update[{}], delete[{}]",
				result.getCreateNumber(), result.getUpdateNumber(), result.getDeleteNumber());
		List<Authority> authorities = authMapper.selectAllByApis(result
				.getApis().stream().map(ApiMap::getId).collect(Collectors.toList()));
		if (CollectionUtil.isEmpty(authorities)) {
			log.info("There isn't any authority rule in DB yet");
		} else {
			result.getApis().stream().forEach(api -> authRule.bindAll(api.getAppellation(),
					authorities.stream().filter(authority -> authority.getApiId().equals(api.getId()))
					.map(Authority::getRoleId).collect(Collectors.toList())));
			log.info("Apply [{}] authorities into rule from DB", authorities.size());
		}
	}

	private List<ApiDto> parseApiAndAddRule() {
		String apiPrefix = compress(context.getEnvironment().getProperty("server.servlet.context-path"));
		List<ApiDto> dtos = new ArrayList<ApiDto>();
		for (String controller : context.getBeanNamesForAnnotation(Controller.class)) {
			Class<?> klass = context.getType(controller);
			if (ofApiPackage(klass)) {
				String api = apiPrefix + compress(getApi(klass));
				for (Method method : klass.getDeclaredMethods()) {
					if (isAnApiMethod(method)) {
						ApiDto dto = new ApiDto();
						dto.setRouter(api + compress(getApi(method)));
						dto.setAppellation(makeMethodKey(klass.getName(), method.getName()));
						Rule rule = method.getAnnotation(Rule.class);
						if (rule != null) {
							authRule.addRule(dto.getAppellation());
							dto.setAccessRule(rule.value().getCode());
						} else {
							dto.setAccessRule(RuleListEnum.BLANK.getCode());
						}
						dtos.add(dto);
					}
				}
			}
		}
		return dtos;
	}

	private boolean ofApiPackage(Class<?> klass) {
		return klass.getName().startsWith("cn.wis.account.controller");
	}

	private boolean isAnApiMethod(Method method) {
		return Modifier.isPublic(method.getModifiers())
				&& Modifier.isStatic(method.getModifiers()) == false
				&& method.getName().equals("newInstance") == false
				&& method.getReturnType().isAssignableFrom(Result.class);
	}

	private String compress(String str) {
		return StrUtil.isBlank(str) ? "" : str.trim();
	}

	private String getApi(AnnotatedElement element) {
		RequestMapping a1 = element.getAnnotation(RequestMapping.class);
		if (a1 != null) {
			return makeApi(a1.path());
		}
		PostMapping a2 = element.getAnnotation(PostMapping.class);
		if (a2 != null) {
			return makeApi(a2.path());
		}
		GetMapping a3 = element.getAnnotation(GetMapping.class);
		if (a3 != null) {
			return makeApi(a3.path());
		}
		PutMapping a4 = element.getAnnotation(PutMapping.class);
		if (a4 != null) {
			return makeApi(a4.path());
		}
		DeleteMapping a5 = element.getAnnotation(DeleteMapping.class);
		if (a5 != null) {
			return makeApi(a5.path());
		}
		PatchMapping a6 = element.getAnnotation(PatchMapping.class);
		if (a6 != null) {
			return makeApi(a6.path());
		}
		return "";
	}

	private String makeApi(String[] path) {
		if (path == null || path.length == 0) {
			return "";
		}
		if (path.length == 1) {
			return path[0];
		}
		StringBuilder builder = new StringBuilder('(');
		builder.append(path[0]);
		for (int i = 1; i < path.length; i++) {
			builder.append('|').append(path[i]);
		}
		return builder.append(')').toString();
	}

	private String makeMethodKey(String className, String methodName) {
		StringBuilder builder = new StringBuilder(className);
		int index = builder.indexOf("$");
		if (index != -1) {
			builder.delete(index, builder.length());
		}
		return builder.append('.').append(methodName).toString();
	}

	private void fetchAndDispatchParam() {
		paramPublisher.publish(paramService.confirm(server.getProviderId()));
		log.info("Publish parameter from DB successfully");
	}

}
