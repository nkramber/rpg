package com.nate.rpg;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.nate.rpg.state.GameState;
import com.nate.rpg.state.menu.MainMenu;

public class RPG extends Canvas implements Runnable, KeyListener {

    private static JFrame frame;
    private static Random random;
    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;
    private static final String TITLE = "RPG";
    private static final double TARGET_FPS = 60.0;
    private static final double TIME_BETWEEN_TICKS = 1000000000 / TARGET_FPS;

    private boolean running;
    private BufferedImage display;
    private boolean keys[];

    private List<GameState> gameStates;
    private MainMenu mainMenu;

    private void init() {
        display = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        random = new Random();
        keys = new boolean[256];
        gameStates = new ArrayList<>();

        mainMenu = new MainMenu();
        gameStates.add(mainMenu);

        addKeyListener(this);
    }

    @Override
    public void run() {
        init();
        double lastUpdateTime = System.nanoTime();

        while(running) {
            double time = System.nanoTime();
            while (time - lastUpdateTime > TIME_BETWEEN_TICKS) {
                tick();
                render();
                lastUpdateTime += TIME_BETWEEN_TICKS;
            }
        }
        System.exit(0);
    }

    private void tick() {
        for (GameState state : gameStates) {
            state.tick(this);
        }
    }

    private void render() {
        Graphics2D g = (Graphics2D) display.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        for (GameState state : gameStates) {
            state.render(g);
        }

        getGraphics().drawImage(display, 0, 0, this);
    }

    private void start() {
        requestFocus();
        running = true;
        new Thread(this).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RPG rpg = new RPG();
            rpg.setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            rpg.setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            rpg.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

            frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(rpg);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setIgnoreRepaint(true);
            rpg.start();
        });
    }

    public int getScreenWidth() { return SCREEN_WIDTH; }
    public int getScreenHeight() { return SCREEN_HEIGHT; }
    public Random getRandom() { return random; }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) { keys[e.getKeyCode()] = true; }
    @Override public void keyReleased(KeyEvent e) { keys[e.getKeyCode()] = false; }
}