# An ordinary Notepad



这是一个非常简陋的一个记事本，不过麻雀虽小五脏俱全，基本的功能算是都实现了，下面简单的介绍一下

## GUI设计

<img src="D:\009-markdown\graphs\image-20241207143832390.png" alt="image-20241207143832390" style="zoom:67%;" />



占据最主要的就是作为主题的**JTextArea**输入框部分，左上角是相关的Options，以及一些带有Icon的快捷功能的打开方式

![image-20241207144106562](D:\009-markdown\graphs\image-20241207144106562.png)

设置部分里面有简单的一些常见功能设计: **刷新当前输入框、打开、保存txt文件、改变字体(~~颜色太懒了没做了~~)、退出**，总的来说作为一个记事本，已经做到了能用的地步



## 功能展示

> 我不知道PDF能不能导入GIF所以全部都用截图来显示过程了

+ 文本框的输入和清除

  ![image-20241207144545866](D:\009-markdown\graphs\image-20241207144545866.png)当前输入了一段文字

  ![image-20241207145133180](D:\009-markdown\graphs\image-20241207145133180.png)点击New后文本框被清空了(脑补一下吧。。。反正就是用了clearText之类的函数)

+ 字体的修改

  ![image-20241207145256436](D:\009-markdown\graphs\image-20241207145256436.png)这是字体的修改界面，通过点击Apply按钮后回到主界面发现字体已经变化

  

  <img src="D:\009-markdown\graphs\image-20241207145418558.png" alt="image-20241207145418558" style="zoom: 80%;" />如图所示 

## 实现细节

1. 组件的使用

程序里面一共用到的组件如下

~~~java
public static void main(String[] args) {
        /* 各种套件的初始化 */
        JFrame frame = new JFrame("Ordinary Notepad"); // 创建框体
        JMenuBar menuBar = new JMenuBar(); // 容纳Menu栏的一个地方
        JToolBar toolBar = new JToolBar(); // 放icon的地方
        JTextArea textArea = new JTextArea(); // 打字的地方
        JScrollPane scrollPane = new JScrollPane(textArea); // 自带滚动条
        FontSelectorDialog fontDialog = new FontSelectorDialog(frame, textArea);// 是JDialog

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New"); // File下面的子menu
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem colorFontMenuItem = new JMenuItem("Color & Font");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        // 三个按钮
        JButton newButton = createButtonWithIcon(ImageScaling.zoom("img/new.png", 0.1), "New");
        JButton saveButton = createButtonWithIcon(ImageScaling.zoom("img/save.png", 0.2), "Save");
        JButton openButton = createButtonWithIcon(ImageScaling.zoom("img/open.png", 0.1), "Open");
		
    
    	// 主要程序按下不表
    }
}
~~~

2. 事件响应

各种按钮选项要有交互才能进行操作，所以事件响应的机制必不可少。在我的程序里我对**open、new、save、fonts**等选项都添加了事件响应，让我们一步一步来

1. 通过实现 ActionListener 接口来完成：

+ SaveFileHandler 类实现了 ActionListener 接口，用于处理保存文件的操作。
+ actionPerformed 方法被重写，当用户点击保存按钮或选择保存菜单项时，该方法会被调用，在这个方法里面实现我们想要的功能

~~~java
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


// 最后我们在Main方法里面为这个组件添加相关的Listener就可,如下
// saveButton.addActionListener(new SaveFileHandler(frame, textArea));
~~~

2. 使用匿名内部类：

+ 在 Main 类中，使用匿名内部类为按钮和菜单项添加事件监听器。例如，exitMenuItem 的 ActionListener 使用匿名内部类来处理退出操作。

~~~java
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
~~~



**下面是自己发现偷偷改的**

在编辑文本的时候发现的一个现象

![image-20241207152552238](D:\009-markdown\graphs\image-20241207152552238.png)

虽然不可能，但如果真有人来用我的这个记事本写代码的话那可就太遭殃了，所以我开始尝试修改按一次tab的大小

1. 第一次尝试

GPT了一下它告诉我说JTextArea这个类有一个**setTabSize(int count)**的函数，可以用来修改tab的空格多少，我试了一下发现空格数量确实改变了，但是并不是我想要的4，即使我参数传入为4，我去官方的文档也说这个是可以按照参数改变tab大小，但在我这里不行，遗留一个悬念吧

2. 第二次尝试

GPT给的答案是行不通了，那只能去用原始的搜索方法了，某flow网站上有大牛偷偷给个hint说改变个key binding就可以了，然后我按照他说的去做了下发现真的ok太牛逼了，有时候还是老办法好使。扯了一点废话下面是代码实现

~~~java
	/**
     * 通过改变binding来改变tab的大小
     * @param textArea   要修改的文本框
     * @param spaceCount tab的大小
     */
    public static void changeTabSize(JTextArea textArea, int spaceCount) {
        String spaces = " ".repeat(spaceCount);
        // Create a custom action to insert spaces
        Action insertSpacesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.replaceSelection(spaces);
            }
        };
        // Bind the Tab key to the custom action
        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = textArea.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("TAB"), "insertSpaces");
        actionMap.put("insertSpaces", insertSpacesAction);
    }
~~~

他做了两件事情 : 

1. 创建自定义动作：定义一个 AbstractAction 的匿名内部类，该类在 actionPerformed 方法中插入指定数量的空格。
2. 修改键绑定：使用 InputMap 和 ActionMap 将 Tab 键绑定到自定义动作上，从而替换默认的插入 Tab 字符的行为。

这样子修改以后如果我们按下Tab那么就会被替换成我们的想要的空格数，在这里我们给这个函数传参个4就OK了，效果还不错

![image-20241207153350618](D:\009-markdown\graphs\image-20241207153350618.png)



## Ending

感谢GPT让我两天内速通两周小作业，不敢想象以前只能一点一点搜的话效率有多慢哈哈哈

