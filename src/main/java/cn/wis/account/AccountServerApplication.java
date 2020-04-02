package cn.wis.account;

import java.util.ArrayList;
import java.util.TimeZone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import cn.wis.account.aop.AuthorityAspect;
import cn.wis.account.aop.TokenAspect;

@EnableAsync
@EnableEurekaClient
@SpringBootApplication
@MapperScan("cn.wis.account.mapper")
public class AccountServerApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		SpringApplication.run(AccountServerApplication.class, args);
	}

	@Bean
	public HttpMessageConverters fastJsonMessageConverters() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.PrettyFormat);
		config.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		converter.setFastJsonConfig(config);
		ArrayList<MediaType> medias = new ArrayList<MediaType>();
		medias.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(medias);
		return new HttpMessageConverters(converter);
	}

	@Bean
	public TokenAspect setTokenAspect() {
		return new TokenAspect();
	}

	@Bean
	public AuthorityAspect setAuthorityAspect() {
		return new AuthorityAspect();
	}

}
