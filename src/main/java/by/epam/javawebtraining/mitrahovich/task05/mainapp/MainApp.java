package by.epam.javawebtraining.mitrahovich.task05.mainapp;

import by.epam.javawebtraining.mitrahovich.task05.controller.Controller;

public class MainApp {

	public MainApp() {

	}

	public static void main(String[] args) {

		try {
			Controller controller = new Controller();
			controller.run();
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
