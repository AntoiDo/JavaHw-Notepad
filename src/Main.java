import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
    /**
     * 初始化窗口
     * @param frame JFrame对象
     */
    public static void initFrame(JFrame frame) {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    /**
     * 创建一个带有图标和提示的按钮
     * @param imgPath   图片路径
     * @param tooltip   提示
     * @return          实体按钮(只有界面按下去没用)
     * @note  放上去的图片都是原大小，记得要压缩处理
     */
    public static JButton createButtonWithIcon(String imgPath, String tooltip) {
        JButton button = new JButton();
        try {
            Image img = ImageIO.read(new File(imgPath));
            button.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * 创建一个带有图标和提示的按钮
     * @param image     图片
     * @param tooltip   提示
     * @return          实体按钮(只有界面按下去没用)
     * @note            放上去的图片都是原大小，记得要压缩处理
     */
    public static JButton createButtonWithIcon(Image image, String tooltip) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(image));
        button.setToolTipText(tooltip);
        return button;
    }

    public static void main(String[] args) {
        /* 各种套件的初始化 */
        JFrame frame = new JFrame("Ordinary Notepad");
        JMenuBar menuBar = new JMenuBar();
        JToolBar toolBar = new JToolBar();
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        FontSelectorDialog fontDialog = new FontSelectorDialog(frame, textArea);


        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem colorFontMenuItem = new JMenuItem("Color & Font");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        initFrame(frame);
        frame.setJMenuBar(menuBar);
        menuBar.add(fileMenu);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(colorFontMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // 三个按钮(With compression)
        JButton newButton = createButtonWithIcon(ImageScaling.zoom("img/new.png", 0.1), "New");
        JButton saveButton = createButtonWithIcon(ImageScaling.zoom("img/save.png", 0.2), "Save");
        JButton openButton = createButtonWithIcon(ImageScaling.zoom("img/open.png", 0.1), "Open");

        // 在toolbar里面添加按钮
        toolBar.add(newButton);
        toolBar.add(saveButton);
        toolBar.add(openButton);

        // 添加toolbar到frame里面
        frame.add(toolBar, BorderLayout.NORTH);

        // 添加文本框
        frame.add(scrollPane, BorderLayout.CENTER);

        // 为按钮和menuItem添加事件
        saveButton.addActionListener(new SaveFileHandler(frame, textArea));
        saveMenuItem.addActionListener(new SaveFileHandler(frame, textArea));
        openButton.addActionListener(new OpenFileHandler(frame, textArea));
        openMenuItem.addActionListener(new OpenFileHandler(frame, textArea));
        newButton.addActionListener(new NewFileHandler(frame, textArea));
        newMenuItem.addActionListener(new NewFileHandler(frame, textArea));

        // 退出按钮
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // 字体的控制
        colorFontMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FontSelectorDialog fontDialog = new FontSelectorDialog(frame, textArea);
                fontDialog.setVisible(true);
            }
        });
        frame.setVisible(true);
        frame.requestFocusInWindow();

    }
}