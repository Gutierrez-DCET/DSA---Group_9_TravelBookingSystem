package com.mycompany.TBS;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class CustomerWindow extends JFrame implements ActionListener {
    private JTextField nameField, emailField, peopleField;
    private JButton saveBtn;

    public CustomerWindow() {
        setTitle("Customer Info");
        setSize(420, 350);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 50, 100, 25);
        add(nameLabel);

        nameField = new JTextField(BookingRepository.currentSession.getOrDefault("Name", ""));
        nameField.setBounds(150, 50, 200, 28);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 95, 100, 25);
        add(emailLabel);

        emailField = new JTextField(BookingRepository.currentSession.getOrDefault("Email", ""));
        emailField.setBounds(150, 95, 200, 28);
        add(emailField);

        JLabel peopleLabel = new JLabel("No. of People:");
        peopleLabel.setBounds(50, 140, 100, 25);
        add(peopleLabel);

        peopleField = new JTextField(BookingRepository.currentSession.getOrDefault("People", "1"));
        peopleField.setBounds(150, 140, 200, 28);
        
        // Restrict input to digits only
        ((AbstractDocument) peopleField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches("\\d*")) super.replace(fb, offset, length, text, attrs);
            }
        });
        add(peopleField);

        saveBtn = new JButton("Save & Back");
        saveBtn.setBounds(150, 210, 120, 35);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String pText = peopleField.getText();
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || pText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        int pCount = Integer.parseInt(pText);
        if (pCount < 1 || pCount > 15) {
            JOptionPane.showMessageDialog(this, "Number of people must be between 1 and 15.");
            return;
        }

        BookingRepository.currentSession.put("Name", nameField.getText());
        BookingRepository.currentSession.put("Email", emailField.getText());
        BookingRepository.currentSession.put("People", pText);
        dispose();
    }
}