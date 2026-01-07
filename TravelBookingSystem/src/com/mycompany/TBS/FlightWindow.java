package com.mycompany.TBS;

import javax.swing.*;
import java.awt.event.*;

public class FlightWindow extends JFrame implements ActionListener {
    private JTextField dateField;
    private JComboBox<String> destinationBox, paymentBox, departureBox;
    private JButton saveBtn;

    public FlightWindow() {
        setTitle("Flight Selection");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        addLabel("Departure:", 80);
        departureBox = new JComboBox<>(OptionsRepository.get("Departure"));
        departureBox.setBounds(150, 80, 220, 28);
        add(departureBox);

        addLabel("Destination:", 125);
        destinationBox = new JComboBox<>(OptionsRepository.get("Destination"));
        destinationBox.setBounds(150, 125, 220, 28);
        add(destinationBox);

        addLabel("Date:", 170);
        dateField = new JTextField();
        dateField.setBounds(150, 170, 220, 28);
        add(dateField);

        addLabel("Payment:", 215);
        paymentBox = new JComboBox<>(OptionsRepository.get("Payment"));
        paymentBox.setBounds(150, 215, 220, 28);
        add(paymentBox);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 280, 150, 35);
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
        if (departureBox.getSelectedItem() == null || destinationBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Admin must add flight options first!");
            return;
        }
        BookingRepository.currentSession.put("Departure", departureBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("Destination", destinationBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("FlightDate", dateField.getText());
        BookingRepository.currentSession.put("Payment", paymentBox.getSelectedItem().toString());
        dispose();
    }
}