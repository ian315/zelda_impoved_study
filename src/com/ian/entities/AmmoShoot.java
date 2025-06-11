package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmmoShoot extends Entity{
    private int directionX, directionY;
    private int bulletSpeed = 4;
    public long bulletLife;

    public AmmoShoot(double x, double y, int width, int height, BufferedImage sprite, int dx, int dy, long bulletLife) {
        super(x, y, width, height, sprite);
        this.directionX = dx;
        this.directionY = dy;
        this.bulletLife = bulletLife;
    }

    public void update() {
        long now = System.currentTimeMillis();

        x += directionX * bulletSpeed;
        y += directionY * bulletSpeed;

        if(now - bulletLife >= 1500)
            Game.bullets.remove(this);
    }

    public void render(Graphics graphics) {
        graphics.drawImage(entities.get("bullet"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), 3, 3, null);
    }
}
