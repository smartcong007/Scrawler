package com.cong.util;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * @Description TODO
 * @Author zheng cong
 * @Date 2019-06-25
 */
public class ImageUtil {

    /**
     * Java拼接多张图片
     *
     * @param pics
     * @param type
     * @param dst_pic
     * @return
     */
    public static boolean merge(String[] pics, String type, String dst_pic) {

        int len = pics.length;
        if (len < 1) {
            System.out.println("pics len < 1");
            return false;
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(pics[i]);
                images[i] = ImageIO.read(src[i]);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            // 从图片中读取RGB
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height,
                ImageArrays[i], 0, width);
        }

        int dst_height = 0;
        int dst_width = images[0].getWidth();
        for (int i = 0; i < images.length; i++) {
            dst_width = dst_width > images[i].getWidth() ? dst_width
                : images[i].getWidth();

            dst_height += images[i].getHeight();
        }
        System.out.println(dst_width);
        System.out.println(dst_height);
        if (dst_height < 1) {
            System.out.println("dst_height < 1");
            return false;
        }

        // 生成新图片
        try {
            // dst_width = images[0].getWidth();
            BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
                BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            for (int i = 0; i < images.length; i++) {
                ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),
                    ImageArrays[i], 0, dst_width);
                height_i += images[i].getHeight();
            }

            File outFile = new File(dst_pic);
            // 写图片
            ImageIO.write(ImageNew, type, outFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
