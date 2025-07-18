package com.ian.graphics;

import com.ian.entities.Player;
import com.ian.Game;

import java.awt.*;

public class Ui {

    public void render(Graphics graphics) {
        //HEALTH BAR
        graphics.setColor(Color.RED);
        graphics.fillRect(5, 5, (int) ((Game.player.getLife() / Player.getMaxLife()) * 100), 8);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("arial", Font.BOLD, 9));
        graphics.drawString((int) Game.player.getLife() + " / " + (int)Player.getMaxLife(), 30, 12 );
        //AMMO
        graphics.setColor(Color.YELLOW);
        graphics.drawString("Ammo: " + Player.getAmmo(), 5, 22);
    }
}