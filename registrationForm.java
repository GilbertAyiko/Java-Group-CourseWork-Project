/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuexhibitionsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.regex.*;

public class registrationForm extends JFrame {

    private JTextField txtID, txtName, txtFaculty, txtTitle, txtContact, txtEmail, txtImagePath;
    private JLabel lblImage, lblImageTitle;
    private JButton btnSave, btnClear, btnSearch, btnUpdate, btnDelete, btnExit, btnUpload;

    public registrationForm() {
        setTitle("Student Registration Form");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("EXHIBITION REGISTRATION FORM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.BLUE);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtID = new JTextField(20);
        txtName = new JTextField(20);
        txtFaculty = new JTextField(20);
        txtTitle = new JTextField(20);
        txtContact = new JTextField(20);
        txtEmail = new JTextField(20);
        txtImagePath = new JTextField(20);
        txtImagePath.setEditable(false);
        btnUpload = new JButton("Upload Image");

        int row = 0;
        formPanelAdd(formPanel, gbc, row++, "Registration ID:", txtID);
        formPanelAdd(formPanel, gbc, row++, "Student Name:", txtName);
        formPanelAdd(formPanel, gbc, row++, "Faculty:", txtFaculty);
        formPanelAdd(formPanel, gbc, row++, "Project Title:", txtTitle);
        formPanelAdd(formPanel, gbc, row++, "Contact Number:", txtContact);
        formPanelAdd(formPanel, gbc, row++, "Email Address:", txtEmail);
        formPanelAdd(formPanel, gbc, row++, "Image Path:", txtImagePath);
        formPanelAdd(formPanel, gbc, row++, "Upload Image:", btnUpload);

        add(formPanel, BorderLayout.CENTER);

        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        lblImageTitle = new JLabel("Prototype", JLabel.CENTER);
        lblImageTitle.setForeground(Color.BLUE);
        lblImageTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(400, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        imagePanel.add(lblImageTitle, BorderLayout.NORTH);
        imagePanel.add(lblImage, BorderLayout.CENTER);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(imagePanel, BorderLayout.EAST);

        JPanel btnPanel = new JPanel(new GridLayout(1, 6, 8, 8));
        btnSave = new JButton("Save");
        btnSave.setBackground(Color.GREEN);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);

        btnClear = new JButton("Clear");
        btnClear.setBackground(Color.YELLOW);
        btnClear.setOpaque(true);
        btnClear.setBorderPainted(false);

        btnSearch = new JButton("Search");
        btnSearch.setBackground(Color.CYAN);
        btnSearch.setOpaque(true);
        btnSearch.setBorderPainted(false);

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(Color.ORANGE);
        btnUpdate.setOpaque(true);
        btnUpdate.setBorderPainted(false);

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(Color.PINK);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);

        btnExit = new JButton("Exit");
        btnExit.setBackground(Color.RED);
        btnExit.setOpaque(true);
        btnExit.setBorderPainted(false);

        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnPanel.add(btnSave); btnPanel.add(btnSearch); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnClear); btnPanel.add(btnExit);
        add(btnPanel, BorderLayout.SOUTH);

        btnUpload.addActionListener(e -> uploadImage());
        btnSave.addActionListener(e -> saveData());
        btnSearch.addActionListener(e -> searchData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnClear.addActionListener(e -> clearFields());
        btnExit.addActionListener(e -> dispose());
    }

    private void formPanelAdd(JPanel panel, GridBagConstraints gbc, int y, String label, Component field) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath();
            txtImagePath.setText(path);
            try {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error displaying image.");
            }
        }
    }

    private void saveData() {
        if (!validateInputs()) return;
        try (Connection con = DBConnection.getConnection()) {
            String sql = "INSERT INTO Participants VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtID.getText().trim());
            ps.setString(2, txtName.getText().trim());
            ps.setString(3, txtFaculty.getText().trim());
            ps.setString(4, txtTitle.getText().trim());
            ps.setString(5, txtContact.getText().trim());
            ps.setString(6, txtEmail.getText().trim());
            ps.setString(7, txtImagePath.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Saved Successfully");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void searchData() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Participants WHERE Registration_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtID.getText().trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtName.setText(rs.getString("Student_Name"));
                txtFaculty.setText(rs.getString("Faculty"));
                txtTitle.setText(rs.getString("Project_Tittle"));
                txtContact.setText(rs.getString("Contact_Number"));
                txtEmail.setText(rs.getString("Email_Address"));
                txtImagePath.setText(rs.getString("Image_Path"));

                String path = rs.getString("Image_Path");
                if (path != null && !path.trim().isEmpty()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image img = icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(img));
                } else {
                    lblImage.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateData() {
        if (!validateInputs()) return;
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE Participants SET Student_Name=?, Faculty=?, Project_Tittle=?, Contact_Number=?, Email_Address=?, Image_Path=? WHERE Registration_ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setString(2, txtFaculty.getText().trim());
            ps.setString(3, txtTitle.getText().trim());
            ps.setString(4, txtContact.getText().trim());
            ps.setString(5, txtEmail.getText().trim());
            ps.setString(6, txtImagePath.getText().trim());
            ps.setString(7, txtID.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Updated Successfully");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteData() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "DELETE FROM Participants WHERE Registration_ID = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtID.getText().trim());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No record found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private boolean validateInputs() {
        String idRegex = "JS-\\d{3}";
        String phoneRegex = "+256\\d{9}";
        String emailRegex = ".+@.+\\..+";

        if (!Pattern.matches(idRegex, txtID.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid Registration ID (e.g., JS-001)");
            return false;
        }
        if (!Pattern.matches(phoneRegex, txtContact.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number (e.g., +256707764215)");
            return false;
        }
        if (!Pattern.matches(emailRegex, txtEmail.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid Email Address");
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtID.setText("");
        txtName.setText("");
        txtFaculty.setText("");
        txtTitle.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        txtImagePath.setText("");
        lblImage.setIcon(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new registrationForm().setVisible(false));
    }
}
