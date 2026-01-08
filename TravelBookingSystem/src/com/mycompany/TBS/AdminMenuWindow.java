package com.mycompany.TBS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminMenuWindow extends JFrame implements ActionListener {
    JButton manageDataBtn, logoutBtn;

    public AdminMenuWindow() {
        setTitle("Admin Panel");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- ADDED THIS LINE ---
        // This ensures the entire application process terminates when X is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        manageDataBtn = new JButton("Manage Bookings & Options");
        manageDataBtn.setBounds(80, 80, 240, 50);
        manageDataBtn.addActionListener(this);
        add(manageDataBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(140, 180, 120, 35);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageDataBtn) {
            new AdminDataWindow();
            dispose();
        } else if (e.getSource() == logoutBtn) {
            new LoginWindow();
            dispose();
        }
    }
}