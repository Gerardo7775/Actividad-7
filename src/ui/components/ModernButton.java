package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernButton extends JButton {
    
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private final Color disabledColor = new Color(203, 213, 225); // Slate 300 (Gris claro cuando está inactivo)
    
    private boolean isHovered = false;
    private boolean isPressed = false;
    private final int radius = 15; // Qué tan redondeadas serán las esquinas

    public ModernButton(String text, Color normalColor, Color hoverColor, Color pressedColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;

        // Configuraciones básicas para apagar el dibujo nativo de Swing
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        
        // Estilos del texto
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Listeners para detectar cuando el mouse entra, sale o presiona
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint(); // Obliga a redibujar el botón
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    // Este es el método mágico donde definimos CÓMO se dibuja el botón
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Activa el antialiasing para que los bordes redondeados se vean suaves y no pixelados
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Lógica de colores según el estado
        if (!isEnabled()) {
            g2.setColor(disabledColor);
        } else if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }

        // Dibuja el fondo del rectángulo redondeado
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        g2.dispose();

        // Llama a super.paintComponent para que dibuje el texto por encima
        super.paintComponent(g);
    }
}
