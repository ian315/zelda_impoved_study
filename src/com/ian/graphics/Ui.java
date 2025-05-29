package com.ian.graphics;

import com.ian.entities.Player;
import com.ian.main.Game;

import java.awt.*;

public class Ui {

    public void render(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(5, 5, (int) ((Player.getLife()/Player.getMaxLife()) * 100), 8);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("arial", Font.BOLD, 9));
        graphics.drawString((int) Player.getLife() + " / " + (int)Player.getMaxLife(), 30, 12 );
    }
}
