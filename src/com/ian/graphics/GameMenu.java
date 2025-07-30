package com.ian.graphics;

import com.ian.Game;
import com.ian.world.World;

import java.awt.*;
import java.io.*;
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

    private static boolean existsSavedGame = false;
    private static boolean saveGame = false;


    public void update() {
        File file = new File("sava.txt");

        if (file.exists()) {
            existsSavedGame = true;
        }

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
                //Caso queira toda a vez que iniciar um novo jogo apaga o último save
                //file = new File("save.txt");
                //file.delete();
            }
        }

        if (enter) {
            if (Objects.equals(options[currentOption], "load")) {
                String saver = loadGame(10);
                applySave(saver);
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
        if (!pause)
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

    public static void applySave(String string) {
        String[] split = string.split("/");
        for (int i = 0; i < split.length; i++) {
            String[] split2 = split[i].split(":");
            switch (split2[0]) {
                case "level":
                    World.restartGame("level" + split2[1].replace(".0", "") + ".png");
                    Game.setGameState("NORMAL");
                    pause = false;
                    break;

                case  "vida":
                    Game.player.setLife(Integer.parseInt(split2[1].replace(".0", "")));
                    break;

                case  "x":
                    Game.player.setX(Double.parseDouble(split2[1]));
                    break;

                case  "y":
                    Game.player.setY(Double.parseDouble(split2[1]));
                    break;
            }
        }
    }

    public static String loadGame(int encode) {
        StringBuilder line = new StringBuilder();
        File file = new File("save.txt");
        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                try {
                    while ((singleLine = reader.readLine()) != null) {
                        //adicionei um limit 2 no regex, por que quando estava salvando com vida 100, no encode 10 ele transforma
                        //o valor de 0 em : sendo assim sem limitador do regex ele removia os outros : retornando a vida em apenas 1
                        String[] transition = singleLine.split(":", 2);
                        char[] val = transition[1].toCharArray();
                        transition[1] = "";
                        for (int i = 0; i < val.length; i++) {
                            val[i] -= (char) encode;
                            transition[1] += val[i];
                        }
                        line.append(transition[0]);
                        line.append(":");
                        line.append(transition[1]);
                        line.append("/");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return line.toString();
    }

    public static void saveGame(String[] val1, double[] val2, int encode) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("save.txt"));

        }catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < val1.length; i++) {
            StringBuilder current = new StringBuilder(val1[i]);
            current.append(":");
            char[] value = Double.toString(val2[i]).toCharArray();
            for (int n = 0; n < value.length; n ++) {
                value[n] += (char) encode;
                current.append(value[n]);
            }
            try {
                Objects.requireNonNull(bufferedWriter).write(current.toString());
                if (i < val1.length - 1) {
                    bufferedWriter.newLine();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Objects.requireNonNull(bufferedWriter).flush();
            bufferedWriter.close();
        } catch (IOException e) {

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
