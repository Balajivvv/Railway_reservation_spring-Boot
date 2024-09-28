package com.example.ticketbooking.train_ticket_reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Passenger {

    // Static variable to assign IDs globally within the class
    
    private static int id = 1; 
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    public int passengerId=id++; // Unique ID for each passenger
    public String name;
    public int age;
    public String berthPreference;
    public String alloted;
    public int number;

    // Constructor
    public Passenger(String name, int age, String berthPreference) {
        //this.passengerId = id++; // Incrementing static ID
        this.name = name;
        this.age = age;
        this.berthPreference = berthPreference;
        this.alloted = "";
        this.number = -1;
    }

    public Passenger(Integer passengerId) {
        this.passengerId = passengerId;
        this.name = ""; // Default value
        this.age = 0;   // Default value
    }

    public Passenger(int passengerId, String name, int age) {
        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
    }

    // Default constructor for JPA
    public Passenger() {}

    // Getters and setters
    public int getPassengerId() {
        return this.passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBerthPreference() {
        return this.berthPreference;
    }

    public void setBerthPreference(String berthPreference) {
        this.berthPreference = berthPreference;
    }

    public String getAlloted() {
        return this.alloted;
    }

    public void setAlloted(String alloted) {
        this.alloted = alloted;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}