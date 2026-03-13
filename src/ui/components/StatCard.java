package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StatCard extends JPanel {
    private JLabel lblValue;

    public StatCard(String title, String initialValue, Color accentColor) {
        setOpaque(false);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Título de la tarjeta
        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitle.setForeground(new Color(100, 116, 139)); // Slate 500

        // Valor de la tarjeta (El número gigante)
        lblValue = new JLabel(initialValue);
        lblValue.setFont(new Font("Consolas", Font.BOLD, 22));
        lblValue.setForeground(new Color(15, 23, 42)); // Slate 900

        // Agrupamos textos
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(lblValue);

        // Indicador de color lateral
        JPanel indicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                // Dibujamos una pequeña píldora vertical
                g2.fill(new RoundRectangle2D.Double(0, 0, 5, getHeight(), 5, 5));
                g2.dispose();
            }
        };
        indicator.setPreferredSize(new Dimension(5, 0));
        indicator.setOpaque(false);

        add(indicator, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    // Método para actualizar el texto en tiempo real
    public void setValue(String val) {
        lblValue.setText(val);
    }

    // Método opcional por si quieres cambiar el color del texto dinámicamente
    public void setValueColor(Color color) {
        lblValue.setForeground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo blanco redondeado
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));

        // Borde sutil
        g2.setColor(new Color(226, 232, 240)); // Slate 200
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));

        g2.dispose();
        super.paintComponent(g);
    }
}
