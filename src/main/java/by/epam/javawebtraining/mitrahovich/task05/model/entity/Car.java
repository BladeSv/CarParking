package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class Car implements Runnable {
	private static Logger log;

	private String name;
	private Thread thread;
	private long stay;
	private long wait;
	private List<CarParking> carParkingList;
	private Lock carParkingLock;

	static {
		log = Logger.getRootLogger();

	}

	public Car(String name, List<CarParking> carParkingList, long wait, long stay) {
		log.trace("create car" + name + " wait time-" + wait + " stay time-" + stay);
		thread = new Thread(this);
		thread.start();
		this.name = name;
		this.carParkingList = carParkingList;
		this.wait = wait;
		this.stay = stay;
		carParkingLock = new ReentrantLock();
	}

	public long getStay() {
		return stay;
	}

	public void setStay(long stay) {
		this.stay = stay;
	}

	public long getWait() {
		return wait;
	}

	public void setWait(long wait) {
		this.wait = wait;
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

			if (carParkingList.get(0).driveInto(this, stay, TimeUnit.MILLISECONDS)) {

				log.trace("car " + name + " START sleep");
				TimeUnit.MILLISECONDS.sleep(wait);
				log.trace("car " + name + " STOP sleep");

				// carParkingList.get(0).changeRandomParkingPlace(this);

				carParkingList.get(0).driveOut(this);

				log.trace("car " + name + " leave car parking");
			}

			else {

				log.trace("car " + name + " leave QUEUE");
			}
		} catch (

		InterruptedException e) {
			log.trace("car " + name + " cant parking" + e.getStackTrace());

		}

	}

	@Override
	public String toString() {
		return "Car: " + name + ", thread: " + thread;
	}

}
