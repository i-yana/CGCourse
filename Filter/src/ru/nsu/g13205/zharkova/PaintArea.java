package ru.nsu.g13205.zharkova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PaintArea extends JPanel{
    private static final int MAX_SIZE = 350;
    private static final int ALIGNMENT = 1;
    private static final int SPACE = 20;
    private static final int Y_OFFSET = 20;
    private static final int X_OFFSET = 20;
    private int RECTANGLE_SIZE;
    private int pixelSize;
    private Point rectangleCords;
    private double compressionFactorX;
    private double compressionFactorY;
    private BufferedImage reduceClip;
    private BufferedImage original;
    private BufferedImage reduceOriginal;
    private BufferedImage clip;
    private BufferedImage transformed;
    private BufferedImage pixelImage;
    private MouseHandler mouseHandler;
    private boolean isPixelSize = false;
    private boolean isSelect = false;

    public PaintArea() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(3 * MAX_SIZE + 4 * ALIGNMENT + 3 * SPACE, Y_OFFSET + MAX_SIZE + 2 * ALIGNMENT));
        this.mouseHandler = new MouseHandler();
        this.pixelSize = 10;
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSquare(X_OFFSET, Y_OFFSET, g);
        drawSquare(X_OFFSET + MAX_SIZE + ALIGNMENT + SPACE, Y_OFFSET, g);
        drawSquare(X_OFFSET + 2 * MAX_SIZE + 2 * ALIGNMENT + 2 * SPACE, Y_OFFSET, g);
        g.drawImage(reduceOriginal, X_OFFSET + ALIGNMENT, Y_OFFSET + ALIGNMENT, null);
        if (isPixelSize && clip != null) {
            BufferedImage im = getPixelSizeImage(clip, pixelSize);
            g.drawImage(im, X_OFFSET + MAX_SIZE + ALIGNMENT + SPACE + ALIGNMENT, Y_OFFSET + ALIGNMENT, null);
        } else {
            g.drawImage(clip, X_OFFSET + MAX_SIZE + ALIGNMENT + SPACE + ALIGNMENT, Y_OFFSET + ALIGNMENT, null);
        }
        if (isPixelSize && pixelImage != null) {
            BufferedImage im = zoomPixelImage(pixelImage, pixelSize);
            g.drawImage(im, X_OFFSET + 2 * MAX_SIZE + 2 * ALIGNMENT + 2 * SPACE + ALIGNMENT, Y_OFFSET + ALIGNMENT, null);
        } else {

            g.drawImage(transformed, X_OFFSET+2 * MAX_SIZE + 2 * ALIGNMENT + 2 * SPACE + ALIGNMENT, Y_OFFSET + ALIGNMENT, null);
        }
        if (isSelect) {
            if (rectangleCords != null) {
                Graphics2D g2D = (Graphics2D) g;
                float[] dashPattern = {5.0f, 5.0f};
                g2D.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0));
                g.setColor(Color.white);
                int w = RECTANGLE_SIZE;
                int h = RECTANGLE_SIZE;
                if(w > reduceOriginal.getWidth()) w = reduceOriginal.getWidth();
                if(h > reduceOriginal.getHeight()) h = reduceOriginal.getHeight();
                g.drawRect(rectangleCords.x + 1, rectangleCords.y + 1, w - 1, h - 1);
            }
        }
    }

    private BufferedImage zoomPixelImage(BufferedImage temp, int size){
        int w = temp.getWidth()*size;
        int h = temp.getHeight()*size;
        if(w>=MAX_SIZE) w = MAX_SIZE;
        if(h>=MAX_SIZE) h = MAX_SIZE;
        BufferedImage outImage = new BufferedImage(w, h, temp.getType());
        for (int y = 0, yy = 0; y < temp.getHeight(); y++, yy += size) {
            for (int x = 0, xx = 0; x < temp.getWidth(); x++, xx += size) {
                int rgb = temp.getRGB(x, y);
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if(xx+j>outImage.getWidth()-1 || yy+i>outImage.getHeight()-1){
                            continue;
                        }
                        outImage.setRGB(xx + j, yy + i, rgb);
                    }
                }
            }
        }
        return outImage;
    }

    private BufferedImage getPixelSizeImage(BufferedImage clip, int size) {
        BufferedImage temp;

        if(Math.round(clip.getWidth()/size) < size || Math.round(clip.getHeight()/size) < size){
            temp = clip;
        }
        else {
            temp = ImageTransformer.resize(clip, Math.round(clip.getWidth() / size), Math.round(clip.getHeight() / size), ImageTransformer.SUPER_SAMPLING);
        }
        int w = temp.getWidth()*size;
        int h = temp.getHeight()*size;
        if(w>=MAX_SIZE) w = MAX_SIZE;
        if(h>=MAX_SIZE) h = MAX_SIZE;
        BufferedImage outImage = new BufferedImage(w, h, clip.getType());
        for (int y = 0, yy = 0; y < temp.getHeight(); y++, yy += size) {
                for (int x = 0, xx = 0; x < temp.getWidth(); x++, xx += size) {
                    int rgb = temp.getRGB(x, y);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if(xx+j>outImage.getWidth()-1 || yy+i>outImage.getHeight()-1){
                                continue;
                            }
                            outImage.setRGB(xx + j, yy + i, rgb);
                        }
                    }
                }
            }
            return outImage;
    }

    private void drawSquare(int x, int y, Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(x, y, MAX_SIZE + ALIGNMENT, MAX_SIZE + ALIGNMENT);

    }

    public void pasteImage(int bitMap[][]) {
        clip = null;
        transformed = null;
        pixelImage = null;
        reduceClip = null;
        BufferedImage tempImage = new BufferedImage(bitMap[0].length, bitMap.length, BufferedImage.TYPE_INT_RGB);
        Graphics g = tempImage.getGraphics();
        for (int i = 0; i < bitMap.length; i++) {
            for (int j = 0; j < bitMap[i].length; j++) {
                g.setColor(new Color(bitMap[i][j]));
                g.drawLine(j, i, j, i);
            }
        }
        original = tempImage;
        double sizeRatio = (double) tempImage.getHeight() / (double) tempImage.getWidth();
        if (tempImage.getHeight() > MAX_SIZE || tempImage.getWidth() > MAX_SIZE) {
            int scaledWidth = 0;
            int scaledHeight = 0;
            if (tempImage.getWidth() < tempImage.getHeight()) {
                if (tempImage.getWidth() > MAX_SIZE) {
                    scaledWidth = MAX_SIZE;
                    scaledHeight = (int) (sizeRatio * scaledWidth);
                }
                if (tempImage.getHeight() > MAX_SIZE) {
                    scaledHeight = MAX_SIZE;
                    scaledWidth = (int) (scaledHeight / sizeRatio);
                }
            } else {
                if (tempImage.getHeight() > MAX_SIZE) {
                    scaledHeight = MAX_SIZE;
                    scaledWidth = (int) (scaledHeight / sizeRatio);
                }
                if (tempImage.getWidth() > MAX_SIZE) {
                    scaledWidth = MAX_SIZE;
                    scaledHeight = (int) (sizeRatio * scaledWidth);
                }
            }
            compressionFactorX = (double) tempImage.getWidth() / scaledWidth;
            compressionFactorY = (double) tempImage.getHeight() / scaledHeight;
            if (scaledWidth > scaledHeight) {
                RECTANGLE_SIZE = (int) Math.round(scaledWidth / compressionFactorX);
            } else {
                RECTANGLE_SIZE = (int) Math.round(scaledHeight / compressionFactorY);
            }
            reduceOriginal = ImageTransformer.resize(tempImage, scaledWidth, scaledHeight, ImageTransformer.SUPER_SAMPLING);
        } else {
            reduceOriginal = tempImage;
            RECTANGLE_SIZE = MAX_SIZE;
        }
        rectangleCords = new Point(X_OFFSET + ALIGNMENT, Y_OFFSET + ALIGNMENT);
        repaint();

    }


    public void grayScale() {
        try {
            transformed = Filter.grayScale(clip);
            pixelImage = Filter.grayScale(reduceClip);
            repaint();
        } catch (NullPointerException ignored) {
        }
    }

    public void negativeScale() {
        try {
            transformed = Filter.negativeScale(clip);
            pixelImage = Filter.negativeScale(reduceClip);
            repaint();
        } catch (NullPointerException ignored) {
        }
    }

    public void zoomIn() {
        transformed = getCentralPart(ImageTransformer.resize(clip, clip.getWidth() * 2, clip.getHeight() * 2, ImageTransformer.BILINEAR));
        pixelImage = createZoomInImage(transformed);
        repaint();
    }

    private BufferedImage getCentralPart(BufferedImage image) {
        if (image.getWidth() <= MAX_SIZE && image.getHeight() <= MAX_SIZE) {
            return image;
        }
        int w = image.getWidth();
        int h = image.getHeight();
        int beginY = 0;
        int beginX = 0;
        if (h > MAX_SIZE) {
            beginY = h / 2 - MAX_SIZE / 2;
        }
        if (w > MAX_SIZE) {
            beginX = w / 2 - MAX_SIZE / 2;
        }
        int endY = beginY + MAX_SIZE;
        int endX = beginX + MAX_SIZE;
        if (h < MAX_SIZE) {
            endY = h;
        }
        if (w < MAX_SIZE) {
            endX = w;
        }
        int newWidth = endX - beginX;
        int newHeight = endY - beginY;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());
        for (int y = beginY, yy = 0; y < endY; y++, yy++) {
            for (int x = beginX, xx = 0; x < endX; x++, xx++) {
                newImage.setRGB(xx, yy, image.getRGB(x, y));
            }
        }
        return newImage;
    }

    private void clipImage() {
        if (original.getHeight() <= MAX_SIZE && original.getWidth() <= MAX_SIZE) {
            clip = original;
            reduceClip = createZoomInImage(clip);
            return;
        }
        int h = MAX_SIZE;
        int w = MAX_SIZE;
        if(original.getWidth() < MAX_SIZE) w = original.getWidth();
        if(original.getHeight() < MAX_SIZE) h = original.getHeight();
        BufferedImage tempImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int startX = (int) Math.round((rectangleCords.x - X_OFFSET) * compressionFactorX);
        int startY = (int) Math.round((rectangleCords.y - Y_OFFSET) * compressionFactorY);
        Graphics g = tempImage.getGraphics();
        for (int i = startY, y = 0; i < startY + 350; i++, y++) {
            for (int j = startX, x = 0; j < startX + 350; j++, x++) {
                if(i >= original.getHeight() || i<0 || j >= original.getWidth() || j<0)
                    continue;
                g.setColor(new Color(original.getRGB(j, i)));
                g.drawLine(x, y, x, y);
            }
        }
        clip = tempImage;
        reduceClip = createZoomInImage(clip);
    }

    public void dithering(int redPalette, int greenPalette, int bluePalette) {
        transformed = Dithering.floydSteinbergDithering(clip, redPalette, greenPalette, bluePalette);
        pixelImage = Dithering.floydSteinbergDithering(reduceClip, redPalette, greenPalette, bluePalette);
        repaint();
    }

    public void orderedDithering(int redPalette, int greenPalette, int bluePalette) {
        transformed = Dithering.orderedDithering(clip, redPalette, greenPalette, bluePalette);
        pixelImage = Dithering.orderedDithering(reduceClip, redPalette, greenPalette, bluePalette);
        repaint();
    }

    public void replace() {
        clip = transformed;
        reduceClip = pixelImage;
        repaint();
    }

    public void blurFilter() {
        transformed = Filter.blur(clip);
        pixelImage = Filter.blur(reduceClip);
        repaint();
    }

    public void sharpnessFilter() {
        transformed = Filter.sharpnessFilter(clip);
        pixelImage = Filter.sharpnessFilter(reduceClip);
        repaint();
    }

    public void embossFilter() {
        transformed = Filter.embossFilter(clip);
        pixelImage = Filter.embossFilter(reduceClip);
        repaint();
    }

    public void robertsFilter(int threshold) {
        transformed = Filter.robertsFilter(clip, threshold);
        pixelImage = Filter.robertsFilter(reduceClip, threshold);
        repaint();
    }

    public void sobelFilter(int threshold) {
        transformed = Filter.sobelFilter(clip, threshold);
        pixelImage = Filter.sobelFilter(reduceClip, threshold);
        repaint();
    }

    public void aquaFilter() {
        transformed = Filter.aquaFilter(clip);
        pixelImage = Filter.aquaFilter(reduceClip);
        repaint();
    }

    public void selectOutline(int threshold) {
        transformed = Filter.selectOutline(clip, threshold);
        pixelImage = Filter.selectOutline(reduceClip, threshold);
        repaint();
    }

    public BufferedImage getTransformImage() {
        return transformed;
    }

    public void turn(int angle) {
        pixelImage = Rotation.turn(reduceClip, angle);
        transformed = Rotation.turn(clip, angle);
        repaint();
    }

    public void gammaCorrection(double gamma) {
        transformed = Filter.gammaCorrection(clip, gamma);
        pixelImage =  Filter.gammaCorrection(reduceClip, gamma);
        repaint();
    }

    private BufferedImage createZoomInImage(BufferedImage clip){
        BufferedImage temp;
        if(Math.round(clip.getWidth()/pixelSize) < pixelSize || Math.round(clip.getHeight()/pixelSize) < pixelSize){
            temp = clip;
        }
        else {
            temp = ImageTransformer.resize(clip, Math.round(clip.getWidth() / pixelSize), Math.round(clip.getHeight() / pixelSize), ImageTransformer.SUPER_SAMPLING);
        }
        return  temp;
    }

    public void replaceBC() {
        transformed = clip;
        pixelImage = reduceClip;
        repaint();
    }

    public boolean isEmptyClip() {
        return clip == null;
    }

    public void setOldImage(BufferedImage oldImage, BufferedImage pixelizeImage) {
        transformed = oldImage;
        pixelImage = pixelizeImage;
        repaint();
    }

    public void setSelect(boolean select) {
        isSelect = select;
        if (!select) {
            repaint();
        }
    }

    public void setIsPixelSize(boolean select) {
        isPixelSize = select;
        repaint();
    }

    public void setPixelSize(int size) {
        pixelSize = size;
        if(reduceClip!=null){
            reduceClip = createZoomInImage(clip);
        }
        //transformed = null;
        if(pixelImage!=null) {
            pixelImage = createZoomInImage(transformed);
        }
        repaint();
    }

    public int getPixelSize() {
        return pixelSize;
    }

    public boolean isPixelSize() {
        return isPixelSize;
    }

    public BufferedImage getPixelImage() {
        return pixelImage;
    }

    class MouseHandler extends MouseAdapter {
        private boolean onReduceOriginalImage = false;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isSelect) {
                return;
            }
            Point point = e.getPoint();
            try {
                reduceOriginal.getRGB(point.x - X_OFFSET, point.y - Y_OFFSET);
                onReduceOriginalImage = true;
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
                onReduceOriginalImage = false;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                if (!isSelect) {
                    return;
                }
                Point clickedPoint = e.getPoint();
                if (original.getWidth() <= MAX_SIZE && original.getHeight() <= MAX_SIZE) {
                    rectangleCords = new Point(X_OFFSET, Y_OFFSET);
                    clipImage();
                    repaint();
                    return;
                }
                reduceOriginal.getRGB(clickedPoint.x - X_OFFSET, clickedPoint.y - Y_OFFSET);
                int rSize = (int) Math.round((double) RECTANGLE_SIZE / 2);
                int centerX = clickedPoint.x;
                int centerY = clickedPoint.y;
                if (centerX + rSize > X_OFFSET + reduceOriginal.getWidth()) {
                    centerX = X_OFFSET + reduceOriginal.getWidth() - rSize;
                }
                if (centerX - rSize <= X_OFFSET) {
                    centerX = X_OFFSET + RECTANGLE_SIZE / 2;
                }
                if (centerY + rSize >= Y_OFFSET + reduceOriginal.getHeight()) {
                    centerY = Y_OFFSET + reduceOriginal.getHeight() - rSize;
                }
                if (centerY - rSize <= Y_OFFSET) {
                    centerY = Y_OFFSET + RECTANGLE_SIZE / 2;
                }
                rectangleCords = new Point(centerX - rSize + Math.abs(rSize - RECTANGLE_SIZE / 2), centerY - rSize + Math.abs(rSize - RECTANGLE_SIZE / 2));
                clipImage();
                repaint();
            } catch (ArrayIndexOutOfBoundsException | NullPointerException ignored) {
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            onReduceOriginalImage = false;
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            if (onReduceOriginalImage && isSelect) {
                int rSize = (int) Math.round((double) RECTANGLE_SIZE / 2);
                Point clickedPoint = e.getPoint();
                int centerX = clickedPoint.x;
                int centerY = clickedPoint.y;
                if (centerX + rSize > X_OFFSET + reduceOriginal.getWidth()) {
                    centerX = X_OFFSET + reduceOriginal.getWidth() - rSize;
                }
                if (centerX - rSize <= X_OFFSET) {
                    centerX = X_OFFSET + RECTANGLE_SIZE / 2;
                }
                if (centerY + rSize >= Y_OFFSET + reduceOriginal.getHeight()) {
                    centerY = Y_OFFSET + reduceOriginal.getHeight() - rSize;
                }
                if (centerY - rSize <= Y_OFFSET) {
                    centerY = Y_OFFSET + RECTANGLE_SIZE / 2;
                }
                rectangleCords = new Point(centerX - rSize + Math.abs(rSize - RECTANGLE_SIZE / 2), centerY - rSize + Math.abs(rSize - RECTANGLE_SIZE / 2));
                clipImage();
                repaint();
            }
        }
    }
}