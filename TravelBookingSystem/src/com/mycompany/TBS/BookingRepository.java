package com.mycompany.TBS;

import java.util.*;

public class BookingRepository {
    public static BookingBST dateIndex = new BookingBST();

    public static Map<String, String> bookings = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            int num1 = Integer.parseInt(s1.replace("BK-", ""));
            int num2 = Integer.parseInt(s2.replace("BK-", ""));
            return Integer.compare(num1, num2);
        }
    });
    
    static {
        refreshFromDatabase();
    }

    public static void refreshFromDatabase() {
        bookings.clear();
        dateIndex.clear();
        Map<String, String> data = DatabaseManager.loadBookings();
        bookings.putAll(data);
        
        for (Map.Entry<String, String> entry : data.entrySet()) {
            updateBST(entry.getKey(), entry.getValue());
        }
    }

    private static void updateBST(String id, String details) {
        String[] p = details.split(";");
        // We expect the flight info (e.g., "NYC to LON on 2025-10-10") at index 3
        if (p.length > 3 && p[3].contains(" on ")) {
            String[] flightParts = p[3].split(" on ");
            if (flightParts.length > 1) {
                dateIndex.insert(flightParts[1].trim(), id);
            }
        }
    }
    
    public static Map<String, String> currentSession = new HashMap<>();

    public static String generateID() {
        int nextNum = 101; 
        if (!bookings.isEmpty()) {
            String lastKey = ((TreeMap<String, String>) bookings).lastKey();
            nextNum = Integer.parseInt(lastKey.replace("BK-", "")) + 1;
        }
        return "BK-" + nextNum;
    }

    public static void add(String id, String details) {
        bookings.put(id, details);
        DatabaseManager.saveBooking(id, details);
        updateBST(id, details);
    }

    public static void delete(String id) {
        bookings.remove(id);
        DatabaseManager.deleteBooking(id);
        // For a full BST implementation, a deleteRec would be added to BookingBST,
        // but for simplicity, we refresh index on significant changes.
        refreshFromDatabase();
    }

    public static Map<String, String> getAll() {
        return bookings;
    }

    public static void clearSession() {
        currentSession.clear();
    }
}