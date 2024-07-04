package com.provider.other;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageResizer {

    public static BufferedImage resizeAndCropImage(BufferedImage originalImage, int targetSize) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Determinar las dimensiones finales de la imagen recortada
        int newWidth = Math.min(originalWidth, originalHeight);
        int newHeight = newWidth;

        // Calcular las coordenadas de recorte para centrar la imagen
        int x = Math.max(0, (originalWidth - newWidth) / 2); // Asegura que x no sea negativo
        int y = Math.max(0, (originalHeight - newHeight) / 2); // Asegura que y no sea negativo

        // Recortar la imagen para que sea cuadrada
        BufferedImage croppedImage = originalImage.getSubimage(x, y, newWidth, newHeight);

        // Redimensionar la imagen al tamaño objetivo
        BufferedImage finalImage = new BufferedImage(targetSize, targetSize, croppedImage.getType());
        Graphics2D g2 = finalImage.createGraphics();
        g2.drawImage(croppedImage, 0, 0, targetSize, targetSize, null);
        g2.dispose();

        return finalImage;
    }
}