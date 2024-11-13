package com.cybernetic;

import java.time.LocalDate;
import java.util.Random;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String bloodType;
    private String organNeeded;
    private int urgencyLevel;
    private LocalDate registrationDate;
    private String status;
    private static final Random random = new Random();

    public Patient(String id, String name, int age, String bloodType, String organNeeded, int urgencyLevel,
                   LocalDate registrationDate, String status) {
        if (!id.matches("PAT-\\d{4}")) throw new IllegalArgumentException("Invalid ID format.");
        if (age < 1 || age > 120) throw new IllegalArgumentException("Age must be between 1 and 120.");
        if (!bloodType.matches("A\\+|A-|B\\+|B-|AB\\+|AB-|O\\+|O-"))
            throw new IllegalArgumentException("Invalid blood type.");
        if (!organNeeded.matches("HEART|LUNG|KIDNEY|LIVER"))
            throw new IllegalArgumentException("Invalid organ type.");
        if (urgencyLevel < 1 || urgencyLevel > 10)
            throw new IllegalArgumentException("Urgency level must be between 1 and 10.");
        if (!status.equals("WAITING"))
            throw new IllegalArgumentException("New patients must have status 'WAITING'.");

        this.id = id;
        this.name = randomizeName(name);
        this.age = age;
        this.bloodType = bloodType;
        this.organNeeded = organNeeded;
        this.urgencyLevel = randomizeUrgency(urgencyLevel);
        this.registrationDate = randomizeRegistrationDate(registrationDate);
        this.status = status;
    }

    private int randomizeUrgency(int baseUrgency) {
        // Introduce slight variation: -1, 0, +1 with respective probabilities
        double chance = random.nextDouble();
        int variation = 0;
        if (chance < 0.33) {
            variation = -1;
        } else if (chance < 0.66) {
            variation = 1;
        }
        int newUrgency = baseUrgency + variation;
        return Math.max(1, Math.min(newUrgency, 10)); // Ensure it's between 1 and 10
    }

    private String randomizeName(String baseName) {
        // 10% chance to add a middle initial
        if (random.nextDouble() < 0.10) { // 10% probability
            char middleInitial = (char) ('A' + random.nextInt(26));
            return baseName + " " + middleInitial + ".";
        }
        return baseName;
    }

    private LocalDate randomizeRegistrationDate(LocalDate baseDate) {
        // Introduce random variation: +/- 5 days
        int variationDays = random.nextInt(11) - 5; // -5 to +5
        LocalDate newDate = baseDate.plusDays(variationDays);
        // Ensure the date is not in the future
        return newDate.isAfter(LocalDate.now()) ? LocalDate.now() : newDate;
    }

    // Getters remain unchanged
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getBloodType() { return bloodType; }
    public String getOrganNeeded() { return organNeeded; }
    public int getUrgencyLevel() { return urgencyLevel; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getStatus() { return status; }
}
