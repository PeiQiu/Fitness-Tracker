package com.example.demo.tools;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by starstar on 17/4/30.
 */
public class ImageUtils {
    public static final String IMAGE_JPEG=".jpg";
    public static final String IMAGE_PNG=".png";
    public static final String IMAGE_NOT_IMAGE="not_image";
    public static String getImageType(byte[] array) throws IOException {
        ImageInputStream iis = new MemoryCacheImageInputStream( new ByteArrayInputStream( array ) );
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if( readers.hasNext() ) {
            ImageReader reader = readers.next();
            reader.setInput(iis, true);
            String name = reader.getFormatName();
            if( "jpeg".equals( name.toLowerCase() ) ) {
                return IMAGE_JPEG;
            } else if( "png".equals(name.toLowerCase()) ) {
                return IMAGE_PNG;
            } else {
                return IMAGE_NOT_IMAGE;
            }
        } else {
            return IMAGE_NOT_IMAGE;
        }
    }
}
