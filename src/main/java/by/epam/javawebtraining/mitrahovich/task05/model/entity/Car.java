package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Car implements Runnable {
	private String name;
	private Thread thread;
	private long stay;
	private long wait;
	private CarParking carParking;
	private static Random rd;

	private static Logger log;

	static {
		log = Logger.getRootLogger();
		rd = new Random();
	}

	public Car(String name, CarParking carParking, long wait, long stay) {
		log.trace("create car" + name);
		thread = new Thread(this);
		thread.start();
		this.name = name;
		this.carParking = carParking;
		this.wait = wait;
		this.stay = stay;

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
				log.trace("car " + name + " start parking");
				carParking.driveInto(this);

				log.trace("car" + name + " wait into parking place waiting " + wait + "mSec");
				try {
					TimeUnit.MILLISECONDS.sleep(wait);

				} catch (InterruptedException e) {
					log.trace("car " + name + "cant sleep" + e.getStackTrace());
					e.printStackTrace();
				}
				while (!carParking.getDriveOutLock().tryLock()) {

				}
				carParking.driveOut(this);
				log.trace("car " + name + " leave car parking");

			} else {

				log.trace("car " + name + "leave queue");
			}
		} catch (InterruptedException e) {
			log.trace("car " + name + " cant parking" + e.getStackTrace());

		}

	}

	@Override
	public String toString() {
		return "Car: " + name + ", thread: " + thread;
	}

}
