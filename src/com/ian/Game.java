package com.ian;

import com.ian.entities.*;
import com.ian.graphics.GameMenu;
import com.ian.graphics.Spritesheet;
import com.ian.graphics.Ui;
import com.ian.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    private boolean isRunning;
    private Thread thread;

    public static JFrame frame;
    public BufferedImage bufferedImage;
    private int CUR_LEVEL = 1, MAX_LEVEL = 2;

    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    public static World world;
    public static List<Entity> entityList; //poderia ter feito uma map, talvez mais pesado, porem mais facil de acessar e pegar as entidades
    public static Spritesheet spritesheet = new Spritesheet("/Spritesheet.png");;
    public static Player player;
    public static List<Enemy> enemyList;
    public static List<AmmoShoot> bullets;
    public Ui ui;
    public GameMenu menu;

//    public InputStream inputFont = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
//    public static Font pixelArtFont;

    private static String gameState = "MENU";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame = false;
    private boolean saveGame = false;

    public static Random random;

    private int mousePositionX, mousePositionY;

    public Game() {
//      para adicionar uma musica de background basta iniciar aqui
//        Audios.backgroundMusic.loop();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initializeFrame();

        //Aqui inicializa os objetos
        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        ui = new Ui();
        menu = new GameMenu();

        random = new Random();
        entityList = new ArrayList<>();
        enemyList = new ArrayList<>();
        player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
        entityList.add(player);
        bullets = new ArrayList<>();
        world = new World("/level1.png"); // World precisa ser depois do spritesheet, pois precisamos inicializar antes, se nao vai dar null pointer

//      Adicionando uma nova fonte
//        try {
//            pixelArtFont = Font.createFont(Font.TRUETYPE_FONT, inputFont).deriveFont(16f);
//        } catch (FontFormatException | IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    private synchronized void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        if  (Objects.equals(gameState, "NORMAL")) {
            if (this.saveGame) {
                this.saveGame = false;
                String[] opt1 = {"level", "vida", "x", "y"};
                int[] opt2 = {this.CUR_LEVEL, (int)player.getLife(), player.getX(), player.getY()};
                GameMenu.saveGame(opt1, opt2, 10);
                System.out.println("Jogo salvo com sucesso!");
            }
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).update();
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).update();
            }

            if (enemyList.isEmpty()) {
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (Objects.equals(gameState, "GAME_OVER")) {
            this.framesGameOver ++;
            if (this.framesGameOver == 25) {
                this.framesGameOver = 0;
                this.showMessageGameOver = !this.showMessageGameOver;
            }

            if (restartGame) {
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                restartGame = false;
                setGameState("NORMAL");
                World.restartGame(newWorld);
            }
        } else if (Objects.equals(gameState, "MENU")) {
            menu.update();
        }
    }

    public void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        //Renderiazacao do mundo
        world.render(graphics);
        for (Entity entity : entityList) {
            entity.render(graphics);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(graphics);
        }

        ui.render(graphics);

        graphics.dispose();
        graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(bufferedImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        if (Objects.equals(gameState, "GAME_OVER")){

            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setColor(new Color(0, 0, 0, 100));
            graphics2D.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
            graphics2D.setFont(new Font("arial", Font.BOLD, 38));
            graphics2D.setColor(Color.WHITE);
            graphics2D.drawString("Game Over", (WIDTH * SCALE) / 2 - 98, (HEIGHT * SCALE) / 2 + 8);
            if (showMessageGameOver)
                graphics2D.drawString("Press ENTER to restart", (WIDTH * SCALE) / 2 - 180, (HEIGHT * SCALE) / 2 + 60);
        }

        else if (Objects.equals(gameState, "MENU")) {
            menu.render(graphics);
        }
        /* Exemplo de rotacao de um retangulo
        Graphics2D graphics2D = (Graphics2D) graphics;
        double angleMouse = Math.atan2(200 + 10 - mousePositionY, 200 + 10 - mousePositionX);
        graphics2D.rotate(angleMouse, 200 + 10, 200 + 10);
        graphics.setColor(Color.red);
        graphics.fillRect(200, 200, 20, 20); */
        bufferStrategy.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfUpdates = 60.0;
        double oneNanoSecond = 1000000000 / amountOfUpdates;
        double updateDelta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while (isRunning) {
            long now = System.nanoTime();
            updateDelta += (now - lastTime) / oneNanoSecond;
            lastTime = now;

            if (updateDelta >= 1) {
                update();
                render();
                frames++;
                updateDelta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
//                System.out.println("frames: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
        stop();
    }

    private void initializeFrame() {
        frame = new JFrame("Title");
        frame.add(this);
        frame.setResizable(false);
        frame.pack(); //Metodo do canvas que serve para calcular certa dimensoes e calcular
        frame.setLocationRelativeTo(null); //null faz referencia ao centro da tela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(true);
            if (Objects.equals(gameState, "MENU")) {
                menu.setUp(true);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(true);
            if (Objects.equals(gameState, "MENU")) {
                menu.setDown(true);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setHasShootedKeyboard(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame = true;

            if (Objects.equals(gameState, "MENU")) {
                menu.setEnter(true);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setGameState("MENU");
            GameMenu.setPause(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            player.setJump(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_F5){
            if (gameState.equals("NORMAL"))
                this.saveGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(false);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(false);

            if (Objects.equals(gameState, "MENU")) {
                menu.setUp(false);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(false);

            if (Objects.equals(gameState, "MENU")) {
                menu.setDown(false);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setHasShootedKeyboard(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            menu.setEnter(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.setMouseShoot(true);
        player.mouseX = e.getX() / 3;
        player.mouseY = e.getY() / 3;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        player.setMouseShoot(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static String getGameState() {
        return gameState;
    }

    public static void setGameState(String gameState) {
        Game.gameState = gameState;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mousePositionX = e.getX();
        this.mousePositionY = e.getY();
    }
}