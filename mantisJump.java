import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class mantisJump extends JPanel {
    boolean damageable = true;
    int health = 3;
    int score = 0;
    int mannyXpos = 150;
    int mannyYpos = 230;
    int velocityY = -10;
    int gravity = 1;
    int enemyXpos = 800;
    int enemyYpos = 210;
    int spriteNum = 0;
    BufferedImage playerSprite;
    BufferedImage playerSprite2;
    BufferedImage playerSprite3;
    BufferedImage enemySprite;
    boolean isJumping = false;
    public mantisJump() {
        setPreferredSize(new Dimension(800, 450));//size of screen
        setBackground(new Color(135, 206, 235));//background color
        Timer timer = new Timer(16, e -> {//16ms delay for 60fps
            updateGame();//calls updateGame
            repaint();//jumps
        });
        timer.start();//starts timer
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jump");
        getActionMap().put("jump", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
        Jump();
        }
            });

        try {
            if(isJumping == false){
            playerSprite = ImageIO.read(new File("sprites/runningman1.png"));
            playerSprite2 = ImageIO.read(new File("sprites/runningman2.png"));
            playerSprite3 = ImageIO.read(new File("sprites/inkling.png"));//still need to make jumping image
            enemySprite = ImageIO.read(new File("sprites/ironmouse.png"));
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

        g.setColor(Color.GREEN);//enemy color placeholder
        g.fillOval(enemyXpos, enemyYpos, 40, 60);

        g.drawImage(enemySprite, enemyXpos, enemyYpos, 192, 192, this);

        g.setColor(Color.PINK);//health color
        g.drawString("Health: " + health,50,80);
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

    public void Jump(){
        if(isJumping == false){//checks if jumping
            isJumping = true;//sets isJumping to true
            velocityY = -20;//sets velocity
        }
    }
    public void Collision(){
        //code for collision check, damages player if collision occurs
        Rectangle manny = new Rectangle(mannyXpos,mannyYpos,32,32);
        Rectangle enemy = new Rectangle(enemyXpos,enemyYpos,32,32);
        boolean intersect = manny.intersects(enemy);
        if(intersect){
            if(damageable){
            health = health - 1;//health minus one
            damageable = false;
            Timer timer2 = new Timer(2000, e -> {//2 second cooldown
            damageable = true;
        });
        timer2.setRepeats(false);//stops repeats
        timer2.start();//starts timer
            }
        }
    }
    public void updateGame(){
        Collision();
        if(isJumping){
            spriteNum = 3;
            enemyXpos = enemyXpos - 10;
            velocityY = velocityY + gravity;//increases velocity for next
            mannyYpos = mannyYpos + velocityY;//changes position
            if(mannyYpos >= 230){
                mannyYpos = 230;
                velocityY = 0;
                isJumping = false;
            }
            if(mannyXpos >= enemyXpos){
                if(mannyYpos > enemyXpos){
                    score = score + 10;//score plus 10 when jump over enemy
                }
            }
        }
        else{
        enemyXpos = enemyXpos - 10;
        }
        if(enemyXpos <= -10){
            enemyXpos = 800;
        }

    }
}
