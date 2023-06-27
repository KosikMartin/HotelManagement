import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Táto trieda sa stará o login, ak náhodou niekto správne uhádne administrátor token a bude chciet ist do adminPanelu, bude sa najprv musiet prihlasit.
 * Slúži ako zábrana pred zneužitím privilegovaného módu.
 *
 * @author Martin Košík
 * @date 21/05/2023
 */
public class LoginPanel extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    /**
     * Konštruktor vytvára objekt typu LoginPanel a inicializuje všetky potrebné hodnoty aby sa loginpanel zobrazil.
     * Na rovnakom princípe funguje aj adminpanel, ale tu je navyše kontrola údajov.
     *
     * Ak používateľ zadá správne meno a heslo, je pustený do admin panelu.
     */

    public LoginPanel() {

        this.setTitle("Login");
        this.setSize(400, 220);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel nameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        this.nameField = new JTextField();
        this.passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = LoginPanel.this.nameField.getText();
                String password = new String(LoginPanel.this.passwordField.getPassword());
                if (LoginPanel.this.validLoginAttempt(name, password)) {
                    AdminPanel adminPanel = new AdminPanel();
                    adminPanel.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Nespravne meno alebo heslo");
                }
            }
        });

        panel.add(nameLabel);
        panel.add(this.nameField);
        panel.add(passwordLabel);
        panel.add(this.passwordField);
        panel.add(loginButton);
        this.add(panel);



    }

    /**
     * Metóda ktorá overuje správnosť zadaných údajov do polí pre meno a heslo loginpanelu
     *
     * @param name zadané meno
     * @param password zadané heslo
     * @return vracia hodnotu true false, (true pustí ma do programu, false nie)
     */
    private boolean validLoginAttempt(String name, String password) {
        return name.equals("admin") && password.equals("password");
    }

    /**
     *Metoda je urcena na spustenie login promptu po zadani adminTokenu.
     */
    public void executeLogin() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPanel loginPanel = new LoginPanel();
                loginPanel.setVisible(true);
            }
        });
    }
}
