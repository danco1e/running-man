import javax.swing.*;
import java.awt.*;

public class mantisJump extends JPanel {
    int mannyYpos = 290;
    int velocityY = -10;
    int gravity = 1;
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the ground
        g.setColor(new Color(80, 170, 80));//ground color
        g.fillRect(0, 350, 800, 100);

        // Temporary player placeholder
        g.setColor(Color.RED);//player color
        g.fillOval(150, mannyYpos, 40, 60);
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
    public void updateGame(){
        if(isJumping){
            velocityY = velocityY + gravity;//increases velocity for next
            mannyYpos = mannyYpos + velocityY;//changes position
            if(mannyYpos >= 290){
                mannyYpos = 290;
                velocityY = 0;
                isJumping = false;
            }
        }
    }
}
