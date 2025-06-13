package com.ian.entities;

import com.ian.main.Game;

import java.awt.image.BufferedImage;
import java.util.List;

public class Ammo extends Entity{
    public Ammo(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public static void checkAmmoCollision(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity actualEntity = entities.get(i);
            if (actualEntity instanceof Ammo){
                if (hasEntityCollide(Game.player, actualEntity)){
                    Player.setAmmo(Player.getAmmo() + 100);
                    entities.remove(actualEntity);
                }
            }
        }
    }
}
