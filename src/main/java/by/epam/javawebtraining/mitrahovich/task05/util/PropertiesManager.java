package by.epam.javawebtraining.mitrahovich.task05.util;

import java.util.ResourceBundle;

public class PropertiesManager {
	private static ResourceBundle rb;

	static {
		rb = ResourceBundle.getBundle("config");

	}

	public PropertiesManager() {

	}

	public static String getCarParkingSize() {
		return rb.getString("size");
	}
}
