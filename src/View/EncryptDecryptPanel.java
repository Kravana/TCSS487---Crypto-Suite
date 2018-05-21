package View;

import Controller.ToolController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class EncryptDecryptPanel extends JPanel {

    private JTextArea FILE_TO_ENCRYPT = new JTextArea();

    private JTextArea DECRYPTED_FILE = new JTextArea();

    private JLabel FILE_TO_ENCRYPT_LABEL = new JLabel("File to encrypt (0x): ");

    private JLabel DECRYPTED_FILE_LABEL = new JLabel("Decrypted file (0x): ");

    private JTextField PASSWORD_AREA = new JTextField("Enter passphrase here.");

    private File file;

    private String fileLocation;

    public EncryptDecryptPanel() {
        super();
        initialize();
    }

    private void initialize() {

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.anchor = (gbc.gridx == 0) ? GridBagConstraints.WEST : GridBagConstraints.CENTER;

        gbc.insets = new Insets(0, 6, 6, 0);

        PASSWORD_AREA.setColumns(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Enter desired passphrase: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(PASSWORD_AREA, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("and"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Select file to encrypt: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(selectFileButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(encryptFileButton(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(returnButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(FILE_TO_ENCRYPT_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(FILE_TO_ENCRYPT, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(DECRYPTED_FILE_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(DECRYPTED_FILE, gbc);


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

    private JButton encryptFileButton() {
        JButton computeHashButton = new JButton("Encrypt File");
        computeHashButton.addActionListener(e -> {
            if (fileLocation != null) {
                try {
                    encryptFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }
            }
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

    private void encryptFile() throws IOException, NoSuchAlgorithmException {
        FILE_TO_ENCRYPT.setText(ToolController.encrypt(fileLocation, PASSWORD_AREA.getText()));
        DECRYPTED_FILE.setText(ToolController.decrypt("encrypted_file", PASSWORD_AREA.getText()));
    }
}
