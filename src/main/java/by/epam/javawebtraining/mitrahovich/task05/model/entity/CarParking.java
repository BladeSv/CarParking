package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class CarParking {
	private static Logger log;

	private Lock carParkingLock;
	private String name;

	private final int carParkingSize;

	private AtomicInteger numberFreeplace;

	private List<ParkingPlace> places;

	static {
		log = Logger.getRootLogger();
	}

	public CarParking(String name) {
		this.name = name;
		carParkingSize = Integer.parseInt(PropertiesManager.getCarParkingSize());

		numberFreeplace = new AtomicInteger(carParkingSize);

		carParkingLock = new ReentrantLock();

		places = new ArrayList<ParkingPlace>(carParkingSize);
		for (int i = 1; i <= carParkingSize; i++) {
			places.add(new ParkingPlace(i));
		}

		log.trace("create car parking");

	}

	public String getName() {
		return name;
	}

	public boolean driveInto(Car car, Long stay, TimeUnit unit) {
		try {

			if (carParkingLock.tryLock(stay, unit)) {
				log.trace("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Into]-[LOCK]");
				if (isFreePlace()) {
					ParkingPlace temp = findFreeParkingPlace();
					if (temp != null) {
						temp.park(car);

						numberFreeplace.getAndDecrement();
						log.debug("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Into]-[TRUE]");

						return true;
					}

				}

			}

		} catch (InterruptedException e) {
			log.trace("driveInto-UNLOCK exception-" + e.getStackTrace());

		} finally {
			log.trace("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Into]-[UNLOCK]");

			carParkingLock.unlock();

		}
		log.debug("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Into]-[FALSE]");
		return false;
	}

	public boolean driveOut(Car car) {
		// if (carParkingLock.tryLock()) {
		carParkingLock.lock();
		try {
			log.trace("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Out]-[LOCK]");
			ParkingPlace temp = findCarParkingPlace(car);
			if (temp != null) {

				temp.leave();
				numberFreeplace.getAndIncrement();
				log.debug("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Out]-[FRUE]");
				return true;
			}

		} finally {
			log.trace("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Out]-[UNLOCK]");
			carParkingLock.unlock();
		}

		// }log.debug("[Parking]-"+name+"-[Car]-"+car.getName()+"-[Out]-[FALSE]");
		return false;

	}

	public boolean changeRandomParkingPlace(Car car) {
		if (carParkingLock.tryLock()) {
			log.trace("CHANGE start change parking place car-" + car);
			ParkingPlace tempPlace = findCarParkingPlace(car);

			ParkingPlace tempRandomPlace = places.get(new Random().nextInt(places.size()));

			Car tempRandomCar = tempRandomPlace.getCar();

			if (tempRandomCar != car) {
				while (tempPlace != null && (!tempPlace.leave())) {
				}

				if (tempRandomCar != null) {
					while (tempRandomPlace != null && (!tempRandomPlace.leave())) {
					}
					tempPlace.park(tempRandomCar);
					tempRandomPlace.park(car);
					log.trace("change car-" + car + " from parking place-" + tempPlace.getNumberPlace() + " with car"
							+ tempRandomCar + "from parking place-" + tempRandomPlace.getNumberPlace());
					carParkingLock.unlock();
					return true;
				} else {
					log.trace("random parking place is empty");
					tempRandomPlace.park(car);
					carParkingLock.unlock();
					return true;

				}

			} else {
				log.trace("do nothing, the same parking place");
				carParkingLock.unlock();
				return true;

			}
		} else {
			carParkingLock.unlock();
			return false;
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
		log.trace("[Parking]-" + name + "-[Find]-[FREE]-[Parking place]-" + parkingPlace.getNumberPlace());

		return parkingPlace;
	}

	private ParkingPlace findCarParkingPlace(Car car) {
		log.trace("Try find car into parking");
		ParkingPlace parkingPlace = null;
		for (ParkingPlace p : places) {
			if (p.getCar() == car) {
				parkingPlace = p;
				log.debug("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Find]-[Parking place]-"
						+ parkingPlace != null ? parkingPlace.getNumberPlace() : null);
			}

		}
		log.debug("[Parking]-" + name + "-[Car]-" + car.getName() + "-[Find]-[Parking place]-" + parkingPlace != null
				? parkingPlace.getNumberPlace()
				: null);
		return parkingPlace;

	}

	// private class Gate {
	// private String name;
	// private Lock gateLock;
	//
	// public Gate(String name) {
	// this.name = name;
	// gateLock = new ReentrantLock();
	//
	// }
	//
	// public Lock getGateLock() {
	//
	// return gateLock;
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public void driveIntoGate(Car car) {
	// if (gateLock.tryLock()) {
	// try {
	// log.trace("gate-" + name + " gateLock-LOCK for drive Into car-" + car);
	// ParkingPlace temp = findFreeParkingPlace();
	// if
	// numberFreeplace.getAndDecrement();
	// } finally {
	// log.trace("gate-" + name + " gateLock-UNLOCK for drive Into car-" + car);
	// gateLock.unlock();
	// gates.get(this).set(true);
	// }
	// }
	//
	// // if (isFreePlace()) {
	// // ParkingPlace temp = findFreeParkingPlace();
	// // log.trace("find free car place N-" + temp.getNumberPlace() + " for car-" +
	// // car);
	// //
	// // if (temp.getParkingPlaceLock().tryLock()) {
	// // log.trace("car-" + car + " stay into car parking");
	// // temp.park(car);
	//
	// // if (numberFreeplace.get() != 0) {
	//
	// // } else if (numberFreeplace.get() < carParkingSize) {
	// // log.trace("gateLock-UNLOCK");
	// // gateLock.unlock();
	// // }
	//
	// }
	//
	// public void driveOutGate(Car car) {
	// if (gateLock.tryLock()) {
	// try {
	// log.trace("gate-" + name + " gateLock-LOCK for drive out car-" + car);
	// gates.get(this).set(false);
	// numberFreeplace.getAndIncrement();
	// // log.trace("car-" + car + " drive out car parking");
	// } finally {
	// log.trace("gate-" + name + " gateLock-UNLOCK for drive out car-" + car);
	//
	// gateLock.unlock();
	// gates.get(this).set(true);
	// }
	//
	// }

	// ParkingPlace temp = findCarParkingPlace(car);
	// if (temp != null && temp.getParkingPlaceLock().tryLock()) {
	// temp.leave();

	// if (numberFreeplace.get() != carParkingSize) {

	//
	// } else if (numberFreeplace.get() > 0) {
	// log.trace("driveIntoLock-UNLOCK");
	// gateLock.unlock();
	//
	// }

}
