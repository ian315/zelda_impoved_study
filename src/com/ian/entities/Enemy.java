package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double SPEED = 1;

    public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void update() {
        // caso eu nao use um cast no na direcao, se eu nao usar else if ele quebra, no entanto se usa apenas IF ele funciona normal
        // como quero q o mob se movimente na diagonal tambem, nao irei usar else if

        if (!hasPlayerColided()) {
            if (Game.random.nextInt(100) < 45) {
                if ((int) x < Game.player.getX() && rightTileIsFree(SPEED) && isNotColiding((int) (this.getX() + SPEED), this.getY())) {
                    x += SPEED;
                } else if ((int) x > Game.player.getX() && leftTileIsFree(SPEED) && isNotColiding((int) (this.getX() - SPEED), this.getY())) {
                    x -= SPEED;
                }

                if ((int) y < Game.player.getY() && downTileIsFree(SPEED) && isNotColiding(this.getX(), (int) (this.getY() + SPEED))) {
                    y += SPEED;
                } else if ((int) y > Game.player.getY() && upTileIsFree(SPEED) && isNotColiding(this.getX(), (int) (this.getY() - SPEED))) {
                    y -= SPEED;
                }
            }
        } else {
            if (Game.random.nextInt(100) < 10) {
                Player.life--;
                System.out.println(Player.life);

                if (Player.life == 0)
                    System.exit(1);
            }
        }
    }

    public boolean hasPlayerColided() {
        Rectangle enemy = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);

        return enemy.intersects(player);
    }

    public boolean isNotColiding(int nextPositionX, int nextPositionY) {
        Rectangle enemyCurrent = new Rectangle(nextPositionX, nextPositionY, World.TILE_SIZE, World.TILE_SIZE);

        for (Enemy enemy : Game.enemyList) {
            if (enemy == this) {
                continue;
            }

            Rectangle nextEnemy = new Rectangle(enemy.getX(), enemy.getY(), World.TILE_SIZE, World.TILE_SIZE);
            if (enemyCurrent.intersects(nextEnemy)) {
                return false;
            }

        }
        return true;
    }
}
