package ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ModernCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Padding horizontal
        setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Letra más grande para los datos

        if (isSelected) {
            c.setBackground(new Color(241, 245, 249)); // Slate 100
            c.setForeground(new Color(15, 23, 42)); // Slate 900
        } else {
            c.setBackground(Color.WHITE);
            c.setForeground(new Color(51, 65, 85)); // Slate 700
        }

        return c;
    }
}
