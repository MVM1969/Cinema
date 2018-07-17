package com.mvm.ticketing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mvm.objects.CinemaSeatingLayout;
import com.mvm.objects.CinemaSeatingSection;
import com.mvm.objects.TicketRequest;

public class TicketingServiceAction implements TicketingService {

	@Override
	public CinemaSeatingLayout getCinemaSeatingLayout(String rawLayout) throws NumberFormatException {

		CinemaSeatingLayout CinemaSeatingLayout = new CinemaSeatingLayout();
		CinemaSeatingSection section;
		List<CinemaSeatingSection> sectionsList = new ArrayList<CinemaSeatingSection>();
		int totalCapacity = 0, value;
		String[] rows = rawLayout.split(System.lineSeparator());
		String[] sections;

		for (int i = 0; i < rows.length; i++) {

			sections = rows[i].split(" ");

			for (int j = 0; j < sections.length; j++) {

				try {

					value = Integer.valueOf(sections[j]);

				} catch (NumberFormatException nfe) {

					throw new NumberFormatException(
							"'" + sections[j] + "'" + " is invalid section capacity. Please correct it.");

				}

				totalCapacity = totalCapacity + value;

				section = new CinemaSeatingSection();
				section.setRowNumber(i + 1);
				section.setSectionNumber(j + 1);
				section.setCapacity(value);
				section.setAvailableSeats(value);

				sectionsList.add(section);

			}

		}

		CinemaSeatingLayout.setTotalCapacity(totalCapacity);
		CinemaSeatingLayout.setAvailableSeats(totalCapacity);
		CinemaSeatingLayout.setSections(sectionsList);

		return CinemaSeatingLayout;

	}

	@Override
	public List<TicketRequest> getTicketRequests(String ticketRequests) throws NumberFormatException {

		List<TicketRequest> requestsList = new ArrayList<TicketRequest>();
		TicketRequest request;

		String[] requests = ticketRequests.split(System.lineSeparator());

		for (String r : requests) {

			String[] rData = r.split(" ");

			request = new TicketRequest();

			request.setName(rData[0]);

			try {

				request.setNoOfTickets(Integer.valueOf(rData[1]));

			} catch (NumberFormatException nfe) {

				throw new NumberFormatException(
						"'" + rData[1] + "'" + " is invalid ticket request. Please correct it.");
			}
			request.setCompleted(false);

			requestsList.add(request);

		}

		return requestsList;

	}

	/*
	 * Find complementing request to avoid waste of seats.
	 * 
	 * We start index from currentRequestIndex+1, because all previous requests are
	 * already completed.
	 */
	private int findComplementRequest(List<TicketRequest> requests, int complementSeats, int currentRequestIndex) {

		int requestNo = -1;

		for (int i = currentRequestIndex + 1; i < requests.size(); i++) {

			TicketRequest request = requests.get(i);

			if (!request.isCompleted() && request.getNoOfTickets() == complementSeats) {

				requestNo = i;
				break;

			}

		}

		return requestNo;
	}

	/*
	 * Find section by it's available seats
	 */
	private int findSectionByAvailableSeats(List<CinemaSeatingSection> sections, int availableSeats) {

		int i = 0;
		CinemaSeatingSection section = new CinemaSeatingSection();
		section.setAvailableSeats(availableSeats);

		Collections.sort(sections);

		Comparator<CinemaSeatingSection> byAvailableSeats = new Comparator<CinemaSeatingSection>() {

			@Override
			public int compare(CinemaSeatingSection o1, CinemaSeatingSection o2) {

				return o1.getAvailableSeats() - o2.getAvailableSeats();

			}
		};

		int sectionNo = Collections.binarySearch(sections, section, byAvailableSeats);

		/*
		 * sectionNo < 0 means could not find section sectionNo = 0 means found section
		 * and it's very first one sectionNo > 0 means found section but have to check
		 * for duplicate sections
		 */

		if (sectionNo > 0) {

			for (i = sectionNo - 1; i >= 0; i--) {

				CinemaSeatingSection s = sections.get(i);

				if (s.getAvailableSeats() != availableSeats)
					break;

			}

			sectionNo = i + 1;

		}

		return sectionNo;
	}

	/*
	 * 
	 * Request Processing in nut-shell
	 * 
	 * 1) Iterate over all ticket requests 2) For each request,
	 * 
	 * - if total available seats are less than requested seats then 'we can't
	 * handle the party'. - iterate over all theater sections starting from first
	 * row
	 * 
	 * - If requested tickets and section's available seats match EXACTLY then
	 * assign it.
	 * 
	 * - If requested tickets < section's available seats - Find complement request,
	 * if any (complement request = section's available seats - original requested
	 * tickets) - If FOUND, complete assignment of original and complement ticket
	 * requests - If NOT found - Find EXCATLY matching section with requested no of
	 * tickets - If FOUND, assign it - If NOT found, then assign the request to
	 * current section
	 * 
	 * - If request is INCOMPLETE, 'Call party to split.'
	 */

	@Override
	public void processTicketRequests(CinemaSeatingLayout layout, List<TicketRequest> requests) {

		for (int i = 0; i < requests.size(); i++) {

			TicketRequest request = requests.get(i);
			if (request.isCompleted())
				continue;

			/*
			 * -2 is an indicator when we can't handle the party.
			 */
			if (request.getNoOfTickets() > layout.getAvailableSeats()) {

				request.getSeatSection().setRowNumber(-2);
				request.getSeatSection().setSectionNumber(-2);
				continue;

			}

			List<CinemaSeatingSection> sections = layout.getSections();

			for (int j = 0; j < sections.size(); j++) {

				CinemaSeatingSection section = sections.get(j);

				if (request.getNoOfTickets() == section.getAvailableSeats()) {

					section.setRowNumber(request.getSeatSection().getRowNumber());
					section.setSectionNumber(request.getSeatSection().getSectionNumber());
					section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
					layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
					request.setCompleted(true);
					break;

				} else if (request.getNoOfTickets() < section.getAvailableSeats()) {

					int requestNo = findComplementRequest(requests,
							section.getAvailableSeats() - request.getNoOfTickets(), i);

					if (requestNo != -1) {

						request.getSeatSection().setRowNumber(section.getRowNumber());
						request.getSeatSection().setSectionNumber(section.getSectionNumber());
						section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
						layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
						request.setCompleted(true);

						TicketRequest complementRequest = requests.get(requestNo);

						complementRequest.getSeatSection().setRowNumber(section.getRowNumber());
						complementRequest.getSeatSection().setSectionNumber(section.getSectionNumber());
						section.setAvailableSeats(section.getAvailableSeats() - complementRequest.getNoOfTickets());
						layout.setAvailableSeats(layout.getAvailableSeats() - complementRequest.getNoOfTickets());
						complementRequest.setCompleted(true);

						break;

					} else {

						int sectionNo = findSectionByAvailableSeats(sections, request.getNoOfTickets());

						if (sectionNo >= 0) {

							CinemaSeatingSection perferctSection = sections.get(sectionNo);

							request.getSeatSection().setRowNumber(perferctSection.getRowNumber());
							request.getSeatSection().setSectionNumber(perferctSection.getSectionNumber());
							perferctSection
									.setAvailableSeats(perferctSection.getAvailableSeats() - request.getNoOfTickets());
							layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
							request.setCompleted(true);
							break;

						} else {

							request.getSeatSection().setRowNumber(section.getRowNumber());
							request.getSeatSection().setSectionNumber(section.getSectionNumber());
							section.setAvailableSeats(section.getAvailableSeats() - request.getNoOfTickets());
							layout.setAvailableSeats(layout.getAvailableSeats() - request.getNoOfTickets());
							request.setCompleted(true);
							break;

						}

					}

				}

			}

			/*
			 * -1 is an indicator when we need to split the party.
			 */
			if (!request.isCompleted()) {

				request.getSeatSection().setRowNumber(-1);
				request.getSeatSection().setSectionNumber(-1);

			}

		}

		System.out.println("Seats Distribution.\n");

		for (TicketRequest request : requests) {

			System.out.println(request.getStatus());

		}

	}

}