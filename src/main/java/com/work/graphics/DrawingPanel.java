package com.work.graphics;

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author ajuar
 */
public class DrawingPanel extends JPanel{
    private PaintState paintState;
    private StatusBarPanel statusBarPanel;
    private DrawingTools drawingTools;
    
    private BufferedImage canvas;
    private Graphics2D g2d;
    private Point startPoint, endPoint, lastPoint;
    private boolean drawing = false;
    private List<BufferedImage> undoStack = new ArrayList<>();
    private final int MAX_UNDO = 20;
    private double currentZoom = 1.0;
    
    public DrawingPanel(PaintState paintState, StatusBarPanel statusBarPanel) {
        this.paintState = paintState;
        this.statusBarPanel = statusBarPanel;
        this.drawingTools = new DrawingTools();
        
        initializeComponents();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(640, 480));
    }
    
    private void setupEventHandlers() {
        addMouseListener(new DrawingMouseListener());
        addMouseMotionListener(new DrawingMouseMotionListener());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canvas == null) {
            canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            g2d = canvas.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 640, 480);
        }
        
        // Dibujar la imagen escalada
        Graphics2D g2d_screen = (Graphics2D) g.create();
        g2d_screen.scale(currentZoom, currentZoom);
        g2d_screen.drawImage(canvas, 0, 0, null);
        g2d_screen.dispose();
        
        // Dibujar selección si existe
        if (paintState.getSelection() != null && paintState.isSelecting()) {
            Graphics2D g2d_sel = (Graphics2D) g.create();
            g2d_sel.setColor(Color.BLACK);
            g2d_sel.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 
                10, new float[]{2, 2}, 0));
            Rectangle selection = paintState.getSelection();
            Rectangle scaledSelection = new Rectangle(
                (int)(selection.x * currentZoom),
                (int)(selection.y * currentZoom),
                (int)(selection.width * currentZoom),
                (int)(selection.height * currentZoom)
            );
            g2d_sel.drawRect(scaledSelection.x, scaledSelection.y, scaledSelection.width, scaledSelection.height);
            g2d_sel.dispose();
        }
    }
    
    private Point getScaledPoint(Point screenPoint) {
        return new Point(
            (int)(screenPoint.x / currentZoom),
            (int)(screenPoint.y / currentZoom)
        );
    }
    
    public void setZoom(double zoom) {
        currentZoom = zoom;
        int newWidth = (int)(640 * zoom);
        int newHeight = (int)(480 * zoom);
        setPreferredSize(new Dimension(newWidth, newHeight));
        revalidate();
        repaint();
    }
    
    private void saveToUndoStack() {
        if (canvas != null) {
            BufferedImage copy = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2dCopy = copy.createGraphics();
            g2dCopy.drawImage(canvas, 0, 0, null);
            g2dCopy.dispose();
            
            undoStack.add(copy);
            if (undoStack.size() > MAX_UNDO) {
                undoStack.remove(0);
            }
        }
    }
    
    public void undo() {
        if (!undoStack.isEmpty()) {
            canvas = undoStack.remove(undoStack.size() - 1);
            g2d = canvas.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
            statusBarPanel.showOperationCompleted("Undo");
        }
    }
    
    public void clearCanvas() {
        saveToUndoStack();
        if (canvas != null) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 640, 480);
            repaint();
            statusBarPanel.showOperationCompleted("Canvas cleared");
        }
    }
    
    public BufferedImage getImage() {
        return canvas;
    }
    
    public void setImage(BufferedImage image) {
        saveToUndoStack();
        canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 640, 480);
        
        // Centrar la imagen si es más pequeña que el canvas
        int x = (640 - image.getWidth()) / 2;
        int y = (480 - image.getHeight()) / 2;
        x = Math.max(0, x);
        y = Math.max(0, y);
        
        g2d.drawImage(image, x, y, null);
        repaint();
    }
    
    private class DrawingMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Point scaledPoint = getScaledPoint(e.getPoint());
            
            switch (paintState.getCurrentTool()) {
                case PaintState.FREE_SELECT:
                case PaintState.SELECT:
                    drawingTools.startSelection(scaledPoint, paintState);
                    break;
                case PaintState.MAGNIFIER:
                    drawingTools.handleMagnifier(e, DrawingPanel.this, paintState, statusBarPanel);
                    return;
                default:
                    saveToUndoStack();
                    startPoint = scaledPoint;
                    lastPoint = scaledPoint;
                    drawing = true;
                    paintState.setCurrentColor(SwingUtilities.isLeftMouseButton(e) ? 
                        paintState.getPrimaryColor() : paintState.getSecondaryColor());
                    
                    switch (paintState.getCurrentTool()) {
                        case PaintState.FILL:
                            drawingTools.floodFill(scaledPoint.x, scaledPoint.y, 
                                paintState.getCurrentColor(), canvas);
                            repaint();
                            break;
                        case PaintState.PICK_COLOR:
                            drawingTools.pickColor(scaledPoint.x, scaledPoint.y, canvas, paintState, statusBarPanel);
                            break;
                        case PaintState.PENCIL:
                        case PaintState.BRUSH:
                        case PaintState.AIRBRUSH:
                            drawingTools.drawPoint(scaledPoint, paintState.getCurrentColor(), g2d);
                            break;
                        case PaintState.TEXT:
                            drawingTools.addText(scaledPoint, paintState.getCurrentColor(), g2d, DrawingPanel.this);
                            break;
                    }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (paintState.isSelecting()) {
                drawingTools.endSelection(getScaledPoint(e.getPoint()), paintState, statusBarPanel);
                repaint();
                return;
            }
            
            if (drawing) {
                Point scaledPoint = getScaledPoint(e.getPoint());
                endPoint = scaledPoint;
                
                switch (paintState.getCurrentTool()) {
                    case PaintState.LINE:
                        drawingTools.drawLine(startPoint, endPoint, paintState.getCurrentColor(), g2d);
                        break;
                    case PaintState.RECTANGLE:
                        drawingTools.drawRectangle(startPoint, endPoint, paintState.getCurrentColor(), g2d);
                        break;
                    case PaintState.ELLIPSE:
                        drawingTools.drawEllipse(startPoint, endPoint, paintState.getCurrentColor(), g2d);
                        break;
                    case PaintState.ROUNDED_RECTANGLE:
                        drawingTools.drawRoundedRectangle(startPoint, endPoint, paintState.getCurrentColor(), g2d);
                        break;
                }
                
                drawing = false;
                repaint();
            }
        }
    }
    
    private class DrawingMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            Point scaledPoint = getScaledPoint(e.getPoint());
            
            if (paintState.isSelecting()) {
                drawingTools.updateSelection(scaledPoint, paintState);
                repaint();
                return;
            }
            
            if (drawing) {
                paintState.setCurrentColor(SwingUtilities.isLeftMouseButton(e) ? 
                    paintState.getPrimaryColor() : paintState.getSecondaryColor());
                
                switch (paintState.getCurrentTool()) {
                    case PaintState.PENCIL:
                        drawingTools.drawPencil(lastPoint, scaledPoint, paintState.getCurrentColor(), g2d);
                        lastPoint = scaledPoint;
                        break;
                    case PaintState.BRUSH:
                        drawingTools.drawBrush(lastPoint, scaledPoint, paintState.getCurrentColor(), 
                            paintState.getBrushSize(), g2d);
                        lastPoint = scaledPoint;
                        break;
                    case PaintState.AIRBRUSH:
                        drawingTools.drawAirbrush(scaledPoint, paintState.getCurrentColor(), 
                            paintState.getBrushSize(), g2d);
                        break;
                    case PaintState.ERASER:
                        drawingTools.erase(lastPoint, scaledPoint, paintState.getBrushSize(), g2d);
                        lastPoint = scaledPoint;
                        break;
                }
                
                repaint();
            }
        }
    } 
}
