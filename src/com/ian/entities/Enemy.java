package com.ian.entities;

import com.ian.audio.Audios;
import com.ian.main.Game;
import com.ian.world.Camera;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double SPEED = 1;
    private int life = 10;
    private boolean isDamaged = false;
    int damageFrames = 0;

    public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void destroyEnemy() {
        Game.enemyList.remove(this);
        Game.entityList.remove(this);
    }

    private void enemyCollisionWithBullet() {
        for (int i = 0; i <Game.bullets.size(); i++) {
            Entity entity = Game.bullets.get(i);

            if (entity instanceof AmmoShoot){
                if (Entity.hasEntityCollide(this, entity)) {
                    Audios.hit.play();
                    isDamaged = true;
                    life -= Game.random.nextInt(2, 4);
                    Game.bullets.remove(i);
                    return;
                }
            }
        }
    }

    public boolean hasPlayerCollided() {
        Rectangle enemy = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), World.TILE_SIZE, World.TILE_SIZE);
        if (enemy.intersects(player) && this.z == Game.player.z) {
            return true;
        }

        return false;
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

    public void render (Graphics graphics){
        if (!isDamaged) {
            graphics.drawImage(Entity.entities.get("slime"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        } else {
            graphics.drawImage(Entity.entities.get("slimeFeedback"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
    }

    public void update() {
        // caso eu nao use um cast no na direcao, se eu nao usar else if ele quebra, no entanto se usa apenas IF ele funciona normal
        // como quero q o mob se movimente na diagonal tambem, nao irei usar else if

        if (!hasPlayerCollided()) {
            if (Game.random.nextInt(100) < 45) {
                if (x < Game.player.getX() && rightTileIsFree(SPEED) && isNotColiding((int) (this.getX() + SPEED), this.getY())) {
                    x += SPEED;
                } else if (x > Game.player.getX() && leftTileIsFree(SPEED) && isNotColiding((int) (this.getX() - SPEED), this.getY())) {
                    x -= SPEED;
                }

                else if (y < Game.player.getY() && downTileIsFree(SPEED) && isNotColiding(this.getX(), (int) (this.getY() + SPEED))) {
                    y += SPEED;
                } else if (y > Game.player.getY() && upTileIsFree(SPEED) && isNotColiding(this.getX(), (int) (this.getY() - SPEED))) {
                    y -= SPEED;
                }
            }
        } else {
            if (Game.random.nextInt(100) < 10) {
                Audios.hit.play();
                Game.player.life -= Game.random.nextInt(3);
                Player.setDamaged(true);
            }
        }

        enemyCollisionWithBullet();

        if (isDamaged) {
            damageFrames++;
            if (damageFrames == 6) {
                damageFrames = 0;
                isDamaged = false;
            }
        }

        if (life <= 0) {
            destroyEnemy();
        }
    }
}
