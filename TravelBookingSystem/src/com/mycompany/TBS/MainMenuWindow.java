package com.mycompany.TBS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class MainMenuWindow extends JFrame implements ActionListener {

    JButton customerBtn, flightBtn, hotelBtn, receiptBtn, registerBookingBtn, logoutBtn;

    public MainMenuWindow() {
        setTitle("Main Menu");
        setSize(430, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Main Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(150, 20, 200, 40);
        add(title);

        customerBtn = new JButton("Customer Registration");
        customerBtn.setBounds(100, 80, 220, 40);
        flightBtn = new JButton("Flight Reservation");
        flightBtn.setBounds(100, 130, 220, 40);
        hotelBtn = new JButton("Hotel Booking");
        hotelBtn.setBounds(100, 180, 220, 40);
        receiptBtn = new JButton("View Receipt");
        receiptBtn.setBounds(100, 230, 220, 40);
        
        registerBookingBtn = new JButton("Register Final Booking");
        registerBookingBtn.setBounds(100, 300, 220, 40);
        registerBookingBtn.setBackground(new Color(144, 238, 144));
        
        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(150, 380, 120, 35);

        customerBtn.addActionListener(this);
        flightBtn.addActionListener(this);
        hotelBtn.addActionListener(this);
        receiptBtn.addActionListener(this);
        registerBookingBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        add(customerBtn); add(flightBtn); add(hotelBtn); 
        add(receiptBtn); add(registerBookingBtn); add(logoutBtn);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == customerBtn) new CustomerWindow();
        else if (e.getSource() == flightBtn) new FlightWindow();
        else if (e.getSource() == hotelBtn) new HotelWindow();
        else if (e.getSource() == receiptBtn) new ReceiptWindow();
        else if (e.getSource() == logoutBtn) { new LoginWindow(); dispose(); }
        else if (e.getSource() == registerBookingBtn) {
            Map<String, String> s = BookingRepository.currentSession;
            
            // Validation: Ensure all necessary parts are filled before registering
            if (s.containsKey("Name") && s.containsKey("Departure") && s.containsKey("Hotel")) {
                String id = BookingRepository.generateID();
                
                // UPDATED: Using " | " consistently so AdminDataWindow can split this into distinct columns
                String fullDetails = "Name: " + s.get("Name") + " | " +
                                     "Email: " + s.get("Email") + " | " +
                                     "Flight: " + s.get("Departure") + " to " + s.get("Destination") + " on " + s.get("FlightDate") + " | " +
                                     "Hotel: " + s.get("Hotel") + " (In: " + s.get("CheckIn") + ", Out: " + s.get("CheckOut") + ") | " +
                                     "Pay: " + s.get("Payment");
                
                BookingRepository.add(id, fullDetails);
                BookingRepository.clearSession();
                JOptionPane.showMessageDialog(this, "Booking Successful! ID: " + id);
            } else {
                JOptionPane.showMessageDialog(this, "Please complete Customer, Flight, and Hotel details first!");
            }
        }
    }
}