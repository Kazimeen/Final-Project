package com.cybernetic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        OrganInventory inventory = new OrganInventory();
        ArrayList<String> validationErrors = new ArrayList<>();
        ArrayList<Patient> validPatients = new ArrayList<>();

        System.out.println("Part 1 - CyberOrgan Hub Demonstration");
        System.out.println("=====================================\n");

        // 1. Load and validate organs
        System.out.println("1. Loading and Validating Organs...");
        System.out.println("----------------------------------");
        loadOrgans(inventory, validationErrors);

        // Print organ validation errors
        System.out.println("\nOrgan Validation Errors:");
        System.out.println("------------------------");
        for (int i = 0; i < Math.min(5, validationErrors.size()); i++) {
            System.out.println(validationErrors.get(i));
        }

        if (validationErrors.size() > 5) {
            System.out.printf("[...%d more validation errors...]\n",
                    validationErrors.size() - 5);
        }

        // 2. Demonstrate sorting
        System.out.println("\n2. Demonstrating Organ Sorting...");
        System.out.println("--------------------------------");

        // Power Level sorting
        System.out.println("\nSorted by Power Level (Quicksort):");
        ArrayList<CyberneticOrgan> powerSorted = inventory.sortByPowerLevel();
        printTopFiveOrgans(powerSorted, organ ->
                String.format("ID: %s, Power Level: %d (%s)",
                        organ.getId(),
                        organ.getPowerLevel(),
                        organ.getType()));

        // Manufacture Date sorting
        System.out.println("\nSorted by Manufacture Date (Mergesort):");
        ArrayList<CyberneticOrgan> dateSorted = inventory.sortByManufactureDate();
        printTopFiveOrgans(dateSorted, organ ->
                String.format("ID: %s, Date: %s (%s)",
                        organ.getId(),
                        organ.getManufactureDate(),
                        organ.getType()));

        // Compatibility Score sorting
        System.out.println("\nSorted by Compatibility Score (Bubblesort):");
        ArrayList<CyberneticOrgan> compatibilitySorted = inventory.sortByCompatibilityScore();
        printTopFiveOrgans(compatibilitySorted, organ ->
                String.format("ID: %s, Compatibility: %.2f (%s)",
                        organ.getId(),
                        organ.getCompatibilityScore(),
                        organ.getType()));

        // 3. Load and validate patients
        validationErrors.clear();
        System.out.println("\n3. Loading and Validating Patients...");
        System.out.println("------------------------------------");
        loadPatients(validPatients, validationErrors);

        // Print patient validation errors
        System.out.println("\nPatient Validation Errors:");
        System.out.println("-------------------------");
        for (int i = 0; i < Math.min(5, validationErrors.size()); i++) {
            System.out.println(validationErrors.get(i));
        }
        if (validationErrors.size() > 5) {
            System.out.printf("[...%d more validation errors...]\n",
                    validationErrors.size() - 5);
        }
    }

    private static void loadOrgans(OrganInventory inventory, ArrayList<String> errors) {
        try (InputStream is = Main.class.getResourceAsStream("/organs.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line = reader.readLine(); // skip header
            int successCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] data = line.split(",");

                try {
                    boolean generateNewId = shouldGenerateNewId(data[0].trim());

                    CyberneticOrgan organ = new CyberneticOrgan(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            Integer.parseInt(data[3].trim()),
                            Double.parseDouble(data[4].trim()),
                            LocalDate.parse(data[5].trim()),
                            data[6].trim(),
                            data[7].trim(),
                            generateNewId
                    );

                    inventory.addOrgan(organ);
                    if (successCount < 5) {
                        String[] successMessages = {
                                "Successfully added: ",
                                "Organ registered: ",
                                "Added to inventory: ",
                                "New organ entry: ",
                                "Organ confirmed: "
                        };
                        System.out.println(successMessages[random.nextInt(successMessages.length)] + organ.getId());
                    } else if (successCount == 5) {
                        System.out.println("[...more successful additions...]");
                    }
                    successCount++;
                } catch (IllegalArgumentException e) {
                    errors.add("Error with organ " + data[0].trim() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading organs file: " + e.getMessage());
        }
    }

    private static boolean shouldGenerateNewId(String id) {
        // Define conditions under which a new ID should be generated.
        // For example, randomly decide to generate a new ID with 20% probability.
        // Alternatively, set specific conditions based on existing IDs.

        // Example: 20% chance to generate a new ID for each organ
        return random.nextDouble() < 0.20;
    }

    private static void loadPatients(ArrayList<Patient> validPatients, ArrayList<String> errors) {
        try (InputStream is = Main.class.getResourceAsStream("/patients.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line = reader.readLine(); // skip header
            int successCount = 0;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                try {
                    Patient patient = new Patient(
                            data[0].trim(),
                            data[1].trim(),
                            Integer.parseInt(data[2].trim()),
                            data[3].trim(),
                            data[4].trim(),
                            Integer.parseInt(data[5].trim()),
                            LocalDate.parse(data[6].trim()),
                            data[7].trim()
                    );

                    validPatients.add(patient);
                    if (successCount < 5) {
                        String[] successMessages = {
                                "Successfully validated: ",
                                "Patient registered: ",
                                "Added to queue: ",
                                "New patient entry: ",
                                "Patient confirmed: "
                        };
                        System.out.printf("%s%s - %s (Age: %d, Blood Type: %s, Organ Needed: %s)\n",
                                successMessages[random.nextInt(successMessages.length)],
                                patient.getId(),
                                patient.getName(),
                                patient.getAge(),
                                patient.getBloodType(),
                                patient.getOrganNeeded());
                    } else if (successCount == 5) {
                        System.out.println("[...more successful validations...]");
                    }
                    successCount++;
                } catch (IllegalArgumentException e) {
                    errors.add("Error with patient " + data[0].trim() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading patients file: " + e.getMessage());
        }
    }

    private static void printTopFiveOrgans(ArrayList<CyberneticOrgan> organs,
                                           java.util.function.Function<CyberneticOrgan, String> formatter) {
        for (int i = 0; i < Math.min(5, organs.size()); i++) {
            System.out.println(formatter.apply(organs.get(i)));
        }
        if (organs.size() > 5) {
            System.out.printf("[...%d more organs...]\n", organs.size() - 5);
        }
    }
}
