package com.ian.entities;

import com.ian.Game;
import com.ian.world.Camera;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    public boolean right, left, up, down;
    private int direction;
    private final int directionRight = 0;
    private final int directionLeft = 1;
    public int mouseX, mouseY;
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

    private boolean jump = false;
    private boolean isJumping = false;
    private boolean jumpUp = false;
    private boolean jumpDown = false;
    private final int maxJumpRange = 45;
    private int currentJumpRange = 0;
    public int z = 0;

    private static boolean isDamaged = false;
    private int damageFrames = 0;

    protected boolean hasGun= false;
    private boolean hasShootedKeyboard = false;
    private boolean mouseShoot = false;

    public Player(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        playerMovements.put("right", getPlayerRightMovement());
        playerMovements.put("left", getPlayerLeftMovement());
        playerDamaged = Game.spritesheet.getSprite(64, 16, 16, 16);
    }

    public void update() {
        if (jump) {
            if (!isJumping) {
                isJumping = true;
                jump = false;
                jumpUp = true;
            }
        }

        if (isJumping) {
            if (jumpUp) {
                currentJumpRange += 2;
            } else if (jumpDown) {
                currentJumpRange -= 2;
                if (currentJumpRange <= 0) {
                    isJumping = false;
                    jumpDown = false;
                }
            }
            z = currentJumpRange;
            if (currentJumpRange >= maxJumpRange) {
                jumpDown = true;
                jumpUp = false;
            }
        }

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
                if (right && currentAnimation >= playerMovements.get("right").length) {
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

        shoots();


        LifePack.checkLifePackCollision(Game.entityList);
        Ammo.checkAmmoCollision(Game.entityList);
        Weapon.checkWeaponCollision(Game.entityList);

        Camera.setX(Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.getWIDTH() * 16 - Game.WIDTH));
        Camera.setY(Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.getHEIGHT() * 16 - Game.HEIGHT));

        if (life <= 0) {
            life = 0;
            Game.setGameState("GAME_OVER");
        }
    }

    public void shoots () {
        if (hasGun &&  ammo > 0) {
            if (hasShootedKeyboard) {
                int dx = 0;

                hasShootedKeyboard = false;
                ammo--;

                if (direction == directionRight) {
                    dx = 1;
                } else {
                    dx = -1;
                }
                AmmoShoot bullet = new AmmoShoot(this.getX() + 10, this.getY() + 9, 3, 3, null, dx, 0, System.currentTimeMillis());
                Game.bullets.add(bullet);
            }

            if (mouseShoot){
                mouseShoot = false;
                ammo--;
                double angle = 0;
                int py = 9;
                int px;

                if (direction == directionRight) {
                    px = 10;
                    angle = Math.atan2(mouseY - (this.getY() + py - Camera.getY()), mouseX - (this.getX() + px - Camera.getX()));
                } else {
                    px = -5;
                    angle = Math.atan2(mouseY - (this.getY() + py - Camera.getY()), mouseX - (this.getX() - px - Camera.getX()));
                }

                double dx = Math.cos(angle);
                double dy = Math.sin(angle);

                AmmoShoot bullet = new AmmoShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy, System.currentTimeMillis());
                Game.bullets.add(bullet);
            }
        }
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

    public void render(Graphics graphics) {
        if (!isDamaged) {
            if (direction == directionRight) {
                graphics.drawImage(playerMovements.get("right")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                if (hasGun){
                    graphics.drawImage(entities.get("weaponRight"), this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                }
            }
            else if (direction == directionLeft) {
                graphics.drawImage(playerMovements.get("left")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                if (hasGun){
                    graphics.drawImage(entities.get("weaponLeft"), this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                }
            }

        } else {
            graphics.drawImage(playerDamaged, this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
            if (hasGun) {
                if (direction == directionRight) {
                    graphics.drawImage(entities.get("weaponFeedbackRight"), this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                }
                if (direction == directionLeft) {
                    graphics.drawImage(entities.get("weaponFeedbackLeft"), this.getX() - Camera.getX(), this.getY() - Camera.getY() - z, null);
                }
            }
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
        return hasShootedKeyboard;
    }

    public void setHasShootedKeyboard(boolean hasShootedKeyboard) {
        this.hasShootedKeyboard = hasShootedKeyboard;
    }

    public boolean isMouseShoot() {
        return mouseShoot;
    }

    public void setMouseShoot(boolean mouseShoot) {
        this.mouseShoot = mouseShoot;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
