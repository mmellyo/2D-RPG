
package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;

public class AIMage extends Mage {
    int attackCooldown = 60; // Cooldown duration in frames (1 second at 60 FPS)
    int attackTimer = 0;
    int attackDamage = 10; // Damage dealt by the mage's attack
    int manaCost = 10; // Mana cost for each attack

    boolean aggro = false;
    int aggroRange = 2 * gamePanel.tileSize; // Aggro range in pixels
    int aggroDuration = 180; // Aggro duration in frames (3 seconds at 60 FPS)
    int aggroTimer = 0;

    public AIMage(GamePanel gamePanel) {
        super(gamePanel);
        setDefaultValues();
        getMageImage();
    }

    @Override
    public void getMageImage() {
        // Load Mage images
        try {
            // Load down frames
            int idleFrameCount = 6;
            int walkFrameCount = 6;
            int attackFrameCount = 5;
            int abilityFrameCount = 7;  //ability02
            int hurtFrameCount = 4;
            int deathFrameCount = 7;

            idleDownFrames = new BufferedImage[idleFrameCount];
            walkDownFrames = new BufferedImage[walkFrameCount];
            attackDownFrames = new BufferedImage[attackFrameCount];
            abilityDownFrames = new BufferedImage[abilityFrameCount];
            hurtDownFrames = new BufferedImage[hurtFrameCount];
            deathDownFrames = new BufferedImage[deathFrameCount];

            loadFrames(idleDownFrames, "/res/player/sorceress/Down/SorceressDownIdle.png", idleFrameCount);
            loadFrames(walkDownFrames, "/res/player/sorceress/Down/SorceressDownRun.png", walkFrameCount);
            loadFrames(attackDownFrames, "/res/player/sorceress/Down/SorceressDownAttack01.png", attackFrameCount);
            loadFrames(abilityDownFrames, "/res/player/sorceress/Down/SorceressDownAttack02.png", abilityFrameCount);
            loadFrames(hurtDownFrames, "/res/player/sorceress/Down/SorceressDownHurt.png", hurtFrameCount);
            loadFrames(deathDownFrames, "/res/player/sorceress/Down/SorceressDownDeath.png", deathFrameCount);

            // Load up frames
            idleUpFrames = new BufferedImage[idleFrameCount];
            walkUpFrames = new BufferedImage[walkFrameCount];
            attackUpFrames = new BufferedImage[attackFrameCount];
            abilityUpFrames = new BufferedImage[abilityFrameCount];
            hurtUpFrames = new BufferedImage[hurtFrameCount];
            deathUpFrames = new BufferedImage[deathFrameCount];

            loadFrames(idleUpFrames, "/res/player/sorceress/Up/SorceressUpIdle.png", idleFrameCount);
            loadFrames(walkUpFrames, "/res/player/sorceress/Up/SorceressUpRun.png", walkFrameCount);
            loadFrames(attackUpFrames, "/res/player/sorceress/Up/SorceressUpAttack01.png", attackFrameCount);
            loadFrames(abilityUpFrames, "/res/player/sorceress/Up/SorceressUpAttack02.png", abilityFrameCount);
            loadFrames(hurtUpFrames, "/res/player/sorceress/Up/SorceressUpHurt.png", hurtFrameCount);
            loadFrames(deathUpFrames, "/res/player/sorceress/Up/SorceressUpDeath.png", deathFrameCount);

            // Load left frames
            idleLeftFrames = new BufferedImage[idleFrameCount];
            walkLeftFrames = new BufferedImage[walkFrameCount];
            attackLeftFrames = new BufferedImage[attackFrameCount];
            abilityLeftFrames = new BufferedImage[abilityFrameCount];
            hurtLeftFrames = new BufferedImage[hurtFrameCount];
            deathLeftFrames = new BufferedImage[deathFrameCount];

            loadFrames(idleLeftFrames, "/res/player/sorceress/Left/SorceressLeftIdle.png", idleFrameCount);
            loadFrames(walkLeftFrames, "/res/player/sorceress/Left/SorceressLeftRun.png", walkFrameCount);
            loadFrames(attackLeftFrames, "/res/player/sorceress/Left/SorceressLeftAttack01.png", attackFrameCount);
            loadFrames(abilityLeftFrames, "/res/player/sorceress/left/SorceressLeftAttack02.png", abilityFrameCount);
            loadFrames(hurtLeftFrames, "/res/player/sorceress/left/SorceressLeftHurt.png", hurtFrameCount);
            loadFrames(deathLeftFrames, "/res/player/sorceress/Left/SorceressLeftDeath.png", deathFrameCount);

            // Load right frames
            idleRightFrames = new BufferedImage[idleFrameCount];
            walkRightFrames = new BufferedImage[walkFrameCount];
            attackRightFrames = new BufferedImage[attackFrameCount];
            abilityRightFrames = new BufferedImage[abilityFrameCount];
            hurtRightFrames = new BufferedImage[hurtFrameCount];
            deathRightFrames = new BufferedImage[deathFrameCount];

            loadFrames(idleRightFrames, "/res/player/sorceress/Right/SorceressRightIdle.png", idleFrameCount);
            loadFrames(walkRightFrames, "/res/player/sorceress/Right/SorceressRightRun.png", walkFrameCount);
            loadFrames(attackRightFrames, "/res/player/sorceress/Right/SorceressRightAttack01.png", attackFrameCount);
            loadFrames(abilityRightFrames, "/res/player/sorceress/Right/SorceressRightAttack02.png", abilityFrameCount);
            loadFrames(hurtRightFrames, "/res/player/sorceress/Right/SorceressRightHurt.png", hurtFrameCount);
            loadFrames(deathRightFrames, "/res/player/sorceress/Right/SorceressRightDeath.png", deathFrameCount);

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
                System.out.println("Loaded frame " + i + " from " + path);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
