package com.ian.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    protected double x;
    protected double y;
    protected int width;
    protected int height;

    BufferedImage sprite;

    public Entity(double x, double y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public void render(Graphics graphics) {
        graphics.drawImage(sprite, this.getX(), this.getY(), null);
    }

    public void update() {

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
