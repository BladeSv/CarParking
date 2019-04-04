package by.epam.javawebtraining.mitrahovich.task05.controller;

import java.util.Random;

import by.epam.javawebtraining.mitrahovich.task05.model.entity.Car;
import by.epam.javawebtraining.mitrahovich.task05.model.entity.ParkingList;
import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class Controller {
	private Random rd;

	public Controller() {
		rd = new Random();
	}

	public void run() {
		// CarParking carParking = new CarParking();
		ParkingList carParkingList = new ParkingList();

		for (int i = 1; i <= Integer.parseInt(PropertiesManager.getCarsNumber()); i++) {
			try {
				new Car("Super car" + i, carParkingList.getParkingList(), 3000, 2000);
			} catch (NumberFormatException e) {

				e.printStackTrace();
			}

		}

	}
}
