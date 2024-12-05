import javax.swing.*;
import java.awt.event.*;
import java.io.*;
public class SaveFileHandler implements ActionListener {
    private JFrame parent;
    private JTextArea textArea;

    public SaveFileHandler(JFrame parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select filepath to save");
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(parent, "File saved successfully!");
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
