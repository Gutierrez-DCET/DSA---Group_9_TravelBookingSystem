package com.mycompany.TBS;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ReceiptWindow extends JFrame {
    public ReceiptWindow() {
        setTitle("Booking Receipts");
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        area.append("========= ALL REGISTERED BOOKINGS =========\n\n");
        
        Map<String, String> all = BookingRepository.getAll();
        if (all.isEmpty()) {
            area.append("No finalized bookings found.");
        } else {
            for (Map.Entry<String, String> entry : all.entrySet()) {
                area.append("ID: " + entry.getKey() + "\n");
                area.append("DETAILS: " + entry.getValue() + "\n");
                area.append("-------------------------------------------\n");
            }
        }
        
        add(new JScrollPane(area));
        setVisible(true);
    }
}