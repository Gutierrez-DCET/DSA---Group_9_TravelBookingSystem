package com.mycompany.TBS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame implements ActionListener {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn, registerBtn;

    public LoginWindow() {
        setTitle("Travel Booking System - Login");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Travel Booking System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(60, 20, 300, 40);
        add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 100, 100, 25);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(150, 100, 200, 28);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 150, 100, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(150, 150, 200, 28);
        add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 210, 120, 35);
        loginBtn.addActionListener(this);
        add(loginBtn);

        registerBtn = new JButton("Register Account");
        registerBtn.setBounds(130, 260, 160, 30);
        registerBtn.addActionListener(this);
        add(registerBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (e.getSource() == loginBtn) {
            // Admin Auto-Detect (Hardcoded for security)
            if (username.equals("admin") && password.equals("admin123")) {
                new AdminMenuWindow();
                dispose();
            } 
            // Check database for regular users
            else if (DatabaseManager.checkUser(username, password)) {
                new MainMenuWindow();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or account doesn't exist!");
            }
        } else if (e.getSource() == registerBtn) {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username and password to register.");
            } else {
                if (DatabaseManager.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration Successful! You can now login.");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already taken.");
                }
            }
        }
    }

    // THIS METHOD TURNS THE CLASS INTO A "MAIN FILE"
    public static void main(String[] args) {
        try {
            // Set System Look and Feel for a modern appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            new LoginWindow();
        });
    }
}