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
    protected int z;
    protected int width;
    protected int height;

    protected int hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight;

    public static final Map<String, BufferedImage> entities = new HashMap<>();

    BufferedImage sprite;

    public Entity(double x, double y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.getEntitiesSprites();

        this.hitBoxHeight = height;
        this.hitBoxWidth = width;
        this.hitBoxY = 0;
        this.hitBoxX = 0;
    }

    public void setHitBox(int hitBoxHeight, int hitBoxWidth, int hitBoxY, int hitBoxX) {
        this.hitBoxHeight = hitBoxHeight;
        this.hitBoxWidth = hitBoxWidth;
        this.hitBoxY = hitBoxY;
        this.hitBoxX = hitBoxX;
    }

    public void getEntitiesSprites(){
        entities.put("lifePack", Game.spritesheet.getSprite(0,16, 16, 16));
        entities.put("bow", Game.spritesheet.getSprite(16,16, 16, 16));
        entities.put("ammo", Game.spritesheet.getSprite(32,16, 16, 16));
        entities.put("bullet", Game.spritesheet.getSprite(48,32, 16, 16));
        entities.put("slime", Game.spritesheet.getSprite(48,16, 16, 16));
        entities.put("weaponRight", Game.spritesheet.getSprite(16,32, 16, 16));
        entities.put("weaponLeft", Game.spritesheet.getSprite(32,32, 16, 16));
        entities.put("slimeFeedback", Game.spritesheet.getSprite(64,32, 16, 16));
        entities.put("weaponFeedbackLeft", Game.spritesheet.getSprite(32,48, 16, 16));
        entities.put("weaponFeedbackRight", Game.spritesheet.getSprite(16,48, 16, 16));
    }

    public static boolean hasEntityCollide(Entity entity1, Entity entity2) {
        Rectangle entityHitBox1 = new Rectangle(entity1.getX() + entity1.hitBoxX, entity1.getY() + entity1.hitBoxY, entity1.hitBoxWidth, entity1.hitBoxHeight);
        Rectangle entityHitBox2 = new Rectangle(entity2.getX() + entity2.hitBoxX, entity2.getY() + entity2.hitBoxY, entity2.hitBoxWidth, entity2.hitBoxHeight);
        return entityHitBox1.intersects(entityHitBox2) && entity1.z == entity2.z;
    }

    public void render(Graphics graphics) {
        graphics.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
    }

    public void update() {

    }

    public boolean rightTileIsFree(double SPEED) {
        return World.isTileFree((int) (this.getX() + SPEED), this.getY());
    }

    public boolean leftTileIsFree(double SPEED) {
        return World.isTileFree((int) (this.getX() - SPEED), this.getY());
    }

    public boolean upTileIsFree(double SPEED) {
        return World.isTileFree(this.getX(), (int) (this.getY() - SPEED));
    }

    public boolean downTileIsFree(double SPEED) {
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
