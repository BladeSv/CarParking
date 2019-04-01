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
		for (int i = 1; i < 4; i++) {
			new Car("Super car" + i, carParking, rd.nextInt(Integer.parseInt(PropertiesManager.getCarWaitTime())), rd.nextInt(Integer.parseInt(PropertiesManager.getCarStayTime())));

		}

	}
}
