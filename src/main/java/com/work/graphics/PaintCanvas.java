
package com.work.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author ajuar
 */
public class PaintCanvas extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage imagen;
    private Graphics2D g2d;
    private int tool = 0;
    private Color color = Color.BLACK;
    private int thickness = 3;
    private double zoom = 1.0;
    private String texto = "";
    private Point2D start, end, selStart, selEnd;
    private Rectangle selectedArea = null;
    private BufferedImage clipboardImg = null, selection = null;
    private boolean dragging = false;
    private int offsetX, offsetY;

    public PaintCanvas() {
        setPreferredSize(new Dimension(1200, 900));
        setBackground(Color.WHITE);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        limpiar();
    }

    // --- Métodos principales de la herramienta ---
    public void setTool(int t) {
        tool = t;
    }

    public void setCurrentColor(Color c) {
        color = c;
    }

    public void setThickness(int t) {
        thickness = t;
    }

    public void setTexto(String s) {
        texto = s;
    }

    public void limpiar() {
        imagen = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_ARGB);
        g2d = imagen.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
        repaint();
    }

    public BufferedImage getImagen() {
        BufferedImage img = new BufferedImage(imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(imagen, 0, 0, null);
        g.dispose();
        return img;
    }

    public void setImagen(BufferedImage img) {
        imagen = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        g2d = imagen.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        repaint();
    }

    public void zoom(double factor) {
        zoom *= factor;
        revalidate();
        repaint();
    }

    public void zoomReset() {
        zoom = 1.0;
        revalidate();
        repaint();
    }

    public void rotar(int grados) {
        double rads = Math.toRadians(grados);
        int w = imagen.getWidth(), h = imagen.getHeight();
        int nw = (grados % 180 == 0) ? w : h;
        int nh = (grados % 180 == 0) ? h : w;
        BufferedImage rot = new BufferedImage(nw, nh, imagen.getType());
        Graphics2D g = rot.createGraphics();
        g.rotate(rads, nw / 2.0, nh / 2.0);
        switch (grados) {
            case 90:
                g.drawImage(imagen, 0, -w, null);
                break;
            case 180:
                g.drawImage(imagen, -w, -h, null);
                break;
            case 270:
                g.drawImage(imagen, -h, 0, null);
                break;
            default:
                g.drawImage(imagen, 0, 0, null);
                break;
        }
        g.dispose();
        imagen = rot;
        g2d = imagen.createGraphics();
        revalidate();
        repaint();
    }

    // --- Portapapeles ---
    public void copiar() {
        if (selectedArea != null) {
            selection = imagen.getSubimage(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
            clipboardImg = new BufferedImage(selection.getWidth(), selection.getHeight(), BufferedImage.TYPE_INT_ARGB);
            clipboardImg.getGraphics().drawImage(selection, 0, 0, null);
            // Copiar al sistema
            TransferableImage trans = new TransferableImage(clipboardImg);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
        }
    }

    public void pegar() {
        try {
            Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                clipboardImg = (BufferedImage) t.getTransferData(DataFlavor.imageFlavor);
                g2d.drawImage(clipboardImg, 10, 10, null);
                repaint();
            }
        } catch (Exception e) {
        }
    }

    // --- Herramientas ---
    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            start = screenToImage(e.getPoint());
            if (tool == 6) { // Selección/Mover
                selStart = start;
                selEnd = null;
                selectedArea = null;
            }
            if (tool == 5 && !texto.isEmpty()) { // Texto
                g2d.setColor(color);
                g2d.setFont(new Font("Arial", Font.BOLD, thickness * 4));
                g2d.drawString(texto, (int) start.getX(), (int) start.getY());
                repaint();
            }
            dragging = true;
        }

        public void mouseReleased(MouseEvent e) {
            if (!dragging) {
                return;
            }
            end = screenToImage(e.getPoint());
            switch (tool) {
                case 0: // Lápiz
                case 4: // Goma
                    break; // Ya se dibujó en mouseDragged
                case 1: // Línea
                    g2d.setColor(color);
                    g2d.setStroke(new BasicStroke(thickness));
                    g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
                    break;
                case 2: // Rectángulo
                    g2d.setColor(color);
                    g2d.setStroke(new BasicStroke(thickness));
                    g2d.drawRect(Math.min((int) start.getX(), (int) end.getX()), Math.min((int) start.getY(), (int) end.getY()),
                            Math.abs((int) start.getX() - (int) end.getX()), Math.abs((int) start.getY() - (int) end.getY()));
                    break;
                case 3: // Elipse
                    g2d.setColor(color);
                    g2d.setStroke(new BasicStroke(thickness));
                    g2d.drawOval(Math.min((int) start.getX(), (int) end.getX()), Math.min((int) start.getY(), (int) end.getY()),
                            Math.abs((int) start.getX() - (int) end.getX()), Math.abs((int) start.getY() - (int) end.getY()));
                    break;
                case 6: // Seleccionar/Mover
                    selEnd = end;
                    int x = (int) Math.min(selStart.getX(), selEnd.getX());
                    int y = (int) Math.min(selStart.getY(), selEnd.getY());
                    int w = (int) Math.abs(selStart.getX() - selEnd.getX());
                    int h = (int) Math.abs(selStart.getY() - selEnd.getY());
                    if (w > 0 && h > 0) {
                        selectedArea = new Rectangle(x, y, w, h);
                        selection = imagen.getSubimage(x, y, w, h);
                    }
                    break;
            }
            dragging = false;
            repaint();
        }

        public void mouseDragged(MouseEvent e) {
            if (!dragging) {
                return;
            }
            end = screenToImage(e.getPoint());
            switch (tool) {
                case 0: // Lápiz
                    g2d.setColor(color);
                    g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
                    start = end;
                    break;
                case 4: // Goma
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(thickness * 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
                    start = end;
                    break;
                case 6: // Selección/Mover
                    if (selectedArea != null && selection != null) {
                        int dx = (int) end.getX() - selectedArea.x;
                        int dy = (int) end.getY() - selectedArea.y;
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(selectedArea.x, selectedArea.y, selectedArea.width, selectedArea.height);
                        g2d.drawImage(selection, dx, dy, null);
                        selectedArea.setLocation(dx, dy);
                        repaint();
                    }
                    break;
            }
            repaint();
        }
    };

    // --- Zoom y transformación ---
    private Point2D screenToImage(Point p) {
        return new Point2D.Double(p.x / zoom, p.y / zoom);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(zoom, zoom);
        g2.drawImage(imagen, 0, 0, null);
        if (tool == 6 && selectedArea != null) {
            g2.setColor(new Color(0, 0, 255, 64));
            g2.draw(selectedArea);
        }
    }

    // --- TransferableImage para portapapeles ---
    private static class TransferableImage implements Transferable {

        private final Image img;

        public TransferableImage(Image i) {
            img = i;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        public Object getTransferData(DataFlavor flavor) {
            return img;
        }
    }
}
