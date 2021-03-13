package com.cafi.firefly.example.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: firefly
 * @description: 图片处理测试类
 * @author: Miao
 * @create: 2021-03-13 19:51
 */
public class imgTest {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("E:\\firefly\\src\\main\\resources\\img\\1.jpg"));
        Graphics g = image.getGraphics();
        Graphics2D g2D = (Graphics2D)g;
        g2D.setStroke(new BasicStroke(3f));
        g2D.setColor(Color.RED);//画笔颜色
        g2D.drawRect(249, 102, 135, 59);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
        //g.dispose();
        FileOutputStream out = new FileOutputStream("E:\\firefly\\src\\main\\resources\\img\\test4.jpg");//输出图片的地址
        ImageIO.write(image, "jpeg", out);
     }
}
