import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JFrame implements ActionListener, KeyListener {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int BIRD_SIZE = 20;
    private final int GRAVITY = 1;

    private Timer timer;
    private int birdY;
    private int velocity;
    private ArrayList<int[]> pipes; // Store pipe positions and gaps
    private int pipeWidth;
    private int pipeGap;
    private int score;
    private boolean gameover;
    private Random rand;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(this);

        birdY = HEIGHT / 2;
        velocity = 0;
        pipes = new ArrayList<>();
        pipeWidth = 50;
        pipeGap = 200; // Reduced gap size
        score = 0;
        gameover = false;
        rand = new Random();

        pipes.add(new int[]{WIDTH, rand.nextInt(HEIGHT - pipeGap)});

        timer = new Timer(20, this);
        timer.start();

        setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.red);
        g.fillRect(WIDTH / 2 - BIRD_SIZE / 2, birdY - BIRD_SIZE / 2, BIRD_SIZE, BIRD_SIZE);

        for (int[] pipe : pipes) {
            int x = pipe[0];
            int gapY = pipe[1];
            g.fillRect(x, 0, pipeWidth, gapY);
            g.fillRect(x, gapY + pipeGap, pipeWidth, HEIGHT - gapY - pipeGap);
        }

        g.setColor(Color.black);
        g.drawString("Score: " + score, 10, 20);

        if (gameover) {
            g.drawString("Game Over! Press Space to restart.", WIDTH / 2 - 70, HEIGHT / 2);
            g.drawString("Final Score: " + score, WIDTH / 2 - 50, HEIGHT / 2 + 20);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameover) {
            birdY += velocity;
            velocity += GRAVITY;

            if (birdY < 0 || birdY > HEIGHT) {
                gameover = true;
            }

            for (int i = 0; i < pipes.size(); i++) {
                int[] pipe = pipes.get(i);
                int x = pipe[0];
                int gapY = pipe[1];
                x -= 2;
                pipes.set(i, new int[]{x, gapY});

                if (x == WIDTH / 2 - pipeWidth / 2) {
                    pipes.add(new int[]{WIDTH, rand.nextInt(HEIGHT - pipeGap)});
                }

                if (x + pipeWidth == WIDTH / 2 - BIRD_SIZE / 2) {
                    score++;
                }

                if (x < WIDTH / 2 + BIRD_SIZE / 2 && x + pipeWidth > WIDTH / 2 - BIRD_SIZE / 2) {
                    if (birdY < gapY || birdY > gapY + pipeGap) {
                        gameover = true;
                    }
                }

                if (x + pipeWidth < 0) {
                    pipes.remove(i);
                    i--;
                }
            }

            // Add new pipes at regular intervals
            if (pipes.size() < 5 && pipes.get(pipes.size() - 1)[0] < WIDTH - 200) {
                pipes.add(new int[]{WIDTH, rand.nextInt(HEIGHT - pipeGap)});
            }
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameover) {
                velocity = -15;
            } else {
                birdY = HEIGHT / 2;
                velocity = 0;
                pipes.clear();
                pipes.add(new int[]{WIDTH, rand.nextInt(HEIGHT - pipeGap)});
                score = 0;
                gameover = false;
            }
        }
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}