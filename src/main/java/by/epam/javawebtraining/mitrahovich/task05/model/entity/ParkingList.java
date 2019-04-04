package by.epam.javawebtraining.mitrahovich.task05.model.entity;

import java.util.ArrayList;
import java.util.List;

import by.epam.javawebtraining.mitrahovich.task05.util.PropertiesManager;

public class ParkingList {
	private List<CarParking> parkingList;

	public ParkingList() {
		super();
		parkingList = new ArrayList<CarParking>();

		for (int i = 1; i <= Integer.parseInt(PropertiesManager.getCarParkingNumber()); i++) {
			parkingList.add(new CarParking("CarParking-" + i));

		}
	}

	public List<CarParking> getParkingList() {
		return parkingList;
	}

	public void setParkingList(List<CarParking> parkingList) {
		this.parkingList = parkingList;
	}

}
