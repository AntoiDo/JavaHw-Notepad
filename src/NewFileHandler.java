import java.awt.event.*;
import javax.swing.*;
public class NewFileHandler implements ActionListener {
    private JFrame parent;
    private JTextArea textArea;

    public NewFileHandler(JFrame parent, JTextArea textArea) {
        this.parent = parent;
        this.textArea = textArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        textArea.setText("");
    }
}
