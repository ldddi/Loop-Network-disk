package com.looppan.looppan.controller.user.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Captcha {

    // 生成包含字母和数字的随机验证码字符串
    public static String generateRandomCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // 字符集
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }

        return captcha.toString();
    }

    // 创建丰富多彩的验证码图片
    public static BufferedImage createCaptchaImage(String captcha) {
        int width = 130;  // 图片宽度
        int height = 50;  // 图片高度
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        Random random = new Random();

        // 设置随机的背景颜色
        Color bgColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);

        // 设置抗锯齿，提升字体绘制的效果
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 40));

        // 计算每个字符的宽度和间距
        int captchaLength = captcha.length();
        int charWidth = width / (captchaLength + 1);  // 每个字符的宽度（+1 预留一些边距）

        // 设置字符的初始X坐标
        int x = charWidth / 2;

        // 绘制验证码字符，确保每个字符之间的间距足够，并且不会超出边界
        for (int i = 0; i < captchaLength; i++) {
            // 生成字符的随机颜色
            Color charColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(charColor);

            // 生成字符的随机高度偏移量，使验证码看起来更有动态性
            int randomY = 30 + random.nextInt(10);  // 在y轴上下浮动
            g.drawString(String.valueOf(captcha.charAt(i)), x, randomY);
            x += charWidth;  // 移动到下一个字符的位置
        }

        // 画一些干扰线并随机设置颜色
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();
        return image;
    }
}
