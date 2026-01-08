package com.mycompany.TBS;

import javax.swing.*;
import java.awt.event.*;

public class HotelWindow extends JFrame implements ActionListener {
    private JComboBox<String> hotelBox, ratingFilter, roomBox;
    private JTextField checkIn, checkOut;
    private JButton saveBtn;

    public HotelWindow() {
        setTitle("Hotel Selection");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel filterLabel = new JLabel("Filter Rating:");
        filterLabel.setBounds(50, 30, 100, 25);
        add(filterLabel);
        
        String[] ratings = {"All Ratings", "5", "4", "3", "2", "1"};
        ratingFilter = new JComboBox<>(ratings);
        ratingFilter.setBounds(150, 30, 220, 28);
        ratingFilter.addActionListener(e -> updateHotelList());
        add(ratingFilter);

        addLabel("Hotel:", 80);
        hotelBox = new JComboBox<>();
        hotelBox.setBounds(150, 80, 220, 28);
        add(hotelBox);
        updateHotelList();

        addLabel("Room Type:", 130);
        roomBox = new JComboBox<>(OptionsRepository.get("Room"));
        roomBox.setBounds(150, 130, 220, 28);
        add(roomBox);

        addLabel("Check-in:", 180);
        checkIn = new JTextField();
        checkIn.setBounds(150, 180, 220, 28);
        add(checkIn);

        addLabel("Check-out:", 230);
        checkOut = new JTextField();
        checkOut.setBounds(150, 230, 220, 28);
        add(checkOut);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 300, 150, 35);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    private void addLabel(String t, int y) {
        JLabel l = new JLabel(t); l.setBounds(50, y, 100, 25); add(l);
    }

    private void updateHotelList() {
        hotelBox.removeAllItems();
        String selectedRating = (String) ratingFilter.getSelectedItem();
        for (String h : OptionsRepository.get("Hotel")) {
            if (h.contains("|")) {
                String[] p = h.split("\\|");
                if (selectedRating.equals("All Ratings") || p[1].equals(selectedRating)) {
                    hotelBox.addItem(p[0] + " [" + p[1] + " Stars]");
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (hotelBox.getSelectedItem() == null || roomBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Select a hotel and room type!");
            return;
        }
        String selection = hotelBox.getSelectedItem().toString();
        // Extract clean name
        String hotelName = selection.contains(" [") ? selection.split(" \\[")[0] : selection;
        
        BookingRepository.currentSession.put("Hotel", hotelName);
        BookingRepository.currentSession.put("RoomType", roomBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("CheckIn", checkIn.getText());
        BookingRepository.currentSession.put("CheckOut", checkOut.getText());
        dispose();
    }
}