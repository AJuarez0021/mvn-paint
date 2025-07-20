
package com.work.graphics;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ajuar
 */
public class StatusBarPanel extends JLabel {

    // Mensaje por defecto
    private static final String DEFAULT_MESSAGE = "For Help, click Help Topics on the Help Menu.";

    /**
     * Constructor que inicializa la barra de estado con el mensaje por defecto
     */
    public StatusBarPanel() {
        super(DEFAULT_MESSAGE);
        initializeComponents();
    }

    /**
     * Inicializa los componentes visuales de la barra de estado
     */
    private void initializeComponents() {
        // Configurar apariencia
        setBorder(BorderFactory.createLoweredBevelBorder());
        setFont(new Font("SansSerif", Font.PLAIN, 11));
        setOpaque(true);
        setBackground(SystemColor.control);

        // Configurar alineación del texto
        setHorizontalAlignment(SwingConstants.LEFT);

        // Añadir padding interno
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
    }

    /**
     * Actualiza el texto de la barra de estado
     *
     * @param text El nuevo texto a mostrar
     */
    @Override
    public void setText(String text) {
        if (text == null || text.trim().isEmpty()) {
            super.setText(DEFAULT_MESSAGE);
        } else {
            super.setText(text);
        }
        // Forzar repaint para actualización inmediata
        repaint();
    }

    /**
     * Muestra información de zoom
     *
     * @param zoomPercentage El porcentaje de zoom actual
     */
    public void showZoomInfo(int zoomPercentage) {
        setText("Zoom: " + zoomPercentage + "%");
    }

    /**
     * Muestra información de selección
     *
     * @param width Ancho de la selección en pixels
     * @param height Alto de la selección en pixels
     */
    public void showSelectionInfo(int width, int height) {
        setText("Selection: " + width + " x " + height + " pixels");
    }

    /**
     * Muestra información de herramienta activa
     *
     * @param toolName Nombre de la herramienta actual
     */
    public void showToolInfo(String toolName) {
        setText("Tool: " + toolName + " - Ready");
    }

    /**
     * Muestra información de color seleccionado
     *
     * @param colorType Tipo de color (Primary/Secondary)
     * @param colorName Nombre del color
     */
    public void showColorInfo(String colorType, String colorName) {
        setText(colorType + " color selected: " + colorName);
    }

    /**
     * Muestra información de archivo
     *
     * @param action Acción realizada (Opened/Saved/Created)
     * @param filename Nombre del archivo
     */
    public void showFileInfo(String action, String filename) {
        setText(action + ": " + filename);
    }

    /**
     * Muestra mensaje de operación completada
     *
     * @param operation Operación realizada (Undo/Clear/etc.)
     */
    public void showOperationCompleted(String operation) {
        setText(operation + " completed successfully");
    }

    /**
     * Muestra información de coordenadas del mouse
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public void showCoordinates(int x, int y) {
        setText("Position: " + x + ", " + y);
    }

    /**
     * Muestra información de tamaño de pincel
     *
     * @param size Tamaño del pincel
     */
    public void showBrushSize(int size) {
        setText("Brush size: " + size + " pixels");
    }

    /**
     * Muestra mensaje de error
     *
     * @param errorMessage Mensaje de error a mostrar
     */
    public void showError(String errorMessage) {
        setText("Error: " + errorMessage);
        // Cambiar color de fondo temporalmente para indicar error
        Color originalBg = getBackground();
        setBackground(new Color(255, 200, 200)); // Fondo rojizo claro

        // Restaurar color original después de 3 segundos
        Timer timer = new Timer(3000, e -> {
            setBackground(originalBg);
            showDefaultMessage();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Muestra mensaje de éxito
     *
     * @param successMessage Mensaje de éxito
     */
    public void showSuccess(String successMessage) {
        setText("Success: " + successMessage);
        // Cambiar color de fondo temporalmente para indicar éxito
        Color originalBg = getBackground();
        setBackground(new Color(200, 255, 200)); // Fondo verdoso claro

        // Restaurar color original después de 2 segundos
        Timer timer = new Timer(2000, e -> {
            setBackground(originalBg);
            showDefaultMessage();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Muestra información de progreso para operaciones largas
     *
     * @param operation Nombre de la operación
     * @param progress Progreso de 0 a 100
     */
    public void showProgress(String operation, int progress) {
        setText(operation + "... " + progress + "%");
    }

    /**
     * Muestra información detallada de la imagen
     *
     * @param width Ancho de la imagen
     * @param height Alto de la imagen
     * @param colorDepth Profundidad de color
     */
    public void showImageInfo(int width, int height, String colorDepth) {
        setText("Image: " + width + " x " + height + " pixels, " + colorDepth);
    }

    /**
     * Muestra mensaje temporal que se auto-elimina
     *
     * @param message Mensaje a mostrar
     * @param durationMs Duración en milisegundos
     */
    public void showTemporaryMessage(String message, int durationMs) {
        setText(message);

        Timer timer = new Timer(durationMs, e -> showDefaultMessage());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Restaura el mensaje por defecto
     */
    public void showDefaultMessage() {
        setText(DEFAULT_MESSAGE);
    }

    /**
     * Limpia la barra de estado (muestra mensaje vacío)
     */
    public void clear() {
        setText(" ");
    }

    /**
     * Verifica si está mostrando el mensaje por defecto
     *
     * @return true si muestra el mensaje por defecto
     */
    public boolean isShowingDefault() {
        return getText().equals(DEFAULT_MESSAGE);
    }

    /**
     * Obtiene el mensaje por defecto
     *
     * @return El mensaje por defecto de la barra de estado
     */
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }
}
