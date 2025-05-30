package com.ian.entities;

import com.ian.main.Game;

import java.awt.image.BufferedImage;
import java.util.List;

public class LifePack extends Entity{
    public LifePack(double x, double y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public static void checkLifePackCollision(List<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity actualEntity = entities.get(i);
            if (actualEntity instanceof LifePack){
                if (hasEntityCollide(Game.player, actualEntity)){
                    Game.player.life += 10;
                    if (Game.player.life >= 100)
                        Game.player.life = 100;
                    entities.remove(actualEntity);
                }
            }
        }
    }
}
