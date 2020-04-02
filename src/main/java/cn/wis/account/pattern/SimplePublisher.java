package cn.wis.account.pattern;

import java.util.ArrayList;

public class SimplePublisher<T> implements Publisher<T> {

	private ArrayList<Subscriber<T>> subscribers;

	public SimplePublisher() {
		subscribers = new ArrayList<Subscriber<T>>();
	}

	@Override
	public void publish(T t) {
		subscribers.parallelStream().forEach(subscriber -> subscriber.accept(t));
	}

	public synchronized boolean addSubscriber(Subscriber<T> subscriber) {
		return !subscribers.contains(subscriber) && subscribers.add(subscriber);
	}

}
