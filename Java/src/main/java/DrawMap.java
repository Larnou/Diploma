import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

class DrawMap extends Frame {

    DrawMap() throws IOException {
        setSize(1100, 745);
        setTitle("Model");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
                              public void windowClosing(WindowEvent we) {
                                  System.exit(0);
                              }
                          }
        );
    }
    private final Image backgroundImage = ImageIO.read(new File("Images/map.png"));


    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(backgroundImage, 50, 50, null);


        g.setColor(Color.RED);
        g.drawOval(600, 300, 100, 100);
    }

}
