package entity;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class SkeletonKing extends Monster {

    public SkeletonKing(GamePanel gamePanel) {
        super(gamePanel);
        getMonsterImage();
    }

    @Override
    public void getMonsterImage() {
        // Load images for the Skeleton King
        int idleFrameCount = 6;
        int walkFrameCount = 10;
        int hurtFrameCount = 4;
        int deathFrameCount = 13;
        int attackFrameCount = 10; 
        idleUpFrames = new BufferedImage[idleFrameCount];
        walkUpFrames = new BufferedImage[walkFrameCount];
        hurtUpFrames = new BufferedImage[hurtFrameCount];
        deathUpFrames = new BufferedImage[deathFrameCount];
        attackUpFrames = new BufferedImage[attackFrameCount];
        idleDownFrames = new BufferedImage[idleFrameCount];
        walkDownFrames = new BufferedImage[walkFrameCount];
        hurtDownFrames = new BufferedImage[hurtFrameCount];
        deathDownFrames = new BufferedImage[deathFrameCount];
        attackDownFrames = new BufferedImage[attackFrameCount];
        idleLeftFrames = new BufferedImage[idleFrameCount];
        walkLeftFrames = new BufferedImage[walkFrameCount];
        hurtLeftFrames = new BufferedImage[hurtFrameCount];
        deathLeftFrames = new BufferedImage[deathFrameCount];
        attackLeftFrames = new BufferedImage[attackFrameCount];
        idleRightFrames = new BufferedImage[idleFrameCount];
        walkRightFrames = new BufferedImage[walkFrameCount];
        hurtRightFrames = new BufferedImage[hurtFrameCount];
        deathRightFrames = new BufferedImage[deathFrameCount];
        attackRightFrames = new BufferedImage[attackFrameCount];

        // Load the images from the resources
        // Load the images from the resources
        loadFrames(idleUpFrames, "/res/monsters/skeleton_king/Up/SkeletonKingUpIdle.png", idleFrameCount);
        loadFrames(walkUpFrames, "/res/monsters/skeleton_king/Up/SkeletonKingUpWalk.png", walkFrameCount);
        loadFrames(hurtUpFrames, "/res/monsters/skeleton_king/Up/SkeletonKingUpHurt.png", hurtFrameCount);
        loadFrames(deathUpFrames, "/res/monsters/skeleton_king/Up/SkeletonKingUpDeath.png", deathFrameCount);
        loadFrames(idleDownFrames, "/res/monsters/skeleton_king/Down/SkeletonKingDownIdle.png", idleFrameCount);
        loadFrames(walkDownFrames, "/res/monsters/skeleton_king/Down/SkeletonKingDownWalk.png", walkFrameCount);
        loadFrames(hurtDownFrames, "/res/monsters/skeleton_king/Down/SkeletonKingDownHurt.png", hurtFrameCount);
        loadFrames(deathDownFrames, "/res/monsters/skeleton_king/Down/SkeletonKingDownDeath.png", deathFrameCount);
        loadFrames(idleLeftFrames, "/res/monsters/skeleton_king/Left/SkeletonKingLeftIdle.png", idleFrameCount);
        loadFrames(walkLeftFrames, "/res/monsters/skeleton_king/Left/SkeletonKingLeftWalk.png", walkFrameCount);
        loadFrames(hurtLeftFrames, "/res/monsters/skeleton_king/Left/SkeletonKingLeftHurt.png", hurtFrameCount);
        loadFrames(deathLeftFrames, "/res/monsters/skeleton_king/Left/SkeletonKingLeftDeath.png", deathFrameCount);
        loadFrames(idleRightFrames, "/res/monsters/skeleton_king/Right/SkeletonKingRightIdle.png", idleFrameCount);
        loadFrames(walkRightFrames, "/res/monsters/skeleton_king/Right/SkeletonKingRightWalk.png", walkFrameCount);
        loadFrames(hurtRightFrames, "/res/monsters/skeleton_king/Right/SkeletonKingRightHurt.png", hurtFrameCount);
        loadFrames(deathRightFrames, "/res/monsters/skeleton_king/Right/SkeletonKingRightDeath.png", deathFrameCount);
        loadFrames(attackDownFrames, "/res/monsters/skeleton_king/Down/SkeletonKingDownAttack01.png", attackFrameCount);
        loadFrames(attackUpFrames, "/res/monsters/skeleton_king/Up/SkeletonKingUpAttack01.png", attackFrameCount);
        loadFrames(attackLeftFrames, "/res/monsters/skeleton_king/Left/SkeletonKingLeftAttack01.png", attackFrameCount);
        loadFrames(attackRightFrames, "/res/monsters/skeleton_king/Right/SkeletonKingRightAttack01.png", attackFrameCount);
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
