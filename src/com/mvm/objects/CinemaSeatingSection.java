package com.mvm.objects;

public class CinemaSeatingSection implements Comparable<CinemaSeatingSection> {

	public CinemaSeatingSection(int row, int section, int capacity, int seats) {
		setRowNumber(row);
		setSectionNumber(section);
		setCapacity(capacity);
		setAvailableSeats(seats);
	}

	public CinemaSeatingSection() {
		// TODO Auto-generated constructor stub
	}

	private int rowNumber;
	private int sectionNumber;
	private int capacity;
	private int availableSeats;

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	@Override
	public int compareTo(CinemaSeatingSection o) {

		int result = 0;

		if ((this.availableSeats - o.availableSeats) == 0) {

			if ((this.rowNumber - o.rowNumber) == 0) {

				result = this.sectionNumber - o.sectionNumber;

			} else {

				result = this.rowNumber - o.rowNumber;

			}

		} else {

			result = this.availableSeats - o.availableSeats;

		}

		return result;

	}

	@Override
	public String toString() {

		return "Row #: " + rowNumber + " " + "Section #: " + sectionNumber
				+ " Capacity: " + capacity + " availableSeats: "
				+ availableSeats;

	}

}