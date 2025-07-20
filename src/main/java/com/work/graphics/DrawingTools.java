
package com.work.graphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Stack;
/**
 *
 * @author ajuar
 */
public class DrawingTools {
  private Random random = new Random();
    
    /**
     * Dibuja un punto simple
     */
    public void drawPoint(Point point, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(point.x, point.y, 1, 1);
    }
    
    /**
     * Dibuja con lápiz (línea fina)
     */
    public void drawPencil(Point start, Point end, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    /**
     * Dibuja con pincel (línea gruesa)
     */
    public void drawBrush(Point start, Point end, Color color, int brushSize, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(brushSize * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    /**
     * Dibuja con aerógrafo (efecto spray)
     */
    public void drawAirbrush(Point point, Color color, int brushSize, Graphics2D g2d) {
        g2d.setColor(color);
        
        // Crear efecto de aerógrafo con puntos aleatorios
        int radius = brushSize * 3;
        
        for (int i = 0; i < 20; i++) {
            int offsetX = random.nextInt(radius * 2) - radius;
            int offsetY = random.nextInt(radius * 2) - radius;
            
            double distance = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
            if (distance <= radius) {
                g2d.fillOval(point.x + offsetX, point.y + offsetY, 1, 1);
            }
        }
    }
    
    /**
     * Borra (dibuja con blanco)
     */
    public void erase(Point start, Point end, int brushSize, Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(brushSize * 3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    /**
     * Dibuja una línea
     */
    public void drawLine(Point start, Point end, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }
    
    /**
     * Dibuja un rectángulo
     */
    public void drawRectangle(Point start, Point end, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);
        
        g2d.drawRect(x, y, width, height);
    }
    
    /**
     * Dibuja una elipse
     */
    public void drawEllipse(Point start, Point end, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);
        
        g2d.drawOval(x, y, width, height);
    }
    
    /**
     * Dibuja un rectángulo redondeado
     */
    public void drawRoundedRectangle(Point start, Point end, Color color, Graphics2D g2d) {
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(1));
        
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);
        
        g2d.drawRoundRect(x, y, width, height, 10, 10);
    }
    
    /**
     * Agrega texto en la posición especificada
     */
    public void addText(Point point, Color color, Graphics2D g2d, Component parent) {
        String text = JOptionPane.showInputDialog(parent, "Enter text:");
        System.out.println("Text: " + text);
        if (text != null && !text.trim().isEmpty()) {
            g2d.setColor(color);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(text, point.x, point.y);
        }
    }
    
    /**
     * Selecciona un color del canvas
     */
    public void pickColor(int x, int y, BufferedImage canvas, PaintState paintState, StatusBarPanel statusBarPanel) {
        if (canvas != null && x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight()) {
            int rgb = canvas.getRGB(x, y);
            Color pickedColor = new Color(rgb);
            paintState.setPrimaryColor(pickedColor);
            statusBarPanel.showColorInfo("Primary", paintState.getColorName(pickedColor));
        }
    }
    
    /**
     * Rellena un área con color (flood fill)
     */
    public void floodFill(int x, int y, Color fillColor, BufferedImage canvas) {
        if (canvas == null || x < 0 || x >= canvas.getWidth() || y < 0 || y >= canvas.getHeight()) {
            return;
        }
        
        int targetColor = canvas.getRGB(x, y);
        int replacementColor = fillColor.getRGB();
        
        if (targetColor == replacementColor) {
            return;
        }
        
        floodFillIterative(x, y, targetColor, replacementColor, canvas);
    }
    
    /**
     * Implementación iterativa del flood fill para evitar stack overflow
     */
    private void floodFillIterative(int startX, int startY, int targetColor, int replacementColor, BufferedImage canvas) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));
        
        while (!stack.isEmpty()) {
            Point point = stack.pop();
            int x = point.x;
            int y = point.y;
            
            if (x < 0 || x >= canvas.getWidth() || y < 0 || y >= canvas.getHeight()) {
                continue;
            }
            
            if (canvas.getRGB(x, y) != targetColor) {
                continue;
            }
            
            canvas.setRGB(x, y, replacementColor);
            
            stack.push(new Point(x + 1, y));
            stack.push(new Point(x - 1, y));
            stack.push(new Point(x, y + 1));
            stack.push(new Point(x, y - 1));
        }
    }
    
    /**
     * Maneja la herramienta de zoom
     */
    public void handleMagnifier(MouseEvent e, DrawingPanel drawingPanel, PaintState paintState, StatusBarPanel statusBarPanel) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            // Zoom in
            double newZoom = Math.min(paintState.getZoomFactor() * 2, 8.0);
            drawingPanel.setZoom(newZoom);
            paintState.setZoomFactor(newZoom);
            statusBarPanel.showZoomInfo((int)(newZoom * 100));
        } else if (SwingUtilities.isRightMouseButton(e)) {
            // Zoom out
            double newZoom = Math.max(paintState.getZoomFactor() / 2, 0.25);
            drawingPanel.setZoom(newZoom);
            paintState.setZoomFactor(newZoom);
            statusBarPanel.showZoomInfo((int)(newZoom * 100));
        }
    }
    
    /**
     * Inicia una selección
     */
    public void startSelection(Point point, PaintState paintState) {
        paintState.setSelecting(true);
        paintState.setSelection(new Rectangle(point.x, point.y, 0, 0));
    }
    
    /**
     * Actualiza la selección actual
     */
    public void updateSelection(Point point, PaintState paintState) {
        Rectangle selection = paintState.getSelection();
        if (selection != null) {
            int x = Math.min(selection.x, point.x);
            int y = Math.min(selection.y, point.y);
            int width = Math.abs(point.x - selection.x);
            int height = Math.abs(point.y - selection.y);
            paintState.setSelection(new Rectangle(x, y, width, height));
        }
    }
    
    /**
     * Finaliza la selección
     */
    public void endSelection(Point point, PaintState paintState, StatusBarPanel statusBarPanel) {
        paintState.setSelecting(false);
        Rectangle selection = paintState.getSelection();
        if (selection != null && selection.width > 0 && selection.height > 0) {
            statusBarPanel.showSelectionInfo(selection.width, selection.height);
        }
    }  
}
