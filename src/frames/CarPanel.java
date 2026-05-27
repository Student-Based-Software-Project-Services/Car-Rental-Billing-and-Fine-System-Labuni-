package frames;

import model.Car;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CarPanel extends JPanel {
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;

    public CarPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 244, 250));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(240, 244, 250));
        header.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JLabel title = new JLabel("Car Inventory");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 45, 70));

        JButton btnAdd = new JButton("+ Add Car");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");

        styleButton(btnAdd, new Color(60, 160, 100));
        styleButton(btnEdit, new Color(70, 130, 200));
        styleButton(btnDelete, new Color(210, 70, 70));

        header.add(title);
        header.add(Box.createHorizontalStrut(20));
        header.add(btnAdd);
        header.add(btnEdit);
        header.add(btnDelete);

        String[] cols = {"ID", "Make", "Model", "Year", "Color", "VIN", "Price", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setSelectionBackground(new Color(190, 215, 245));
        table.setGridColor(new Color(220, 228, 240));
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadData();

        btnAdd.addActionListener(e -> {
            CarDialog dlg = new CarDialog(null, null);
            dlg.setVisible(true);
            if (dlg.getCar() != null) {
                RepoManager.getInstance().getCarRepo().addCar(dlg.getCar());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Added car: " + dlg.getCar().getMake() + " " + dlg.getCar().getModel());
                loadData();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a car to edit."); return; }
            Car selected = getCar(row);
            CarDialog dlg = new CarDialog(null, selected);
            dlg.setVisible(true);
            if (dlg.getCar() != null) {
                RepoManager.getInstance().getCarRepo().updateCar(dlg.getCar());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Updated car ID: " + selected.getId());
                loadData();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a car to delete."); return; }
            Car selected = getCar(row);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this car?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                RepoManager.getInstance().getCarRepo().deleteCar(selected.getId());
                RepoManager.getInstance().getLogRepo().log(currentUser.getUsername(), "Deleted car ID: " + selected.getId());
                loadData();
            }
        });
    }

    private Car getCar(int row) {
        return new Car(
                (int) tableModel.getValueAt(row, 0),
                (String) tableModel.getValueAt(row, 1),
                (String) tableModel.getValueAt(row, 2),
                Integer.parseInt(tableModel.getValueAt(row, 3).toString()),
                (String) tableModel.getValueAt(row, 4),
                (String) tableModel.getValueAt(row, 5),
                Double.parseDouble(tableModel.getValueAt(row, 6).toString()),
                (String) tableModel.getValueAt(row, 7)
        );
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Car> cars = RepoManager.getInstance().getCarRepo().getAllCars();
        for (Car c : cars) {
            tableModel.addRow(new Object[]{c.getId(), c.getMake(), c.getModel(), c.getYear(), c.getColor(), c.getVin(), c.getPrice(), c.getStatus()});
        }
    }

    private void styleButton(JButton btn, Color fg) {
        btn.setBackground(Color.decode("#F0F4FA"));
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}