import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordGeneratorUI extends JFrame {

    private JSlider lengthSlider;
    private JCheckBox digitsCheckBox;
    private JCheckBox lettersCheckBox;
    private JCheckBox specialCheckBox;
    private JTextArea passwordListArea;
    private List<String> passwordList = new ArrayList<>();

    public PasswordGeneratorUI() {
        setTitle("Password Generator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JMenu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpMenuItem = new JMenuItem("Help");
        JMenuItem saveMenuItem = new JMenuItem("Save to File");
        saveMenuItem.addActionListener(new SaveMenuItemListener());
        fileMenu.add(saveMenuItem);
        helpMenu.add(helpMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Slider for password length
        panel.add(new JLabel("Password Length:"));
        lengthSlider = new JSlider(6, 20, 8);
        lengthSlider.setPaintLabels(true);
        lengthSlider.setMajorTickSpacing(2);
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
            String password = PasswordGenerator.generatePassword(length, onlyDigits, onlyLetters, specialChar);
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

    private void updatePasswordList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String password : passwordList) {
            stringBuilder.append(password).append("\n");
        }
        passwordListArea.setText(stringBuilder.toString());
    }
}
