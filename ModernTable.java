import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class ModernTable extends JTable {

    public ModernTable(TableModel model) {
        super(model);

        // Configuraciones estructurales "React-like"
        setRowHeight(50); // Cambia el 45 por un 50
        setShowVerticalLines(false); // Fuera líneas verticales
        setShowHorizontalLines(true);
        setGridColor(new Color(241, 245, 249)); // Líneas horizontales muy sutiles
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
        setBackground(Color.WHITE);
        setBorder(null);

        // Quitar el borde de enfoque (la línea punteada que sale al hacer clic)
        setFocusable(false);
        setRowSelectionAllowed(true);

        // Esto bloquea el arrastre de columnas
        getTableHeader().setReorderingAllowed(false); 

        // Aplicar cabecera personalizada
        getTableHeader().setDefaultRenderer(new ModernTableHeaderRenderer());
        getTableHeader().setPreferredSize(new Dimension(0, 45));
        getTableHeader().setBorder(null);

        // Aplicar renderizadores a las columnas
        ModernCellRenderer generalRenderer = new ModernCellRenderer();
        StatusBadgeRenderer badgeRenderer = new StatusBadgeRenderer();

        // Asumimos que la tabla tiene 5 columnas: "Hora", "Cajero", "Estatus", "Monto",
        // "Saldo Restante"
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            if (i == 2) { // Índice 2 es la columna "Estatus"
                getColumnModel().getColumn(i).setCellRenderer(badgeRenderer);
            } else {
                getColumnModel().getColumn(i).setCellRenderer(generalRenderer);
            }
        }
    }
}