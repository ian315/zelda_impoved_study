package com.ian.entities;

import com.ian.graphics.Spritesheet;
import com.ian.main.Game;
import com.ian.world.Camera;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    public boolean right, left, up, down;
    private int direction;
    private int directionRight = 0;
    private int directionLeft = 1;
    private boolean moved;
    private final double SPEED = 1;

    protected double life = 100;
    protected static double maxLife = 100;
    protected static int ammo = 0;

    public int frames = 0;
    public int maxFrames = 5;
    public int currentAnimation = 0;
    private final Map<String, BufferedImage[]> playerMovements = new HashMap<>();
    private final BufferedImage playerDamaged;

    private static boolean isDamaged = false;
    private int damageFrames = 0;

    protected boolean hasGun= false;
    private boolean hasShooted = false;

    public Player(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        playerMovements.put("right", getPlayerRightMovement());
        playerMovements.put("left", getPlayerLeftMovement());
        playerDamaged = Game.spritesheet.getSprite(64, 16, 16, 16);
    }

    public void update() {
        moved = false;
        if (right && rightTileIsFree(SPEED)) {
            direction = directionRight;
            moved = true;
            this.setX(x += SPEED);
        }
        if (left && leftTileIsFree(SPEED)) {
            direction = directionLeft;
            moved = true;
            this.setX(x -= SPEED);
        }
        if (down && downTileIsFree(SPEED)) {
            moved = true;
            this.setY(y += SPEED);
        }
        if (up && upTileIsFree(SPEED)) {
            moved = true;
            this.setY(y -= SPEED);
        }

        if (moved && !up && !down) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                currentAnimation++;
                if (left && currentAnimation >= playerMovements.get("left").length) {
                    currentAnimation = 0;
                }
                if (right && currentAnimation >= playerMovements.get("left").length) {
                    currentAnimation = 0;
                }
            }
        }

        if (isDamaged()) {
            damageFrames++;
            if (damageFrames == 6) {
                damageFrames = 0;
                isDamaged = false;
            }
        }

            if (hasGun && hasShooted && ammo > 0) {
                int dx;
                hasShooted = false;
                ammo--;
                if (direction == directionRight) {
                    dx = 1;
                } else {
                    dx = -1;
                }
                AmmoShoot bullet = new AmmoShoot(this.getX() + 10, this.getY() + 9, 50, 50, null, dx, 0, System.currentTimeMillis());
                Game.bullets.add(bullet);
            }

        LifePack.checkLifePackCollision(Game.entityList);
        Ammo.checkAmmoCollision(Game.entityList);
        Weapon.checkWeaponCollision(Game.entityList);

        Camera.setX(Camera.clamp(this.getX() - (Game.getWIDTH() / 2), 0, World.getWIDTH() * 16 - Game.getWIDTH()));
        Camera.setY(Camera.clamp(this.getY() - (Game.getHEIGHT() / 2), 0, World.getHEIGHT() * 16 - Game.getHEIGHT()));

        gameOver();
    }

    public BufferedImage[] getPlayerRightMovement() {
        BufferedImage[] rightMovement = new BufferedImage[3];

        for (int i = 0; i < rightMovement.length; i++) {
            rightMovement[i] = Game.spritesheet.getSprite(64 + (i * 16), 0, 16, 16);
        }
        return rightMovement;
    }

    public BufferedImage[] getPlayerLeftMovement() {
        BufferedImage[] leftMovement = new BufferedImage[3];

        for (int i = 0; i < leftMovement.length; i++) {
            leftMovement[i] = Game.spritesheet.getSprite(112 + (i * 16), 0, 16, 16);
        }
        return leftMovement;
    }

    public void gameOver() {
        if (life <= 0) {
            Game.entityList = new ArrayList<>();
            Game.enemyList = new ArrayList<>();
            Game.spritesheet = new Spritesheet("/Spritesheet.png");
            Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
            Game.entityList.add(Game.player);
            Game.world = new World("/WorldSpritesheet.png");
        }
    }

    public void render(Graphics graphics) {
        if (!isDamaged) {

            if (direction == directionRight) {
                graphics.drawImage(playerMovements.get("right")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
                if (hasGun){
                    graphics.drawImage(entities.get("weaponRight"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
                }
            }
            else if (direction == directionLeft) {
                graphics.drawImage(playerMovements.get("left")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
                if (hasGun){
                    graphics.drawImage(entities.get("weaponLeft"), this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
                }
            }

        } else {
            graphics.drawImage(playerDamaged, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public double getLife() {
        return life;
    }

    public static double getMaxLife() {
        return maxLife;
    }

    public static int getAmmo() {
        return ammo;
    }

    public static void setAmmo(int ammo) {
        Player.ammo = ammo;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public static void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }

    public boolean hasGun() {
        return hasGun;
    }

    public void setHasGun(boolean hasGun) {
        this.hasGun = hasGun;
    }

    public boolean hasShooted() {
        return hasShooted;
    }

    public void setHasShooted(boolean hasShooted) {
        this.hasShooted = hasShooted;
    }
}
