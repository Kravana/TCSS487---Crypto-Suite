package View;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class GUIMain {

        public GUIMain() {
            throw new IllegalStateException();
        }

        public static void main(final String[] theArgs) {
            EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    final JFrame window = new JFrame();
                    final JPanel mainPanel = new HomePanel();

                    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    window.setContentPane(mainPanel);
                    window.pack();
                    window.setLocationRelativeTo(null);
                    window.setVisible(true);

                    window.setSize(new Dimension(1350, 450));
                }
            });
        }

}
