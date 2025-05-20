package com.ian.entities;

import com.ian.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity{
    public boolean right, left, up, down;
    private final double SPEED = 1.0;

    public int frames = 0;
    public int maxFrames = 5;
    public int currentAnimation = 0;
    public boolean moved = false;
    private Map<String, BufferedImage[]> playerMovements = new HashMap<>();

    public Player(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        playerMovements.put("right", getPlayerRightMovement());
        playerMovements.put("left", getPlayerLeftMovement());
    }

    public void update() {
        moved = false;
        if(right){
            moved = true;
            this.setX(x += SPEED);
        }
        if(left) {
            moved = true;
            this.setX(x -= SPEED);
        }
        if(down) {
            moved = true;
            this.setY(y += SPEED);
        }
        if(up) {
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
    }

    public BufferedImage[] getPlayerRightMovement() {
        BufferedImage[] rightMovement = new BufferedImage[3];

        for (int i = 0; i < rightMovement.length; i++){
            rightMovement[i] = Game.spritesheet.getSprite(64 + (i * 16), 0, 16, 16);
        }
        return rightMovement;
    }

    public BufferedImage[] getPlayerLeftMovement() {
        BufferedImage[] leftMovement = new BufferedImage[3];

        for (int i = 0; i < leftMovement.length; i++){
            leftMovement[i] = Game.spritesheet.getSprite(112 + (i * 16), 0, 16, 16);
        }
        return leftMovement;
    }

    public void render(Graphics graphics) {
        if (!moved) {
            graphics.drawImage(Game.spritesheet.getSprite(32, 0, 16, 16),
                    this.getX(), this.getY(), null);
        }
        if (down) { //mais correto seria ter uma animacao para idle e uma animacao para quando estiver andando para baixo
            graphics.drawImage(Game.spritesheet.getSprite(32, 0, 16, 16),
                    this.getX(), this.getY(), null);
        }
        if (right){
            graphics.drawImage(playerMovements.get("right")[currentAnimation], this.getX(), this.getY(), null);
        }
        if (left){
            graphics.drawImage(playerMovements.get("left")[currentAnimation], this.getX(), this.getY(), null);
        }
        if (up) {
            graphics.drawImage(Game.spritesheet.getSprite(48, 0, 16, 16),
                    this.getX(), this.getY(), null);
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
}
