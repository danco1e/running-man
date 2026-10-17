import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class mantisJump extends JPanel {
    boolean damageable = true;
    int health = 3;
    int mannyXpos = 150;
    int mannyYpos = 230;
    int velocityY = -10;
    int gravity = 1;
    int enemyXpos = 800;
    int enemyYpos = 260;
    int enemyXpos2 = 800;
    int enemyYpos2 = 80;
    int spriteNum = 0;
    int enemySpeed = (int) (Math.random() * 3) + 1;
    int enemySpeed2 = (int) (Math.random() * 3) + 1;
    BufferedImage playerSprite;
    BufferedImage playerSprite2;
    BufferedImage playerSprite3;
    BufferedImage enemySprite;
    BufferedImage enemySprite2;
    BufferedImage healthSprite;
    BufferedImage gameOverScreen;
    BufferedImage winCon;
    boolean isJumping = false;
    boolean gameOver = false;
    boolean dashed = false;
    boolean gameWon = false;
    public mantisJump() {
        setPreferredSize(new Dimension(800, 450));//size of screen
        setBackground(new Color(135, 206, 235));//background color
        Timer timer = new Timer(16, e -> {//16ms delay for 60fps
            updateGame();//calls updateGame
            repaint();//jumps
        });
        timer.start();//starts timer
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Z"), "jump");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("X"), "jump2");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("C"), "dash");
        getActionMap().put("jump", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        Jump();
        } 
        });
        getActionMap().put("jump2", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        Jump2();
        }
        });
        getActionMap().put("dash", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        if(dashed == false){
        dash();
        }
        }
        });

        try {
            if(isJumping == false){
            playerSprite = ImageIO.read(new File("sprites/runningman1.png"));
            playerSprite2 = ImageIO.read(new File("sprites/runningman2.png"));
            playerSprite3 = ImageIO.read(new File("sprites/jumpingman4.png"));
            enemySprite = ImageIO.read(new File("sprites/snail.png"));
            enemySprite2 = ImageIO.read(new File("sprites/burt3.png"));
            healthSprite = ImageIO.read(new File("sprites/heart.png"));
            gameOverScreen = ImageIO.read(new File("sprites/gameOverImg.png"));
            winCon = ImageIO.read(new File("sprites/ironmouse.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the ground
        g.setColor(new Color(80, 170, 80));//ground color
        g.fillRect(0, 350, 800, 100);
        g.drawImage(winCon, 700, 270, 128,128,this);
        g.drawImage(enemySprite, enemyXpos, enemyYpos, 128, 128, this);
        g.drawImage(enemySprite2, enemyXpos2, enemyYpos2,128, 128, this);

        if(health == 3){
        g.drawImage(healthSprite,0,0,96,96, this);
        g.drawImage(healthSprite,60,0,96,96,this);
        g.drawImage(healthSprite,120,0,96,96,this);
        }
        else if(health == 2){
        g.drawImage(healthSprite,0,0,96,96, this);
        g.drawImage(healthSprite,60,0,96,96,this);
        }
        else if(health == 1){
        g.drawImage(healthSprite,0,0,96,96, this);
        }
        
        if(spriteNum == 0){
        g.drawImage(playerSprite, mannyXpos, mannyYpos, 128,128, this);
        spriteNum = 1;
        }
        else if(spriteNum ==  1){
            g.drawImage(playerSprite2, mannyXpos, mannyYpos, 128, 128, this);
            spriteNum = 0;
        }
        else{
            g.drawImage(playerSprite3, mannyXpos, mannyYpos, 128, 128, this);
            spriteNum = 0;
        }

        if(gameOver){

            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,450);

            g.drawImage(gameOverScreen, 200, 50, 400, 300, this);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            g.drawString("Reach the end!", 250, 350);

            return;
        }
        if(gameWon){
            damageable = false;
            g.setColor(Color.WHITE);
            g.fillRect(0,0,800,450);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            g.drawString("You Win!", 250, 350);
        }
    }

    public void drawImage(){

    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Mantis Jump");

        mantisJump game = new mantisJump();

        window.add(game);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    public void Jump(){//smaller jump
        if(isJumping == false){//checks if jumping
            isJumping = true;//sets isJumping to true
            velocityY = -15;//sets velocity
        }
    }
    public void Jump2(){//bigger jump
        if(isJumping == false){//checks if jumping
            isJumping = true;//sets isJumping to true
            velocityY = -19;//sets velocity
        }
    }
    public void dash(){//dashs forward/teleport
        if(mannyYpos < enemyYpos - 10){
        damageable = false;
        }
        mannyXpos = mannyXpos + 80;
        dashed = true;
        Timer dashTimer = new Timer(3000, e -> {
            dashed = false;
        });
        Timer shieldTimer = new Timer(3000, e -> {
            damageable = true;
        });
        dashTimer.start();
        dashTimer.setRepeats(false);
        shieldTimer.start();
        shieldTimer.setRepeats(false);
    }
    public void Collision(){
        //code for collision check, damages player if collision occurs
        Rectangle manny = new Rectangle(mannyXpos,mannyYpos,32,32);
        Rectangle enemy = new Rectangle(enemyXpos,enemyYpos,64,64);
        Rectangle enemy2 = new Rectangle(enemyXpos2,enemyYpos2,64,64);
        Rectangle goal = new Rectangle(700, 260, 128, 128);
        boolean intersect = manny.intersects(enemy);
        boolean intersect2 = manny.intersects(enemy2);
        boolean intersect3 = manny.intersects(goal);
        if(intersect){//collision for enemy1
            if(damageable){
            health = health - 1;//health minus one
            if(health <= 0){
                gameOver = true;
            }
            damageable = false;
            Timer timer2 = new Timer(1000, e -> {//2 second cooldown
            damageable = true;
        });
            timer2.setRepeats(false);//stops repeats
            timer2.start();//starts timer
            }
        }
        if(intersect2){//collision for enemy2
            if(damageable){
            health = health - 1;//health minus one
            if(health <= 0){
                gameOver = true;
            }
            damageable = false;
            Timer timer3 = new Timer(1000, e -> {//2 second cooldown
            damageable = true;
        });
            timer3.setRepeats(false);//stops repeats
            timer3.start();//starts timer
            }
        }
        if(intersect3){
            gameWon = true;
            damageable = false;
        }
    }
    public void updateGame(){
        Collision();
        if(isJumping){
            spriteNum = 3;
            enemyXpos = enemyXpos - 8;
            enemyXpos2 = enemyXpos2 - 10;
            velocityY = velocityY + gravity;//increases velocity for next
            mannyYpos = mannyYpos + velocityY;//changes position
            if(mannyYpos >= 230){
                mannyYpos = 230;
                velocityY = 0;
                isJumping = false;
            }
            if(mannyXpos >= enemyXpos){
                if(mannyYpos > enemyYpos){
                }
            }
        }
        else{
        if(enemySpeed == 1){
        enemyXpos = enemyXpos - 8;
        }
        else if(enemySpeed ==2){
        enemyXpos = enemyXpos - 9;
        }
        else{
        enemyXpos = enemyXpos - 13;
        }
        if(enemySpeed2 == 1){
        enemyXpos2 = enemyXpos2 - 11;
        }
        else if(enemySpeed2 == 2){
        enemyXpos2 = enemyXpos2 - 7;
        }
        else{
        enemyXpos2 = enemyXpos2 - 15;
        }
        }
        if(enemyXpos <= -10){
            enemyXpos = 800;
            enemySpeed = (int) (Math.random() * 3) + 1;
        }
        if(enemyXpos2 <= -10){
            enemyXpos2 = 800;
            enemySpeed2 = (int) (Math.random() * 3) + 1;
        }
    }
}
