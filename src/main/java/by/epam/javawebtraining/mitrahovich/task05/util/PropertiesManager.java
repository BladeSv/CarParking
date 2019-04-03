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

	public static String getCarWaitTime() {
		return rb.getString("car.time.wait");
	}

	public static String getCarStayTime() {
		return rb.getString("car.time.stay");

	}

	public static String getGateNumber() {
		return rb.getString("gate");
	}
}
