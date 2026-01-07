package com.mycompany.TBS;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BookingRepository {
    // Custom Comparator to sort numerically by the ID number
    public static Map<String, String> bookings = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            // Extracts numbers from "BK-101" to compare 101 vs 102
            int num1 = Integer.parseInt(s1.replace("BK-", ""));
            int num2 = Integer.parseInt(s2.replace("BK-", ""));
            return Integer.compare(num1, num2);
        }
    });
    
    // Load existing bookings from the database immediately
    static {
        bookings.putAll(DatabaseManager.loadBookings());
    }
    
    public static Map<String, String> currentSession = new HashMap<>();

    public static String generateID() {
        int nextNum = 101; 
        if (!bookings.isEmpty()) {
            // lastKey() now returns the highest numerical ID
            String lastKey = ((TreeMap<String, String>) bookings).lastKey();
            nextNum = Integer.parseInt(lastKey.replace("BK-", "")) + 1;
        }
        return "BK-" + nextNum;
    }

    public static void add(String id, String details) {
        bookings.put(id, details);
        DatabaseManager.saveBooking(id, details);
    }

    public static void delete(String id) {
        bookings.remove(id);
        DatabaseManager.deleteBooking(id);
    }

    public static Map<String, String> getAll() {
        return bookings;
    }
    
    public static void clearSession() {
        currentSession.clear();
    }
}