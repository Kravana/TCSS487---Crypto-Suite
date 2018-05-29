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
class GenerateECCKeyPairPanel extends JPanel {

    private JTextArea PRIVATE_KEY = new JTextArea(4, 20);

    private JTextArea PUBLIC_KEY_X = new JTextArea(4, 20);

    private JTextArea PUBLIC_KEY_Y = new JTextArea(4, 20);

    private JLabel PRIVATE_KEY_LABEL = new JLabel("Private key: ");

    private JLabel PUBLIC_KEY_X_LABEL = new JLabel("Public key x: ");

    private JLabel PUBLIC_KEY_Y_LABEL = new JLabel("Public key y: ");

    private JTextField PASSWORD_AREA = new JTextField("password123");

    private Dimension DEFAULT_BUTTON_SIZE = new Dimension(150, 30);

    private JScrollPane privateKeyPane;

    private JScrollPane publicKeyXPane;

    private JScrollPane publicKeyYPane;

    private ECCController ec;

    GenerateECCKeyPairPanel() {
        super();
        initialize();
    }

    private void initialize() {

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

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(returnButton(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(PRIVATE_KEY_LABEL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(privateKeyPane, gbc);

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

}
