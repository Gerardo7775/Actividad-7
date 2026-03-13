package ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernScrollBarUI extends BasicScrollBarUI {
    
    // Grosor de la barra que arrastramos
    private final int thumbWidth = 8; 

    // Ocultar el botón de arriba/izquierda
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    // Ocultar el botón de abajo/derecha
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    // Crea un botón invisible con tamaño 0x0
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Fondo del canal por donde desliza la barra (Transparente para más limpieza)
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(245, 246, 250)); // Mismo color de fondo de nuestra app
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Elegir color según si el mouse está encima (hover) o presionando
        if (isThumbRollover()) {
            g2.setColor(new Color(148, 163, 184)); // Slate 400 (Hover)
        } else {
            g2.setColor(new Color(203, 213, 225)); // Slate 300 (Normal)
        }

        // Calcular la posición para centrar el pulgar (thumb) dentro del canal
        int x = thumbBounds.x + (thumbBounds.width - thumbWidth) / 2;
        int y = thumbBounds.y + 2; // Pequeño margen
        int width = thumbWidth;
        int height = thumbBounds.height - 4; // Pequeño margen

        // Dibujar el pulgar como un rectángulo muy redondeado (píldora)
        g2.fill(new RoundRectangle2D.Double(x, y, width, height, width, width));

        g2.dispose();
    }
}
