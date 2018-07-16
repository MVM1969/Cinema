package com.mvm.objects;

public class TicketRequest {

	public TicketRequest(String name, int numTickets, boolean isCompleted,
			CinemaSeatingSection section) {
		setName(name);
		setNoOfTickets(numTickets);
		setCompleted(isCompleted);
		setSeatSection(section);
	}

	public TicketRequest() {
		// TODO Auto-generated constructor stub
	}

	private String name;
	private int noOfTickets;
	private boolean isCompleted;
	private CinemaSeatingSection seatSection;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfTickets() {
		return noOfTickets;
	}

	public void setNoOfTickets(int noOfTickets) {
		this.noOfTickets = noOfTickets;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public CinemaSeatingSection getSeatSection() {
		return seatSection;
	}

	public void setSeatSection(CinemaSeatingSection seatSection) {
		this.seatSection = seatSection;
	}

	public String getStatus() {

		String status = null;

		if (isCompleted) {

			status = name + " " + "Row " + seatSection.getRowNumber() + " "
					+ "Section " + seatSection.getSectionNumber();

		} else {

			if (seatSection.getRowNumber() == -1
					&& seatSection.getSectionNumber() == -1) {

				status = name + " " + "Call to split party.";

			} else {

				status = name + " " + "Sorry, we can't handle your party.";

			}

		}

		return status;
	}

}