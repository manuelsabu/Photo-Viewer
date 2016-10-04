/**
 * Created by Manuel on 2016-03-04.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main {

    public static JFrame frame = new JFrame("Fotag");
    public static JPanel panel = new JPanel();
    public static JScrollPane scroll;
    public static Model model = new Model();
    public static GridView view = new GridView(model);
    public static Toolbar tools = new Toolbar(model);

    public static void main(String[] args) {

        model.addObserver(view);
        model.addObserver(tools);
        model.notifyObservers();

        panel.setLayout(new BorderLayout());
        panel.add(tools, BorderLayout.NORTH);
        scroll = new JScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setBackground(Color.darkGray);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                scroll.setVisible(false);
                if(!model.isList) {
                    view.cols = (int)Math.floor(frame.getWidth() / (double)view.picWidth);
                    view.rows = (int)Math.ceil(view.picCount / (double)view.cols);
                    int h = (int)Math.ceil((view.picHeight) * view.rows);
                    view.width = frame.getWidth();
                    view.height = h;

                    view.setMinimumSize(new Dimension(frame.getWidth(), h));
                    view.setPreferredSize(new Dimension(frame.getWidth(), h));
                    view.setMaximumSize(new Dimension(frame.getWidth(), h));
                }
                else{
                    view.width = panel.getWidth();
                    view.height = (view.picHeight-40) * view.picCount;
                    view.setMinimumSize(new Dimension(view.width, view.height));
                    view.setPreferredSize(new Dimension(view.width, view.height));
                    view.setMaximumSize(new Dimension(view.width, view.height));
                }
                scroll.setVisible(true);
            }
        });

        frame.getContentPane().add(panel);
        frame.setMinimumSize(new Dimension(300, 300));
        frame.setPreferredSize(new Dimension(500, 500));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addWindowListener(new ListenCloseWdw());

    }
    public static class ListenCloseWdw extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            try{
                File myFile = new File("photos.txt");
                if(!myFile.exists()) {
                    myFile.createNewFile();
                }
                else{
                        PrintWriter writer2 = new PrintWriter(myFile);
                        writer2.print("");
                        writer2.close();

                    }
                BufferedWriter writer = new BufferedWriter(new FileWriter(myFile, true));
                for(int i = 0; i < model.photos.size(); ++i) {
                        writer.write(model.photos.get(i).path);
                        writer.write(" ");
                        writer.write(Integer.toString(model.photos.get(i).rating.getCurIndex()));
                        writer.newLine();
                }
                writer.close();
        } catch (IOException f) {
        }
            System.exit(0);
        }
    }
}