import start.StartWindow;

import javax.swing.*;

public class Swing_Program {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartWindow().setVisible(true);
            }
        });
    }
}
