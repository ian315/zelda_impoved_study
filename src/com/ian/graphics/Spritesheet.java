package com.ian.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Spritesheet {

    BufferedImage spritesheet;

    public Spritesheet(String path) {
        try {
            spritesheet = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getSprite(int x, int y, int widht, int height) {
        return spritesheet.getSubimage(x, y, widht, height);
    }
}
