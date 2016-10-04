/**
 * Created by Manuel on 2016-03-11.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageComp extends JComponent {
    BufferedImage img;

    public ImageComp(String path){
        try {
            Image img2 = ImageIO.read(new File(path));
            int w = img2.getWidth(null), h = img2.getHeight(null);

            double ratio = (w/(double)h);
            double ratio2 = (4/3.0);

            if(ratio >= ratio2){
                if(w > 800) {
                    img2 = img2.getScaledInstance(800, -1, Image.SCALE_SMOOTH);
                }
                else{
                    if(h > 600){
                        img2 = img2.getScaledInstance(-1,600,Image.SCALE_SMOOTH);
                    }
                }

            }else{
                if(h > 600) {
                    img2 = img2.getScaledInstance(-1, 600, Image.SCALE_SMOOTH);
                }

                else{
                    if(w > 800){
                        img2 = img2.getScaledInstance(800, -1, Image.SCALE_SMOOTH);
                    }
                }
            }
            img = new BufferedImage(img2.getWidth(null), img2.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D tp = img.createGraphics();
            tp.drawImage(img2, 0, 0, null);
            tp.dispose();

            setZoom(1);
        }catch(IOException e){}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = this.getPreferredSize();
        g.drawImage(img, 0, 0, size.width, size.height, this);
    }

    public void setZoom(double zoom) {
        int width = (int) (zoom * img.getWidth());
        int height = (int) (zoom * img.getHeight());
        setPreferredSize(new Dimension(width, height));
        repaint();
    }
}
