package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class CarParking {

	// private Map<Gate, AtomicBoolean> gates;
	// private List<Gate> gates;
	private final int numberParkingGate;
	private Lock carParkingLock;
	private Lock carParkingPlaceLock;
	private static Logger log;

	private final int carParkingSize;

	private AtomicInteger numberFreeplace;

	private List<ParkingPlace> places;

	private ConcurrentLinkedQueue<Car> outQueue;

	static {
		log = Logger.getRootLogger();
	}

	public CarParking() {

		carParkingSize = Integer.parseInt(PropertiesManager.getCarParkingSize());

		numberParkingGate = Integer.parseInt(PropertiesManager.getGateNumber());

		numberFreeplace = new AtomicInteger(carParkingSize);

		carParkingLock = new ReentrantLock();
		carParkingPlaceLock = new ReentrantLock();
		places = new ArrayList<ParkingPlace>(carParkingSize);
		for (int i = 1; i <= carParkingSize; i++) {
			places.add(new ParkingPlace(i));
		}
		// gates = new ConcurrentHashMap<CarParking.Gate, AtomicBoolean>();
		// for (int i = 1; i <= numberParkingGate; i++) {
		// CarParking.Gate temp = new Gate("gate-" + i);
		//
		// gates.put(temp, new AtomicBoolean(true));
		// }outQueue=new ConcurrentLinkedQueue();

		log.trace("create car parking");

	}

	public List<ParkingPlace> getPlace() {
		return places;
	}

	// public Gate findFreeGate(Long time, TimeUnit unit) {
	//
	// try {
	// if (findGateLock.tryLock(time, unit)) {
	// for (Gate gate : gates.keySet()) {
	// if (gates.get(gate).get()) {
	// gates.get(gate).set(false);
	// findGateLock.unlock();
	// return gate;
	//
	// }
	// }
	// }
	//
	// } catch (InterruptedException e) {
	// log.trace("InterruptedException " + e.getStackTrace());
	//
	// }
	// return null;
	// }

	public void driveInto(Car car) {
		if (isFreePlace()) {
			ParkingPlace temp = findFreeParkingPlace();
			while (temp != null && (!temp.park(car))) {
				temp = findFreeParkingPlace();
			}
			numberFreeplace.getAndDecrement();
		}

	}

	// public boolean driveInto(Car car, Long time, TimeUnit unit) {
	//
	//
	// if (isFreePlace()) {
	// ParkingPlace tempPlace=findCarParkingPlace(Car car);
	//
	// }}}}}catch(InterruptedException e)
	// {
	// log.trace("InterruptedException " + e.getStackTrace());
	// e.printStackTrace();
	//
	// }finally
	// {
	//
	// if (tempGate != null) {
	// carParkingLock.unlock();
	// tempGate.driveIntoGate(car);
	// ParkingPlace temp = findFreeParkingPlace();
	// if (temp != null) {
	// temp.park(car);
	// } else {
	// return false;
	// }
	//
	// return true;
	// }
	//
	// }return false;
	// }

	// if (!check) {
	// gates.get(lock).driveIntoGate(car);
	// check = true;
	// ParkingPlace temp = findFreeParkingPlace();
	//
	// if (temp.getParkingPlaceLock().tryLock()) {
	// // log.trace("car-" + car + " stay into car parking");
	// temp.park(car);
	// }
	// }

	// log.trace("driveIntoLock-LOCK");
	// driveIntoLock.lock();
	//
	// if (isFreePlace()) {
	// ParkingPlace temp = findFreeParkingPlace();
	// log.trace("find free car place N-" + temp.getNumberPlace() + " for car-" +
	// car);
	// if (temp.getParkingPlaceLock().tryLock()) {
	// temp.park(car);
	//
	// numberFreeplace.getAndDecrement();
	// if (numberFreeplace.get() != 0) {
	// log.trace("driveIntoLock-UNLOCK");
	// driveIntoLock.unlock();
	//
	// } else if (numberFreeplace.get() < carParkingSize) {
	// log.trace("driveOutLock-UNLOCK");
	// driveIntoLock.unlock();
	// }
	// log.trace("car-" + car + " stay into car parking");
	//
	// }
	//
	// }

	public boolean driveOut(Car car) {
		if (carParkingLock.tryLock()) {
			log.trace("ENTER in driveOut");
			ParkingPlace temp = findCarParkingPlace(car);

			while (temp != null && (!temp.leave())) {

			}
			log.trace("Car-" + car + "LEAVE parking place");
			numberFreeplace.getAndIncrement();
			carParkingLock.unlock();
			return true;
		} else {

			return false;
		}

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

	// private void putInOutQueue(Car car) {
	// outQueue.add(car);
	// while (!outQueue.isEmpty()) {
	// for (Lock lock : gates.keySet()) {
	// if (lock.tryLock()) {
	// gates.get(lock).driveOutGate(outQueue.poll());
	//
	// }
	// }
	//
	// }
	//
	// }

	private boolean isFreePlace() {
		return numberFreeplace.get() > 0;
	}

	private ParkingPlace findFreeParkingPlace() {
		log.trace("Try find free parking place");
		ParkingPlace parkingPlace = null;
		for (ParkingPlace p : places) {
			if (p.isEmpty()) {
				parkingPlace = p;
				log.trace("FIND parking place-" + parkingPlace.getNumberPlace());
			}

		}
		return parkingPlace;
	}

	private ParkingPlace findCarParkingPlace(Car car) {
		log.trace("Try find car into parking");
		ParkingPlace parkingPlace = null;
		for (ParkingPlace p : places) {
			if (p.getCar() == car) {
				parkingPlace = p;
				log.trace("FIND parking place-" + parkingPlace.getNumberPlace() + "with car-" + parkingPlace.getCar());
			}

		}
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
