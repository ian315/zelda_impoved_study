package com.ian.main;

import com.ian.entities.*;
import com.ian.graphics.Spritesheet;
import com.ian.graphics.Ui;
import com.ian.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
    private boolean isRunning;
    private Thread thread;

    public static JFrame frame;
    public BufferedImage bufferedImage;
    private int CUR_LEVEL = 1, MAX_LEVEL = 2;

    private static final int WIDTH = 240;
    private static final int HEIGHT = 160;
    private final int SCALE = 3;

    public static World world;
    public static List<Entity> entityList; //poderia ter feito uma map, talvez mais pesado, porem mais facil de acessar e pegar as entidades
    public static Spritesheet spritesheet = new Spritesheet("/Spritesheet.png");;
    public static Player player;
    public static List<Enemy> enemyList;
    public static List<AmmoShoot> bullets;
    public Ui ui;

    public static Random random;

    public Game() {
        addKeyListener(this);
        addMouseListener(this);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initializeFrame();
        random = new Random();
        //Aqui inicializa os objetos
        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        ui = new Ui();
        entityList = new ArrayList<>();
        enemyList = new ArrayList<>();
        player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
        entityList.add(player);
        bullets = new ArrayList<>();
        world = new World("/level1.png"); // World precisa ser depois do spritesheet, pois precisamos inicializar antes, se nao vai dar nullpointer
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
        for (int i = 0; i < entityList.size(); i++) {
            entityList.get(i).update();
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
        }

        if (enemyList.isEmpty()){
            CUR_LEVEL++;
            if (CUR_LEVEL > MAX_LEVEL){
                CUR_LEVEL = 1;
            }
            String newWorld = "level" + CUR_LEVEL + ".png";
            World.restartGame(newWorld);
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
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(true);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(true);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setHasShootedKeyboard(true);
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
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setHasShootedKeyboard(false);
        }
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
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
}