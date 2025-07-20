
package com.work.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author ajuar
 */
public class ToolboxPanel extends JPanel{
    private PaintState paintState;
    private StatusBarPanel statusBarPanel;
    private DrawingPanel drawingPanel;
    private JButton[] toolButtons;
    private JPanel primaryColorPanel, secondaryColorPanel;
    private ToolIconRenderer iconRenderer;
    
    public ToolboxPanel(PaintState paintState, StatusBarPanel statusBarPanel, DrawingPanel drawingPanel) {
        this.paintState = paintState;
        this.statusBarPanel = statusBarPanel;
        this.drawingPanel = drawingPanel;
        this.iconRenderer = new ToolIconRenderer();
        
        initializeComponents();
        layoutComponents();
    }
    
    private void initializeComponents() {
        createToolButtons();
        createColorPanels();
    }
    
    private void createToolButtons() {
        toolButtons = new JButton[16];
        
        for (int i = 0; i < 16; i++) {
            final int toolIndex = i;
            toolButtons[i] = new JButton();
            toolButtons[i].setPreferredSize(new Dimension(24, 24));
            toolButtons[i].setToolTipText(paintState.getToolName(i));
            toolButtons[i].setIcon(iconRenderer.createToolIcon(i));
            toolButtons[i].setMargin(new Insets(1, 1, 1, 1));
            toolButtons[i].setFocusPainted(false);
            
            toolButtons[i].addActionListener(new ToolButtonListener(toolIndex));
        }
        
        updateToolSelection();
    }
    
    private void createColorPanels() {
        primaryColorPanel = new JPanel();
        primaryColorPanel.setBackground(paintState.getPrimaryColor());
        primaryColorPanel.setPreferredSize(new Dimension(20, 20));
        primaryColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        secondaryColorPanel = new JPanel();
        secondaryColorPanel.setBackground(paintState.getSecondaryColor());
        secondaryColorPanel.setPreferredSize(new Dimension(20, 20));
        secondaryColorPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(54, 0));
        setBorder(BorderFactory.createRaisedBevelBorder());
        
        // Panel de herramientas (2 columnas)
        JPanel toolPanel = new JPanel(new GridLayout(8, 2, 1, 1));
        toolPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        for (JButton tool : toolButtons) {
            toolPanel.add(tool);
        }
        
        // Panel de colores primario/secundario
        JPanel colorSelectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        // Panel que contiene los colores superpuestos
        JPanel colorContainer = new JPanel();
        colorContainer.setLayout(null); // Layout absoluto para superponer
        colorContainer.setPreferredSize(new Dimension(35, 35));
        
        // Posicionar color secundario (atrás, abajo-derecha)
        secondaryColorPanel.setBounds(12, 12, 20, 20);
        colorContainer.add(secondaryColorPanel);
        
        // Posicionar color primario (adelante, arriba-izquierda)
        primaryColorPanel.setBounds(3, 3, 20, 20);
        colorContainer.add(primaryColorPanel);
        
        colorSelectorPanel.add(colorContainer);
        
        add(toolPanel, BorderLayout.NORTH);
        add(colorSelectorPanel, BorderLayout.CENTER);
    }
    
    public void updateToolSelection() {
        for (int i = 0; i < toolButtons.length; i++) {
            toolButtons[i].setBorder(i == paintState.getCurrentTool() ? 
                BorderFactory.createLoweredBevelBorder() : 
                BorderFactory.createRaisedBevelBorder());
        }
    }
    
    public void updateColorPanels() {
        primaryColorPanel.setBackground(paintState.getPrimaryColor());
        secondaryColorPanel.setBackground(paintState.getSecondaryColor());
        repaint();
    }
    
    private class ToolButtonListener implements ActionListener {
        private final int toolIndex;
        
        public ToolButtonListener(int toolIndex) {
            this.toolIndex = toolIndex;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            paintState.setCurrentTool(toolIndex);
            updateToolSelection();
            statusBarPanel.setText("Tool: " + paintState.getToolName(toolIndex));
            
            // Actualizar cursor
            drawingPanel.setCursor(paintState.getToolCursor());
        }
    }
}
