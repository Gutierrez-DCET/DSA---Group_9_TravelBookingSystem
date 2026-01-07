package com.mycompany.TBS;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:tbs.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            createTables();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void createTables() {
        try (Connection c = DriverManager.getConnection(DB_URL);
             Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS bookings (id TEXT PRIMARY KEY, details TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS options (category TEXT, value TEXT, UNIQUE(category, value))");
            s.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Bookings ---
    public static HashMap<String, String> loadBookings() {
        HashMap<String, String> map = new HashMap<>();
        try (Connection c = DriverManager.getConnection(DB_URL);
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM bookings")) {
            while (rs.next()) map.put(rs.getString("id"), rs.getString("details"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public static void saveBooking(String id, String details) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("INSERT OR REPLACE INTO bookings VALUES (?, ?)")) {
            ps.setString(1, id);
            ps.setString(2, details);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteBooking(String id) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("DELETE FROM bookings WHERE id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Options ---
    public static Map<String, List<String>> loadOptions() {
        Map<String, List<String>> options = new HashMap<>();
        String[] cats = {"Hotel", "Departure", "Destination", "Payment"};
        for(String cat : cats) options.put(cat, new ArrayList<>());

        try (Connection c = DriverManager.getConnection(DB_URL);
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM options")) {
            while (rs.next()) {
                String cat = rs.getString("category");
                if(options.containsKey(cat)) options.get(cat).add(rs.getString("value"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return options;
    }

    public static void saveOption(String category, String value) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("INSERT OR IGNORE INTO options VALUES (?, ?)")) {
            ps.setString(1, category);
            ps.setString(2, value);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteOption(String category, String value) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("DELETE FROM options WHERE category=? AND value=?")) {
            ps.setString(1, category);
            ps.setString(2, value);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Users ---
    public static boolean registerUser(String u, String p) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("INSERT INTO users VALUES (?, ?)")) {
            ps.setString(1, u); ps.setString(2, p);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean checkUser(String u, String p) {
        try (Connection c = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
            ps.setString(1, u); ps.setString(2, p);
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }
}