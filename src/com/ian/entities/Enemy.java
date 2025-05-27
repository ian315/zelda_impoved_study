package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.World;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double SPEED = 1;

    public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void update() {
        // caso eu nao use um cast no na direcao, se eu nao usar else if ele quebra, no entanto se usa apenas IF ele funciona normal
        // como quero q o mob se movimente na diagonal tambem, nao irei usar else if

        if ((int)x < Game.player.getX() && rightTileIsFree(SPEED)){
            x += SPEED;
        } else if ((int)x > Game.player.getX() && leftTileIsFree(SPEED)){
            x -= SPEED;
        }

        else if ((int)y < Game.player.getY() && downTileIsFree(SPEED)){
            y += SPEED;
        } else if ((int)y > Game.player.getY() && upTileIsFree(SPEED)){
            y -= SPEED;
        }
    }
}
