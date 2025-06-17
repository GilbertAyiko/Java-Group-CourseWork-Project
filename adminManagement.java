/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuexhibitionsystem;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class adminManagement extends JFrame {
    JTextField userField;
    JPasswordField passField;
    JButton saveBtn, clearBtn;

    public adminManagement() {
        setTitle("Admin Management");
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Add New Admin", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.BLUE);
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        formPanel.add(new JLabel("New Username:"));
        userField = new JTextField();
        formPanel.add(userField);

        formPanel.add(new JLabel("New Password:"));
        passField = new JPasswordField();
        formPanel.add(passField);
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        saveBtn = new JButton("Save Admin");
        clearBtn = new JButton("Clear");
        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Save button action
        saveBtn.addActionListener(e -> saveAdmin());

        // Clear button action
        clearBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
        });

        setVisible(true);  
    }

    private void saveAdmin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO Admins (Username, Password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "New admin saved successfully!");
                userField.setText("");
                passField.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add admin. Please check database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}