package com.ilqjx.util;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    // 将文件转换为 jpg 格式
    public static BufferedImage chang2jpg(File file) {
        try {
            // 创建 Image 对象
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage(file.getAbsolutePath());
            // 从指定的图像中抓取像素矩形部分
            PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
            // 请求 Image 开始传递像素 并等待传递完相关矩形中的所有像素
            pg.grabPixels();
            // 获取图片的宽度
            int width = pg.getWidth();
            // 获取图片的高度
            int height = pg.getHeight();
            final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
            // 根据指定的指示 int 像素表示形式中哪些位包含红色、绿色和蓝色颜色样本的掩码构造 DirectColorModel
            // ColorModel 存储图像的颜色数据
            final ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
            // new DataBufferInt 使用指定数组构建具有单个存储单元且基于整数的 DataBuffer
            // DataBuffer 此类用于包装数据数组
            DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), width * height);
            // 根据 SinglePixelPackedSampleModel 创建一个具有指定 DataBuffer、宽度、高度、扫描行间距和 band 掩码的 Raster
            // 通过 location 参数(最后一个参数)给出 Raster 的左上角 如果 location 为 null 则将使用 (0, 0)
            // WritableRaster 类的构造方法为 protected 要实例化 WritableRaster 需使用 Raster 类中的一个 createWritableRaster 工厂方法
            // WritableRaster 类扩展了 Raster 以提供像素写入功能
            // raster 存储图像的像素数据
            WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
            // 构造一个具有指定 ColorModel 和 Raster 的新 BufferedImage
            BufferedImage img = new BufferedImage(RGB_OPAQUE, raster, false, null);
            return img;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 改变图片大小
    public static void resizeImage(File srcFile, int width, int height, File destFile) {
        try {
            Image image = ImageIO.read(srcFile);
            image = resizeImage(image, width, height);
            ImageIO.write((RenderedImage) image, "jpg", destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image resizeImage(Image srcImage, int width, int height) {
        try {
            // 其中width表示图像的宽度 height表示图像的高度 最后一个参数表示图像字节灰度图像
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            return buffImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
