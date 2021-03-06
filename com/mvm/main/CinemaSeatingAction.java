package com.mvm.main;

import java.util.List;
import java.util.Scanner;

import com.mvm.objects.CinemaSeatingLayout;
import com.mvm.objects.TicketRequest;
import com.mvm.ticketing.TicketingServiceAction;

/**
 *
 * @author mmarlow
 */
public class CinemaSeatingAction {

	public static void main(String[] args) {
		String line;
		StringBuilder layout = new StringBuilder();
		StringBuilder ticketRequests = new StringBuilder();
		boolean isLayoutFinished = false;

		System.out
				.println("Please enter the cinema layout and ticket requests. When complete, type 'end'.\n");

		Scanner input = new Scanner(System.in);

		while ((line = input.nextLine()) != null && !line.equals("end")) {

			if (line.length() == 0) {

				isLayoutFinished = true;
				continue;

			}

			if (!isLayoutFinished) {

				layout.append(line + System.lineSeparator());

			} else {

				ticketRequests.append(line + System.lineSeparator());

			}

		}

		input.close();

		TicketingServiceAction service = new TicketingServiceAction();

		try {

			CinemaSeatingLayout seatingLayout = service
					.getCinemaSeatingLayout(layout.toString());

			List<TicketRequest> requests = service
					.getTicketRequests(ticketRequests.toString());

			service.processTicketRequests(seatingLayout, requests);

		} catch (NumberFormatException nfe) {

			System.out.println(nfe.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
