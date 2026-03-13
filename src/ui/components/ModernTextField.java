package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernTextField extends JTextField {

    // Colores basados en la paleta de Tailwind CSS (Slate y Blue)
    private Color borderColor = new Color(203, 213, 225); // Borde inactivo
    private Color focusedBorderColor = new Color(59, 130, 246); // Borde activo (focus)

    public ModernTextField(String text) {
        super(text);
        setOpaque(false); // Necesario para que se vea nuestro dibujo redondeado
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding interno
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(15, 23, 42)); // Texto oscuro

        // Cambiar el color del borde cuando el componente recibe el foco
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                borderColor = focusedBorderColor;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                borderColor = new Color(203, 213, 225);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo blanco redondeado
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el borde redondeado
        g2.setColor(borderColor);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
        g2.dispose();
    }
}
