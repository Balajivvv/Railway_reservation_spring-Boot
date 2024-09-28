package com.example.ticketbooking.train_ticket_reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketBooker {

    @Autowired
    private BerthService berthService;

    static Map<Integer, Passenger> passengers = new HashMap<>();
    static List<Integer> bookedTicketList = new ArrayList<>();
    static Queue<Integer> racList = new LinkedList<>();
    static Queue<Integer> waitingList = new LinkedList<>();

    // Method to book a ticket
    public void bookTicket(Passenger p) {
        System.out.println("Booking Passenger: " + p.getName());
        System.out.println("Requested berth preference: " + p.getBerthPreference());

        String preferredBerth = p.getBerthPreference();

        if (berthService.isBerthAvailable(preferredBerth)) {
            p.alloted = berthService.bookBerth(preferredBerth);
            p.number = berthService.getBerthPosition(preferredBerth);
            System.out.println("Preferred berth " + preferredBerth + " booked.");
        } else {
            String allocatedBerth = tryToAllocateBerth(p);
            if (allocatedBerth != null) {
                p.alloted = allocatedBerth;
                berthService.bookBerth(allocatedBerth);
            } else {
                System.out.println("No tickets available.");
                return;
            }
        }

        passengers.put(p.passengerId, p);
        bookedTicketList.add(p.passengerId);
        System.out.println("Ticket booked successfully for " + p.getName());
        System.out.println("Current Passengers Count: " + passengers.size());
    }

    private String tryToAllocateBerth(Passenger p) {
        String[] berthTypes = {"L", "M", "U"};
        for (String berthType : berthTypes) {
            if (berthService.isBerthAvailable(berthType)) {
                p.number = berthService.getBerthPosition(berthType);
                return berthType;
            }
        }

        if (berthService.isRacAvailable()) {
            racList.add(p.passengerId);
            p.alloted = berthService.bookRac(p);
            p.number = racList.size();
            System.out.println("RAC booked.");
            return "RAC";
        }

        if (berthService.isWaitingListAvailable()) {
            waitingList.add(p.passengerId);
            p.alloted = berthService.bookWaitingList(p);
            p.number = -1;
            System.out.println("WL booked.");
            return "WL";
        }

        return null;
    }

    public void cancelTicket(int passengerId) {
        Passenger p = passengers.get(passengerId);
        if (p != null) {
            passengers.remove(passengerId);
            bookedTicketList.remove(Integer.valueOf(passengerId));
            berthService.releaseBerth(p.alloted, p);
            System.out.println("Ticket canceled successfully for passenger ID: " + passengerId);

            // Promote RAC to confirmed if a berth was canceled
            promoteRacToConfirmed();
        } else {
            System.out.println("Passenger ID not found.");
        }
    }

    private void promoteRacToConfirmed() {
        if (!racList.isEmpty()) {
            Integer racPassengerId = racList.poll(); 
            Passenger racPassenger = passengers.get(racPassengerId);
            
            if (racPassenger != null) {
                String allocatedBerth = null;

                if (berthService.isBerthAvailable("L")) {
                    allocatedBerth = "L";
                } else if (berthService.isBerthAvailable("M")) {
                    allocatedBerth = "M";
                } else if (berthService.isBerthAvailable("U")) {
                    allocatedBerth = "U";
                }

                if (allocatedBerth != null) {
                    int canceledSeatNumber = racPassenger.number; 

                    racPassenger.alloted = allocatedBerth;
                    racPassenger.number = canceledSeatNumber; 

                    berthService.releaseBerth("RAC", racPassenger);
                    berthService.bookBerth(allocatedBerth);

                    System.out.println("RAC passenger " + racPassenger.name + " has been assigned berth " + racPassenger.number);
                }
            }

            shiftRacPassengers(); 
            promoteWaitingListToRac(); 
        }
    }

    private void shiftRacPassengers() {
        Queue<Integer> tempQueue = new LinkedList<>(racList);
        racList.clear();

        int seatNumber = 1;

        while (!tempQueue.isEmpty()) {
            Integer nextRacPassengerId = tempQueue.poll();
            Passenger nextRacPassenger = passengers.get(nextRacPassengerId);

            if (nextRacPassenger != null) {
                nextRacPassenger.number = seatNumber; 
                racList.add(nextRacPassengerId);
                seatNumber++;
            }
        }
    }

    private void promoteWaitingListToRac() {
        while (!waitingList.isEmpty() && berthService.isRacAvailable()) {
            Integer wlPassengerId = waitingList.poll(); 
            Passenger wlPassenger = passengers.get(wlPassengerId); 

            if (wlPassenger != null) {
                wlPassenger.alloted = "RAC"; 
                wlPassenger.number = racList.size() + 1; 

                racList.add(wlPassengerId); 
                berthService.bookRac(wlPassenger); 
                System.out.println("Waiting list passenger " + wlPassengerId + " has been promoted to RAC with seat number " + wlPassenger.number);
            }
        }
    }

    public void printAvailable() {
        System.out.println("Available Lower Berths: " + berthService.getAvailableLowerBerths());
        System.out.println("Available Middle Berths: " + berthService.getAvailableMiddleBerths());
        System.out.println("Available Upper Berths: " + berthService.getAvailableUpperBerths());
        System.out.println("Available RACs: " + berthService.getAvailableRacTickets());
        System.out.println("Available Waiting List: " + berthService.getAvailableWaitingList());
    }

    public void printPassengers() {
        if (passengers.isEmpty()) {
            System.out.println("No details of passengers");
            return;
        }
        for (Passenger p : passengers.values()) {
            System.out.println("Passenger ID: " + p.passengerId);
            System.out.println("Name: " + p.name);
            System.out.println("Age: " + p.age);
            System.out.println("Alloted: " + p.number + " (" + p.alloted + ")");
            System.out.println("--------------------------");
        }
    }
}
