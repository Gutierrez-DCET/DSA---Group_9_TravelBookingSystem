package com.mycompany.TBS;

import javax.swing.*;
import java.awt.event.*;

public class FlightWindow extends JFrame implements ActionListener {
    private JTextField dateField;
    private JComboBox<String> destinationBox, paymentBox, departureBox, seatBox;
    private JButton saveBtn;

    public FlightWindow() {
        setTitle("Flight Selection");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        addLabel("Departure:", 50);
        departureBox = new JComboBox<>(OptionsRepository.get("Departure"));
        departureBox.setBounds(150, 50, 220, 28);
        add(departureBox);

        addLabel("Destination:", 100);
        destinationBox = new JComboBox<>(OptionsRepository.get("Destination"));
        destinationBox.setBounds(150, 100, 220, 28);
        add(destinationBox);

        addLabel("Seat Type:", 150);
        // Dynamically loaded from Admin Options
        seatBox = new JComboBox<>(OptionsRepository.get("Seat"));
        seatBox.setBounds(150, 150, 220, 28);
        add(seatBox);

        addLabel("Date:", 200);
        dateField = new JTextField();
        dateField.setBounds(150, 200, 220, 28);
        add(dateField);

        addLabel("Payment:", 250);
        paymentBox = new JComboBox<>(OptionsRepository.get("Payment"));
        paymentBox.setBounds(150, 250, 220, 28);
        add(paymentBox);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 320, 150, 35);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    private void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(50, y, 100, 25);
        add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (seatBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Admin must add Seat types first!");
            return;
        }
        BookingRepository.currentSession.put("Departure", departureBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("Destination", destinationBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("SeatType", seatBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("FlightDate", dateField.getText());
        BookingRepository.currentSession.put("Payment", paymentBox.getSelectedItem().toString());
        dispose();
    }
}