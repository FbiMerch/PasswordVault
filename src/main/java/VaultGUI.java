import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class VaultGUI extends JFrame {
    private final DatabaseManager dbManager;
    private SecretKey masterKey;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public VaultGUI() {
        dbManager = new DatabaseManager();
        if (!authenticateMasterPassword()) {
            System.exit(0);
        }
        initUI();
        loadCredentials("");
    }

    private boolean authenticateMasterPassword() {
        JPasswordField pf = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(null, pf, "Introdu Master Password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(pf.getPassword());
            if (password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Parola nu poate fi goala!", "Eroare", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                this.masterKey = CryptoUtils.deriveKey(password);
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Eroare la derivarea cheii: " + e.getMessage());
            }
        }
        return false;
    }

    private void initUI() {
        setTitle("🔒 Password Vault");
        setSize(850, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Cauta");
        JButton addBtn = new JButton("➕ Adauga");
        JButton deleteBtn = new JButton("🗑️ Sterge");
        JButton genPassBtn = new JButton("🎲 Generare Parola");
        JButton exportBtn = new JButton("💾 Export Criptat");

        topPanel.add(new JLabel("Cauta Site:"));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        topPanel.add(deleteBtn);
        topPanel.add(genPassBtn);
        topPanel.add(exportBtn);

        String[] columns = {"ID", "Site", "Username", "Parola (Decriptata)", "Notite"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> loadCredentials(searchField.getText().trim()));
        addBtn.addActionListener(e -> showAddDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        genPassBtn.addActionListener(e -> {
            String pass = CryptoUtils.generateStrongPassword(16);
            JOptionPane.showInputDialog(this, "Parola generata (16 caractere):", pass);
        });
        exportBtn.addActionListener(e -> exportEncryptedBackup());
    }

    private void loadCredentials(String filter) {
        tableModel.setRowCount(0);
        try {
            List<Credential> list = dbManager.getAllCredentials();
            for (Credential c : list) {
                if (filter.isEmpty() || c.getSite().toLowerCase().contains(filter.toLowerCase())) {
                    String decryptedPass;
                    try {
                        decryptedPass = CryptoUtils.decrypt(c.getPassword(), masterKey);
                    } catch (Exception e) {
                        decryptedPass = "[Cheie Invalida]";
                    }
                    tableModel.addRow(new Object[]{c.getId(), c.getSite(), c.getUsername(), decryptedPass, c.getNotes()});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la incarcarea datelor: " + e.getMessage());
        }
    }

    private void showAddDialog() {
        JTextField siteField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField notesField = new JTextField();

        Object[] message = {
                "Site:", siteField,
                "Username:", userField,
                "Parola:", passField,
                "Notite:", notesField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Adauga Inregistrare", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String encrypted = CryptoUtils.encrypt(new String(passField.getPassword()), masterKey);
                Credential c = new Credential(0, siteField.getText(), userField.getText(), encrypted, notesField.getText());
                dbManager.addCredential(c);
                loadCredentials("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la salvare: " + e.getMessage());
            }
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            try {
                dbManager.deleteCredential(id);
                loadCredentials("");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Eroare la stergere: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecteaza un rand mai intai!");
        }
    }

    private void exportEncryptedBackup() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vault_backup.txt"))) {
            List<Credential> list = dbManager.getAllCredentials();
            for (Credential c : list) {
                writer.write(c.getSite() + " | " + c.getUsername() + " | " + c.getPassword() + " | " + c.getNotes());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Backup salvat cu succes in 'vault_backup.txt'!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare export: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VaultGUI().setVisible(true));
    }
}