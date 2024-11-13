package com.cybernetic;

import java.time.LocalDate;
import java.util.Random;

public class CyberneticOrgan {
    private String id;
    private String type;
    private String model;
    private int powerLevel;
    private double compatibilityScore;
    private LocalDate manufactureDate;
    private String status;
    private String manufacturer;
    private static final Random random = new Random();

    // Static variable to track the next available ID number
    private static int nextIdNumber = 2001; // Starting after ORG-0020

    public CyberneticOrgan(String id, String type, String model, int powerLevel, double compatibilityScore,
                           LocalDate manufactureDate, String status, String manufacturer, boolean generateNewId) {
        if (generateNewId) {
            this.id = generateUniqueId();
        } else {
            if (!id.matches("ORG-\\d{4}")) throw new IllegalArgumentException("Invalid ID format.");
            this.id = id;
        }

        if (!type.matches("HEART|LUNG|KIDNEY|LIVER")) throw new IllegalArgumentException("Invalid organ type.");
        if (powerLevel < 1 || powerLevel > 100)
            throw new IllegalArgumentException("Power level must be between 1 and 100.");
        if (compatibilityScore < 0.0 || compatibilityScore > 1.0)
            throw new IllegalArgumentException("Compatibility score must be between 0.0 and 1.0.");
        if (manufactureDate.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Manufacture date cannot be in the future.");
        if (!status.matches("AVAILABLE|ALLOCATED|DEFECTIVE"))
            throw new IllegalArgumentException("Invalid status.");

        this.type = type;
        this.model = model;
        this.powerLevel = powerLevel;
        this.compatibilityScore = compatibilityScore;
        this.manufactureDate = manufactureDate;
        this.status = status;
        this.manufacturer = manufacturer;

        // Introduce randomness: 20% chance to change status randomly
        if (random.nextDouble() < 0.20) { // 20% probability
            this.status = getRandomStatus();
        }

        // Introduce randomness: 10% chance to alter the manufacturer name
        if (random.nextDouble() < 0.10) { // 10% probability
            this.manufacturer += "-" + (char) ('A' + random.nextInt(26));
        }

        // Introduce randomness: 15% chance to alter the model name
        if (random.nextDouble() < 0.15) { // 15% probability
            this.model = model + "-" + (random.nextInt(900) + 100); // e.g., ModelX-123
        }
    }

    // Constructor without ID generation (default)
    public CyberneticOrgan(String id, String type, String model, int powerLevel, double compatibilityScore,
                           LocalDate manufactureDate, String status, String manufacturer) {
        this(id, type, model, powerLevel, compatibilityScore, manufactureDate, status, manufacturer, false);
    }

    private String getRandomStatus() {
        String[] statuses = {"AVAILABLE", "ALLOCATED", "DEFECTIVE"};
        return statuses[random.nextInt(statuses.length)];
    }

    private synchronized String generateUniqueId() {
        String newId;
        do {
            newId = String.format("ORG-%04d", nextIdNumber++);
        } while (!isIdUnique(newId));
        return newId;
    }

    private boolean isIdUnique(String newId) {
        // Implement a mechanism to check uniqueness.
        // This could be a static list tracking all IDs or a database query.
        // For simplicity, assume uniqueness since we increment nextIdNumber.
        return true;
    }

    // Getters remain unchanged
    public String getId() { return id; }
    public String getType() { return type; }
    public String getModel() { return model; }
    public int getPowerLevel() { return powerLevel; }
    public double getCompatibilityScore() { return compatibilityScore; }
    public LocalDate getManufactureDate() { return manufactureDate; }
    public String getStatus() { return status; }
    public String getManufacturer() { return manufacturer; }
}
