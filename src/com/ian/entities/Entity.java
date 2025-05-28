package com.ian.entities;

import com.ian.main.Game;
import com.ian.world.Camera;
import com.ian.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    protected double x;
    protected double y;
    protected int width;
    protected int height;

    public static final Map<String, BufferedImage> entities = new HashMap<>();

    BufferedImage sprite;

    public Entity(double x, double y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.getEntitiesSprites();
    }

    public void getEntitiesSprites(){
        entities.put("health", Game.spritesheet.getSprite(0,16, 16, 16));
        entities.put("bow", Game.spritesheet.getSprite(16,16, 16, 16));
        entities.put("ammo", Game.spritesheet.getSprite(32,16, 16, 16));
        entities.put("slime", Game.spritesheet.getSprite(48,16, 16, 16));
    }

    public void render(Graphics graphics) {
        graphics.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
    }

    public void update() {

    }

    protected boolean rightTileIsFree(double SPEED) {
        return World.isTileFree((int) (this.getX() + SPEED), this.getY());
    }

    protected boolean leftTileIsFree(double SPEED) {
        return World.isTileFree((int) (this.getX() - SPEED), this.getY());
    }

    protected boolean upTileIsFree(double SPEED) {
        return World.isTileFree(this.getX(), (int) (this.getY() - SPEED));
    }

    protected boolean downTileIsFree(double SPEED) {
        return World.isTileFree(this.getX(), (int) (this.getY() + SPEED));
    }


    public int getX() {
        return (int) this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
