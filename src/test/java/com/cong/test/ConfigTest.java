package com.cong.test;

import com.cong.util.Config;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class ConfigTest {

    @Test
    public void test() {

        String val = Config.getVal("CHROME_PATH");
        System.out.println(val);

    }

    @Test
    public void testnio(){


        try {
            RandomAccessFile aFile = new RandomAccessFile(System.getProperty("user.dir")+"/.gitignore","rw");
            FileChannel inChannel = aFile.getChannel();
            System.out.println(inChannel.size());

            ByteBuffer buf = ByteBuffer.allocate(42);

            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {

                System.out.println("Read " + bytesRead);
                buf.flip();

                while(buf.hasRemaining()){
                    System.out.print((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
