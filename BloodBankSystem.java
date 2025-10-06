import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Donor {
    String name, bloodGroup, contact;

    Donor(String name, String bloodGroup, String contact) {
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
    }
}

public class BloodBankSystem extends JFrame {

    private ArrayList<Donor> donorList = new ArrayList<>();
    private JTextField tfName, tfContact, tfSearch;
    private JComboBox<String> cbBloodGroup;
    private JTable donorTable;
    private DefaultTableModel tableModel;

    public BloodBankSystem() {
        setTitle("INDER BLOOD BANK SOFTWARE");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- TOP PANEL (Logo + Title) --------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 240, 240));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Load logo image
        ImageIcon icon = new ImageIcon("inderinsta.png"); // <-- your image file
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        // Title
        JLabel title = new JLabel("INDER BLOOD BANK SOFTWARE", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.RED);

        // Add both image and title
        topPanel.add(logoLabel, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH); // --- Form Panel (Left) ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        formPanel.setBackground(new Color(255, 240, 240));

        tfName = new JTextField();
        tfContact = new JTextField();
        cbBloodGroup = new JComboBox<>(new String[] { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" });
        tfSearch = new JTextField();

        // --- Buttons ---
        JButton btnAdd = createStyledButton("Add Donor", new Color(220, 53, 69)); // Red
        JButton btnSearch = createStyledButton("Search by Group", new Color(0, 123, 255)); // Blue
        JButton btnDelete = createStyledButton("Delete Selected", new Color(40, 167, 69)); // Green
        JButton btnShowAll = createStyledButton("Show All", new Color(108, 117, 125)); // Gray

        // --- Add components to form panel ---
        formPanel.add(new JLabel("Name:"));
        formPanel.add(tfName);
        formPanel.add(new JLabel("Blood Group:"));
        formPanel.add(cbBloodGroup);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(tfContact);
        formPanel.add(btnAdd);
        formPanel.add(btnSearch);

        add(formPanel, BorderLayout.WEST);

        // --- Table Panel (Center) ---
        tableModel = new DefaultTableModel(new String[] { "Name", "Blood Group", "Contact" }, 0);
        donorTable = new JTable(tableModel);
        donorTable.setRowHeight(25);
        donorTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        donorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        donorTable.getTableHeader().setBackground(new Color(230, 230, 230));
        JScrollPane scrollPane = new JScrollPane(donorTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 240, 240));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(new JLabel("Search Blood Group:"));
        bottomPanel.add(tfSearch);
        bottomPanel.add(btnShowAll);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Listeners ---
        btnAdd.addActionListener(_ -> addDonor());
        btnSearch.addActionListener(_ -> searchByBloodGroup());
        btnDelete.addActionListener(_ -> deleteSelected());
        btnShowAll.addActionListener(_ -> loadTable());

        // --- Sample data ---
        donorList.add(new Donor("Inderjit Singh", "B+", "9876543210"));
        donorList.add(new Donor("Sidhu Moosewala", "O-", "9123456789"));
        loadTable();
    }

    // --- Custom Button Creator ---
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    private void addDonor() {
        String name = tfName.getText().trim();
        String contact = tfContact.getText().trim();
        String blood = cbBloodGroup.getSelectedItem().toString();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all details.");
            return;
        }

        donorList.add(new Donor(name, blood, contact));
        loadTable();
        tfName.setText("");
        tfContact.setText("");
    }

    private void searchByBloodGroup() {
        String bg = tfSearch.getText().trim().toUpperCase();
        if (bg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter blood group to search.");
            return;
        }

        tableModel.setRowCount(0);
        for (Donor d : donorList) {
            if (d.bloodGroup.equalsIgnoreCase(bg)) {
                tableModel.addRow(new Object[] { d.name, d.bloodGroup, d.contact });
            }
        }
    }

    private void deleteSelected() {
        int row = donorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        String name = tableModel.getValueAt(row, 0).toString();
        donorList.removeIf(d -> d.name.equals(name));
        loadTable();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Donor d : donorList) {
            tableModel.addRow(new Object[] { d.name, d.bloodGroup, d.contact });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloodBankSystem().setVisible(true));
    }
}
