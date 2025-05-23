package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.Camera;
import com.ian.world.World;

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
    private final Map<String, BufferedImage[]> playerMovements = new HashMap<>();

    public Player(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        playerMovements.put("right", getPlayerRightMovement());
        playerMovements.put("left", getPlayerLeftMovement());
    }

    public void update() {
        moved = false;
        if(right && World.isTileFree((int)(this.getX() + SPEED), this.getY())){
            moved = true;
            this.setX(x += SPEED);
        }
        if(left && World.isTileFree((int)(this.getX() - SPEED), this.getY())) {
            moved = true;
            this.setX(x -= SPEED);
        }
        if(down && World.isTileFree(this.getX(), (int) (this.getY() + SPEED))) {
            moved = true;
            this.setY(y += SPEED);
            System.out.println(World.isTileFree(this.getX(), (int) (this.getY() - SPEED)));
        }
        if(up && World.isTileFree(this.getX(), (int) (this.getY() - SPEED))) {
            moved = true;
            this.setY(y -= SPEED);
            System.out.println(World.isTileFree(this.getX(), (int) (this.getY() + SPEED)));
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
//        Camera.setX(this.getX() - (Game.getWIDTH()/2));
//        Camera.setY(this.getY() - (Game.getHEIGHT()/2));

        Camera.setX(Camera.clamp(this.getX() - (Game.getWIDTH()/2), 0, World.getWIDTH() * 16 - Game.getWIDTH()));
        Camera.setY(Camera.clamp(this.getY() - (Game.getHEIGHT()/2), 0, World.getHEIGHT() * 16 - Game.getHEIGHT()));
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
                    this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
        if (down) { //deveria ter uma animacao para idle e uma animacao para quando estiver andando para baixo
            graphics.drawImage(Game.spritesheet.getSprite(32, 0, 16, 16),
                    this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
        if (right){
            graphics.drawImage(playerMovements.get("right")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
        if (left){
            graphics.drawImage(playerMovements.get("left")[currentAnimation], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
        }
        if (up) {
            graphics.drawImage(Game.spritesheet.getSprite(48, 0, 16, 16),
                    this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
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
