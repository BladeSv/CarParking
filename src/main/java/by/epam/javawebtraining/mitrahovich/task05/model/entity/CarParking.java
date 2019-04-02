package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class CarParking {
	private Lock driveIntoLock;
	private Lock driveOutLock;

	private static Logger log;

	// private static final int DEFAULT_CAR_PARKING_SIZE = 5;
	private final int carParkingSize;

	private AtomicInteger numberFreeplace;

	private List<ParkingPlace> places;

	static {
		log = Logger.getRootLogger();
	}

	public CarParking() {
		driveIntoLock = new ReentrantLock();
		driveOutLock = new ReentrantLock();
		carParkingSize = Integer.parseInt(PropertiesManager.getCarParkingSize());

		numberFreeplace = new AtomicInteger(carParkingSize);

		places = new ArrayList<ParkingPlace>(carParkingSize);
		for (int i = 1; i <= carParkingSize; i++) {
			places.add(new ParkingPlace(i));
		}

		log.trace("create car parking");
	}

	public List<ParkingPlace> getPlace() {
		return places;
	}

	public Lock getDriveIntoLock() {
		return driveIntoLock;
	}

	public Lock getDriveOutLock() {
		return driveOutLock;
	}

	public void driveInto(Car car) {
		log.trace("driveIntoLock-LOCK");
		driveIntoLock.lock();

		if (isFreePlace()) {
			ParkingPlace temp = findFreeParkingPlace();
			log.trace("find free car place N-" + temp.getNumberPlace() + " for car-" + car);
			if (temp.getParkingPlaceLock().tryLock()) {
				temp.park(car);

				numberFreeplace.getAndDecrement();
				if (numberFreeplace.get() != 0) {
					log.trace("driveIntoLock-UNLOCK");
					driveIntoLock.unlock();

				} else if (numberFreeplace.get() < carParkingSize) {
					log.trace("driveOutLock-UNLOCK");
					driveIntoLock.unlock();
				}
				log.trace("car-" + car + " stay into car parking");

			}

		}

	}

	public void driveOut(Car car) {
		log.trace("driveOutLock-LOCK");
		driveOutLock.lock();

		ParkingPlace temp = findCarParkingPlace(car);
		if (temp != null && temp.getParkingPlaceLock().tryLock()) {
			temp.leave();

			numberFreeplace.getAndIncrement();
			if (numberFreeplace.get() != carParkingSize) {
				log.trace("driveOutLock-UNLOCK");
				driveOutLock.unlock();

			} else if (numberFreeplace.get() > 0) {
				log.trace("driveIntoLock-UNLOCK");
				driveIntoLock.unlock();

			}
			log.trace("car-" + car + " drive out car parking");

		}

	}

	private boolean isFreePlace() {
		return numberFreeplace.get() > 0;
	}

	private ParkingPlace findFreeParkingPlace() {
		ParkingPlace parkingPlace = null;
		for (ParkingPlace p : places) {
			if (p.isEmpty()) {
				parkingPlace = p;
			}

		}
		return parkingPlace;
	}

	private ParkingPlace findCarParkingPlace(Car car) {
		ParkingPlace parkingPlace = null;
		for (ParkingPlace p : places) {
			if (p.getCar() == car) {
				parkingPlace = p;
			}

		}
		return parkingPlace;

	}

}
