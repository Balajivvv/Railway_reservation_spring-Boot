package com.example.ticketbooking.train_ticket_reservation;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BerthService {

    // Static fields for berth availability
    public static int availableLowerBerths = 1; // Total available Lower Berths
    public static int availableMiddleBerths =2; // Total available Middle Berths
    public static int availableUpperBerths = 1; // Total available Upper Berths
    public static int availableRacTickets = 2; // Total available RAC tickets
    public static int availableWaitingList = 2; // Total available waiting list tickets

    // Lists for managing positions
    private static List<Integer> lowerBerthsPositions = new ArrayList<>(Arrays.asList(1, 2, 3));
    private static List<Integer> middleBerthsPositions = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21));
    private static List<Integer> upperBerthsPositions = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21));
    private static List<Integer> racPositions = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18));
    private static List<Integer> waitingListPositions = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    // Method to check if a specific berth type is available
    public boolean isBerthAvailable(String berthType) {
        switch (berthType) {
            case "L":
                return availableLowerBerths > 0;
            case "M":
                return availableMiddleBerths > 0;
            case "U":
                return availableUpperBerths > 0;
            default:
                return false;
        }
    }

    // Method to book a specific berth type
    public String bookBerth(String berthType) {
        switch (berthType) {
            case "L":
                if (availableLowerBerths > 0) {
                    availableLowerBerths--;
                    return "L"; // Return the berth type booked
                }
                break;
            case "M":
                if (availableMiddleBerths > 0) {
                    availableMiddleBerths--;
                    return "M";
                }
                break;
            case "U":
                if (availableUpperBerths > 0) {
                    availableUpperBerths--;
                    return "U";
                }
                break;
        }
        return null; // No berth available
    }

    // Methods for RAC and Waiting List management
    public boolean isRacAvailable() {
        return availableRacTickets > 0;
    }

    public boolean isWaitingListAvailable() {
        return availableWaitingList > 0;
    }

    public String bookRac(Passenger passenger) {
        if (isRacAvailable()) {
            passenger.number = racPositions.remove(0);
            availableRacTickets--;
            return "RAC";
        }
        return null; // No RAC available
    }

    public String bookWaitingList(Passenger passenger) {
        if (isWaitingListAvailable()) {
            passenger.number = waitingListPositions.remove(0);
            availableWaitingList--;
            return "WL";
        }
        return null; // No WL available
    }

    // New methods to get berth positions
    public int getBerthPosition(String berthType) {
        switch (berthType) {
            case "L":
                return lowerBerthsPositions.remove(0); // Return first available position and remove it from the list
            case "M":
                return middleBerthsPositions.remove(0);
            case "U":
                return upperBerthsPositions.remove(0);
            default:
                return -1; // Invalid berth type
        }
    }

    public int getRacPosition() {
        return racPositions.remove(0); // Return RAC position and remove it from the list
    }

    public void releaseBerth(String berthType, Passenger passenger) {
        if (berthType.equals("L")) {
            availableLowerBerths++;
            lowerBerthsPositions.add(passenger.number); // Add position back to available list
        } else if (berthType.equals("M")) {
            availableMiddleBerths++;
            middleBerthsPositions.add(passenger.number);
        } else if (berthType.equals("U")) {
            availableUpperBerths++;
            upperBerthsPositions.add(passenger.number);
        } else if (berthType.equals("RAC")) {
            availableRacTickets++;
            racPositions.add(passenger.number);
        } else if (berthType.equals("WL")) {
            availableWaitingList++;
            waitingListPositions.add(passenger.number);
        }
    }

    public void incrementRacCount() {
        availableRacTickets++;
    }

  
    public static int getAvailableLowerBerths() {
        return availableLowerBerths;
    }

    public static int getAvailableMiddleBerths() {
        return availableMiddleBerths;
    }

    public static int getAvailableUpperBerths() {
        return availableUpperBerths;
    }

    public static int getAvailableRacTickets() {
        return availableRacTickets;
    }

    public static int getAvailableWaitingList() {
        return availableWaitingList;
    }
}


