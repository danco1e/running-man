import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.sound.sampled.*;

public class runningMan extends JPanel {
    boolean gameOverSoundPlayed = false;
    boolean ongoing = true;
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
    BufferedImage playerSprite;//sprite images
    BufferedImage playerSprite2;
    BufferedImage playerSprite3;
    BufferedImage enemySprite;
    BufferedImage enemySprite2;
    BufferedImage healthSprite;
    BufferedImage gameOverScreen;
    BufferedImage winCon;
    BufferedImage flag;
    BufferedImage floor;
    BufferedImage wall;
    BufferedImage gameWonScreen;
    boolean isJumping = false;
    boolean gameOver = false;
    boolean dashed = false;
    boolean gameWon = false;
    Clip runningSound;
    Clip bgmSound;
    Clip endSound;
    public runningMan() {
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
            gameWonScreen = ImageIO.read(new File("sprites/gameWon.png"));
            floor = ImageIO.read(new File("sprites/floor.png"));
            flag = ImageIO.read(new File("sprites/flag.png"));
            wall = ImageIO.read(new File("sprites/wall.png"));
            AudioInputStream audio = AudioSystem.getAudioInputStream(
            new File("sounds/Mutant.wav"));
            runningSound = AudioSystem.getClip();
            runningSound.open(audio);
            }
            AudioInputStream bgmAudio =
            AudioSystem.getAudioInputStream(new File("sounds/BGMFinal.wav"));
            bgmSound = AudioSystem.getClip();
            bgmSound.open(bgmAudio);
            AudioInputStream gameOverAudio =
            AudioSystem.getAudioInputStream(new File("sounds/gameOver.wav"));
            endSound = AudioSystem.getClip();
            endSound.open(gameOverAudio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the ground
        g.drawImage(floor, 0, 350,800,100, this);
        g.drawImage(wall,0, 0, 800, 350,this);
        g.drawImage(flag, 700, 230, 128,128,this);
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
            stopRunningSound();
            stopBGM();
            if(!gameOverSoundPlayed){
            playGameOverSound();
            gameOverSoundPlayed = true;
            }
            ongoing = false;
            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,450);

            g.drawImage(gameOverScreen, 200, 50, 400, 300, this);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            g.drawString("Reach the end!", 250, 350);

            return;
        }
        if(gameWon){
            stopRunningSound();
            stopBGM();
            damageable = false;
            ongoing = false;
            g.drawImage(gameWonScreen, 0, 0, 800, 450, this);
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Mantis Jump");

        runningMan game = new runningMan();

        window.add(game);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        game.startRunningSound();
        game.startBGM();
    }

    public void Jump(){//smaller jump
        if(isJumping == false && ongoing == true){//checks if jumping
            isJumping = true;//sets isJumping to true
            velocityY = -15;//sets velocity
            stopRunningSound();
            playSound("sounds/Jump11.wav");
        }
    }
    public void Jump2(){//bigger jump
        if(isJumping == false && ongoing == true){//checks if jumping
            isJumping = true;//sets isJumping to true
            velocityY = -19;//sets velocity
            stopRunningSound();
            playSound("sounds/Jump30.wav");
        }
    }
    public void dash(){//dashs forward/teleport
        playSound("sounds/airdash8.wav");
        if(ongoing == true){
        if(mannyYpos < enemyYpos - 30){
        damageable = false;
        }
        mannyXpos = mannyXpos + 70;
        dashed = true;
        Timer dashTimer = new Timer(3000, e -> {
            dashed = false;
        });
        Timer shieldTimer = new Timer(1000, e -> {
            damageable = true;
        });
        dashTimer.start();
        dashTimer.setRepeats(false);
        shieldTimer.start();
        shieldTimer.setRepeats(false);
    }
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
        if(intersect && ongoing == true){//collision for enemy1
            if(damageable){
            health = health - 1;//health minus one
            playSound("sounds/Hit3.wav");
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
        if(intersect2 && ongoing == true){//collision for enemy2
            if(damageable){
            health = health - 1;//health minus one
            playSound("sounds/Hit3.wav");
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
        if(intersect3 && ongoing == true){
            gameWon = true;
        }
    }
    public void playSound(String filePath) {
    try {
        AudioInputStream audio = AudioSystem.getAudioInputStream(
            new File(filePath)
        );

        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();

    } catch (Exception e) {
        e.printStackTrace();
    }
    
}
    public void playGameOverSound() {//plays game over sound when game over
        if (endSound != null) {
            endSound.setFramePosition(0);
            endSound.start();
        }
    }
    public void startRunningSound() {//starts player running sound
    if (runningSound != null && !runningSound.isRunning()) {
        runningSound.setFramePosition(0);
        runningSound.loop(Clip.LOOP_CONTINUOUSLY);
    }
}

    public void stopRunningSound() {//stops player running sound
        if (runningSound != null && runningSound.isRunning()) {
            runningSound.stop();
        }
    }
    public void startBGM() {//starts background music on loop
    if (bgmSound != null) {
        bgmSound.stop();
        bgmSound.setFramePosition(0);
        bgmSound.loop(Clip.LOOP_CONTINUOUSLY);
    }
}

public void stopBGM() {//stops background music
    if (bgmSound != null && bgmSound.isRunning()) {
        bgmSound.stop();
    }
}
    public void updateGame(){
        Collision();
        if(isJumping){
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
            spriteNum = 3;
            velocityY = velocityY + gravity;//increases velocity for next
            mannyYpos = mannyYpos + velocityY;//changes position
            if(mannyYpos >= 230){
                mannyYpos = 230;
                velocityY = 0;
                isJumping = false;
                startRunningSound();
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
