package cn.wis.account.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import cn.hutool.core.collection.ConcurrentHashSet;

@Component
public class AuthorityRule {

	private Map<String, Set<String>> rules;

	public AuthorityRule() {
		rules = new HashMap<String, Set<String>>();
	}

	public boolean bind(String methodKey, String roleId) {
		return rules.containsKey(methodKey) && rules.get(methodKey).add(roleId);
	}

	public boolean bindAll(String methodKey, Collection<String> roleIds) {
		return rules.containsKey(methodKey) && rules.get(methodKey).addAll(roleIds);
	}

	public boolean remove(String methodKey, String roleId) {
		return rules.containsKey(methodKey) && rules.get(methodKey).remove(roleId);
	}

	public boolean removeAll(String methodKey, Collection<String> roleIds) {
		return rules.containsKey(methodKey) && rules.get(methodKey).removeAll(roleIds);
	}

	public boolean isBound(String methodKey, String roleId) {
		return rules.containsKey(methodKey) && rules.get(methodKey).contains(roleId);
	}

	void addRule(String methodKey) {
		if (methodKey == null || rules.containsKey(methodKey)) {
			return;
		}
		rules.put(methodKey, new ConcurrentHashSet<String>());
	}

}
