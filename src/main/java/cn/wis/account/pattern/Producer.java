package cn.wis.account.pattern;

public interface Producer<T> extends Runnable {

	@Override
	void run();

}
