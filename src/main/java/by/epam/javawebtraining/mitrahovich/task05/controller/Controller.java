package by.epam.javawebtraining.mitrahovich.task05.controller;

import java.util.Random;

import by.epam.javawebtraining.mitrahovich.task05.model.entity.Car;
import by.epam.javawebtraining.mitrahovich.task05.model.entity.CarParking;

public class Controller {
	private Random rd;

	public Controller() {
		rd = new Random();
	}

	public void run() {
		CarParking carParking = new CarParking();
		for (int i = 1; i < 300; i++) {
			new Car("Super car" + i, carParking, rd.nextInt(10000), rd.nextInt(1000));

		}

	}
}
