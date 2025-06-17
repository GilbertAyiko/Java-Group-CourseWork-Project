/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuexhibitionsystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class adminDashBoard extends JFrame {
    
    public adminDashBoard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with border and padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 0)); // Dark green color
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel with grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Create buttons
        JButton registerBtn = createStyledButton("Register Student");
        JButton manageAdminBtn = createStyledButton("Admin Management");
        JButton viewParticipantsBtn = createStyledButton("View Participants");

        // Add buttons to panel
        buttonPanel.add(registerBtn);
        buttonPanel.add(manageAdminBtn);
        buttonPanel.add(viewParticipantsBtn);

        // Add button panel to main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Button actions
        registerBtn.addActionListener(e -> new registrationForm().setVisible(true));
        manageAdminBtn.addActionListener(e -> new adminManagement().setVisible(true));
        viewParticipantsBtn.addActionListener(e -> new viewParticipants().setVisible(true));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLACK); // Black text color
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new adminDashBoard().setVisible(true));
    }
}
