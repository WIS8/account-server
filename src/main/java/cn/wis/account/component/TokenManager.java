package cn.wis.account.component;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.wis.account.config.Constant;
import cn.wis.account.model.entity.Token;
import cn.wis.account.model.result.ResultEnum;
import cn.wis.account.model.result.ServiceException;
import cn.wis.account.model.vo.MemberVo;
import cn.wis.account.util.TimeHelper;
import cn.wis.account.util.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenManager {

	private volatile int maximum;

	private volatile long duration;

	private volatile Boolean clearing;

	private ConcurrentHashMap<String, Token> tokens;

	@Autowired
	public TokenManager(ParameterPublisher publisher) {
		publisher.addSubscriber(this::setParameters);
		maximum = Constant.DEFAULT_MAX_COUNT_OF_TOKEN;
		duration = Constant.DEFAULT_DURATION_OF_TOKEN;
		tokens = new ConcurrentHashMap<String, Token>();
	}

	public Token createToken(MemberVo memberVo) {
		if (isFull()) {
			clear();
			throw new ServiceException(ResultEnum.MAX_LOGIN_COUNT);
		}
		Token token = add(Token.getInstance(memberVo));
		if (token == null) {
			throw new ServiceException(ResultEnum.UNKNOWN);
		}
		if (token.getCookie() == null) {
			token.setCookie(cas(token));
		} else {
			updateCookie(token);
		}
		return token;
	}

	public boolean removeToken(String memberId) {
		Token token = tokens.remove(memberId);
		return token != null
				&& StrUtil.isNotEmpty(token.getCookie())
				&& tokens.remove(token.getCookie()) != null;
	}

	public int getOnlineNumber() {
		return tokens.size() >> 1;
	}

	public Token updateToken(String cookie) {
		return updateCookie(checkAndGetToken(cookie));
	}

	public void updateTime(String cookie) {
		checkAndGetToken(cookie).setUpdateTime(System.currentTimeMillis());
	}

	public Token getToken(String cookie) {
		return checkAndGetToken(cookie);
	}

	public Token getTokenByMemberId(String memberId) {
		return tokens.get(memberId);
	}

	public boolean contains(String cookie) {
		return tokens.containsKey(cookie);
	}

	private void setParameters(Map<String, String> map) {
		try {
			int max = Integer.parseInt(map.get("token-manage-number"));
			long temp = Long.parseLong(map.get("token-manage-duration"));
			temp = TimeHelper.millis(temp, TimeUnit.MINUTE);
			duration = (temp < Constant.MINIMUM_DURATION_OF_TOKEN)
					? Constant.DEFAULT_DURATION_OF_TOKEN : temp;
			maximum = (max > 0) ? max : maximum;
			log.info("Update duration({}m) and maximum({}) of TokenManager",
					TimeUnit.MINUTE.convert(duration), maximum);
		} catch (Exception e) {
			log.warn("Fail to parse arguments of TokenManager for: {}", e);
		}
	}

	private boolean isFull() {
		return (maximum << 1) <= tokens.size();
	}

	private Token add(Token token) {
		Token temp = tokens.put(token.getMemberVo().getId(), token);
		if (temp == null) {
			return token;
		}
		if (StrUtil.isEmpty(temp.getCookie())) {
			return null;
		}
		tokens.put(token.getMemberVo().getId(), temp);
		return temp;
	}

	private String cas(Token token) {
		String cookie = RandomUtil.randomString(Constant.COOKIE_SIZE);
		token = tokens.put(cookie, token);
		if (token == null) {
			return cookie;
		}
		tokens.put(cookie, token);
		throw new ServiceException(ResultEnum.FAILURE);
	}

	private Token checkAndGetToken(String cookie) {
		Token token = tokens.get(cookie);
		if (token == null) {
			throw new ServiceException(ResultEnum.INVALID_COOKIE);
		}
		if (expire(token, System.currentTimeMillis())) {
			synchronized (token) {
				tokens.remove(token.getCookie());
				tokens.remove(token.getMemberVo().getId());
			}
			throw new ServiceException(ResultEnum.EXPIRED_COOKIE);
		}
		return token;
	}

	private boolean expire(Token token, long current) {
		return token.getUpdateTime() + duration <= current;
	}

	private Token updateCookie(Token token) {
		String temp = token.getCookie();
		synchronized (token) {
			if (temp.equals(token.getCookie())) {
				temp = cas(token);
				tokens.remove(token.getCookie());
				token.updateCookie(temp);
			}
		}
		return token;
	}

	private void clear() {
		if (clearing) {
			return;
		}
		synchronized (clearing) {
			if (clearing || isFull() == false) {
				return;
			}
			clearing = true;
		}
		long current = System.currentTimeMillis();
		Iterator<Entry<String, Token>> it;
		for (it = tokens.entrySet().iterator(); it.hasNext(); ) {
			if (expire(it.next().getValue(), current)) {
				it.remove();
			}
		}
		clearing = false;
	}

}
