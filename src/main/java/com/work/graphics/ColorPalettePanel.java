package com.work.graphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author ajuar
 */
public class ColorPalettePanel extends JPanel{
   private PaintState paintState;
    private StatusBarPanel statusBarPanel;
    private JButton[] colorButtons;
    
    public ColorPalettePanel(PaintState paintState, StatusBarPanel statusBarPanel) {
        this.paintState = paintState;
        this.statusBarPanel = statusBarPanel;
        
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        createColorButtons();
    }
    
    private void createColorButtons() {
        // Colores estándar de MS Paint en orden exacto
        Color[] colors = {
            // Primera fila
            Color.BLACK, new Color(128, 128, 128), new Color(128, 0, 0), new Color(128, 128, 0),
            new Color(0, 128, 0), new Color(0, 128, 128), new Color(0, 0, 128), new Color(128, 0, 128),
            new Color(128, 128, 64), new Color(0, 64, 64), new Color(0, 128, 255), new Color(0, 64, 128),
            new Color(64, 0, 255), new Color(128, 64, 0),
            // Segunda fila
            Color.WHITE, new Color(192, 192, 192), Color.RED, Color.YELLOW,
            Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA,
            new Color(255, 255, 128), new Color(0, 255, 128), new Color(128, 255, 255), new Color(128, 128, 255),
            new Color(255, 0, 255), new Color(255, 128, 64)
        };
        
        colorButtons = new JButton[colors.length];
        
        for (int i = 0; i < colors.length; i++) {
            final Color color = colors[i];
            colorButtons[i] = createColorButton(color);
        }
    }
    
    /**
     * Crea un botón de color individual con el color especificado
     */
    private JButton createColorButton(Color color) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                // Dibujar el fondo del color
                g.setColor(color);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibujar borde
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                // Dibujar borde interior más claro para efecto 3D
                g.setColor(Color.WHITE);
                g.drawLine(1, 1, getWidth() - 2, 1); // Top
                g.drawLine(1, 1, 1, getHeight() - 2); // Left
                
                // Dibujar borde inferior más oscuro para efecto 3D
                g.setColor(Color.GRAY);
                g.drawLine(1, getHeight() - 2, getWidth() - 2, getHeight() - 2); // Bottom
                g.drawLine(getWidth() - 2, 1, getWidth() - 2, getHeight() - 2); // Right
            }
        };
        
        // Configurar propiedades del botón
        button.setPreferredSize(new Dimension(16, 16));
        button.setMinimumSize(new Dimension(16, 16));
        button.setMaximumSize(new Dimension(16, 16));
        button.setBorder(null); // Sin borde por defecto, lo dibujamos nosotros
        button.setOpaque(true);
        button.setContentAreaFilled(false); // No rellenar automáticamente
        button.setFocusPainted(false);
        button.setToolTipText(getColorName(color));
        
        // Agregar listener para selección de colores
        button.addMouseListener(new ColorButtonMouseListener(color));
        
        return button;
    }
    
    /**
     * Obtiene el nombre descriptivo de un color
     */
    private String getColorName(Color color) {
        if (color.equals(Color.BLACK)) return "Black";
        if (color.equals(Color.WHITE)) return "White";
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.GREEN)) return "Green";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.YELLOW)) return "Yellow";
        if (color.equals(Color.CYAN)) return "Cyan";
        if (color.equals(Color.MAGENTA)) return "Magenta";
        if (color.equals(new Color(128, 128, 128))) return "Gray";
        if (color.equals(new Color(192, 192, 192))) return "Light Gray";
        if (color.equals(new Color(128, 0, 0))) return "Dark Red";
        if (color.equals(new Color(128, 128, 0))) return "Dark Yellow";
        if (color.equals(new Color(0, 128, 0))) return "Dark Green";
        if (color.equals(new Color(0, 128, 128))) return "Dark Cyan";
        if (color.equals(new Color(0, 0, 128))) return "Dark Blue";
        if (color.equals(new Color(128, 0, 128))) return "Dark Magenta";
        return "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }
    
    private void layoutComponents() {
        setLayout(new GridLayout(2, 14, 1, 1));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        setPreferredSize(new Dimension(0, 50));
        setBackground(SystemColor.control);
        
        for (JButton colorButton : colorButtons) {
            add(colorButton);
        }
    }
    
    /**
     * Actualiza la visualización cuando se selecciona un nuevo color
     */
    public void updateSelectedColors() {
        // Forzar repaint de todos los botones para actualizar bordes si es necesario
        for (JButton button : colorButtons) {
            button.repaint();
        }
    }
    
    private class ColorButtonMouseListener extends MouseAdapter {
        private final Color color;
        
        public ColorButtonMouseListener(Color color) {
            this.color = color;
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                paintState.setPrimaryColor(color);
                statusBarPanel.showColorInfo("Primary", getColorName(color));
            } else if (SwingUtilities.isRightMouseButton(e)) {
                paintState.setSecondaryColor(color);
                statusBarPanel.showColorInfo("Secondary", getColorName(color));
            }
            
            // Notificar al toolbox para actualizar los paneles de color
            updateToolboxColors();
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            // Mostrar información del color al pasar el mouse
            statusBarPanel.showTemporaryMessage("Color: " + getColorName(color), 2000);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            // Restaurar mensaje por defecto
            if (statusBarPanel.getText().startsWith("Color: ")) {
                statusBarPanel.showDefaultMessage();
            }
        }
        
        /**
         * Actualiza los paneles de color en el toolbox
         */
        private void updateToolboxColors() {
            Container parent = getParent();
            while (parent != null && !(parent instanceof MSPaintApp)) {
                parent = parent.getParent();
            }
            if (parent instanceof MSPaintApp) {
                // Buscar el ToolboxPanel y actualizar colores
                Component[] components = ((MSPaintApp) parent).getContentPane().getComponents();
                for (Component comp : components) {
                    if (comp instanceof ToolboxPanel) {
                        ((ToolboxPanel) comp).updateColorPanels();
                        break;
                    }
                }
            }
        }
    }
}
