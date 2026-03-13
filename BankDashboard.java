import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BankDashboard extends JFrame implements BankAccount.TransactionListener {

    private StatCard cardBalance;
    private StatCard cardSuccess;
    private StatCard cardFail;
    private DefaultTableModel tableModel;

    private ModernButton btnStart;
    private ModernButton btnReset;

    private ModernTextField txtInitialBalance;
    private ModernSpinner spinAtms;
    private ModernSpinner spinIterations;
    private ModernTable tableTransactions;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private ModernTextField txtSearch;

    private int successCount = 0;
    private int failCount = 0;
    private AtomicInteger activeThreads;

    public BankDashboard() {
        setTitle("Dashboard - Sistema de Retiro Bancario");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 246, 250));

        // --- LÍNEA NUEVA ---
        setGlassPane(new ModernGlassPane());

        initComponents();
    }

    private void initComponents() {
        // --- PANEL LATERAL (Configuración y Estadísticas) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título Lateral
        JLabel lblTitle = new JLabel("Panel de Control");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(15, 23, 42)); // Slate 900
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de Configuración
        JPanel configPanel = new JPanel(new GridLayout(6, 1, 5, 8)); // 8px de espacio vertical entre elementos
        configPanel.setBackground(Color.WHITE);
        configPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)), "Configuración", TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(100, 116, 139))); // Título del borde estilizado

        JLabel lblSaldo = new JLabel("Saldo Inicial ($):");
        lblSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSaldo.setForeground(new Color(71, 85, 105));
        configPanel.add(lblSaldo);

        txtInitialBalance = new ModernTextField("2000");
        configPanel.add(txtInitialBalance);

        JLabel lblCajeros = new JLabel("Número de Cajeros (Hilos):");
        lblCajeros.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCajeros.setForeground(new Color(71, 85, 105));
        configPanel.add(lblCajeros);

        spinAtms = new ModernSpinner(4, 1, 10, 1);
        configPanel.add(spinAtms);

        JLabel lblRetiros = new JLabel("Retiros por Cajero:");
        lblRetiros.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRetiros.setForeground(new Color(71, 85, 105));
        configPanel.add(lblRetiros);

        spinIterations = new ModernSpinner(5, 1, 20, 1);
        configPanel.add(spinIterations);

        leftPanel.add(configPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- BOTONES MODERNIZADOS ---
        
        // Botón principal (Azul)
        btnStart = new ModernButton("INICIAR SIMULACIÓN", 
                new Color(59, 130, 246), // Blue 500 (Normal)
                new Color(37, 99, 235),  // Blue 600 (Hover)
                new Color(29, 78, 216)   // Blue 700 (Presionado)
        );
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.setMaximumSize(new Dimension(250, 45)); // Tamaño fijo

        // Botón secundario (Gris)
        btnReset = new ModernButton("LIMPIAR DATOS", 
                new Color(148, 163, 184), // Slate 400 (Normal)
                new Color(100, 116, 139), // Slate 500 (Hover)
                new Color(71, 85, 105)    // Slate 600 (Presionado)
        );
        btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset.setMaximumSize(new Dimension(250, 45));

        // Agregar al panel (el código que ya tenías)
        leftPanel.add(btnStart);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(btnReset);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- ESTADÍSTICAS EN TIEMPO REAL (TARJETAS) ---
        // Usamos un GridLayout para apilar las 3 tarjetas con un buen espaciado (12px)
        JPanel statsContainer = new JPanel(new GridLayout(3, 1, 0, 12));
        statsContainer.setOpaque(false);

        // Instanciamos las tarjetas con sus respectivos colores de acento
        cardBalance = new StatCard("Saldo Actual", "$2000", new Color(59, 130, 246)); // Azul
        cardSuccess = new StatCard("Retiros Exitosos", "0", new Color(34, 197, 94)); // Verde
        cardFail = new StatCard("Retiros Rechazados", "0", new Color(239, 68, 68)); // Rojo

        // Cambiamos el color del texto del saldo para que resalte más
        cardBalance.setValueColor(new Color(59, 130, 246));

        statsContainer.add(cardBalance);
        statsContainer.add(cardSuccess);
        statsContainer.add(cardFail);

        leftPanel.add(statsContainer);

        // --- PANEL CENTRAL (Tabla de Transacciones) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15)); // 15px de separación vertical
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Un poco más de margen
        centerPanel.setBackground(new Color(248, 250, 252));

        String[] columns = { "Hora", "Cajero", "Estatus", "Monto", "Saldo Restante" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTransactions = new ModernTable(tableModel);

        // 1. INICIALIZAR EL ORDENADOR/FILTRADOR (Sorter)
        rowSorter = new TableRowSorter<>(tableModel);

        // Crear una regla de ordenamiento (Comparator) para las columnas de dinero
        java.util.Comparator<String> currencyComparator = (s1, s2) -> {
            try {
                // Quitar el "$" y espacios para dejar solo el número puro
                int val1 = Integer.parseInt(s1.replaceAll("[^0-9-]", ""));
                int val2 = Integer.parseInt(s2.replaceAll("[^0-9-]", ""));
                return Integer.compare(val1, val2);
            } catch (NumberFormatException e) {
                return 0;
            }
        };

        // Asignar esta regla numérica a la columna 3 (Monto) y 4 (Saldo Restante)
        rowSorter.setComparator(3, currencyComparator);
        rowSorter.setComparator(4, currencyComparator);

        tableTransactions.setRowSorter(rowSorter); // Se lo asignamos a la tabla

        // 2. CREAR EL PANEL DE BÚSQUEDA (Arriba a la derecha)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Buscar:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(100, 116, 139)); // Slate 500

        txtSearch = new ModernTextField("");
        txtSearch.setPreferredSize(new Dimension(250, 35)); // Un poco más chaparrito que los inputs laterales

        // 3. LÓGICA DE FILTRADO EN TIEMPO REAL
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            private void filter() {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null); // Si está vacío, muestra todo
                } else {
                    // (?i) hace que la búsqueda ignore mayúsculas y minúsculas
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        // 4. ENSAMBLAR LA TABLA (Con su contenedor redondeado)
        JScrollPane scrollPane = new JScrollPane(tableTransactions);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(searchPanel, BorderLayout.NORTH); // El buscador arriba
        centerPanel.add(tableContainer, BorderLayout.CENTER); // La tabla en el centro

        // --- ENSAMBLAR LA VENTANA PRINCIPAL ---

        // 1. Envolvemos el panel izquierdo en un ScrollPane
        JScrollPane leftScroll = new JScrollPane(leftPanel);
        leftScroll.setPreferredSize(new Dimension(300, 0)); // Establecemos el ancho fijo en el scroll en lugar del
                                                            // panel
        leftScroll.setBorder(null); // Sin bordes feos
        leftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Solo scroll vertical
        leftScroll.getVerticalScrollBar().setUnitIncrement(16); // Hace que la rueda del mouse baje más rápido y suave

        // --- LÍNEAS NUEVAS ---
        leftScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        leftScroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0)); // Aún más delgadita para el sidebar

        // 2. Agregamos el scroll al Oeste en lugar del panel directamente
        add(leftScroll, BorderLayout.WEST);

        // El panel central de la tabla se queda igual
        add(centerPanel, BorderLayout.CENTER);

        // Eventos de botones
        btnStart.addActionListener(e -> iniciarSimulacion());
        btnReset.addActionListener(e -> limpiarInterfaz());
    }

    private void iniciarSimulacion() {
        int initialBalance;
        try {
            initialBalance = Integer.parseInt(txtInitialBalance.getText());
        } catch (NumberFormatException ex) {
            // --- NUEVA ALERTA DE ERROR ---
            ModernAlert.showMessage(this, "Error de validación", "Por favor ingrese un saldo inicial válido.", ModernAlert.AlertType.ERROR);
            return;
        }

        int numAtms = spinAtms.getValue();
        int iterations = spinIterations.getValue();

        limpiarInterfaz();
        cardBalance.setValue("$" + initialBalance);
        
        // --- ACTIVAR LA ANIMACIÓN Y BLOQUEAR LA UI ---
        getGlassPane().setVisible(true);

        // Bloquear UI durante la simulación
        btnStart.setEnabled(false);
        txtInitialBalance.setEnabled(false);
        spinAtms.setEnabled(false);
        spinIterations.setEnabled(false);

        BankAccount account = new BankAccount(initialBalance, this);
        activeThreads = new AtomicInteger(numAtms);

        // Crear e iniciar hilos
        for (int i = 1; i <= numAtms; i++) {
            String atmName = "Cajero " + i;
            ATMThread thread = new ATMThread(account, atmName, iterations, () -> {
                // Se ejecuta cuando un hilo termina
                // ... código anterior ...
                if (activeThreads.decrementAndGet() == 0) {
                    SwingUtilities.invokeLater(() -> {
                        
                        // --- APAGAR LA ANIMACIÓN ---
                        getGlassPane().setVisible(false);

                        btnStart.setEnabled(true);
                        // Se vuelven a habilitar los inputs
                        txtInitialBalance.setEnabled(true);
                        spinAtms.setEnabled(true);
                        spinIterations.setEnabled(true);

                        // --- NUEVA ALERTA DE ÉXITO ---
                        ModernAlert.showMessage(BankDashboard.this, "¡Simulación Exitosa!", "Todos los cajeros han terminado sus operaciones.", ModernAlert.AlertType.SUCCESS);
                    });
                }
            });
            thread.start();
        }
    }

    private void limpiarInterfaz() {
        tableModel.setRowCount(0);
        successCount = 0;
        failCount = 0;
        cardSuccess.setValue("0");
        cardFail.setValue("0");
        cardBalance.setValue("$" + txtInitialBalance.getText());

        // Limpiamos el buscador
        txtSearch.setText("");
    }

    // Implementación del Callback (Se llama desde el Semaphore)
    @Override
    public void onTransactionProcessed(TransactionRecord record, boolean isSuccess) {
        SwingUtilities.invokeLater(() -> {
            // Actualizar tabla
            tableModel.insertRow(0, record.toRow());

            // Actualizar contadores
            if (isSuccess)
                successCount++;
            else
                failCount++;

            // Actualizar valores en las tarjetas
            cardSuccess.setValue(String.valueOf(successCount));
            cardFail.setValue(String.valueOf(failCount));
            cardBalance.setValue(record.toRow()[4].toString()); // Actualiza la tarjeta de saldo
        });
    }

    public static void main(String[] args) {
        // Look and Feel del sistema para que se vea más moderno en Windows/Linux/Mac
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new BankDashboard().setVisible(true);
        });
    }
}
