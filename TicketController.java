package com.example.ticketbooking.train_ticket_reservation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TicketController {

    @Autowired
    private TicketBooker ticketBooker;

    @Autowired
    private BerthService berthService;

    @GetMapping("/")
    public String home() {
        return "index"; // Load the index.html page
    }

    @GetMapping("/available")
    public String availableTickets(Model model) {
        model.addAttribute("availableLowerBerths", BerthService.getAvailableLowerBerths());
        model.addAttribute("availableMiddleBerths", BerthService.getAvailableMiddleBerths());
        model.addAttribute("availableUpperBerths", BerthService.getAvailableUpperBerths());
        model.addAttribute("availableRacTickets", BerthService.getAvailableRacTickets());
        model.addAttribute("availableWaitingList", BerthService.getAvailableWaitingList());
        return "available"; // Returns the available.html page
    }

    @PostMapping("/book")
public String bookTicket(@ModelAttribute Passenger passenger, Model model) {
    // Book ticket and check berth availability
    ticketBooker.bookTicket(passenger); // Call the bookTicket method

    // Prepare confirmation message
    model.addAttribute("passengerId", passenger.passengerId);
    model.addAttribute("name", passenger.name);
    model.addAttribute("age", passenger.age);
    model.addAttribute("seatNumber", passenger.number);
    model.addAttribute("berthType", passenger.alloted);
    
    return "confirmation"; // Redirect to confirmation page
}

    @PostMapping("/cancel")
    public String cancelTicket(@RequestParam("passengerId") int passengerId, Model model) {
        ticketBooker.cancelTicket(passengerId); // Cancel ticket
        model.addAttribute("message", "Ticket cancelled successfully.");
        return "confirmation"; // Returns the confirmation.html page
    }

    @GetMapping("/booked")
public String bookedTickets(Model model) {
    model.addAttribute("passengers", new ArrayList<>(TicketBooker.passengers.values())); // Convert Map values to List
    return "booked"; // Returns the booked.html page
}

}


