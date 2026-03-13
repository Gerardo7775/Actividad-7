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

    // Crea un botón invisible y sin tamaño
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    // Dibuja el fondo por donde se desliza la barra (Track)
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Usamos un color transparente o igual al fondo para que sea invisible
        g2.setColor(new Color(248, 250, 252)); // Slate 50 (Color de fondo de tu panel)
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    // Dibuja la barra que el usuario arrastra (Thumb)
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Colores que cambian si le pasas el mouse o le das clic (efecto hover)
        if (isDragging) {
            g2.setColor(new Color(148, 163, 184)); // Slate 400 (Más oscuro)
        } else if (isThumbRollover()) {
            g2.setColor(new Color(203, 213, 225)); // Slate 300
        } else {
            g2.setColor(new Color(226, 232, 240)); // Slate 200 (Reposo)
        }

        // Cálculos para centrar la píldora dentro del espacio de la barra
        int x = thumbBounds.x + (thumbBounds.width - thumbWidth) / 2;
        int y = thumbBounds.y + 2; // Margen superior
        int width = thumbWidth;
        int height = thumbBounds.height - 4; // Margen inferior

        // Dibujar el rectángulo completamente redondeado
        g2.fill(new RoundRectangle2D.Double(x, y, width, height, width, width));
        g2.dispose();
    }
}
