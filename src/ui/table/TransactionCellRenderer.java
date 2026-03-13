package ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TransactionCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtener el estatus de la columna 2 ("Estatus")
        String status = (String) table.getModel().getValueAt(row, 2);

        if (isSelected) {
            c.setBackground(table.getSelectionBackground());
            c.setForeground(table.getSelectionForeground());
        } else {
            // Colorear según el estatus
            if ("Aprobado".equals(status)) {
                c.setBackground(new Color(230, 255, 230)); // Verde muy claro
                c.setForeground(new Color(0, 100, 0)); // Texto verde oscuro
            } else {
                c.setBackground(new Color(255, 230, 230)); // Rojo muy claro
                c.setForeground(new Color(150, 0, 0)); // Texto rojo oscuro
            }
        }

        // Centrar el texto
        setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }
}
