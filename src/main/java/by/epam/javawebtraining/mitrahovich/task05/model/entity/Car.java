package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class Car implements Runnable {
	private String name;
	private Thread thread;
	private long stay;
	private long wait;
	private CarParking carParking;
	private Lock carParkingLock;
	private static Logger log;

	static {
		log = Logger.getRootLogger();

	}

	public Car(String name, CarParking carParking, long wait, long stay) {
		log.trace("create car" + name + " wait time-" + wait + " stay time-" + stay);
		thread = new Thread(this);
		thread.start();
		this.name = name;
		this.carParking = carParking;
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

	// private boolean tryParking(CarParking currentCarParking) {
	//
	// boolean check = false;
	// final long deadline = System.nanoTime() +
	// TimeUnit.MICROSECONDS.toNanos(stay);
	// while (deadline - System.nanoTime() > 0 && (!check)) {
	// check = currentCarParking.driveInto(this);
	//
	// }
	// return check;
	// }

	public void run() {
		log.trace("car " + name + " stay in queue");
		// carParking.driveInto(this, stay, TimeUnit.MILLISECONDS);

		try {

			// log.trace("car " + name + " trylock into LOCK car parking -result-" + cheak);
			if (carParkingLock.tryLock(stay, TimeUnit.MILLISECONDS)) {
				carParking.driveInto(this);
				carParkingLock.unlock();

				log.trace("car " + name + " START sleep");
				TimeUnit.MILLISECONDS.sleep(wait);
				log.trace("car " + name + " STOP sleep");

				while (!carParking.changeRandomParkingPlace(this)) {
				}

				while (!carParking.driveOut(this)) {

				}

				log.trace("car " + name + " leave car parking");
			}
			// slog.trace("car " + name + " start parking");

			/*
			 * log.trace("car" + name + " wait into parking place waiting " + wait +
			 * "mSec"); try { TimeUnit.MILLISECONDS.sleep(wait);
			 * 
			 * } catch (InterruptedException e) { log.trace("car " + name + "cant sleep" +
			 * e.getStackTrace()); e.printStackTrace(); }
			 */
			// if (!carParking.getDriveOutLock().tryLock()) {
			//

			// }

			else {

				log.trace("car " + name + " leave queue");
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
