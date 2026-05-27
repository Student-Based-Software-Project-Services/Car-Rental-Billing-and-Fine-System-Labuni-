package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Car Retail System - Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(240, 244, 250));

        JPanel sidebar = buildSidebar();
        main.add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 244, 250));
        main.add(contentPanel, BorderLayout.CENTER);

        setContentPane(main);
        showHome();
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(20, 30, 45));
        sidebar.setPreferredSize(new Dimension(180, 500));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("🚗 CarRetail");
        brand.setFont(new Font("SansSerif", Font.BOLD, 16));
        brand.setForeground(new Color(100, 180, 255));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        brand.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        sidebar.add(brand);

        String[][] menuItems = {
            {"🏠", "Home"},
            {"🚘", "Cars"},
            {"👥", "Customers"},
            {"🛒", "Transactions"},
            {"💳", "Payments"},
            {"📋", "Activity Log"}
        };

        for (String[] item : menuItems) {
            JButton btn = createNavButton(item[0] + "  " + item[1]);
            String label = item[1];
            btn.addActionListener(e -> navigate(label));
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel userInfo = new JLabel("👤 " + currentUser.getUsername());
        userInfo.setForeground(new Color(140, 160, 190));
        userInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        userInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfo.setBorder(BorderFactory.createEmptyBorder(0, 10, 4, 10));
        sidebar.add(userInfo);

        JButton btnLogout = createNavButton("🚪  Logout");
        btnLogout.setForeground(new Color(210, 70, 70));
        btnLogout.addActionListener(e -> {
            RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Logged out");
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBackground(new Color(20, 30, 45));
//        btn.setForeground(new Color(200, 215, 235));
//        btn.setBackground(new Color(255,255,255));
        btn.setForeground(Color.decode("#141E2D"));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void navigate(String section) {
        contentPanel.removeAll();
        switch (section) {
            case "Cars" -> contentPanel.add(new CarPanel(currentUser), BorderLayout.CENTER);
            case "Customers" -> contentPanel.add(new CustomerPanel(currentUser), BorderLayout.CENTER);
            case "Transactions" -> contentPanel.add(new TransactionPanel(currentUser), BorderLayout.CENTER);
            case "Payments" -> contentPanel.add(new PaymentPanel(currentUser), BorderLayout.CENTER);
            case "Activity Log" -> contentPanel.add(new LogPanel(), BorderLayout.CENTER);
            default -> {
                showHome(); return;
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showHome() {
        contentPanel.removeAll();
        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(new Color(240, 244, 250));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 240), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        JLabel welcome = new JLabel("Welcome back, " + currentUser.getUsername() + "!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcome.setForeground(new Color(30, 45, 70));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel role = new JLabel("Role: " + currentUser.getRole().toUpperCase());
        role.setFont(new Font("SansSerif", Font.PLAIN, 13));
        role.setForeground(new Color(100, 120, 150));
        role.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Use the sidebar to navigate the system.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 12));
        hint.setForeground(new Color(140, 160, 190));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(welcome);
        card.add(Box.createVerticalStrut(8));
        card.add(role);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        home.add(card);
        contentPanel.add(home, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}