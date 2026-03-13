import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernAlert extends JDialog {

    // Definimos los tipos de alerta que soporta nuestro modal
    public enum AlertType {
        SUCCESS, ERROR
    }

    public ModernAlert(Frame parent, String title, String message, AlertType type) {
        super(parent, true); // Modal
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); 
        setSize(320, 220);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.setColor(new Color(226, 232, 240)); 
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));

        // -- CABECERA E ICONO DINÁMICO --
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (type == AlertType.SUCCESS) {
                    // Círculo Verde con Palomita
                    g2.setColor(new Color(34, 197, 94)); // Green 500
                    g2.fillOval(0, 0, 46, 46);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(14, 24, 20, 30); 
                    g2.drawLine(20, 30, 32, 16); 
                } else {
                    // Círculo Rojo con "X"
                    g2.setColor(new Color(239, 68, 68)); // Red 500
                    g2.fillOval(0, 0, 46, 46);
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawLine(16, 16, 30, 30); 
                    g2.drawLine(30, 16, 16, 30); 
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(46, 46);
            }
        };
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(15, 23, 42)); 
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        JLabel lblMessage = new JLabel("<html><div style='text-align: center; width: 220px;'>" + message + "</div></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMessage.setForeground(new Color(71, 85, 105)); 
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblIcon);
        headerPanel.add(lblTitle);
        headerPanel.add(lblMessage);

        contentPanel.add(headerPanel, BorderLayout.CENTER);

        // -- BOTÓN --
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Si es error, el botón también lo hacemos un poco más rojizo para que combine, si no, azul.
        Color btnColor = (type == AlertType.SUCCESS) ? new Color(59, 130, 246) : new Color(220, 38, 38);

        JButton btnOk = new JButton("Entendido") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(btnColor); 
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnOk.setContentAreaFilled(false);
        btnOk.setBorderPainted(false);
        btnOk.setFocusPainted(false);
        btnOk.setForeground(Color.WHITE);
        btnOk.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnOk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOk.setPreferredSize(new Dimension(130, 36));

        btnOk.addActionListener(e -> dispose());
        
        buttonPanel.add(btnOk);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel);
    }

    // Actualizamos el método show para que reciba el tipo de alerta
    public static void showMessage(Frame parent, String title, String message, AlertType type) {
        ModernAlert alert = new ModernAlert(parent, title, message, type);
        alert.setVisible(true);
    }
}