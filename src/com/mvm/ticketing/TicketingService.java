package com.mvm.ticketing;

import java.util.List;

import com.mvm.objects.CinemaSeatingLayout;
import com.mvm.objects.TicketRequest;

public interface TicketingService {

	CinemaSeatingLayout getCinemaSeatingLayout(String rawLayout);

	List<TicketRequest> getTicketRequests(String ticketRequests);

	void processTicketRequests(CinemaSeatingLayout layout,
			List<TicketRequest> requests);

}