package View;

import Controller.ECCController;
import Controller.KMACController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
class ECCEncryptDecryptPanel extends JPanel {

    private JTextArea FILE_TO_ENCRYPT = new JTextArea(4, 20);

    private JTextArea DECRYPTED_FILE = new JTextArea(4, 20);

    private JTextArea PRIVATE_KEY = new JTextArea(4, 20);

    private JTextArea PUBLIC_KEY_X = new JTextArea(4, 20);

    private JTextArea PUBLIC_KEY_Y = new JTextArea(4, 20);

    private JLabel FILE_TO_ENCRYPT_LABEL = new JLabel("Encrypted file: ");

    private JLabel DECRYPTED_FILE_LABEL = new JLabel("Decrypted result: ");

    private JLabel PRIVATE_KEY_LABEL = new JLabel("Private key: ");

    private JLabel PUBLIC_KEY_X_LABEL = new JLabel("Public key x: ");

    private JLabel PUBLIC_KEY_Y_LABEL = new JLabel("Public key y: ");

    private JTextField PASSWORD_AREA = new JTextField("password123");

    private Dimension DEFAULT_BUTTON_SIZE = new Dimension(150, 30);

    private JButton encryptFileButton;

    private JButton decryptFileButton;

    private JScrollPane fileHexPane;

    private JScrollPane cryptogramHexPane;

    private JScrollPane privateKeyPane;

    private JScrollPane publicKeyXPane;

    private JScrollPane publicKeyYPane;

    private String fileLocation;

    private String cryptogramLocation;

    private ECCController ec;

    ECCEncryptDecryptPanel() {
        super();
        initialize();
    }

    private void initialize() {

        encryptFileButton = encryptFileButton();
        encryptFileButton.setEnabled(false);
        encryptFileButton.setPreferredSize(DEFAULT_BUTTON_SIZE);

        decryptFileButton = decryptFileButton();
        decryptFileButton.setEnabled(false);
        decryptFileButton.setPreferredSize(DEFAULT_BUTTON_SIZE);

        JButton selectFileToEncryptButton = selectFileToEncryptButton();
        selectFileToEncryptButton.setPreferredSize(DEFAULT_BUTTON_SIZE);

        JButton selectFileToDecryptButton = selectFileToDecryptButton();
        selectFileToDecryptButton.setPreferredSize(DEFAULT_BUTTON_SIZE);

        fileHexPane = new JScrollPane(new JPanel().add(FILE_TO_ENCRYPT),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        FILE_TO_ENCRYPT.setEditable(false);
        FILE_TO_ENCRYPT.setLineWrap(true);

        cryptogramHexPane = new JScrollPane(new JPanel().add(DECRYPTED_FILE),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        DECRYPTED_FILE.setEditable(false);
        DECRYPTED_FILE.setLineWrap(true);

        privateKeyPane = new JScrollPane(new JPanel().add(PRIVATE_KEY),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PRIVATE_KEY.setEditable(false);
        PRIVATE_KEY.setLineWrap(true);

        publicKeyXPane = new JScrollPane(new JPanel().add(PUBLIC_KEY_X),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PUBLIC_KEY_X.setEditable(false);
        PUBLIC_KEY_X.setLineWrap(true);

        publicKeyYPane = new JScrollPane(new JPanel().add(PUBLIC_KEY_Y),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PUBLIC_KEY_Y.setEditable(false);
        PUBLIC_KEY_Y.setLineWrap(true);

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
        this.add(new JLabel("Generate a key pair: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(generateKeyPairButton(), gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        this.add(selectFileToEncryptButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        this.add(selectFileToDecryptButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        this.add(encryptFileButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        this.add(decryptFileButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(returnButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(PRIVATE_KEY_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(privateKeyPane, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        this.add(FILE_TO_ENCRYPT_LABEL, gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        this.add(fileHexPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(PUBLIC_KEY_X_LABEL, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        this.add(PUBLIC_KEY_Y_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(publicKeyXPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        this.add(publicKeyYPane, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        this.add(DECRYPTED_FILE_LABEL, gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        this.add(cryptogramHexPane, gbc);


    }

    private JButton selectFileToEncryptButton() {
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fc.setCurrentDirectory(workingDirectory);
            int returnValue = fc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                fileLocation = file.getAbsolutePath();
                encryptFileButton.setEnabled(true);
                System.out.println(file.getAbsolutePath());
            }
        });
        selectFileButton.setSize(new Dimension(100, selectFileButton.getHeight()));
        return selectFileButton;
    }

    private JButton selectFileToDecryptButton() {
        JButton selectFileButton = new JButton("Select Cryptogram");
        selectFileButton.addActionListener(e -> {
            final JFileChooser fc = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fc.setCurrentDirectory(workingDirectory);
            int returnValue = fc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                cryptogramLocation = file.getAbsolutePath();
                decryptFileButton.setEnabled(true);
                System.out.println(file.getAbsolutePath());
            }
        });
        selectFileButton.setSize(new Dimension(100, selectFileButton.getHeight()));
        return selectFileButton;
    }

    private JButton generateKeyPairButton() {
        JButton generateKeyPairButton = new JButton("Generate Key Pair");
        generateKeyPairButton.addActionListener(e -> {
            try {
                generateKeyPair();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        generateKeyPairButton.setSize(new Dimension(100, generateKeyPairButton.getHeight()));
        return generateKeyPairButton;
    }

    private JButton encryptFileButton() {
        JButton encryptFileButton = new JButton("Encrypt File");
        encryptFileButton.addActionListener(e -> {
            if (fileLocation != null) {
                try {
                    encryptFile();
                    fileHexPane.revalidate();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        encryptFileButton.setSize(new Dimension(100, encryptFileButton.getHeight()));
        return encryptFileButton;
    }

    private JButton decryptFileButton() {
        JButton decryptFileButton = new JButton("Decrypt File");
        decryptFileButton.addActionListener(e -> {
            if (cryptogramLocation != null) {
                try {
                    decryptFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        decryptFileButton.setSize(new Dimension(100, decryptFileButton.getHeight()));
        return decryptFileButton;
    }

    private JButton returnButton() {
        JButton returnButton = new JButton("Return to Services");
        returnButton.addActionListener(e -> returnToServices());
        return returnButton;
    }

    private void returnToServices() {
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new HomePanel());
        frame.revalidate();
        frame.repaint();
    }

    private void generateKeyPair() throws IOException {
        ec = new ECCController();
        ec.generateECDHIESKeyPair(PASSWORD_AREA.getText());
        ((JTextArea)(privateKeyPane.getViewport().getView())).setText(ec.ECDHIESPrivateKeyToString());
        ((JTextArea)(publicKeyXPane.getViewport().getView())).setText(ec.ECDHIESPublicKeyXToString());
        ((JTextArea)(publicKeyYPane.getViewport().getView())).setText(ec.ECDHIESPublicKeyYToString());
    }

    private void encryptFile() throws IOException {
        ((JTextArea)(fileHexPane.getViewport().getView())).setText(KMACController.encrypt(fileLocation, PASSWORD_AREA.getText()));
    }

    private void decryptFile() throws IOException {
        ((JTextArea)(cryptogramHexPane.getViewport().getView())).setText(KMACController.decrypt(cryptogramLocation, PASSWORD_AREA.getText()));
    }

}
