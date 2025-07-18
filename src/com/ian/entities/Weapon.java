package com.ian.entities;

import com.ian.Game;

import java.awt.image.BufferedImage;
import java.util.List;

public class Weapon extends Entity{
    public Weapon(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public static void checkWeaponCollision(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity actualEntity = entities.get(i);
            if (actualEntity instanceof Weapon){
                if (hasEntityCollide(Game.player, actualEntity)){
                    Game.player.setHasGun(true);
                    entities.remove(actualEntity);
                }
            }
        }
    }
}
