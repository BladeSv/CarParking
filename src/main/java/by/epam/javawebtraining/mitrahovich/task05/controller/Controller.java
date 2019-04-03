package by.epam.javawebtraining.mitrahovich.task05.controller;

import java.util.Random;

import by.epam.javawebtraining.mitrahovich.task05.model.entity.Car;
import by.epam.javawebtraining.mitrahovich.task05.model.entity.CarParking;
import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class Controller {
	private Random rd;

	public Controller() {
		rd = new Random();
	}

	public void run() {
		CarParking carParking = new CarParking();
		for (int i = 1; i < 40; i++) {
			try {
				new Car("Super car" + i, carParking, rd.nextInt(Integer.parseInt(PropertiesManager.getCarWaitTime())),
						10000000L).getThread().join();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// new Car("Super car" + i, carParking,
			// rd.nextInt(Integer.parseInt(PropertiesManager.getCarWaitTime())),
			// rd.nextInt(Integer.parseInt(PropertiesManager.getCarStayTime())));

		}

	}
}
