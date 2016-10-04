/**
 * Created by Manuel on 2016-03-08.
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GridView extends JPanel implements Observer{
    int width,height, cols, rows, picCount;
    int picHeight = 290;
    int picWidth = 230;
    private Model model;
    GridView(Model model_){
        model = model_;
        this.setLayout(new GridLayout(0, 4, 7,7));
        initLoad();
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize(){
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g){
        if(!model.isList) {
            this.setLayout(new GridLayout(0, cols, 7, 7));
        }
        else{
            this.setLayout(new GridLayout(0, 1, 7,7));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        removeAll();
        revalidate();
        picCount = 0;
            for (int i = 0; i < model.photos.size(); ++i) {
                if (model.photos.get(i).rating.getCurIndex() >= model.filter.getCurIndex()) {
                    picCount+=1;
                    model.photos.get(i).setBorder(BorderFactory.createLineBorder(Color.darkGray));;
                    model.photos.get(i).removeAll();
                    model.photos.get(i).setLayout(new BorderLayout());
                    model.photos.get(i).setMinimumSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setPreferredSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setMaximumSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setBackground(Color.GRAY);

                    JPanel stars = new JPanel();
                    JPanel attributes = new JPanel();
                    attributes.setLayout(new BoxLayout(attributes, BoxLayout.Y_AXIS));
                    JPanel info = new JPanel(new BorderLayout());

                    if(!model.isList) {
                        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
                        for (int j = 0; j < model.photos.get(i).rating.stars.size(); ++j) {
                            stars.add(model.photos.get(i).rating.stars.get(j), CENTER_ALIGNMENT);
                        }

                        model.photos.get(i).add(model.photos.get(i).label, BorderLayout.CENTER);
                        info.add(model.photos.get(i).name, BorderLayout.WEST);
                        info.add(model.photos.get(i).date, BorderLayout.EAST);
                        attributes.setPreferredSize(new Dimension(picWidth, 50));
                        attributes.add(stars);
                        attributes.add(info);
                        model.photos.get(i).add(attributes, BorderLayout.SOUTH);
                    }
                    else{
                        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
                        for (int j = 0; j < model.photos.get(i).rating.stars.size(); ++j) {
                            stars.add(model.photos.get(i).rating.stars.get(j), LEFT_ALIGNMENT);
                        }
                        info.add(stars, BorderLayout.NORTH);
                        JPanel temp = new JPanel(new BorderLayout());
                        temp.add(model.photos.get(i).name, BorderLayout.WEST);
                        temp.add(Box.createRigidArea(new Dimension(20, 4)), BorderLayout.CENTER);
                        temp.add(model.photos.get(i).date, BorderLayout.EAST);
                        info.add(temp, BorderLayout.WEST);
                        info.add(new JLabel(model.photos.get(i).path), BorderLayout.SOUTH);
                        attributes.setPreferredSize(new Dimension(picWidth, 50));
                        attributes.add(info, RIGHT_ALIGNMENT);
                        model.photos.get(i).add(attributes, BorderLayout.CENTER);
                        model.photos.get(i).add(model.photos.get(i).label, BorderLayout.WEST);
                    }
                    this.add(model.photos.get(i));
                }
            }

        Main.scroll.setVisible(false);
        if(!model.isList) {
            this.cols = (int)Math.floor(Main.panel.getWidth() / (double)this.picWidth);
            this.rows = (int)Math.ceil(picCount / (double)this.cols);
            int h = (int)Math.ceil((this.picHeight) * this.rows);
            this.width = Main.panel.getWidth();
            this.height = h;

            this.setMinimumSize(new Dimension(Main.panel.getWidth(), h));
            this.setPreferredSize(new Dimension(Main.panel.getWidth(), h));
            this.setMaximumSize(new Dimension(Main.panel.getWidth(), h));
        }

        else{
            this.width = Main.panel.getWidth();
            this.height = (this.picHeight-40) * picCount;
            this.setMinimumSize(new Dimension(this.width, this.height));
            this.setPreferredSize(new Dimension(this.width, this.height));
            this.setMaximumSize(new Dimension(this.width, this.height));
        }
        Main.scroll.setVisible(true);
        repaint();
    }

    public void imageExpand(Images pic){
        Border empty = BorderFactory.createEmptyBorder();
        JFrame frame = new JFrame(pic.name.getText());
        JPanel details = new JPanel();
        JLabel zoomtext = new JLabel("Zoom:");
        JSlider zoom = new JSlider(50,100,50);
        zoomtext.setBorder(empty);
        zoom.setBorder(empty);
        zoom.setVisible(false);
        zoom.setVisible(false);
        ImageComp img = new ImageComp(pic.path);
        zoom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                img.setZoom(2.0 * zoom.getValue() / 100);
            }
        });


        details.add(zoomtext);
        details.add(zoom);
        details.setBorder(empty);

        img.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                zoom.setVisible(true);
                zoomtext.setVisible(true);            }

            @Override
            public void mouseExited(MouseEvent e) {
                zoom.setVisible(false);
                zoomtext.setVisible(false);            }
        });

        JScrollPane pane = new JScrollPane(img);

        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                zoom.setVisible(true);
                zoomtext.setVisible(true);            }
        });

        frame.getContentPane().add(pane);
        frame.add(details, BorderLayout.SOUTH);
        frame.setMaximumSize(new Dimension(800,600));
        frame.setPreferredSize(new Dimension(800,600));
        frame.setMinimumSize(new Dimension(800,600));
        frame.setVisible(true);

    }
    public void initLoad(){
        File myFile = new File("photos.txt");
        if(!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
            }
        }
        else{
        }

        Path fpath = Paths.get(myFile.getAbsolutePath());

        try {
            List<String> Temp;
            Temp = Files.readAllLines(fpath, Charset.defaultCharset());
            for(String line : Temp) {
                String split[] = line.split(" ");
                File f = new File(split[0]);
                Path file = Paths.get(f.getAbsolutePath());
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

                model.filename.add(split[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                BufferedImage buff = ImageIO.read(f);
                ImageIcon grassIcon = new ImageIcon(buff);
                Image img = grassIcon.getImage();
                Image newimg = img.getScaledInstance(210, -1, Image.SCALE_SMOOTH);
                grassIcon = new ImageIcon(newimg);
                Images temp = new Images();
                temp.name = new JLabel(f.getName());
                temp.date = new JLabel(sdf.format(attr.creationTime().toMillis()));
                temp.path = new String(f.getPath());
                temp.rating = new stars();
                model.initializeStars(temp.rating);
                temp.rating.setCurIndex(Integer.parseInt(split[1]));
                temp.label = new JLabel(grassIcon);
                temp.label.setMinimumSize(new Dimension(210,250));
                temp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        imageExpand(((Images)e.getSource()));
                    }
                });

                model.photos.add(temp);
            }

            picCount = 0;
            for (int i = 0; i < model.photos.size(); ++i) {
                if (model.photos.get(i).rating.getCurIndex() >= model.filter.getCurIndex()) {
                    picCount+=1;
                    model.photos.get(i).setBorder(BorderFactory.createLineBorder(Color.darkGray));;
                    model.photos.get(i).removeAll();
                    model.photos.get(i).setLayout(new BorderLayout());
                    model.photos.get(i).setMinimumSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setPreferredSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setMaximumSize(new Dimension(picWidth, picHeight));
                    model.photos.get(i).setBackground(Color.GRAY);

                    JPanel stars = new JPanel();
                    JPanel attributes = new JPanel();
                    attributes.setLayout(new BoxLayout(attributes, BoxLayout.Y_AXIS));
                    JPanel info = new JPanel(new BorderLayout());

                    if(!model.isList) {
                        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
                        for (int j = 0; j < model.photos.get(i).rating.stars.size(); ++j) {
                            stars.add(model.photos.get(i).rating.stars.get(j), CENTER_ALIGNMENT);
                        }

                        model.photos.get(i).add(model.photos.get(i).label, BorderLayout.CENTER);
                        info.add(model.photos.get(i).name, BorderLayout.WEST);
                        info.add(model.photos.get(i).date, BorderLayout.EAST);
                        attributes.setPreferredSize(new Dimension(picWidth, 50));
                        attributes.add(stars);
                        attributes.add(info);
                        model.photos.get(i).add(attributes, BorderLayout.SOUTH);
                    }
                    else{
                        stars.setLayout(new BoxLayout(stars, BoxLayout.X_AXIS));
                        for (int j = 0; j < model.photos.get(i).rating.stars.size(); ++j) {
                            stars.add(model.photos.get(i).rating.stars.get(j), LEFT_ALIGNMENT);
                        }
                        info.add(stars, BorderLayout.NORTH);
                        JPanel temp = new JPanel(new BorderLayout());
                        temp.add(model.photos.get(i).name, BorderLayout.WEST);
                        temp.add(Box.createRigidArea(new Dimension(20, 4)), BorderLayout.CENTER);
                        temp.add(model.photos.get(i).date, BorderLayout.EAST);
                        info.add(temp, BorderLayout.WEST);
                        info.add(new JLabel(model.photos.get(i).path), BorderLayout.SOUTH);
                        attributes.setPreferredSize(new Dimension(picWidth, 50));
                        attributes.add(info, RIGHT_ALIGNMENT);
                        model.photos.get(i).add(attributes, BorderLayout.CENTER);
                        model.photos.get(i).add(model.photos.get(i).label, BorderLayout.WEST);
                    }
                    this.add(model.photos.get(i));
                }
            }
        }catch (IOException e){}

    }
}
