package ui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatusBadgeRenderer extends DefaultTableCellRenderer {

    private String status = "";

    public StatusBadgeRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(false); // Importante para dibujar nuestro propio fondo
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.status = value != null ? value.toString() : "";

        if ("Aprobado".equals(status)) {
            setForeground(new Color(22, 163, 74)); // Green 600
        } else {
            setForeground(new Color(220, 38, 38)); // Red 600
        }
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el badge (fondo redondeado)
        int width = getWidth();
        int height = getHeight();

        // Colores de fondo del badge
        if ("Aprobado".equals(status)) {
            g2.setColor(new Color(220, 252, 231)); // Green 100
        } else {
            g2.setColor(new Color(254, 226, 226)); // Red 100
        }

        // Medidas para centrar el badge verticalmente y darle padding horizontal
        int badgeHeight = 24;
        int badgeWidth = 90;
        int y = (height - badgeHeight) / 2;
        int x = (width - badgeWidth) / 2;

        g2.fill(new RoundRectangle2D.Double(x, y, badgeWidth, badgeHeight, 20, 20));

        g2.dispose();

        // Llamar a super para que dibuje el texto encima de nuestro badge
        super.paintComponent(g);
    }
}
