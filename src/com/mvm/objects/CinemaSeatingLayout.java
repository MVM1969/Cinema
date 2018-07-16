package com.mvm.objects;

import java.util.List;

public class CinemaSeatingLayout {

	public CinemaSeatingLayout(int totalCapacity, int availableSeats,
			List<CinemaSeatingSection> sections) {
		setTotalCapacity(totalCapacity);
		setAvailableSeats(availableSeats);
		setSections(sections);
	}

	public CinemaSeatingLayout() {

	}

	private int totalCapacity;
	private int availableSeats;
	private List<CinemaSeatingSection> sections;

	public int getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public List<CinemaSeatingSection> getSections() {
		return sections;
	}

	public void setSections(List<CinemaSeatingSection> sections) {
		this.sections = sections;
	}

}