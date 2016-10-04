/**
 * Created by Manuel on 2016-03-08.
 */
import javax.swing.*;

public class Images extends JPanel{
    public Images(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    JLabel label, date, name;
    String path;
    stars rating = new stars();
    int rate = 0;

}
