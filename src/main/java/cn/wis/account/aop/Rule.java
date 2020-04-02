package cn.wis.account.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cn.wis.account.model.enums.RuleListEnum;

@Target(METHOD)
@Retention(RUNTIME)
public @interface Rule {

	RuleListEnum value() default RuleListEnum.WHITE;

}
