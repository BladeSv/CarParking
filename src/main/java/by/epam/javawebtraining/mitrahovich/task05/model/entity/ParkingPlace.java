package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class ParkingPlace {

	private Car car;

	private final int numberPlace;

	private Lock parkingPlaceLock;

	private static Logger log;

	static {
		log = Logger.getRootLogger();
	}

	public ParkingPlace(int numberPlace) {
		this.numberPlace = numberPlace;
		this.parkingPlaceLock = new ReentrantLock();
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public int getNumberPlace() {
		return numberPlace;
	}

	public Lock getParkingPlaceLock() {
		return parkingPlaceLock;
	}

	public boolean park(Car car) {
		parkingPlaceLock.lock();
		boolean check = false;
		if (isEmpty()) {

			this.car = car;
			log.trace("car " + car + " park into parking place number " + numberPlace);
			check = true;
		}
		parkingPlaceLock.unlock();
		return check;
	}

	public boolean leave() {
		parkingPlaceLock.lock();
		boolean check = false;
		if (!isEmpty()) {
			car = null;
			check = true;
			log.trace("car " + car + " leave parking place number " + numberPlace);
		}
		parkingPlaceLock.unlock();
		return check;

	}

	public boolean isEmpty() {

		return car == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((car == null) ? 0 : car.hashCode());
		result = prime * result + numberPlace;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParkingPlace other = (ParkingPlace) obj;
		if (car == null) {
			if (other.car != null)
				return false;
		} else if (!car.equals(other.car))
			return false;
		if (numberPlace != other.numberPlace)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Car: " + car + ", stay in parking place number: " + numberPlace;
	}

}
