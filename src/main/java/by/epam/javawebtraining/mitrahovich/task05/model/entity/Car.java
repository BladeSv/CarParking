package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Car implements Runnable {
	private String name;
	private Thread thread;
	private long stay;
	private long wait;
	private CarParking carParking;

	private static Logger log;

	static {
		log = Logger.getRootLogger();
	}

	public Car(String name, CarParking carParking, long wait, long stay) {
		thread = new Thread(this);
		thread.start();
		this.name = name;
		this.carParking = carParking;
		this.wait = wait;
		this.stay = stay;

		log.trace("create car" + name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void run() {
		log.trace("car " + name + " stay in queue");
		try {
			if (carParking.getDriveIntoLock().tryLock(stay, TimeUnit.MILLISECONDS)) {
				carParking.driveInto(this);
				log.trace("car " + name + " has parking");
				carParking.getDriveIntoLock().unlock();
			}
		} catch (InterruptedException e) {
			log.trace("car " + name + " cant parking" + e.getStackTrace());

		}
		log.trace("car wait into parking place" + wait + "mSec");
		try {
			thread.sleep(wait);
		} catch (InterruptedException e) {
			log.trace("car " + name + "cant sleep" + e.getStackTrace());
			e.printStackTrace();
		}
		if (carParking.getDriveOutLock().tryLock()) {
			carParking.driveOut(this);
			log.trace("car " + name + "leave car parking");
		}

	}

	@Override
	public String toString() {
		return "Car: " + name + ", thread: " + thread;
	}

}
