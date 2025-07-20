
package com.work.graphics;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ajuar
 */
public class ToolIconRenderer {

    public Icon createToolIcon(int toolIndex) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1.0f));

                switch (toolIndex) {
                    case 0: // Free-form select
                        drawFreeSelectIcon(g2d, x, y);
                        break;
                    case 1: // Select
                        drawSelectIcon(g2d, x, y);
                        break;
                    case 2: // Eraser
                        drawEraserIcon(g2d, x, y);
                        break;
                    case 3: // Fill/Bucket
                        drawFillIcon(g2d, x, y);
                        break;
                    case 4: // Pick color/Eyedropper
                        drawPickColorIcon(g2d, x, y);
                        break;
                    case 5: // Magnify
                        drawMagnifyIcon(g2d, x, y);
                        break;
                    case 6: // Pencil
                        drawPencilIcon(g2d, x, y);
                        break;
                    case 7: // Brush
                        drawBrushIcon(g2d, x, y);
                        break;
                    case 8: // Airbrush
                        drawAirbrushIcon(g2d, x, y);
                        break;
                    case 9: // Text
                        drawTextIcon(g2d, x, y);
                        break;
                    case 10: // Line
                        drawLineIcon(g2d, x, y);
                        break;
                    case 11: // Curve
                        drawCurveIcon(g2d, x, y);
                        break;
                    case 12: // Rectangle
                        drawRectangleIcon(g2d, x, y);
                        break;
                    case 13: // Polygon
                        drawPolygonIcon(g2d, x, y);
                        break;
                    case 14: // Ellipse
                        drawEllipseIcon(g2d, x, y);
                        break;
                    case 15: // Rounded Rectangle
                        drawRoundedRectangleIcon(g2d, x, y);
                        break;
                }
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    private void drawFreeSelectIcon(Graphics2D g2d, int x, int y) {
        g2d.drawLine(x + 2, y + 2, x + 8, y + 2);
        g2d.drawLine(x + 8, y + 2, x + 8, y + 8);
        g2d.drawLine(x + 8, y + 8, x + 2, y + 8);
        g2d.drawLine(x + 2, y + 8, x + 2, y + 2);
        // Líneas punteadas para selección
        for (int i = 0; i < 6; i += 2) {
            g2d.drawLine(x + 3 + i, y + 3, x + 3 + i, y + 3);
            g2d.drawLine(x + 3 + i, y + 7, x + 3 + i, y + 7);
        }
    }

    private void drawSelectIcon(Graphics2D g2d, int x, int y) {
        g2d.drawRect(x + 3, y + 3, 10, 8);
        // Cuadraditos de selección
        g2d.fillRect(x + 2, y + 2, 2, 2);
        g2d.fillRect(x + 7, y + 2, 2, 2);
        g2d.fillRect(x + 12, y + 2, 2, 2);
        g2d.fillRect(x + 2, y + 12, 2, 2);
        g2d.fillRect(x + 12, y + 12, 2, 2);
    }

    private void drawEraserIcon(Graphics2D g2d, int x, int y) {
        g2d.fillRect(x + 4, y + 3, 8, 6);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 5, y + 4, 6, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 4, y + 3, 8, 6);
    }

    private void drawFillIcon(Graphics2D g2d, int x, int y) {
        // Balde
        g2d.drawLine(x + 6, y + 4, x + 10, y + 4);
        g2d.drawLine(x + 5, y + 5, x + 11, y + 5);
        g2d.drawLine(x + 4, y + 6, x + 12, y + 6);
        g2d.drawLine(x + 4, y + 7, x + 12, y + 7);
        g2d.drawLine(x + 4, y + 8, x + 12, y + 8);
        g2d.drawLine(x + 5, y + 9, x + 11, y + 9);
        g2d.drawLine(x + 6, y + 10, x + 10, y + 10);
        // Mango
        g2d.drawLine(x + 11, y + 3, x + 13, y + 1);
        // Gota
        g2d.fillOval(x + 9, y + 11, 3, 3);
    }

    private void drawPickColorIcon(Graphics2D g2d, int x, int y) {
        g2d.drawLine(x + 3, y + 12, x + 12, y + 3);
        g2d.fillOval(x + 2, y + 11, 3, 3);
        g2d.drawRect(x + 11, y + 2, 3, 3);
    }

    private void drawMagnifyIcon(Graphics2D g2d, int x, int y) {
        g2d.drawOval(x + 2, y + 2, 8, 8);
        g2d.drawLine(x + 9, y + 9, x + 13, y + 13);
        g2d.drawLine(x + 5, y + 6, x + 7, y + 6);
        g2d.drawLine(x + 6, y + 5, x + 6, y + 7);
    }

    private void drawPencilIcon(Graphics2D g2d, int x, int y) {
        g2d.drawLine(x + 2, y + 13, x + 13, y + 2);
        g2d.drawLine(x + 2, y + 12, x + 12, y + 2);
        g2d.fillPolygon(new int[]{x + 1, x + 4, x + 2}, new int[]{y + 14, y + 11, y + 13}, 3);
    }

    private void drawBrushIcon(Graphics2D g2d, int x, int y) {
        g2d.fillOval(x + 6, y + 2, 4, 8);
        g2d.fillRect(x + 7, y + 10, 2, 4);
    }

    private void drawAirbrushIcon(Graphics2D g2d, int x, int y) {
        g2d.drawOval(x + 4, y + 2, 6, 4);
        g2d.drawLine(x + 7, y + 6, x + 7, y + 12);
        g2d.drawLine(x + 5, y + 8, x + 9, y + 8);
        // Puntos de spray
        for (int i = 0; i < 5; i++) {
            g2d.fillOval(x + 2 + i * 2, y + 10 + i, 1, 1);
        }
    }

    private void drawTextIcon(Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.drawString("A", x + 5, y + 12);
    }

    private void drawLineIcon(Graphics2D g2d, int x, int y) {
        g2d.drawLine(x + 2, y + 12, x + 13, y + 3);
    }

    private void drawCurveIcon(Graphics2D g2d, int x, int y) {
        g2d.drawArc(x + 2, y + 4, 12, 8, 0, 180);
    }

    private void drawRectangleIcon(Graphics2D g2d, int x, int y) {
        g2d.drawRect(x + 3, y + 4, 9, 6);
    }

    private void drawPolygonIcon(Graphics2D g2d, int x, int y) {
        int[] xPoints = {x + 7, x + 12, x + 9, x + 5, x + 2};
        int[] yPoints = {y + 2, y + 6, y + 12, y + 12, y + 6};
        g2d.drawPolygon(xPoints, yPoints, 5);
    }

    private void drawEllipseIcon(Graphics2D g2d, int x, int y) {
        g2d.drawOval(x + 3, y + 4, 9, 6);
    }

    private void drawRoundedRectangleIcon(Graphics2D g2d, int x, int y) {
        g2d.drawRoundRect(x + 3, y + 4, 9, 6, 3, 3);
    }
}
