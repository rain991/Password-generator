import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator extends JFrame {

    private JSlider lengthSlider;
    private JCheckBox digitsCheckBox;
    private JCheckBox lettersCheckBox;
    private JCheckBox specialCheckBox;
    private JTextArea passwordListArea;
    private SecureRandom random = new SecureRandom();
    private List<String> passwordList = new ArrayList<>();

    public PasswordGenerator() {
        setTitle("Password Generator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a "File" menu
        JMenu fileMenu = new JMenu("File");

        // Create a "Save" menu item
        JMenuItem saveMenuItem = new JMenuItem("Save to File");
        saveMenuItem.addActionListener(new SaveMenuItemListener());

        // Add the "Save" menu item to the "File" menu
        fileMenu.add(saveMenuItem);

        // Add the "File" menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Slider for password length
        panel.add(new JLabel("Password Length:"));
        lengthSlider = new JSlider(6, 20, 8);
        lengthSlider.setMajorTickSpacing(2);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        panel.add(lengthSlider);

        // Checkboxes for password options
        digitsCheckBox = new JCheckBox("Contains only digits");
        lettersCheckBox = new JCheckBox("Contains only letters");
        specialCheckBox = new JCheckBox("Contains at least one special character");
        panel.add(digitsCheckBox);
        panel.add(lettersCheckBox);
        panel.add(specialCheckBox);

        // Button to generate password
        JButton generateButton = new JButton("Generate Password");
        generateButton.addActionListener(new GenerateButtonListener());
        panel.add(generateButton);

        // Text area to display generated passwords
        passwordListArea = new JTextArea(10, 30);
        passwordListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(passwordListArea);
        panel.add(scrollPane);

        add(panel);
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int length = lengthSlider.getValue();
            boolean onlyDigits = digitsCheckBox.isSelected();
            boolean onlyLetters = lettersCheckBox.isSelected();
            boolean specialChar = specialCheckBox.isSelected();

            if (onlyDigits && onlyLetters) {
                JOptionPane.showMessageDialog(null, "Cannot select both 'Contains only digits' and 'Contains only letters'");
                return;
            }

            String password = generatePassword(length, onlyDigits, onlyLetters, specialChar);
            passwordList.add(password);
            updatePasswordList();
        }
    }

    private class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                    for (String password : passwordList) {
                        writer.write(password + System.lineSeparator());
                    }
                    JOptionPane.showMessageDialog(null, "Passwords saved successfully");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving passwords: " + ex.getMessage());
                }
            }
        }
    }

    private String generatePassword(int length, boolean onlyDigits, boolean onlyLetters, boolean specialChar) {
        String digits = "0123456789";
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String special = "!@#$%^&*()-_=+<>?";

        String characterPool = "";
        if (onlyDigits) {
            characterPool = digits;
        } else if (onlyLetters) {
            characterPool = letters;
        } else {
            characterPool = digits + letters;
            if (specialChar) {
                characterPool += special;
            }
        }

        StringBuilder password = new StringBuilder();
        while (password.length() < length) {
            int index = random.nextInt(characterPool.length());
            char ch = characterPool.charAt(index);
            password.append(ch);
        }

        if (specialChar && !containsSpecialCharacter(password.toString())) {
            int index = random.nextInt(special.length());
            char specialCh = special.charAt(index);
            int pos = random.nextInt(password.length());
            password.setCharAt(pos, specialCh);
        }

        return password.toString();
    }

    private boolean containsSpecialCharacter(String password) {
        String special = "!@#$%^&*()-_=+<>?";
        for (char ch : password.toCharArray()) {
            if (special.indexOf(ch) >= 0) {
                return true;
            }
        }
        return false;
    }

    private void updatePasswordList() {
        StringBuilder sb = new StringBuilder();
        for (String password : passwordList) {
            sb.append(password).append("\n");
        }
        passwordListArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordGenerator().setVisible(true);
            }
        });
    }
}
