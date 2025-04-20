package com.ian.main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Main extends Canvas implements Runnable {
    private boolean isRunning;
    private Thread thread;

    public static JFrame frame;
    public BufferedImage bufferedImage;

    private final int WIDTH = 240;
    private final int HEIGHT = 160;
    private final int SCALE = 3;

    public Main() {
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initializeFrame();
        bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.start();
    }

    private synchronized void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning= false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {

    }

    public void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

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

            if (System.currentTimeMillis() - timer >= 1000){
                System.out.println("frames: " + frames);
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
}