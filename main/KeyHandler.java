package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    //DEBUG1 
    public boolean checkDrawTime = false;
    GamePanel gamePanel;

    //DEBUG2
    public boolean PlayerPos = false;

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_Q) {
            // Move character left
            leftPressed = true;
        } else if (keyCode == KeyEvent.VK_D) {
            // Move character right
            rightPressed = true;
        } else if (keyCode == KeyEvent.VK_Z) {
            // Move character up
            upPressed = true;
        } else if (keyCode == KeyEvent.VK_S) {
            // Move character down
            downPressed = true;

        }
        //DEBUG1
        else if (keyCode == KeyEvent.VK_T) {
            if (checkDrawTime == false) {
                checkDrawTime = true;
            }else {
                checkDrawTime = false;
            }
        }
        //DEBUG2
        else if (keyCode == KeyEvent.VK_A) {
            if (PlayerPos == false) {
                PlayerPos = true;
            }else {
                PlayerPos = false;
            }
        }

    }

    public void keyReleased(KeyEvent e) {
        // Stop character movement
        int keyCode = e.getKeyCode();
        
        if(keyCode == KeyEvent.VK_Q) {
            leftPressed = false;
        } else if(keyCode == KeyEvent.VK_D) {
            rightPressed = false;
        } else if(keyCode == KeyEvent.VK_Z) {
            upPressed = false;
        } else if(keyCode == KeyEvent.VK_S) {
            downPressed = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        // Not used
    }

}
