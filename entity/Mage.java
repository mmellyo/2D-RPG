package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Mage extends Player {


    public Mage(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel, keyHandler);
    }

    @Override
    public void getPlayerImage() {
        // Load Sorceress images
        
        try {
            // Load down frames
            int idleFrameCount = 6;
            int walkFrameCount = 6;
            int attackFrameCount = 5;
            int attack3FrameCount = 7;

            idleDownFrames = new BufferedImage[idleFrameCount];
            walkDownFrames = new BufferedImage[walkFrameCount];
            attackDownFrames = new BufferedImage[attackFrameCount];
            attack3DownFrames = new BufferedImage[attack3FrameCount];

            loadFrames(idleDownFrames, "/res/player/sorceress/Down/SorceressDownIdle.png", idleFrameCount);
            loadFrames(walkDownFrames, "/res/player/sorceress/Down/SorceressDownRun.png", walkFrameCount);
            loadFrames(attackDownFrames, "/res/player/sorceress/Down/SorceressDownAttack01.png", attackFrameCount);
            loadFrames(attack3DownFrames, "/res/player/sorceress/Down/SorceressDownAttack03.png", attack3FrameCount);

            // Load up frames
            idleUpFrames = new BufferedImage[idleFrameCount];
            walkUpFrames = new BufferedImage[walkFrameCount];
            attackUpFrames = new BufferedImage[attackFrameCount];
            attack3UpFrames = new BufferedImage[attack3FrameCount];

            loadFrames(idleUpFrames, "/res/player/sorceress/Up/SorceressUpIdle.png", idleFrameCount);
            loadFrames(walkUpFrames, "/res/player/sorceress/Up/SorceressUpRun.png", walkFrameCount);
            loadFrames(attackUpFrames, "/res/player/sorceress/Up/SorceressUpAttack01.png", attackFrameCount);
            loadFrames(attack3UpFrames, "/res/player/sorceress/Up/SorceressUpAttack03.png", attack3FrameCount);

            // Load left frames
            idleLeftFrames = new BufferedImage[idleFrameCount];
            walkLeftFrames = new BufferedImage[walkFrameCount];
            attackLeftFrames = new BufferedImage[attackFrameCount];
            attack3LeftFrames = new BufferedImage[attack3FrameCount];

            loadFrames(idleLeftFrames, "/res/player/sorceress/Left/SorceressLeftIdle.png", idleFrameCount);
            loadFrames(walkLeftFrames, "/res/player/sorceress/Left/SorceressLeftRun.png", walkFrameCount);
            loadFrames(attackLeftFrames, "/res/player/sorceress/Left/SorceressLeftAttack01.png", attackFrameCount);
            loadFrames(attack3LeftFrames, "/res/player/sorceress/Left/SorceressLeftAttack03.png", attack3FrameCount);

            // Load right frames
            idleRightFrames = new BufferedImage[idleFrameCount];
            walkRightFrames = new BufferedImage[walkFrameCount];
            attackRightFrames = new BufferedImage[attackFrameCount];
            attack3RightFrames = new BufferedImage[attack3FrameCount];

            loadFrames(idleRightFrames, "/res/player/Sorceress/Right/SorceressRightIdle.png", idleFrameCount);
            loadFrames(walkRightFrames, "/res/player/Sorceress/Right/SorceressRightRun.png", walkFrameCount);
            loadFrames(attackRightFrames, "/res/player/Sorceress/Right/SorceressRightAttack01.png", attackFrameCount);
            loadFrames(attack3RightFrames, "/res/player/Sorceress/Right/SorceressRightAttack03.png", attack3FrameCount);

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