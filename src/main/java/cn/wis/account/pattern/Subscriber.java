package cn.wis.account.pattern;

import java.util.function.Consumer;

public interface Subscriber<T> extends Consumer<T> {

	@Override
	void accept(T t);

}
