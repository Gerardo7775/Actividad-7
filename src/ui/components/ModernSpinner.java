package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernSpinner extends JPanel {

    private int value;
    private final int min;
    private final int max;
    private final int step;
    private JLabel lblValue;
    private final Color borderColor = new Color(203, 213, 225); // Slate 300

    public ModernSpinner(int initialValue, int min, int max, int step) {
        this.value = initialValue;
        this.min = min;
        this.max = max;
        this.step = step;

        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 40)); // Altura fija para que coincida con el TextField

        // Crear botones personalizados
        JButton btnMinus = createSpinnerButton("-");
        JButton btnPlus = createSpinnerButton("+");

        // Etiqueta central
        lblValue = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblValue.setForeground(new Color(15, 23, 42));

        // Lógica de incremento/decremento
        btnMinus.addActionListener(e -> updateValue(-this.step));
        btnPlus.addActionListener(e -> updateValue(this.step));

        add(btnMinus, BorderLayout.WEST);
        add(lblValue, BorderLayout.CENTER);
        add(btnPlus, BorderLayout.EAST);
    }

    private void updateValue(int change) {
        int newValue = value + change;
        if (newValue >= min && newValue <= max) {
            value = newValue;
            lblValue.setText(String.valueOf(value));
        }
    }

    public int getValue() {
        return value;
    }

    private JButton createSpinnerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(new Color(100, 116, 139)); // Slate 500
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover sutil
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(15, 23, 42)); // Más oscuro al pasar el mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(100, 116, 139)); // Vuelve al color original
            }
        });
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo blanco y borde
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
        g2.setColor(borderColor);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));

        g2.dispose();
        super.paintComponent(g);
    }
}
