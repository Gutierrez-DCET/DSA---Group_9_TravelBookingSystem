package com.mycompany.TBS;

import javax.swing.*;
import java.awt.event.*;

public class CustomerWindow extends JFrame implements ActionListener {
    private JTextField nameField, emailField;
    private JButton saveBtn;

    public CustomerWindow() {
        setTitle("Customer Info");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 80, 100, 25);
        add(nameLabel);

        nameField = new JTextField(BookingRepository.currentSession.getOrDefault("Name", ""));
        nameField.setBounds(150, 80, 200, 28);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 125, 100, 25);
        add(emailLabel);

        emailField = new JTextField(BookingRepository.currentSession.getOrDefault("Email", ""));
        emailField.setBounds(150, 125, 200, 28);
        add(emailField);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 180, 120, 35);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        BookingRepository.currentSession.put("Name", nameField.getText());
        BookingRepository.currentSession.put("Email", emailField.getText());
        dispose();
    }
}