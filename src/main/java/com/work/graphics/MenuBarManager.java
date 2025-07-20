package com.work.graphics;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author ajuar
 */
public class MenuBarManager {

    private final MSPaintApp mainWindow;
    private final DrawingPanel drawingPanel;
    private final PaintState paintState;
    private final StatusBarPanel statusBarPanel;

    public MenuBarManager(MSPaintApp mainWindow, DrawingPanel drawingPanel,
            PaintState paintState, StatusBarPanel statusBarPanel) {
        this.mainWindow = mainWindow;
        this.drawingPanel = drawingPanel;
        this.paintState = paintState;
        this.statusBarPanel = statusBarPanel;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createImageMenu());
        menuBar.add(createColorsMenu());
        menuBar.add(createHelpMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem newItem = new JMenuItem("New");
        newItem.setMnemonic('N');
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newItem.addActionListener(e -> {
            drawingPanel.clearCanvas();
            mainWindow.setTitle("untitled - Paint");
        });

        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setMnemonic('O');
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic('S');
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveFile());

        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> saveFile());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic('U');
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(e -> drawingPanel.undo());

        JMenuItem clearItem = new JMenuItem("Clear Image");
        clearItem.addActionListener(e -> drawingPanel.clearCanvas());

        editMenu.add(undoItem);
        editMenu.addSeparator();
        editMenu.add(clearItem);

        return editMenu;
    }

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic('V');

        JMenuItem toolBoxItem = new JMenuItem("Tool Box");
        JMenuItem colorBoxItem = new JMenuItem("Color Box");
        JMenuItem statusBarItem = new JMenuItem("Status Bar");

        JMenu zoomMenu = new JMenu("Zoom");
        JMenuItem normalSizeItem = new JMenuItem("Normal Size");
        normalSizeItem.addActionListener(e -> setZoom(1.0));
        JMenuItem largeSize = new JMenuItem("Large Size");
        largeSize.addActionListener(e -> setZoom(4.0));
        JMenuItem customZoom = new JMenuItem("Custom...");
        customZoom.addActionListener(e -> showCustomZoomDialog());

        zoomMenu.add(normalSizeItem);
        zoomMenu.add(largeSize);
        zoomMenu.addSeparator();
        zoomMenu.add(customZoom);

        viewMenu.add(toolBoxItem);
        viewMenu.add(colorBoxItem);
        viewMenu.add(statusBarItem);
        viewMenu.addSeparator();
        viewMenu.add(zoomMenu);

        return viewMenu;
    }

    private JMenu createImageMenu() {
        JMenu imageMenu = new JMenu("Image");
        imageMenu.setMnemonic('I');

        JMenuItem flipRotateItem = new JMenuItem("Flip/Rotate...");
        JMenuItem stretchSkewItem = new JMenuItem("Stretch/Skew...");
        JMenuItem clearImageItem = new JMenuItem("Clear Image");
        clearImageItem.addActionListener(e -> drawingPanel.clearCanvas());

        imageMenu.add(flipRotateItem);
        imageMenu.add(stretchSkewItem);
        imageMenu.addSeparator();
        imageMenu.add(clearImageItem);

        return imageMenu;
    }

    private JMenu createColorsMenu() {
        JMenu colorsMenu = new JMenu("Colors");
        colorsMenu.setMnemonic('C');

        JMenuItem editColorsItem = new JMenuItem("Edit Colors...");
        colorsMenu.add(editColorsItem);

        return colorsMenu;
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem helpTopicsItem = new JMenuItem("Help Topics");
        helpTopicsItem.addActionListener(e -> showHelpDialog());

        JMenuItem aboutPaintItem = new JMenuItem("About Paint");
        aboutPaintItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(helpTopicsItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutPaintItem);

        return helpMenu;
    }

    private void setZoom(double factor) {
        paintState.setZoomFactor(factor);
        drawingPanel.setZoom(factor);
        statusBarPanel.showZoomInfo((int) (factor * 100));
    }

    private void showCustomZoomDialog() {
        String input = JOptionPane.showInputDialog(mainWindow,
                "Enter zoom percentage (25-800):",
                "Custom Zoom",
                JOptionPane.QUESTION_MESSAGE);

        if (input != null) {
            try {
                int percentage = Integer.parseInt(input);
                if (percentage >= 25 && percentage <= 800) {
                    setZoom(percentage / 100.0);
                } else {
                    JOptionPane.showMessageDialog(mainWindow,
                            "Zoom must be between 25% and 800%",
                            "Invalid Zoom",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainWindow,
                        "Please enter a valid number",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files (*.bmp, *.jpg, *.gif, *.png)", "bmp", "jpg", "jpeg", "gif", "png"));

        if (fileChooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                drawingPanel.setImage(image);
                mainWindow.setTitle(file.getName() + " - Paint");
                statusBarPanel.showFileInfo("Opened", file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainWindow, "Cannot open file:\n" + e.getMessage(),
                        "Paint", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("24-bit Bitmap (*.bmp)", "bmp"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG (*.png)", "png"));
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG (*.jpg)", "jpg"));

        if (fileChooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String extension = "png";

                if (fileChooser.getFileFilter().getDescription().contains("bmp")) {
                    extension = "bmp";
                } else if (fileChooser.getFileFilter().getDescription().contains("jpg")) {
                    extension = "jpg";
                }

                if (!file.getName().toLowerCase().endsWith("." + extension)) {
                    file = new File(file.getAbsolutePath() + "." + extension);
                }

                BufferedImage image = drawingPanel.getImage();
                ImageIO.write(image, extension.toUpperCase(), file);
                mainWindow.setTitle(file.getName() + " - Paint");
                statusBarPanel.showFileInfo("Saved", file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainWindow, "Cannot save file:\n" + e.getMessage(),
                        "Paint", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showHelpDialog() {
        JOptionPane.showMessageDialog(mainWindow, """
                                                  MS Paint
                                                  
                                                  Tools:
                                                  \u2022 Use left mouse button for primary color
                                                  \u2022 Use right mouse button for secondary color
                                                  \u2022 Select tools from the toolbox on the left
                                                  \u2022 Choose colors from the palette below
                                                  
                                                  Keyboard shortcuts:
                                                  \u2022 Ctrl+N: New
                                                  \u2022 Ctrl+O: Open
                                                  \u2022 Ctrl+S: Save
                                                  \u2022 Ctrl+Z: Undo
                                                  
                                                  Magnifier:
                                                  \u2022 Left click: Zoom in
                                                  \u2022 Right click: Zoom out""",
                "Paint Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(mainWindow,
                "MS Paint \nVersion 1.0\n\nCreated with Java\nby A. Juarez",
                "About Paint", JOptionPane.INFORMATION_MESSAGE);
    }
}
