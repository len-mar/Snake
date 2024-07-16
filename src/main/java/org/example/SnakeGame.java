package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {gameLoop.stop();}
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
//        else if(e.getKeyCode() == KeyEvent.VK_ENTER){
//            gameLoop.restart();
//        }
    }

    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    boolean gameOver = false;
    int height, width;
    int tileSize = 25;
    Tile snakeHead, food;
    java.util.List<Tile> snakeTail = new ArrayList<Tile>();

    // Game Logic
    Timer gameLoop;
    int velocityX, velocityY;

    SnakeGame(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(this.width, this.height));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        // Snake
        snakeHead = new Tile(5, 5);

        // Food
        food = new Tile(5, 5);
        placeFood();

        velocityX = 0;
        velocityY = 0;

        // Game Loop
        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
//        // Grid
//        for (int i = 0; i < width / tileSize; i++) {
//            g.setColor(Color.darkGray);
//            // for vertical lines
//            g.drawLine(i * tileSize, 0, i * tileSize, height);
//            // for horizontal lines
//            g.drawLine(0, i * tileSize, width, i * tileSize);
//        }

        // Snake
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for (Tile scale : snakeTail) {
            g.fill3DRect(scale.x * tileSize, scale.y * tileSize, tileSize, tileSize, true);
        }

        // Food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeTail.size()), tileSize - 16, tileSize );
        }
        else{
            g.setColor(Color.blue);
            g.drawString("Score: " + String.valueOf(snakeTail.size()), tileSize-16, tileSize);
        }


    }

    public void placeFood() {
        Random rand = new Random();
        food.x = rand.nextInt(width / tileSize);
        food.y = rand.nextInt(height / tileSize);
    }

    public void move() {
        // collision with itself
        for (Tile snakePart : snakeTail) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // collision with wall
        if (snakeHead.x == -1 || snakeHead.x == width/tileSize || snakeHead.y == -1 || snakeHead.y == height/tileSize) {
            gameOver = true;
        }

        // eat
        if (collision(snakeHead, food)) {
            snakeTail.add(new Tile(food.x, food.y));
            placeFood();
        }

        // and grow

        for (int i = snakeTail.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeTail.get(i);
            // if it's the first part of the tail
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeTail.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;


    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }


}


