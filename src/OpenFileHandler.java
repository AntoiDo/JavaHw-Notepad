import java.awt.event.*;
import javax.swing.*;
import java.io.*;
public class OpenFileHandler implements ActionListener {
    private JFrame parent;
    private JTextArea textArea;

    public OpenFileHandler(JFrame parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file to open");
        int userSelection = fileChooser.showOpenDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                textArea.setText("");
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
