package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        setTitle("Car Retail System - Login");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(30, 40, 55));

        JPanel left = new JPanel();
        left.setBackground(new Color(20, 30, 45));
        left.setPreferredSize(new Dimension(350, 500));
        left.setLayout(new GridBagLayout());

        JLabel logo = new JLabel("🚗 CarRetail");
        logo.setFont(new Font("SansSerif", Font.BOLD, 28));
        logo.setForeground(new Color(100, 180, 255));

        JLabel sub = new JLabel("Management System");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(new Color(150, 170, 200));

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftContent.add(logo);
        leftContent.add(Box.createVerticalStrut(8));
        leftContent.add(sub);

        left.add(leftContent);

        JPanel right = new JPanel();
        right.setBackground(new Color(30, 40, 55));
        right.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(300, 280));

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(new Color(180, 200, 220));
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(300, 36));
        txtUsername.setPreferredSize(new Dimension(300, 36));
        txtUsername.setBackground(new Color(45, 55, 70));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 90, 120), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(new Color(180, 200, 220));
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(300, 36));
        txtPassword.setPreferredSize(new Dimension(300, 36));
        txtPassword.setBackground(new Color(45, 55, 70));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 90, 120), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnLogin = new JButton("LOGIN");
        btnLogin.setMaximumSize(new Dimension(300, 40));
        btnLogin.setPreferredSize(new Dimension(300, 40));
        btnLogin.setBackground(new Color(100, 180, 255));
        btnLogin.setForeground(new Color(20, 30, 45));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setFocusPainted(false);

        form.add(title);
        form.add(Box.createVerticalStrut(24));
        form.add(userLabel);
        form.add(Box.createVerticalStrut(4));
        form.add(txtUsername);
        form.add(Box.createVerticalStrut(14));
        form.add(passLabel);
        form.add(Box.createVerticalStrut(4));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(20));
        form.add(btnLogin);

        right.add(form);

        main.add(left, BorderLayout.WEST);
        main.add(right, BorderLayout.CENTER);

        setContentPane(main);

        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter credentials.");
            return;
        }
        User u = RepoManager.getInstance().getUserRepo().login(user, pass);
        if (u != null) {
            RepoManager.getInstance().getLogRepo().log(u.getUsername(), "Logged in");
            new DashboardFrame(u).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}