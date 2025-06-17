/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuexhibitionsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class viewParticipants extends JFrame {
    public viewParticipants() {
        setTitle("View Participants");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(area));

        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Participants");

            while (rs.next()) {
                area.append("ID: " + rs.getString("Registration_ID") +
                            ", Name: " + rs.getString("Student_Name") +
                            ", Faculty: " + rs.getString("Faculty") +
                            ", Title: " + rs.getString("Project_Tittle") +
                            ", Contact: " + rs.getString("Contact_Number") +
                            ", Email: " + rs.getString("Email_Address") +
                            ", Image Path: " + rs.getString("Image_Path") + "\n\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            area.append("Failed to load participants.\n");
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new viewParticipants();
    }
}