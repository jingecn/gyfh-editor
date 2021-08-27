package cn.qianzf.widget.test;

import cn.qianzf.widget.letex.ExtendCommands;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Conver {

    public static void toPng(String latex, String f) throws IOException {
        //DefaultTeXFont.registerAlphabet(new CyrillicRegistration());
        //DefaultTeXFont.registerAlphabet(new GreekRegistration());

        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setBackground(Color.white);
        g2.clearRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        //g2.setColor(Color.white);
        //JLabel jl = new JLabel();
        // jl.setForeground(new Color(0, 0, 0));
        g2.setColor(new Color(0, 0, 0));
        icon.paintIcon(null, g2, 0, 0);
        File file = new File(f);
        ImageIO.write(image, "png", file.getAbsoluteFile());
    }


    public static void main(String[] args) {
        ExtendCommands.regPreExtCommands();

        String latex = "\\begin{array}{lr}\\mbox{\\textcolor{Blue}{Russian}}&\\mbox{\\textcolor{Melon}{Greek}}\\\\";
        latex += "\\mbox{" + "привет мир".toUpperCase() + "}&\\mbox{" + "γειά κόσμο".toUpperCase() + "}\\\\";
        latex += "\\mbox{привет мир}&\\mbox{γειά κόσμο}\\\\";
        latex += "\\mathbf{\\mbox{привет мир}}&\\mathbf{\\mbox{γειά κόσμο}}\\\\";
        latex += "\\mathit{\\mbox{привет мир}}&\\mathit{\\mbox{γειά κόσμο}}\\\\";
        latex += "\\mathsf{\\mbox{привет мир}}&\\mathsf{\\mbox{γειά κόσμο}}\\\\";
        latex += "\\mathtt{\\mbox{привет мир}}&\\mathtt{\\mbox{γειά κόσμο}}\\\\";
        latex += "\\mathbf{\\mathit{\\mbox{привет мир}}}&\\mathbf{\\mathit{\\mbox{γειά κόσμο}}}\\\\";
        latex += "\\mathbf{\\mathsf{\\mbox{привет мир}}}&\\mathbf{\\mathsf{\\mbox{γειά κόσμο}}}\\\\";
        latex += "\\mathsf{\\mathit{\\mbox{привет мир}}}&\\mathsf{\\mathit{\\mbox{γειά κόσμο}}}\\\\";
        latex += "&\\\\";
        latex += "\\mbox{\\textcolor{Salmon}{Bulgarian}}&\\mbox{\\textcolor{Tan}{Serbian}}\\\\";
        latex += "\\mbox{здравей свят}&\\mbox{Хелло уорлд}\\\\";
        latex += "&\\\\";
        latex += "\\mbox{\\textcolor{Turquoise}{Bielorussian}}&\\mbox{\\textcolor{LimeGreen}{Ukrainian}}\\\\";
        latex += "\\mbox{прывітаньне Свет}&\\mbox{привіт світ}\\\\";
        latex += "\\end{array}";


        latex="{\\boxed{gggg}";//方框
        latex="{\\circled{gggg}";//圆框
        latex="\\xrightarrow{ysddsfdsfsdfsdfs手动阀手动阀d}";//下面箭头
        latex="\\xrightarrow[ysddsfdsfsdfsdfs手动阀手动阀d]";//上面箭头
        latex="\\overline{士大夫士大夫士大夫士大夫}";//上面横线

        latex="\\overset{发射点发生士大夫发}\\tolerances{1}{a}{{R}}{g}{d}{e}";//形位公差

        //  粗糙度
        //latex="\\surfaceness{3}{1}{abbbbbert545664646}{b8797898798798797}{c5676575980809800809}{d}{e34643643636}{f}{h}";

        final JFrame frame=new JFrame("符号生成测试");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLayout(new BorderLayout());
        final JTextArea textArea=new JTextArea(latex,5,60);
        textArea.setSize(500,100);
        final JLabel label=new JLabel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                TeXFormula formula = new TeXFormula(textArea.getText());
                TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
                Graphics2D g2= (Graphics2D) g;
                g2.clearRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
                g2.setColor(new Color(0, 0, 0));
                icon.paintIcon(null, g2, 0, 0);
            }
        };
        frame.add(label,BorderLayout.CENTER);
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p=e.getLocationOnScreen();
                 frame.setTitle(p.toString());
            }
        });
        JPanel panel=new JPanel(new FlowLayout());

        panel.add(textArea);
        JButton button=new JButton("生成");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                label.repaint(new Rectangle(0,0,label.getWidth(), label.getHeight()));
            }
        });
        panel.add(button);
        frame.add(panel,BorderLayout.SOUTH);
        frame.setVisible(true);
        //latex="\\boxed{sdfdsf}";

    }
}
