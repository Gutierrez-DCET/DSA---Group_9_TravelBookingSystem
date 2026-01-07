package com.mycompany.TBS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AdminDataWindow extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnBookings, btnOptions, btnEdit, btnDelete, btnBack;
    private String currentView = "BOOKINGS"; 

    public AdminDataWindow() {
        setTitle("Admin Spreadsheet View");
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        // --- KEY CHANGE: Closes the whole program when X is pressed ---
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        
        setLayout(new BorderLayout());

        // Sidebar Navigation
        JPanel sidebar = new JPanel(new GridLayout(7, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnBookings = new JButton("View Bookings");
        btnOptions = new JButton("View Options");
        btnEdit = new JButton("Edit Selected");
        btnDelete = new JButton("Delete Selected");
        btnBack = new JButton("Back to Menu");

        sidebar.add(btnBookings);
        sidebar.add(btnOptions);
        sidebar.add(new JSeparator()); 
        sidebar.add(btnEdit);
        sidebar.add(btnDelete);
        sidebar.add(btnBack);
        add(sidebar, BorderLayout.WEST);

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Action Listeners ---
        
        btnBookings.addActionListener(e -> refreshBookings());
        btnOptions.addActionListener(e -> refreshOptions());
        
        // Back Button logic: We change to DISPOSE so it doesn't kill the app when navigating back
        btnBack.addActionListener(e -> { 
            new AdminMenuWindow(); 
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
            dispose(); 
        });
        
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }

            String firstCol = model.getValueAt(row, 0).toString(); 
            String secondCol = model.getValueAt(row, 1).toString(); 
            String newValue = JOptionPane.showInputDialog(this, "Edit Value:", secondCol);
            
            if (newValue != null && !newValue.trim().isEmpty()) {
                if (currentView.equals("BOOKINGS")) {
                    BookingRepository.add(firstCol, newValue); 
                    refreshBookings();
                } else {
                    OptionsRepository.delete(firstCol, secondCol);
                    OptionsRepository.add(firstCol, newValue);
                    refreshOptions();
                }
            }
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            String firstCol = model.getValueAt(row, 0).toString();
            String secondCol = model.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (currentView.equals("BOOKINGS")) {
                    BookingRepository.delete(firstCol);
                    refreshBookings();
                } else {
                    OptionsRepository.delete(firstCol, secondCol);
                    refreshOptions();
                }
            }
        });

        refreshBookings(); 
        setVisible(true);
    }

    private void refreshBookings() {
        currentView = "BOOKINGS";
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Booking Code", "Value / Details"});
        
        // Sorting numerically (101, 102, 103)
        for (Map.Entry<String, String> entry : BookingRepository.getAll().entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void refreshOptions() {
        currentView = "OPTIONS";
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Category", "Option Value"});

        Map<String, List<String>> rawOptions = DatabaseManager.loadOptions();
        TreeMap<String, List<String>> sortedOptions = new TreeMap<>(rawOptions);

        for (Map.Entry<String, List<String>> entry : sortedOptions.entrySet()) {
            String category = entry.getKey();
            for (String value : entry.getValue()) {
                model.addRow(new Object[]{category, value});
            }
        }
    }
}