package com.work.graphics;

import java.awt.*;

/**
 * Clase que mantiene el estado global de la aplicación Paint
 */
public class PaintState {

    // Herramientas disponibles
    public static final int FREE_SELECT = 0;
    public static final int SELECT = 1;
    public static final int ERASER = 2;
    public static final int FILL = 3;
    public static final int PICK_COLOR = 4;
    public static final int MAGNIFIER = 5;
    public static final int PENCIL = 6;
    public static final int BRUSH = 7;
    public static final int AIRBRUSH = 8;
    public static final int TEXT = 9;
    public static final int LINE = 10;
    public static final int CURVE = 11;
    public static final int RECTANGLE = 12;
    public static final int POLYGON = 13;
    public static final int ELLIPSE = 14;
    public static final int ROUNDED_RECTANGLE = 15;

    // Estado actual
    private int currentTool = PENCIL;
    private Color primaryColor = Color.BLACK;
    private Color secondaryColor = Color.WHITE;
    private Color currentColor = Color.BLACK;
    private int brushSize = 2;
    private double zoomFactor = 1.0;

    // Selección
    private Rectangle selection = null;
    private boolean isSelecting = false;

    // Getters y Setters
    public int getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(int currentTool) {
        this.currentTool = currentTool;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
        this.currentColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public int getBrushSize() {
        return brushSize;
    }

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public Rectangle getSelection() {
        return selection;
    }

    public void setSelection(Rectangle selection) {
        this.selection = selection;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    public void setSelecting(boolean isSelecting) {
        this.isSelecting = isSelecting;
    }

    // Métodos de utilidad
    public String getToolName(int tool) {
        String[] toolNames = {
            "Free-form Select", "Select", "Eraser/Color Eraser", "Fill With Color",
            "Pick Color", "Magnifier", "Pencil", "Brush", "Airbrush", "Text",
            "Line", "Curve", "Rectangle", "Polygon", "Ellipse", "Rounded Rectangle"
        };

        if (tool >= 0 && tool < toolNames.length) {
            return toolNames[tool];
        }
        return "Unknown Tool";
    }

    public String getColorName(Color color) {
        if (color.equals(Color.BLACK)) {
            return "Black";
        }
        if (color.equals(Color.WHITE)) {
            return "White";
        }
        if (color.equals(Color.RED)) {
            return "Red";
        }
        if (color.equals(Color.GREEN)) {
            return "Green";
        }
        if (color.equals(Color.BLUE)) {
            return "Blue";
        }
        if (color.equals(Color.YELLOW)) {
            return "Yellow";
        }
        if (color.equals(Color.CYAN)) {
            return "Cyan";
        }
        if (color.equals(Color.MAGENTA)) {
            return "Magenta";
        }
        return "Custom Color";
    }

    public Cursor getToolCursor() {
        switch (currentTool) {
            case FREE_SELECT:
            case SELECT:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            case ERASER:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            case FILL:
                return Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            case PICK_COLOR:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            case MAGNIFIER:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            case PENCIL:
            case BRUSH:
            case AIRBRUSH:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
            case TEXT:
                return Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            default:
                return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        }
    }
}
