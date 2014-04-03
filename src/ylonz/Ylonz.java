package ylonz;

import GUI.GUI;
import java.io.IOException;

/**
 *
 * @author Lasse Lybeck
 */
public class Ylonz {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GUI().setVisible(true);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

}
