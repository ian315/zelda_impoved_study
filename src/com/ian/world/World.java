package com.ian.world;

import com.ian.entities.*;
import com.ian.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class World {
    BufferedImage map;

    private static Tile[] tiles;
    private static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

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
                    int actualPixel = pixels[posX + (posY * WIDTH)];
                    tiles[posX + (posY * WIDTH)] = new TileFloor(posX * 16, posY * 16, Tile.TILE_FLOOR);

                    if (actualPixel == 0xFF000000) {//BLACK/FLOOR
                        tiles[posX + (posY * WIDTH)] = new TileFloor(posX * 16, posY * 16, Tile.TILE_FLOOR);
                    } else if (actualPixel == 0xFFFFFFFF) { //WHITE/WALL
                        tiles[posX + (posY * WIDTH)] = new TileWall(posX * 16, posY * 16, Tile.TILE_WALL);
                    } else if (actualPixel == 0xFF0026FF) {//BLUE/PLAYER//POSITION
                        Game.player.setX(posX * 16);
                        Game.player.setY(posY * 16);
                    } else if (actualPixel == 0xFFFF0000) {//RED/ENEMY
                        Enemy enemies = new Enemy(posX * 16, posY * 16, 16, 16, Entity.entities.get("slime"));
                        Game.entityList.add(enemies);
                        Game.enemyList.add(enemies);
                    } else if (actualPixel == 0xFFFFD800) {//YELLOW/AMMO
                        Game.entityList.add(new Ammo(posX * 16, posY * 16, 16, 16, Entity.entities.get("ammo")));
                    } else if (actualPixel == 0xFF4CFF00) {//GREEN/HEATLH
                        Game.entityList.add(new LifePack(posX * 16, posY * 16, 16, 16, Entity.entities.get("lifePack")));
                    } else if (actualPixel == 0xFFFF00DC) {//PINK/BOW
                        Game.entityList.add(new Weapon(posX * 16, posY * 16, 16, 16, Entity.entities.get("bow")));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics graphics) {
        int xStart = Camera.getX() >> 4;
        int yStart = Camera.getY() >> 4;

        int xFinal = xStart + (Game.getWIDTH() >> 4);
        int yFinal = yStart + (Game.getHEIGHT() >> 4);

        for (int positionX = xStart; positionX <= xFinal; positionX++) {
            for (int positionY = yStart; positionY <= yFinal; positionY++) {

                if (positionX < 0 || positionY < 0 || positionX >= WIDTH || positionY >= HEIGHT)
                    continue;

                Tile tile = tiles[positionX + (positionY * WIDTH)];
                tile.render(graphics);
            }
        }
    }

    public static boolean isTileFree(int nextPositionX, int nextPositionY){
        int x1 = nextPositionX / TILE_SIZE;
        int y1 = nextPositionY / TILE_SIZE;

        int x2 = (nextPositionX + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = nextPositionY / TILE_SIZE;

        int x3 = nextPositionX / TILE_SIZE;
        int y3 = (nextPositionY + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (nextPositionX + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (nextPositionY + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof TileWall)
                || (tiles[x2 + (y2 * World.WIDTH)] instanceof TileWall)
                || (tiles[x3 + (y3 * World.WIDTH)] instanceof TileWall)
                || (tiles[x4 + (y4 * World.WIDTH)] instanceof TileWall));
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
}