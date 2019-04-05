package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class Car implements Runnable {
	private static Logger log;
	private static Random rd;

	private String name;

	private long stay;
	private long wait;
	private List<CarParking> carParkingList;

	private Thread thread;

	static {
		log = Logger.getRootLogger();
		rd = new Random();
	}

	public Car(String name, List<CarParking> carParkingList, long wait, long stay) {

		log.debug("[Car]-" + name + "-[CREAT]-[Stay]-" + stay + "-[Wait]-" + wait + " [PARKING LIST SIZE]-"
				+ carParkingList.size());

		thread = new Thread(this);
		thread.start();
		this.name = name;
		this.carParkingList = carParkingList;
		this.wait = wait;
		this.stay = stay;

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

		boolean check = false;
		while (!check) {

			try {
				CarParking tempCarParking = carParkingList.get(rd.nextInt(carParkingList.size()));
				log.debug("[Car]-" + name + "-[STAY]-stay-[QUEUE]-[Parking]-" + tempCarParking.getName());
				// check = ;
				if (tempCarParking.driveInto(this, stay, TimeUnit.MILLISECONDS)) {

					log.trace("car " + name + " START sleep");
					TimeUnit.MILLISECONDS.sleep(wait);

					log.debug("[Car]-" + name + "-[!!!!!!SLEEP]-[STOP]-[Parking]-" + tempCarParking.getName());

					// carParkingList.get(0).changeRandomParkingPlace(this);

					tempCarParking.driveOut(this);

					log.trace("car " + name + " leave car parking");
					check = true;
				}

				else {
					log.debug("!!!!!!!!!![Car]-" + name + "-[Leave]-[QUEUE]-[Parking]-" + tempCarParking.getName());
					check = true;

				}
			} catch (

			InterruptedException e) {
				log.trace("car " + name + " cant parking" + e.getStackTrace());

			}
		}
	}

	@Override
	public String toString() {
		return "Car: " + name + ", thread: " + thread;
	}

}
