package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.Camera;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AmmoShoot extends Entity{
    private final double directionX;
    private final double directionY;
    public long bulletLife;

    public AmmoShoot(double x, double y, int width, int height, BufferedImage sprite, double dx, double dy, long bulletLife) {
        super(x, y, width, height, sprite);
        this.directionX = dx;
        this.directionY = dy;
        this.bulletLife = bulletLife;
    }

    public void update() {
        long now = System.currentTimeMillis();

        double bulletSpeed = 4;
        x += directionX * bulletSpeed;
        y += directionY * bulletSpeed;

        if(now - bulletLife >= 750 || bulletCollisionWithTile()){
            Game.bullets.remove(this);
        }
    }

    public boolean bulletCollisionWithTile() {
        return !World.isTileFree((int) x, (int) y);
    }

    public void render(Graphics graphics) {
        graphics.drawImage(entities.get("bullet"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), this.getWidth(), this.getHeight(), null);
    }
}
