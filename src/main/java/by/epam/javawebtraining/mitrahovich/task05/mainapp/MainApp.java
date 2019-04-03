package by.epam.javawebtraining.mitrahovich.task05.mainapp;

import by.epam.javawebtraining.mitrahovich.task05.controller.Controller;

public class MainApp {

	public MainApp() {

	}

	public static void main(String[] args) {

		Controller controller = new Controller();
		controller.run();
		// Thread.currentThread().join();

	}

}
