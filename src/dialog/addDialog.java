package dialog;

import bean.Fruit;
import client.TableFrame;
import dao.FruitDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class addDialog extends JDialog {
    private JLabel lab1, lab2, lab3, lab4;
    private JTextField t1, t2, t3;
    private JButton b1, b2, b3;
    String path;
    Font font = new Font("Serif", Font.BOLD, 20);

    public addDialog(JFrame parent) {
        super(parent, "添加物品", true);
        initUI();
    }

    private void initUI() {
        setSize(330, 300);
        setLayout(new FlowLayout());
        lab1 = createLabel("物品名称：");
        lab2 = createLabel("物品单价：");
        lab3 = createLabel("计量单位：");
        lab4 = createLabel("物品图片：");
        //创建单行文本框
        t1 = createTextField(12);
        t2 = createTextField(12);
        t3 = createTextField(12);
        b1 = new JButton("选择图片");
        Object _this = this;
        b1.addActionListener((ActionEvent ac) -> {
            JFileChooser fc = new JFileChooser();
            //获取用户选取的结果
            int result = fc.showOpenDialog((Component) _this);
            //判断用户是否成功选择了图片
            if (result == JFileChooser.APPROVE_OPTION) {
                //获取用户选择的图片
                File file = fc.getSelectedFile();
                //获取后缀名 1.获取原文件名 xxxx.png
                String fileName = file.getName();
                //获取后缀名 2.截取后缀名 .png
                String extension = fileName.substring(fileName.lastIndexOf("."));
                //生成新名字 UUID去除-之后 加上 后缀名
                String newName = UUID.randomUUID().toString().replaceAll("-", "") + extension;
                Path path1 = Paths.get(file.getAbsolutePath());
                try {
                    path = "img/" + newName;
                    Files.copy(path1, new FileOutputStream("img/" + newName));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        addAll(this, lab1, t1, lab2, t2, lab3, t3, lab4, b1);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        b2 = new JButton("保存");
        b2.addActionListener((ActionEvent ac) -> {
            //TODO: 调用dao进行添加

            FruitDao dao = new FruitDao();
            Fruit fruit = new Fruit(null,
                    t1.getText(),
                    Double.parseDouble(t2.getText()),
                    t3.getText(),
                    path);
            int i = dao.addFruit(fruit);
            if(i>0){
                //回显
                TableFrame.query();
                //关闭模态框
                dispose();
                JOptionPane.showMessageDialog(this, "添加成功");
            }else{
                JOptionPane.showMessageDialog(this, "添加失败");
            }
        });
        b3 = new JButton("取消");
        b3.addActionListener((ActionEvent ac) -> {
            //关闭当前模态框
            dispose();
        });
        panel.add(b2);
        panel.add(b3);
        add(panel, "South");
        //居中显示对话框
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(2);
    }

    //创建标签
    public JLabel createLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(font);
        return label;
    }

    //创建单行文本框
    public JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(font);
        return tf;
    }

    public static void addAll(Container ctn, Component... cs) {
        for (Component c : cs) {
            ctn.add(c);
        }
    }
}