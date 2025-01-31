package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Warrior extends Player {


    public Warrior(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel, keyHandler);
        getPlayerImage();
    }

    @Override
    public void getPlayerImage() {
        // Load warrior images
        
        try {
            // Load down frames
            int idleFrameCount = 5;
            int walkFrameCount = 8;
            int attackFrameCount = 6;
            int attack3FrameCount = 6;  ///actually attack02
            int deathFrameCount = 5;
            int hurtFrameCount = 4;

            //down
            idleDownFrames = new BufferedImage[idleFrameCount];
            walkDownFrames = new BufferedImage[walkFrameCount];
            attackDownFrames = new BufferedImage[attackFrameCount];
            attack3DownFrames = new BufferedImage[attack3FrameCount];
            deathDownFrames = new BufferedImage[deathFrameCount];
            hurtDownFrames = new BufferedImage[hurtFrameCount];


            loadFrames(idleDownFrames, "/res/player/warrior/Down/WarriorDownIdle.png", idleFrameCount);
            loadFrames(walkDownFrames, "/res/player/warrior/Down/WarriorDownWalk.png", walkFrameCount);
            loadFrames(attackDownFrames, "/res/player/warrior/Down/WarriorDownAttack01.png", attackFrameCount);
            loadFrames(attack3DownFrames, "/res/player/warrior/Down/WarriorDownAttack02.png", attack3FrameCount);
            loadFrames(deathDownFrames, "/res/player/warrior/Down/WarriorDownDeath.png", deathFrameCount);
            loadFrames(hurtDownFrames, "/res/player/warrior/Down/WarriorDownHurt.png", hurtFrameCount);

            //up
            idleUpFrames = new BufferedImage[idleFrameCount];
            walkUpFrames = new BufferedImage[walkFrameCount];
            attackUpFrames = new BufferedImage[attackFrameCount];
            attack3UpFrames = new BufferedImage[attack3FrameCount];
            deathUpFrames = new BufferedImage[deathFrameCount];
            hurtUpFrames = new BufferedImage[hurtFrameCount];
    
            loadFrames(idleUpFrames, "/res/player/warrior/Up/WarriorUpIdle.png", idleFrameCount);
            loadFrames(walkUpFrames, "/res/player/warrior/Up/WarriorUpWalk.png", walkFrameCount);
            loadFrames(attackUpFrames, "/res/player/warrior/Up/WarriorUpAttack01.png", attackFrameCount);
            loadFrames(attack3UpFrames, "/res/player/warrior/Up/WarriorUpAttack02.png", attack3FrameCount);
            loadFrames(deathUpFrames, "/res/player/warrior/Up/WarriorUpDeath.png", deathFrameCount);
            loadFrames(hurtUpFrames, "/res/player/warrior/Up/WarriorUpHurt.png", hurtFrameCount);
    
            //  left 
            idleLeftFrames = new BufferedImage[idleFrameCount];
            walkLeftFrames = new BufferedImage[walkFrameCount];
            attackLeftFrames = new BufferedImage[attackFrameCount];
            attack3LeftFrames = new BufferedImage[attack3FrameCount];
            deathLeftFrames = new BufferedImage[deathFrameCount];
            hurtLeftFrames = new BufferedImage[hurtFrameCount];
    
            loadFrames(idleLeftFrames, "/res/player/warrior/Left/WarriorLeftIdle.png", idleFrameCount);
            loadFrames(walkLeftFrames, "/res/player/warrior/Left/WarriorLeftWalk.png", walkFrameCount);
            loadFrames(attackLeftFrames, "/res/player/warrior/Left/WarriorLeftAttack01.png", attackFrameCount);
            loadFrames(attack3LeftFrames, "/res/player/warrior/Left/WarriorLeftAttack02.png", attack3FrameCount);
            loadFrames(deathLeftFrames, "/res/player/warrior/Left/WarriorLeftDeath.png", deathFrameCount);
            loadFrames(hurtLeftFrames, "/res/player/warrior/Left/WarriorLeftHurt.png", hurtFrameCount);
    
            //  right 
            idleRightFrames = new BufferedImage[idleFrameCount];
            walkRightFrames = new BufferedImage[walkFrameCount];
            attackRightFrames = new BufferedImage[attackFrameCount];
            attack3RightFrames = new BufferedImage[attack3FrameCount];
            deathRightFrames = new BufferedImage[deathFrameCount];
            hurtRightFrames = new BufferedImage[hurtFrameCount];
    
            loadFrames(idleRightFrames, "/res/player/warrior/Right/WarriorRightIdle.png", idleFrameCount);
            loadFrames(walkRightFrames, "/res/player/warrior/Right/WarriorRightWalk.png", walkFrameCount);
            loadFrames(attackRightFrames, "/res/player/warrior/Right/WarriorRightAttack01.png", attackFrameCount);
            loadFrames(attack3RightFrames, "/res/player/warrior/Right/WarriorRightAttack02.png", attack3FrameCount);
            loadFrames(deathRightFrames, "/res/player/warrior/Right/WarriorRightDeath.png", deathFrameCount);
            loadFrames(hurtRightFrames, "/res/player/warrior/Right/WarriorRightHurt.png", hurtFrameCount);
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFrames(BufferedImage[] frames, String path, int frameCount) {
        try {
            BufferedImage sheet = gamePanel.loadImage(path);
            int frameWidth = sheet.getWidth() / frameCount;
            int frameHeight = sheet.getHeight();
            for (int i = 0; i < frameCount; i++) {
                frames[i] = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}