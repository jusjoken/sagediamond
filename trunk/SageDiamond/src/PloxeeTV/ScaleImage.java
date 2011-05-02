/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;


import javax.imageio.*;
import org.apache.log4j.Logger;

public class ScaleImage {
    static private final Logger LOG = Logger.getLogger(ScaleImage.class);

    @SuppressWarnings("static-access")
  public static void scale(String src, int scalewidth, int scaleheight, String dest){
boolean preserveAlpha=src.contains(".png");
System.out.println(BufferedImage.TYPE_INT_RGB);
System.out.println(BufferedImage.TYPE_INT_ARGB);

 int imageType = !preserveAlpha ? BufferedImage.TYPE_INT_RGB: BufferedImage.TYPE_INT_ARGB;
 System.out.println("Type"+imageType);

    BufferedImage originalimage;
    int i=0;
    System.out.println(1);
        try {

    originalimage = ImageIO.read(new File(src));
    Integer[] dims= GetDimsToScale(scalewidth,scaleheight,originalimage.getWidth(),originalimage.getHeight());
    int width = dims[0];
    int height = dims[1];
   System.out.println(2);
    BufferedImage scaledBI = new BufferedImage(width,height,imageType);
       System.out.println(3);
    Graphics2D g = scaledBI.createGraphics();

       System.out.println(4);
    if (preserveAlpha) {
           System.out.println(5);
       g.setComposite(AlphaComposite.Src);}


          System.out.println(6);
   g.drawImage(originalimage, 0, 0,width, height, null);
       System.out.println(7);
    g.dispose();
       System.out.println(8);

    ImageIO.write(scaledBI, "png", new File(dest));
}
 catch (IOException ex) {
//             SMMLogger.SMMLogger.fatal("Problem Scaling images"+ScaleImage.class.getName()+ ex);
        }    catch (Exception e){


//  SMMLogger.SMMLogger.info("Problem loading original image may be corrupt"+ScaleImage.class.getName()+ e);
    }

}



  public static Integer[] GetDimsToScale(int width, int height, int w, int h){
if (w > h) {
  h = (h * width) / w;
  w = width;
}
else {
  w = (w * height) / h;
  h = height;
}

  return new Integer[]{w,h};

  }

  public static void main(String[] args) throws IOException{
scale("C:\\backdrop7.jpg",200,300,"c:\\simpsonscaled.jpg");

  }


}