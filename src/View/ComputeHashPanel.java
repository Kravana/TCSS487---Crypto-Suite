package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;

import Model.SHA;

/**
 * @author Kevin Ravna
 * @version Winter2017
 */
public class ComputeHashPanel extends JPanel {

    private JLabel TEXT_HASH_OUTPUT = new JLabel();

    private JLabel FILE_HASH_OUTPUT = new JLabel();

    private JLabel TEXT_HASH_OUTPUT_LABEL = new JLabel("Text hash: ");

    private JLabel FILE_HASH_OUTPUT_LABEL = new JLabel("File hash: ");

    private JTextField MESSAGE_AREA = new JTextField("Enter message to hash here.");

    private File file;

    private String fileLocation;

    public ComputeHashPanel() {
        super();
        initialize();
    }

    private void initialize() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.anchor = (gbc.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.CENTER;

        gbc.insets = new Insets(0, 6, 6, 0);

        MESSAGE_AREA.setColumns(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Enter message to hash: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(MESSAGE_AREA);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("or"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Select file to hash: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(selectFileButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(computeHashButton(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(returnButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(TEXT_HASH_OUTPUT_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(TEXT_HASH_OUTPUT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(FILE_HASH_OUTPUT_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(FILE_HASH_OUTPUT, gbc);


    }

    private JButton selectFileButton() {
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser();
            int returnValue = fc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                fileLocation = file.getAbsolutePath();
                System.out.println(file.getAbsolutePath());
            }
        });
        selectFileButton.setSize(new Dimension(100, selectFileButton.getHeight()));
        return selectFileButton;
    }

    private JButton computeHashButton() {
        JButton computeHashButton = new JButton("Compute Hash");
        computeHashButton.addActionListener(e -> {
            computeTextHash();
            if (fileLocation != null) computeFileHash();
        });
        computeHashButton.setSize(new Dimension(100, computeHashButton.getHeight()));
        return computeHashButton;
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
        frame.repaint();
    }

    private void computeTextHash() {
        try {
            String hash = SHA.computeWithJavaSec(MESSAGE_AREA.getText());
            TEXT_HASH_OUTPUT.setText(hash);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }

    private void computeFileHash() {
        try {
            String hash = SHA.computeWithJavaSec(fileLocation);
            FILE_HASH_OUTPUT.setText(hash);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
}
