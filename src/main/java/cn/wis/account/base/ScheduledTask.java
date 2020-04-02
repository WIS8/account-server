package cn.wis.account.base;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ScheduledTask extends Thread {

	private volatile long interval;

	public ScheduledTask(String name, long interval) {
		this(name, MIN_PRIORITY, interval);
	}

	public ScheduledTask(String name, int priority, long interval) {
		setName(name);
		setPriority(priority);
		setInterval(interval);
	}

	public final long getInterval() {
		return interval;
	}

	public final void setInterval(long interval) {
		this.interval = (interval < 0) ? this.interval : interval;
	}

	@Override
	public final void run() {
		if (Thread.currentThread() != this) {
			throw new IllegalStateException(getName() + " has already started");
		}
		initiate();
		while (true) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				continue;
			}
			try {
				schedule();
			} catch (Exception e) {
				log.warn("Fail to schedule {} for: {}", getName(), e);
				break;
			}
		}
		complete();
	}

	protected void initiate() {
		log.info("Start {} successfully", getName());
	}

	protected void schedule() {
		log.info("Ready for schedule {}", getName());
	}

	protected void complete() {
		log.info("Cease {} successfully", getName());
	}

}
