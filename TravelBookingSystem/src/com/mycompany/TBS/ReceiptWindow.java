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
        area.append("========= YOUR BOOKING RECEIPTS =========\n\n");
        
        Map<String, String> all = BookingRepository.getAll();
        if (all.isEmpty()) {
            area.append("No finalized bookings found.");
        } else {
            for (Map.Entry<String, String> entry : all.entrySet()) {
                String raw = entry.getValue();
                area.append("ID: " + entry.getKey() + "\n");
                
                if (raw.contains(";")) {
                    String[] p = raw.split(";");
                    area.append("Customer: " + p[0] + " (" + p[1] + ")\n");
                    area.append("Flight  : " + p[3] + " [" + p[4] + " Seat]\n");
                    area.append("Hotel   : " + p[5] + " [" + p[6] + ", " + p[7] + " Rooms]\n");
                    area.append("Total   : " + p[8] + " (for " + p[2] + " people)\n");
                } else {
                    area.append("Details : " + raw + "\n");
                }
                area.append("-------------------------------------------\n");
            }
        }
        
        add(new JScrollPane(area));
        setVisible(true);
    }
}