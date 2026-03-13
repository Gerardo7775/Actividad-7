package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;

public class ModernGlassPane extends JPanel {

    private Timer timer;
    private int angle = 0;
    private final String message = "Procesando transacciones...";

    public ModernGlassPane() {
        setOpaque(false); // Necesario para que sea transparente
        
        // --- BLOQUEADOR DE EVENTOS ---
        // Esto es lo que evita que el usuario pueda hacer clic en lo que hay debajo
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseAdapter() {});
        addKeyListener(new KeyAdapter() {});
        setFocusTraversalKeysEnabled(false);

        // --- ANIMACIÓN A 60 FPS ---
        // Un Timer que actualiza el ángulo del arco cada 16 milisegundos
        timer = new Timer(16, e -> {
            angle += 6; // Velocidad de giro
            if (angle >= 360) {
                angle = 0;
            }
            repaint();
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            requestFocusInWindow(); // Captura el foco del teclado
            timer.start();
        } else {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Dibujar el fondo oscuro translúcido
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new Color(15, 23, 42, 150)); // Slate 900 con 150 de opacidad (Alfa)
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 2. Dibujar la tarjeta central blanca
        int cardWidth = 280;
        int cardHeight = 200;
        int centerX = (getWidth() - cardWidth) / 2;
        int centerY = (getHeight() - cardHeight) / 2;

        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(centerX, centerY, cardWidth, cardHeight, 20, 20));

        // 3. Dibujar el anillo animado (Spinner)
        int spinnerSize = 60;
        int spinnerX = centerX + (cardWidth - spinnerSize) / 2;
        int spinnerY = centerY + 40;

        // Anillo de fondo (Gris claro)
        g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(226, 232, 240)); // Slate 200
        g2.drawOval(spinnerX, spinnerY, spinnerSize, spinnerSize);

        // Arco animado (Azul)
        g2.setColor(new Color(59, 130, 246)); // Blue 500
        // Dibujamos un arco que empieza en el 'angle' actual y tiene una longitud de 100 grados
        g2.draw(new Arc2D.Double(spinnerX, spinnerY, spinnerSize, spinnerSize, -angle, 100, Arc2D.OPEN));

        // 4. Dibujar el texto
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.setColor(new Color(15, 23, 42)); // Slate 900
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        g2.drawString(message, centerX + (cardWidth - textWidth) / 2, spinnerY + spinnerSize + 40);

        g2.dispose();
    }
}
