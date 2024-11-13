package com.cybernetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class OrganInventory {
    private ArrayList<CyberneticOrgan> organs;
    private int maxCapacity;
    private static final Random random = new Random();

    public OrganInventory() {
        this.organs = new ArrayList<>();
        this.maxCapacity = 1000;
    }

    public void addOrgan(CyberneticOrgan organ) {
        if (organs.size() >= maxCapacity)
            throw new IllegalArgumentException("Inventory at max capacity.");
        organs.add(organ);

        // Introduce randomness: 15% chance to shuffle the list after adding an organ
        if (random.nextDouble() < 0.15) { // 15% probability
            Collections.shuffle(organs, random);
            System.out.println("[DEBUG] OrganInventory: Shuffled the organ list.");
        }
    }

    public void removeOrgan(String organId, String reason) {
        CyberneticOrgan organ = findOrganById(organId);
        if (organ != null && organ.getStatus().equals("AVAILABLE")) {
            organs.remove(organ);
            // Log removal with reason if needed
            System.out.println("[DEBUG] OrganInventory: Removed organ " + organId + " due to " + reason + ".");
        } else {
            throw new IllegalArgumentException("Organ cannot be removed.");
        }
    }

    public ArrayList<CyberneticOrgan> sortByPowerLevel() {
        ArrayList<CyberneticOrgan> sortedList = new ArrayList<>(organs);
        quickSort(sortedList, 0, sortedList.size() - 1,
                Comparator.comparingInt(CyberneticOrgan::getPowerLevel).reversed());
        return sortedList;
    }

    public ArrayList<CyberneticOrgan> sortByManufactureDate() {
        ArrayList<CyberneticOrgan> sortedList = new ArrayList<>(organs);
        mergeSort(sortedList, Comparator.comparing(CyberneticOrgan::getManufactureDate).reversed());
        return sortedList;
    }

    public ArrayList<CyberneticOrgan> sortByCompatibilityScore() {
        ArrayList<CyberneticOrgan> sortedList = new ArrayList<>(organs);
        bubbleSort(sortedList,
                Comparator.comparingDouble(CyberneticOrgan::getCompatibilityScore).reversed());
        return sortedList;
    }

    private CyberneticOrgan findOrganById(String organId) {
        return organs.stream().filter(o -> o.getId().equals(organId)).findFirst().orElse(null);
    }

    private void quickSort(ArrayList<CyberneticOrgan> list, int low, int high,
                           Comparator<CyberneticOrgan> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private int partition(ArrayList<CyberneticOrgan> list, int low, int high,
                          Comparator<CyberneticOrgan> comparator) {
        CyberneticOrgan pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) > 0) {
                i++;
                CyberneticOrgan temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        CyberneticOrgan temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    private void mergeSort(ArrayList<CyberneticOrgan> list,
                           Comparator<CyberneticOrgan> comparator) {
        if (list.size() < 2)
            return;
        int mid = list.size() / 2;
        ArrayList<CyberneticOrgan> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<CyberneticOrgan> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(list, left, right, comparator);
    }

    private void merge(ArrayList<CyberneticOrgan> list, ArrayList<CyberneticOrgan> left,
                      ArrayList<CyberneticOrgan> right,
                      Comparator<CyberneticOrgan> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) > 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }
        while (i < left.size())
            list.set(k++, left.get(i++));
        while (j < right.size())
            list.set(k++, right.get(j++));
    }

    private void bubbleSort(ArrayList<CyberneticOrgan> list,
                            Comparator<CyberneticOrgan> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // Optimization to stop if no swaps occur
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) < 0) {
                    CyberneticOrgan temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }
}
