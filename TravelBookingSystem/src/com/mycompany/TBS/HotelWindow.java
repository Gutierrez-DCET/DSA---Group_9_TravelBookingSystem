package com.mycompany.TBS;

import javax.swing.*;
import java.awt.event.*;

public class HotelWindow extends JFrame implements ActionListener {
    private JComboBox<String> hotelBox, paymentBox;
    private JTextField checkIn, checkOut;
    private JButton saveBtn;

    public HotelWindow() {
        setTitle("Hotel Selection");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel hotelLabel = new JLabel("Hotel:");
        hotelLabel.setBounds(50, 80, 100, 25);
        add(hotelLabel);
        hotelBox = new JComboBox<>(OptionsRepository.get("Hotel"));
        hotelBox.setBounds(150, 80, 220, 28);
        add(hotelBox);

        JLabel inLabel = new JLabel("Check-in:");
        inLabel.setBounds(50, 125, 100, 25);
        add(inLabel);
        checkIn = new JTextField();
        checkIn.setBounds(150, 125, 220, 28);
        add(checkIn);

        JLabel outLabel = new JLabel("Check-out:");
        outLabel.setBounds(50, 170, 100, 25);
        add(outLabel);
        checkOut = new JTextField();
        checkOut.setBounds(150, 170, 220, 28);
        add(checkOut);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 280, 150, 35);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (hotelBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Admin must add hotel options first!");
            return;
        }
        BookingRepository.currentSession.put("Hotel", hotelBox.getSelectedItem().toString());
        BookingRepository.currentSession.put("CheckIn", checkIn.getText());
        BookingRepository.currentSession.put("CheckOut", checkOut.getText());
        dispose();
    }
}