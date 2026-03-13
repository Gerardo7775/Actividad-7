import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ModernTableHeaderRenderer extends DefaultTableCellRenderer {

    public ModernTableHeaderRenderer() {
        setOpaque(true);
        setBackground(new Color(241, 245, 249)); // Slate 100
        setForeground(new Color(15, 23, 42));    // Slate 900
        setFont(new Font("Segoe UI", Font.BOLD, 15));
        setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        setHorizontalAlignment(SwingConstants.LEFT);
        
        // Colocar la flecha a la derecha del texto
        setHorizontalTextPosition(SwingConstants.LEFT);
        setIconTextGap(8); // Espacio entre texto y flecha
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Detectar si esta columna está siendo ordenada
        SortOrder sortOrder = SortOrder.UNSORTED;
        if (table.getRowSorter() != null) {
            java.util.List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
            if (!sortKeys.isEmpty() && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                sortOrder = sortKeys.get(0).getSortOrder();
            }
        }

        // Asignar el ícono correspondiente (nuestra flecha dibujada)
        setIcon(new SortIcon(sortOrder));

        return this;
    }

    // --- CLASE INTERNA PARA DIBUJAR LA FLECHA ---
    private class SortIcon implements Icon {
        private final SortOrder order;

        public SortIcon(SortOrder order) {
            this.order = order;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            if (order == SortOrder.UNSORTED) return; // No dibuja nada si no está ordenada

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(100, 116, 139)); // Slate 500 (Gris oscuro)

            // Dibujar un triángulo hacia arriba o hacia abajo
            int size = 8;
            int yOffset = y + 4; // Centrar verticalmente
            
            if (order == SortOrder.ASCENDING) {
                int[] xPoints = {x, x + (size/2), x + size};
                int[] yPoints = {yOffset + size, yOffset, yOffset + size};
                g2.fillPolygon(xPoints, yPoints, 3); // Triángulo hacia arriba
            } else {
                int[] xPoints = {x, x + (size/2), x + size};
                int[] yPoints = {yOffset, yOffset + size, yOffset};
                g2.fillPolygon(xPoints, yPoints, 3); // Triángulo hacia abajo
            }
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return 10; } // Ancho reservado para el icono

        @Override
        public int getIconHeight() { return 10; }
    }
}