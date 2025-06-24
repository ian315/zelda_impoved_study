package com.ian.graphics;

import com.ian.main.Game;

import java.awt.*;
import java.util.Objects;

public class GameMenu {

    private String[] options = {"novoJogo", "load", "exit"};
    private int currentOption = 0;
    private int maxOption = options.length - 1;
    private boolean up, down;
    private boolean enter;

    private final int WIDTH = Game.WIDTH * Game.SCALE;
    private final int HEIGHT = Game.HEIGHT * Game.SCALE;

    private static boolean pause = false;


    public void update() {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOption;
            }
        }

        if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOption) {
                currentOption = 0;
            }
        }

        if (enter){
            if (Objects.equals(options[currentOption], "novoJogo") || (Objects.equals(options[currentOption], "continuar"))){
                Game.setGameState("NORMAL");
                pause = false;
            }
        }

        if (enter){
            if (Objects.equals(options[currentOption], "exit")){
                System.exit(1);
            }
        }
    }

    public void render(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setFont(new Font("arial", Font.BOLD, 48));
        graphics.setColor(Color.YELLOW);
        graphics.drawString("GAME NAME", WIDTH / 2 - 160, HEIGHT / 2 - 180);

        graphics.setColor(Color.WHITE);
        if (pause == false)
            graphics.drawString("New Game", WIDTH / 2 - 160, HEIGHT / 2 - 50);
        else
            graphics.drawString("Continuar", WIDTH / 2 - 160, HEIGHT / 2 - 50);

        graphics.drawString("Load Game", WIDTH / 2 - 160, HEIGHT / 2 + 20);
        graphics.drawString("Exit", WIDTH / 2 - 160, HEIGHT / 2 + 90);

        if (Objects.equals(options[currentOption], "novoJogo")) {
            graphics.drawString("> ", WIDTH / 2 - 200, HEIGHT / 2 - 50);
        }

        if (Objects.equals(options[currentOption], "load")) {
            graphics.drawString("> ", WIDTH / 2 - 200, HEIGHT / 2 + 20);
        }
        if (Objects.equals(options[currentOption], "exit")) {
            graphics.drawString("> ", WIDTH / 2 - 200, HEIGHT / 2 + 90);
        }
    }


    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(int currentOption) {
        this.currentOption = currentOption;
    }

    public int getMaxOption() {
        return maxOption;
    }

    public void setMaxOption(int maxOption) {
        this.maxOption = maxOption;
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

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        GameMenu.pause = pause;
    }
}
