/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuexhibitionsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class logInForm extends JFrame {  
    private JTextField usernameField;
    private JPasswordField passwordField;

    public logInForm() {
        setTitle("Admin Login");
        setSize(400, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());  // Better layout control
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username components
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);
        
        // Password components
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        JButton loginBtn = new JButton("Login");
    
        loginBtn.setForeground(Color.BLUE);
        loginBtn.setFocusPainted(true);
        loginBtn.setPreferredSize(new Dimension(100, 30));
        mainPanel.add(loginBtn, gbc);
        
        add(mainPanel);
        
        // Login action
        loginBtn.addActionListener(this::performLogin);
        
        // Add Enter key listener
        passwordField.addActionListener(this::performLogin);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        
        // Clear password from memory
        java.util.Arrays.fill(passwordChars, '0');
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password",
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM Admins WHERE Username=? AND Password=?")) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Login Successful", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Open admin dashboard
                    SwingUtilities.invokeLater(() -> {
                        new adminDashBoard().setVisible(true);
                        dispose();
                    });
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid username or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Database Error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                logInForm form = new logInForm();
                form.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}