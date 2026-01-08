package com.mycompany.TBS;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminDataWindow extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnBookings, btnOptions, btnAdd, btnEdit, btnDelete, btnBack;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private String currentView = "BOOKINGS"; 
    private boolean isRefreshing = false; 

    public AdminDataWindow() {
        setTitle("Admin Spreadsheet View - Comprehensive Management");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 
        setLayout(new BorderLayout());

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // --- Sidebar ---
        JPanel sidebar = new JPanel(new GridLayout(8, 1, 10, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnBookings = new JButton("View Bookings");
        btnOptions = new JButton("View Options");
        btnAdd = new JButton("Add New Option");
        btnEdit = new JButton("Edit Selected");
        btnDelete = new JButton("Delete Selected");
        btnBack = new JButton("Back to Menu");
        sidebar.add(btnBookings); sidebar.add(btnOptions); sidebar.add(new JSeparator());
        sidebar.add(btnAdd); sidebar.add(btnEdit); sidebar.add(btnDelete); sidebar.add(btnBack);
        add(sidebar, BorderLayout.WEST);

        // --- Table Setup ---
        model = new DefaultTableModel();
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Inline Cell Editing Listener
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && !isRefreshing) {
                handleCellEdit(e.getFirstRow());
            }
        });

        // Search logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Button Listeners
        btnBookings.addActionListener(e -> refreshBookings());
        btnOptions.addActionListener(e -> refreshOptions());
        
        btnAdd.addActionListener(e -> {
            if (currentView.equals("OPTIONS")) {
                String[] categories = {"Hotel", "Departure", "Destination", "Payment", "Seat", "Room"};
                String cat = (String) JOptionPane.showInputDialog(this, "Category:", "Add", 3, null, categories, categories[0]);
                if (cat != null) {
                    String name = JOptionPane.showInputDialog(this, "Enter Name/Type:");
                    String val = JOptionPane.showInputDialog(this, "Enter Price/Rating:");
                    if (name != null && val != null) {
                        OptionsRepository.add(cat, name.trim() + "|" + val.trim());
                        refreshOptions();
                    }
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) table.editCellAt(row, table.getSelectedColumn());
        });

        btnDelete.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow == -1) return;
            int modelRow = table.convertRowIndexToModel(viewRow);
            String idOrCat = model.getValueAt(modelRow, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "Delete this?") == JOptionPane.YES_OPTION) {
                if (currentView.equals("BOOKINGS")) {
                    BookingRepository.delete(idOrCat);
                    refreshBookings();
                } else {
                    OptionsRepository.delete(idOrCat, model.getValueAt(modelRow, 1).toString());
                    refreshOptions();
                }
            }
        });

        btnBack.addActionListener(e -> { new AdminMenuWindow(); dispose(); });
        
        refreshBookings();
        setVisible(true);
    }

    private void handleCellEdit(int row) {
        String idOrCat = model.getValueAt(row, 0).toString();
        if (currentView.equals("BOOKINGS")) {
            // Re-stitch columns back into the " | " format for DB storage
            String name = model.getValueAt(row, 1).toString();
            String email = model.getValueAt(row, 2).toString();
            String flightRoute = model.getValueAt(row, 3).toString();
            String flightDate = model.getValueAt(row, 4).toString();
            String hotelName = model.getValueAt(row, 5).toString();
            String checkIn = model.getValueAt(row, 6).toString();
            String checkOut = model.getValueAt(row, 7).toString();
            String pay = model.getValueAt(row, 8).toString();

            String fullDetails = "Name: " + name + " | " +
                                 "Email: " + email + " | " +
                                 "Flight: " + flightRoute + " on " + flightDate + " | " +
                                 "Hotel: " + hotelName + " (In: " + checkIn + ", Out: " + checkOut + ") | " +
                                 "Pay: " + pay;

            BookingRepository.add(idOrCat, fullDetails);
        }
    }

    private void refreshBookings() {
        isRefreshing = true;
        currentView = "BOOKINGS";
        model.setRowCount(0);
        
        // Headers with separate date columns
        model.setColumnIdentifiers(new String[]{
            "ID", "Name", "Email", "Flight Route", "Flight Date", "Hotel", "Check-In", "Check-Out", "Payment"
        });

        for (Map.Entry<String, String> entry : BookingRepository.getAll().entrySet()) {
            String[] parts = entry.getValue().split(" \\| ");
            Object[] row = new Object[9];
            row[0] = entry.getKey();
            
            if (parts.length >= 5) {
                row[1] = parts[0].replace("Name: ", "");
                row[2] = parts[1].replace("Email: ", "");
                
                // Parse Flight and Date
                String flightRaw = parts[2].replace("Flight: ", "");
                if (flightRaw.contains(" on ")) {
                    String[] fSub = flightRaw.split(" on ");
                    row[3] = fSub[0]; 
                    row[4] = fSub[1];
                } else {
                    row[3] = flightRaw; row[4] = "";
                }

                // Parse Hotel, Check-in, and Check-out
                String hotelRaw = parts[3].replace("Hotel: ", "");
                if (hotelRaw.contains(" (In: ")) {
                    row[5] = hotelRaw.split(" \\(")[0];
                    try {
                        String datesSub = hotelRaw.substring(hotelRaw.indexOf("(In: ") + 5, hotelRaw.indexOf(")"));
                        String[] dSub = datesSub.split(", Out: ");
                        row[6] = dSub[0];
                        row[7] = dSub[1];
                    } catch (Exception ex) {
                        row[6] = ""; row[7] = "";
                    }
                } else {
                    row[5] = hotelRaw; row[6] = ""; row[7] = "";
                }

                row[8] = parts[4].replace("Pay: ", "");
            }
            model.addRow(row);
        }
        isRefreshing = false;
    }

    private void refreshOptions() {
        isRefreshing = true;
        currentView = "OPTIONS";
        model.setRowCount(0);
        model.setColumnIdentifiers(new String[]{"Category", "Name/Value", "Details"});
        Map<String, List<String>> opt = DatabaseManager.loadOptions();
        for (Map.Entry<String, List<String>> entry : opt.entrySet()) {
            for (String v : entry.getValue()) {
                if (v.contains("|")) {
                    String[] p = v.split("\\|");
                    model.addRow(new Object[]{entry.getKey(), p[0], p[1]});
                } else {
                    model.addRow(new Object[]{entry.getKey(), v, "-"});
                }
            }
        }
        isRefreshing = false;
    }
}