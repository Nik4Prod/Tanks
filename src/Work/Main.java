package Work;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {
    char[][] objects = {
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'},
            {'B', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'B'},
            {'B', 'G', 'G', 'G', 'G', 'G', 'G', 'B', 'B'},
            {'B', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'B'},
            {'B', 'G', 'G', 'B', 'B', 'G', 'G', 'B', 'B'},
            {'B', 'G', 'G', 'G', 'B', 'G', 'G', 'G', 'B'},
            {'B', 'G', 'B', 'W', 'W', 'G', 'G', 'G', 'B'},
            {'B', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'B'},
            {'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B'}
    };

    final boolean PlAY = true;
    final boolean STOP = false;
    final int OBJECT_SIZE = 64;
    final int PATTERN_SIZE = 9;
    final int WindowSize = OBJECT_SIZE * PATTERN_SIZE;
    final Color back = new Color(66, 101, 52);
    public int direction;
    final int UP = 1;
    final int DOWN = 2;
    final int LEFT = 3;
    final int RIGHT = 4;
    public int tankX, tankY;
    int speed = 10;
    public int bulletX = -10, bulletY = -10;

    //*****************************************************************************************

    boolean cantMove() {
        boolean rule1 = (direction == UP && tankY == 0);
        boolean rule2 = (direction == DOWN && tankY == WindowSize - OBJECT_SIZE);
        boolean rule3 = (direction == LEFT && tankX == 0);
        boolean rule4 = (direction == RIGHT && tankX == WindowSize - OBJECT_SIZE);
        boolean rule5 = (nextObject() != 'G');


        return rule1 || rule2 || rule3 || rule4 || rule5;
    }

    void move(int direction) throws Exception {
        this.direction = direction;
        if (cantMove()) return;
        switch (direction) {
            case UP -> {
                tankY--;
                Thread.sleep(speed);
                repaint();
            }
            case DOWN -> {
                tankY++;
                Thread.sleep(speed);
                repaint();
            }
            case LEFT -> {
                tankX--;
                Thread.sleep(speed);
                repaint();
            }
            case RIGHT -> {
                tankX++;
                Thread.sleep(speed);
                repaint();
            }
        }

    }

    char nextObject() {
        int mX = (tankX) / OBJECT_SIZE;
        int mY = (tankY) / OBJECT_SIZE;
        if (mY == -1 || mX == -1)
            return 'B';
        else
            return objects[mY][mX];
    }

    boolean ifObstacle() {
        try {
            int y = bulletY / OBJECT_SIZE;
            int x = bulletX / OBJECT_SIZE;
            if (objects[y][x] == 'B') {
                objects[y][x] = 'G';
                return true;
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }

    }

    void fire() throws InterruptedException {

        bulletY = tankY + OBJECT_SIZE / 3;
        bulletX = tankX + OBJECT_SIZE / 3;

        if (direction == UP || direction == DOWN) {
            while (bulletY < WindowSize) {
                switch (direction) {
                    case UP -> {
                        bulletY--;
                        Thread.sleep(2);

                        repaint();
                    }
                    case DOWN -> {
                        bulletY++;
                        Thread.sleep(2);
                        repaint();
                    }
                }
                if (ifObstacle()) {
                    destroyBullet();
                    break;
                }
            }
        }
        if (direction == LEFT || direction == RIGHT) {
            while (bulletX < WindowSize) {
                switch (direction) {
                    case LEFT -> {
                        bulletX--;
                        Thread.sleep(2);
                        repaint();
                    }
                    case RIGHT -> {
                        bulletX++;
                        Thread.sleep(2);
                        repaint();
                    }
                }
                if (ifObstacle()) {
                    destroyBullet();
                    break;
                }
            }
        }
    }

    void destroyBullet() {
        bulletX = -100;
        bulletY = -100;
        repaint();
    }

    //***********************/Work.Main/**********************************************************

    void runTheGame() throws Exception {

        tankX = OBJECT_SIZE * 3;
        tankY = OBJECT_SIZE * 5;
        direction = RIGHT;
        fire();
        fire();
        //move(UP);
        for (int i = 0; i < 576; i++) {
            move(direction);
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();

        main.runTheGame();

    }

    Main() {
        JFrame frame = new JFrame("Tanks");
        frame.setMinimumSize(new Dimension(WindowSize + 15, WindowSize + 40));

        frame.getContentPane().add(this);
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    //**************************/DRAW/****************************************************

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //background
        for (int i = 0; i < PATTERN_SIZE; i++) {
            for (int j = 0; j < PATTERN_SIZE; j++) {
                switch (objects[j][i]) {
                    case 'B':
                        g.setColor(new Color(66, 6, 15));
                        g.fillRect(i * OBJECT_SIZE, j * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
                        break;
                    case 'G':
                        g.setColor(back);
                        g.fillRect(i * OBJECT_SIZE, j * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
                        break;
                    case 'W':
                        g.setColor(new Color(14, 163, 220, 255));
                        g.fillRect(i * OBJECT_SIZE, j * OBJECT_SIZE, OBJECT_SIZE, OBJECT_SIZE);
                        break;
                }
            }
        }


        //Tank paint
        g.setColor(Color.RED);
        g.fillRect(tankX, tankY, OBJECT_SIZE - 1, OBJECT_SIZE - 1);
        switch (direction) {
            case UP:
                // Base
                g.setColor(back);
                g.fillRect(tankX + OBJECT_SIZE / 4, tankY, OBJECT_SIZE / 2, OBJECT_SIZE / 3);
                g.fillRect(tankX + OBJECT_SIZE / 4, tankY + 53, OBJECT_SIZE / 2, OBJECT_SIZE / 6);
                //Barrel
                g.setColor(Color.GREEN);
                g.fillRect(tankX + 28, tankY, 7, 25);
                //Tower
                g.setColor(Color.PINK);
                g.fillRect(tankX + 20, tankY + 25, 23, 23);
                break;
            case DOWN:
                // Base
                g.setColor(back);
                g.fillRect(tankX + 15, tankY + 43, 33, 20);
                g.fillRect(tankX + 15, tankY, 33, 10);
                //Barrel
                g.setColor(Color.GREEN);
                g.fillRect(tankX + 28, tankY + 38, 7, 25);
                //Tower
                g.setColor(Color.PINK);
                g.fillRect(tankX + 20, tankY + OBJECT_SIZE / 4, 23, 23);
                break;
            case LEFT:
                // Base
                g.setColor(back);
                g.fillRect(tankX, tankY + OBJECT_SIZE / 4, 20, 33);
                g.fillRect(tankX + 53, tankY + 15, 10, 33);
                //Barrel
                g.setColor(Color.GREEN);
                g.fillRect(tankX, tankY + 28, 25, 7);
                //Tower
                g.setColor(Color.PINK);
                g.fillRect(tankX + 25, tankY + 20, 23, 23);
                break;
            case RIGHT:
                // Base
                g.setColor(back);
                g.fillRect(tankX + 43, tankY + OBJECT_SIZE / 4, 20, 33);
                g.fillRect(tankX, tankY + OBJECT_SIZE / 4, 10, 33);
                //Barrel
                g.setColor(Color.GREEN);
                g.fillRect(tankX + 28, tankY + 28, 35, 7);
                //Tower
                g.setColor(Color.PINK);
                g.fillRect(tankX + OBJECT_SIZE / 4, tankY + 20, 23, 23);
                break;
            default:
                g.setColor(back);
                g.fillRect(tankX + OBJECT_SIZE / 4, tankY, 33, 20);
                g.fillRect(tankX + OBJECT_SIZE / 4, tankY + 53, 33, 10);
                //Barrel
                g.setColor(Color.GREEN);
                g.fillRect(tankX + 28, tankY, 7, 25);
                //Tower
                g.setColor(Color.PINK);
                g.fillRect(tankX + 20, tankY + 25, 23, 24);


        }

        //Bullet
        g.setColor(Color.YELLOW);
        g.fillRect(bulletX, bulletY, 6, 6);


    }
}
