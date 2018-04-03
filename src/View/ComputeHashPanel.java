package View;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kevin Ravna
 * @version Winter2017
 */
public class ComputeHashPanel extends JPanel {

    public ComputeHashPanel() {
        super();
        initialize();
    }

    private void initialize() {
        this.setLayout(new FlowLayout());
        this.add(new JLabel("SmileyFace:"));
        this.add(returnButton());
    }

    private JButton returnButton() {
        JButton returnButton = new JButton("Return to Services");
        returnButton.addActionListener(e -> {
            returnToServices();
        });

        return returnButton;
    }

    private void returnToServices() {
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new HomePanel());
        frame.revalidate();
    }

}
