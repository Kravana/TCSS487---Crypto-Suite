package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * @author Kevin Ravana
 * @version Spring 2018
 */
public class HomePanel extends JPanel {


    private final String[] SERVICE_NAMES = {"1. Compute hash", "2. KMAC Encrypt/Decrypt", "3. Generate Key Pair", "4. ECC Encrypt/Decrypt"};

    private final JPanel[] PANELS = {new ComputeHashPanel(), new EncryptDecryptPanel(), new GenerateECCKeyPairPanel(), new ECCEncryptDecryptPanel()};


    public HomePanel() {
        super();
        initialize();
    }

    private void initialize() {
        this.setLayout(new FlowLayout());
        this.add(new JLabel("Select a service:"));
        this.add(serviceSelector(SERVICE_NAMES));
    }

    private JComboBox<String> serviceSelector(final String[] theServices) {
        JComboBox<String> serviceSelector = new JComboBox<>(theServices);
        serviceSelector.addActionListener(e -> {
            JComboBox combo = (JComboBox)e.getSource();
            String currentSelection = (String)combo.getSelectedItem();
            changePanel(currentSelection);
        });


        return serviceSelector;
    }

    private void changePanel(final String thePanelName) {
        int index = 0;
        for (final String name : SERVICE_NAMES) {
            if (name.equals(thePanelName)) {
                break;
            } else {
                index++;
            }
        }
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(PANELS[index]);
        frame.revalidate();
        frame.repaint();
    }


}
