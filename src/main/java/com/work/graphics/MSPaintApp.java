package com.work.graphics;

import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;
import java.awt.*;

public class MSPaintApp extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private DrawingPanel drawingPanel;
    private ToolboxPanel toolboxPanel;
    private ColorPalettePanel colorPalettePanel;
    private MenuBarManager menuBarManager;
    private StatusBarPanel statusBarPanel;

    // Estado de la aplicación
    private PaintState paintState;
    
    public MSPaintApp() {
        initializeLookAndFeel();
        initializeState();
        initializeComponents();
        layoutComponents();
        setupWindow();
    }
    
    private void initializeLookAndFeel() {
        try {
            FlatDarculaLaf.setup();
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | IllegalAccessException 
                    | InstantiationException | UnsupportedLookAndFeelException ex) {
                System.err.println("Error:  " + ex.getMessage());
            }
        }
    }
    
    private void initializeState() {
        paintState = new PaintState();
    }
    
    private void initializeComponents() {
        // Panel de estado
        statusBarPanel = new StatusBarPanel();

        // Panel de dibujo
        drawingPanel = new DrawingPanel(paintState, statusBarPanel);

        // Panel de herramientas
        toolboxPanel = new ToolboxPanel(paintState, statusBarPanel, drawingPanel);

        // Panel de colores
        colorPalettePanel = new ColorPalettePanel(paintState, statusBarPanel);

        // Barra de menús
        menuBarManager = new MenuBarManager(this, drawingPanel, paintState, statusBarPanel);
        setJMenuBar(menuBarManager.createMenuBar());
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        add(toolboxPanel, BorderLayout.WEST);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(colorPalettePanel, BorderLayout.SOUTH);
        add(statusBarPanel, BorderLayout.NORTH);
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        centerPanel.setBackground(Color.LIGHT_GRAY);
        
        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBackground(Color.LIGHT_GRAY);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private void setupWindow() {
        setTitle("untitled - Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Métodos públicos para acceso desde MenuBarManager
    public DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }
    
    public PaintState getPaintState() {
        return paintState;
    }
    
    public StatusBarPanel getStatusBarPanel() {
        return statusBarPanel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MSPaintApp::new);
    }
}
