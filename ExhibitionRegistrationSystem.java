// ExhibitionRegistrationSystem.java - Main application class
package exhibition.registration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExhibitionRegistrationSystem extends JFrame {
    private JTextField txtRegId, txtName, txtFaculty, txtProject, txtContact, txtEmail;
    private JLabel lblImage;
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit, btnUpload;
    private String imagePath;
    
    private Connection connection;
    
    public ExhibitionRegistrationSystem() {
        initializeUI();
        connectToDatabase();
    }
    
    private void initializeUI() {
        setTitle("Victoria University Exhibition Registration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        txtRegId = new JTextField();
        txtName = new JTextField();
        txtFaculty = new JTextField();
        txtProject = new JTextField();
        txtContact = new JTextField();
        txtEmail = new JTextField();
        
        formPanel.add(new JLabel("Registration ID:"));
        formPanel.add(txtRegId);
        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Faculty:"));
        formPanel.add(txtFaculty);
        formPanel.add(new JLabel("Project Title:"));
        formPanel.add(txtProject);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(txtContact);
        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Project Image:"));
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnUpload = new JButton("Upload Image");
        btnUpload.addActionListener(this::uploadImage);
        imagePanel.add(btnUpload);
        formPanel.add(imagePanel);
        
        // Image display
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(300, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        btnRegister = new JButton("Register");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnExit = new JButton("Exit");
        
        btnRegister.addActionListener(this::registerParticipant);
        btnSearch.addActionListener(this::searchParticipant);
        btnUpdate.addActionListener(this::updateParticipant);
        btnDelete.addActionListener(this::deleteParticipant);
        btnClear.addActionListener(this::clearForm);
        btnExit.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnExit);
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(lblImage), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void connectToDatabase() {
        try {
            // Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            
            // Connect to the database
            String dbPath = "jdbc:ucanaccess://VUE_Exhibition.accdb";
            connection = DriverManager.getConnection(dbPath);
            
            // Create table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Participants (" +
                "RegistrationID TEXT PRIMARY KEY, " +
                "StudentName TEXT, " +
                "Faculty TEXT, " +
                "ProjectTitle TEXT, " +
                "ContactNumber TEXT, " +
                "Email TEXT, " +
                "ImagePath TEXT)";
                
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSQL);
                
                // Insert sample data if table is empty
                ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Participants");
                rs.next();
                if (rs.getInt(1) == 0) {
                    String[] sampleData = {
                        "INSERT INTO Participants VALUES ('REG001', 'John Doe', 'Science and Technology', 'Smart Campus', '0777123456', 'john@vu.edu', '')",
                        "INSERT INTO Participants VALUES ('REG002', 'Jane Smith', 'Business', 'E-Commerce Platform', '0777654321', 'jane@vu.edu', '')",
                        "INSERT INTO Participants VALUES ('REG003', 'Mike Johnson', 'Engineering', 'Solar Tracker', '0777111222', 'mike@vu.edu', '')",
                        "INSERT INTO Participants VALUES ('REG004', 'Sarah Williams', 'Health Sciences', 'Medical App', '0777333444', 'sarah@vu.edu', '')",
                        "INSERT INTO Participants VALUES ('REG005', 'David Brown', 'Arts', 'VR Gallery', '0777555666', 'david@vu.edu', '')"
                    };
                    
                    for (String sql : sampleData) {
                        statement.execute(sql);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void uploadImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePath = selectedFile.getAbsolutePath();
            
            // Display the image
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(
                lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        }
    }
    
    private boolean validateInput() {
        if (txtRegId.getText().isEmpty() || txtName.getText().isEmpty() || 
            txtFaculty.getText().isEmpty() || txtProject.getText().isEmpty() || 
            txtContact.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid email format", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Validate contact number (simple check for Uganda numbers)
        if (!txtContact.getText().matches("^07\\d{8}$")) {
            JOptionPane.showMessageDialog(this, "Contact number must be 10 digits starting with 07", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void registerParticipant(ActionEvent e) {
        if (!validateInput()) return;
        
        try {
            String sql = "INSERT INTO Participants (RegistrationID, StudentName, Faculty, " +
                "ProjectTitle, ContactNumber, Email, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, txtRegId.getText());
                pstmt.setString(2, txtName.getText());
                pstmt.setString(3, txtFaculty.getText());
                pstmt.setString(4, txtProject.getText());
                pstmt.setString(5, txtContact.getText());
                pstmt.setString(6, txtEmail.getText());
                pstmt.setString(7, imagePath != null ? imagePath : "");
                
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Participant registered successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Registration error: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void searchParticipant(ActionEvent e) {
        String regId = txtRegId.getText();
        if (regId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Registration ID to search", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, regId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        txtName.setText(rs.getString("StudentName"));
                        txtFaculty.setText(rs.getString("Faculty"));
                        txtProject.setText(rs.getString("ProjectTitle"));
                        txtContact.setText(rs.getString("ContactNumber"));
                        txtEmail.setText(rs.getString("Email"));
                        
                        String imgPath = rs.getString("ImagePath");
                        if (imgPath != null && !imgPath.isEmpty()) {
                            imagePath = imgPath;
                            ImageIcon icon = new ImageIcon(imagePath);
                            Image img = icon.getImage().getScaledInstance(
                                    lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                            lblImage.setIcon(new ImageIcon(img));
                        } else {
                            lblImage.setIcon(null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Participant not found",
                                "Not Found", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void updateParticipant(ActionEvent e) {
        if (!validateInput()) return;
        
        try {
            String sql = "UPDATE Participants SET StudentName = ?, Faculty = ?, " +
                "ProjectTitle = ?, ContactNumber = ?, Email = ?, ImagePath = ? " +
                "WHERE RegistrationID = ?";
                
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, txtName.getText());
                pstmt.setString(2, txtFaculty.getText());
                pstmt.setString(3, txtProject.getText());
                pstmt.setString(4, txtContact.getText());
                pstmt.setString(5, txtEmail.getText());
                pstmt.setString(6, imagePath != null ? imagePath : "");
                pstmt.setString(7, txtRegId.getText());
                
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Participant updated successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Participant not found",
                            "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void deleteParticipant(ActionEvent e) {
        String regId = txtRegId.getText();
        if (regId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Registration ID to delete", 
                                        "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this participant?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            String sql = "DELETE FROM Participants WHERE RegistrationID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, regId);
                
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Participant deleted successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Participant not found",
                            "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Delete error: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void clearForm(ActionEvent e) {
        clearForm();
    }
    
    private void clearForm() {
        txtRegId.setText("");
        txtName.setText("");
        txtFaculty.setText("");
        txtProject.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        lblImage.setIcon(null);
        imagePath = null;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExhibitionRegistrationSystem app = new ExhibitionRegistrationSystem();
            app.setVisible(true);
        });
    }
}
