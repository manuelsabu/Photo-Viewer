import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Manuel on 2016-03-08.
 */
public class stars {
    public ArrayList<star> stars = new ArrayList<>();
    private int curIndex = 0;
    public Image cross, crossu, star, staru;
    public stars() {
        try {
            cross = ImageIO.read(getClass().getResource("cross.png"));
            crossu = ImageIO.read(getClass().getResource("crossu.png"));
            star = ImageIO.read(getClass().getResource("star.png"));
            staru = ImageIO.read(getClass().getResource("staru.png"));
        } catch (IOException ex) {}

    }

    public void setCurIndex(int i){
        this.curIndex = i;
        updateStars(this, i);
    }

    public int getCurIndex(){
        return this.curIndex;
    }
    public void updateStars(stars filter, int index){
        if(index == 0){
            filter.stars.get(0).setIcon(new ImageIcon(cross));
        }
        else {
            filter.stars.get(0).setIcon(new ImageIcon(crossu));
            for (int i = 1; i <= index; ++i) {
                filter.stars.get(i).setIcon(new ImageIcon(star));
            }
        }
        int i = 5;
        while (i > index) {
            filter.stars.get(i).setIcon(new ImageIcon(staru));
            --i;
        }
    }
}
