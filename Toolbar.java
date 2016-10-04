/**
 * Created by Manuel on 2016-03-04.
 */
import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

class Toolbar extends JPanel implements Observer {
    public static JLabel label = new JLabel("Filter: ");
    private Model model;
    Toolbar(Model model_) {
        model = model_;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(model.GridViewBut);
        this.add(model.ListViewBut);
        this.add(Box.createHorizontalGlue());
        this.add(model.LoadImgBut);
        this.add(Box.createHorizontalGlue());

        this.add(label);
        for(int i = 0; i < model_.filter.stars.size(); ++i) {
            this.add(model.filter.stars.get(i));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
