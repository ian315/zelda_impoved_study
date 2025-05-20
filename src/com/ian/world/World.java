package com.ian.world;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class World {
    BufferedImage map;

    private Tile[] tiles;
    private static int WIDTH, HEIGHT;

    public World(String path) {
        try {
            map = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            int[] pixels = new int[WIDTH * HEIGHT];
            tiles = new Tile[WIDTH * HEIGHT];
            map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

            for (int posX = 0; posX < WIDTH; posX++) {
                for (int posY =0; posY < HEIGHT; posY++) {
                    int pixelAtual = pixels[posX + (posY * WIDTH)];

                    if (pixelAtual== 0xFF000000) {//PRETO / CHAO
                        tiles[posX + (posY * WIDTH)] = new TileFloor(posX * 16, posY * 16, Tile.TILE_FLOOR);
                    }
                    if (pixelAtual == 0xFFFFFFFF) { //BRANCO / PAREDE
                        tiles[posX + (posY * WIDTH)] = new TileFloor(posX * 16, posY * 16, Tile.TILE_WALL);
                    }
                    else { //so para poder executar o jogo
                        tiles[posX + (posY * WIDTH)] = new TileFloor(posX * 16, posY * 16, Tile.TILE_FLOOR);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics graphics) {
        for (int positionX = 0; positionX < WIDTH; positionX++) {
            for (int positionY = 0; positionY < HEIGHT; positionY++) {
                Tile tile = tiles[positionX + (positionY * WIDTH)];
                tile.render(graphics);
            }
        }
    }
}