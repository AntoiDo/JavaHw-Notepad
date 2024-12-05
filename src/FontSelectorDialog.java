import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FontSelectorDialog extends JDialog {
    private JTextArea textArea;
    private JComboBox<String> fontComboBox;
    private JSpinner fontSizeSpinner;
    private JButton applyButton;

    public FontSelectorDialog(JFrame parent, JTextArea textArea) {
        super(parent, "Select Font", true);
        this.textArea = textArea;

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        // Font family selection
        centerPanel.add(new JLabel("Font:"));
        fontComboBox = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        centerPanel.add(fontComboBox);

        // Font size selection
        centerPanel.add(new JLabel("Size:"));
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 72, 1));
        centerPanel.add(fontSizeSpinner);

        add(centerPanel, BorderLayout.CENTER);

        // Apply button
        applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFont();
            }
        });
        add(applyButton, BorderLayout.SOUTH);

        setSize(300, 150);
        setLocationRelativeTo(parent);
    }

    private void applyFont() {
        String selectedFont = (String) fontComboBox.getSelectedItem();
        int selectedSize = (int) fontSizeSpinner.getValue();
        Font newFont = new Font(selectedFont, Font.PLAIN, selectedSize);
        textArea.setFont(newFont);
        dispose();
    }
}