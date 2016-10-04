/**
 * Created by Manuel on 2016-03-04.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class Model extends Observable {
    
    public JButton GridViewBut = new JButton();
    public JButton ListViewBut = new JButton();
    public JButton LoadImgBut = new JButton();
    public boolean isList = false;
    public ArrayList<Images> photos = new ArrayList<>();
    public stars filter = new stars();
    public ArrayList<Image> images = new ArrayList<>();
    public List<String> filename = new ArrayList<String> ();
    File files[] = null;
    
    public Model(){
        initializeStars(filter);
        setView();
    }
    
    public void initializeStars(stars filter){
        Border empty = BorderFactory.createEmptyBorder();
        
        for(int i = 0; i <= 5; ++i) {
            star temp = new star(i);
            temp.setBorder(empty);
            
            if(i == 0){
                try {
                    Image img = ImageIO.read(getClass().getResource("cross.png"));
                    temp.setIcon(new ImageIcon(img));
                } catch (IOException ex) {
                }
            }
            else {
                try {
                    Image img = ImageIO.read(getClass().getResource("staru.png"));
                    temp.setIcon(new ImageIcon(img));
                } catch (IOException ex) {
                }
            }
            filter.stars.add(temp);
        }
        
        for(int i = 0; i <= 5; ++i){
            filter.stars.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    filter.setCurIndex(((star)e.getSource()).index);
                    filter.updateStars(filter, filter.getCurIndex());
                    setChanged();
                    notifyObservers();
                }
                public void mouseEntered(MouseEvent e) {
                    filter.updateStars(filter, ((star)e.getSource()).index);
                }
                public void mouseExited(MouseEvent e) {
                    filter.updateStars(filter, filter.getCurIndex());
                }
            });
        }
    }
    
    public void setView(){
        Border empty = BorderFactory.createEmptyBorder();
        
        try {
            Image img = ImageIO.read(getClass().getResource("load.png"));
            LoadImgBut.setIcon(new ImageIcon(img));
            img = ImageIO.read(getClass().getResource("loadB.png"));
            LoadImgBut.setDisabledIcon(new ImageIcon(img));
            img = ImageIO.read(getClass().getResource("list.png"));
            ListViewBut.setIcon(new ImageIcon(img));
            img = ImageIO.read(getClass().getResource("gridB.png"));
            GridViewBut.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
        }
        
        GridViewBut.setBorder(empty);
        GridViewBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if(isList){
                        Image img = ImageIO.read(getClass().getResource("gridB.png"));
                        GridViewBut.setIcon(new ImageIcon(img));
                        img = ImageIO.read(getClass().getResource("list.png"));
                        ListViewBut.setIcon(new ImageIcon(img));
                        isList = false;
                        setChanged();
                        notifyObservers();
                    }
                } catch (IOException ex) {}
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                try{
                    if(isList){
                        Image img = ImageIO.read(getClass().getResource("gridB.png"));
                        GridViewBut.setIcon(new ImageIcon(img));
                    }
                } catch (IOException ex) {}
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                try{
                    if(isList){
                        Image img = ImageIO.read(getClass().getResource("grid.png"));
                        GridViewBut.setIcon(new ImageIcon(img));
                    }
                } catch (IOException ex) {}
            }
        });
        
        ListViewBut.setBorder(empty);
        ListViewBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if(!isList){
                        Image img = ImageIO.read(getClass().getResource("grid.png"));
                        GridViewBut.setIcon(new ImageIcon(img));
                        img = ImageIO.read(getClass().getResource("listB.png"));
                        ListViewBut.setIcon(new ImageIcon(img));
                        isList = true;
                        setChanged();
                        notifyObservers();
                    }
                } catch (IOException ex) {}
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                try{
                    if(!isList){
                        Image img = ImageIO.read(getClass().getResource("listB.png"));
                        ListViewBut.setIcon(new ImageIcon(img));
                    }
                } catch (IOException ex) {}
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                try{
                    if(!isList){
                        Image img = ImageIO.read(getClass().getResource("list.png"));
                        ListViewBut.setIcon(new ImageIcon(img));
                    }
                } catch (IOException ex) {}
            }
        });
        
        LoadImgBut.setBorder(empty);
        LoadImgBut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Choose Images");
                fc.setMultiSelectionEnabled(true);
                fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
                fc.setAcceptAllFileFilterUsed(false);
                
                int retval = fc.showOpenDialog(LoadImgBut);
                files = fc.getSelectedFiles();
                
                try{
                    for(File myFile : files) {
                        if(!filename.contains(myFile.getAbsolutePath())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                            BufferedImage buff = ImageIO.read(myFile.getAbsoluteFile());
                            ImageIcon grassIcon = new ImageIcon(buff);
                            Image img = grassIcon.getImage();
                            Image newimg = img.getScaledInstance(210, -1, Image.SCALE_SMOOTH);
                            grassIcon = new ImageIcon(newimg);
                            Path file = Paths.get(myFile.getAbsolutePath());
                            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                            
                            Images temp = new Images();
                            temp.name = new JLabel(myFile.getName());
                            temp.date = new JLabel(sdf.format(attr.creationTime().toMillis()));
                            temp.path = new String(myFile.getPath());
                            temp.rating = new stars();
                            initializeStars(temp.rating);
                            temp.label = new JLabel(grassIcon);
                            //temp.label.setMinimumSize(new Dimension(210, 250));
                            temp.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    Main.view.imageExpand(((Images)e.getSource()));
                                }
                            });
                            photos.add(temp);
                        }
                    }
                    for(int i = 0; i < files.length; ++i){
                        images.add(ImageIO.read(files[i]));
                    }
                } catch(IOException f){}
                setChanged();
                notifyObservers();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                try{
                    Image img = ImageIO.read(getClass().getResource("loadB.png"));
                    LoadImgBut.setIcon(new ImageIcon(img));
                } catch (IOException ex) {}
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                try{
                    Image img = ImageIO.read(getClass().getResource("load.png"));
                    LoadImgBut.setIcon(new ImageIcon(img));
                } catch (IOException ex) {}
            }
        });
        
    }
    
    
}
