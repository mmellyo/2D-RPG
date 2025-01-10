package main;

import entity.Warrior;
import entity.Entity;
import entity.Monster;
import entity.Player;
import entity.SkeletonKing;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // Map settings
    final int maxMapCol = 30;
    final int maxMapRow = 30;
    public Player getPlayer() {

        return player;

    }
    public int getMaxMapCol() {
        return maxMapCol;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxMapRow() {
        return maxMapRow;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public SkeletonKing getSkeletonKing() {
        return skeletonKing;
    }

    // FPS
    final int FPS = 60;
    int frames = 0;
    long lastTime = System.nanoTime();
    int fps = 0;

    Player player;
    SkeletonKing skeletonKing;
    Thread gameThread;
    KeyHandler keyHandler;

    BufferedImage potionImage;
    BufferedImage grassTile;
    BufferedImage waterTile;
    int[][] mapTileNum;
    int potionX, potionY;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        player = new Warrior(this, keyHandler);
        skeletonKing = new SkeletonKing(this);
        loadPotionImage();
        loadTileSet();
        generateRandomMap();
        placePotionRandomly();
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDraw = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            // Game loop code

            // Update character position
            update();

            // Draw screen with updated position
            repaint();

            // Calculate FPS
            long currentTime = System.nanoTime();
            frames++;
            if (currentTime - lastTime >= 1000000000) {
                fps = frames;
                frames = 0;
                lastTime = currentTime;
            }

            // Delay to keep FPS consistent
            double remainder = (nextDraw - System.nanoTime()) / 1000000;
            if (remainder > 0) {
                try {
                    Thread.sleep((long) remainder);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextDraw += drawInterval;
        }
    }

    public void update() {
        // Update game logic
        player.update();
        skeletonKing.update();

        // Prevent the player from crossing the borders
        if (player.x < 0)
            player.x = 0;
        if (player.y < 0)
            player.y = 0;
        if (player.x > maxMapCol * tileSize - tileSize)
            player.x = maxMapCol * tileSize - tileSize;
        if (player.y > maxMapRow * tileSize - tileSize)
            player.y = maxMapRow * tileSize - tileSize;

        // Check for interaction with mana potion
        if (checkPotionInteraction()) {
            player.updateMana(50); // Restore 50 mana points, max 100
            placePotionRandomly(); // Move the potion to a new random position
        }
        // Check for player attack on skeleton king
        if (!skeletonKing.dead) {
            if (player.isAttacking() && checkCollision(player, skeletonKing)  && !player.isAttackRegistered()) {
                skeletonKing.decreaseHp(10); // Decrease skeleton king's health by 10
                skeletonKing.hurt = true;
                player.setAttackRegistered(true); // Register the attack
                if (skeletonKing.getHp() <= 0) {
                    skeletonKing.dead = true;
                }
            }
            if (player.isAttacking3() && checkCollision(player, skeletonKing)  && !player.isAttackRegistered()) {
                skeletonKing.decreaseHp(30); // Decrease skeleton king's health by 30
                skeletonKing.hurt = true;
                player.setAttackRegistered(true); // Register the attack
                if (skeletonKing.getHp() <= 0) {
                    skeletonKing.dead = true;
                }
            }
           // player.setAttacking(false);
        }
    }

    public void loadPotionImage() {
        potionImage = loadImage("/res/icons/potions.png");
    }

    public void loadTileSet() {
        BufferedImage tileSet = loadImage("/res/tiles/natureTiles.png");
        int tileWidth = 32;
        int tileHeight = 32;
        grassTile = tileSet.getSubimage(1 * tileWidth, 3 * tileHeight, tileWidth, tileHeight);
        waterTile = tileSet.getSubimage(2 * tileWidth, 3 * tileHeight, tileWidth, tileHeight);
    }

    public void generateRandomMap() {
        Random rand = new Random();
        mapTileNum = new int[maxMapCol][maxMapRow];
        for (int col = 0; col < maxMapCol; col++) {
            for (int row = 0; row < maxMapRow; row++) {
                if (rand.nextInt(10) < 2) { // 20% chance to place water
                    mapTileNum[col][row] = 1; // Water tile
                } else {
                    mapTileNum[col][row] = 0; // Grass tile
                }
            }
        }
    }

    public void placePotionRandomly() {
        Random rand = new Random();
        potionX = rand.nextInt(maxMapCol) * tileSize;
        potionY = rand.nextInt(maxMapRow) * tileSize;
    }

    public boolean checkPotionInteraction() {
        int potionWidth = tileSize;
        int potionHeight = tileSize;

        return player.x < potionX + potionWidth && player.x + tileSize > potionX &&
                player.y < potionY + potionHeight && player.y + tileSize > potionY;
    }

    public boolean checkCollision(Entity entity1, Entity entity2) {
        int solidWidth = tileSize / 2;
    int solidHeight = tileSize * 3 / 4;

    int entity1Left = entity1.x + tileSize / 4;
    int entity1Right = entity1Left + solidWidth;
    int entity1Top = entity1.y + tileSize / 4;
    int entity1Bottom = entity1Top + solidHeight;

    int entity2Left = entity2.x + tileSize / 4;
    int entity2Right = entity2Left + solidWidth;
    int entity2Top = entity2.y + tileSize / 4;
    int entity2Bottom = entity2Top + solidHeight;

    return entity1Right > entity2Left &&
           entity1Left < entity2Right &&
           entity1Bottom > entity2Top &&
           entity1Top < entity2Bottom;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw game objects
        Graphics2D g2d = (Graphics2D) g;

        // Calculate camera position
        int cameraX = player.x - screenWidth / 2 + tileSize / 2;
        int cameraY = player.y - screenHeight / 2 + tileSize / 2;

        // Draw the map
        for (int col = 0; col < maxMapCol; col++) {
            for (int row = 0; row < maxMapRow; row++) {
                int drawX = col * tileSize - cameraX;
                int drawY = row * tileSize - cameraY;

                // Only draw tiles that are visible on the screen
                if (drawX + tileSize > 0 && drawX < screenWidth && drawY + tileSize > 0 && drawY < screenHeight) {
                    BufferedImage tileImage = (mapTileNum[col][row] == 0) ? grassTile : waterTile;
                    g2d.drawImage(tileImage, drawX, drawY, tileSize, tileSize, null);
                }
            }
        }

        // Draw the player
        player.draw(g2d);

        // Draw the skeleton king
       
            int drawSkeletonKingX = skeletonKing.x - cameraX;
            int drawSkeletonKingY = skeletonKing.y - cameraY;
            skeletonKing.draw(g2d, drawSkeletonKingX, drawSkeletonKingY);
       
        

        // Draw the mana potion
        if (potionImage != null) {
            int potionTileX = 12; // Column in the sprite sheet
            int potionTileY = 5; // Row in the sprite sheet
            int potionWidth = 16;
            int potionHeight = 16;
            BufferedImage manaPotion = potionImage.getSubimage(potionTileX * potionWidth, potionTileY * potionHeight,
                    potionWidth, potionHeight);
            int drawPotionX = potionX - cameraX;
            int drawPotionY = potionY - cameraY;
            g2d.drawImage(manaPotion, drawPotionX, drawPotionY, tileSize, tileSize, null); // Scale the potion to the
                                                                                           // tile size
        }

        // Draw FPS
        g2d.drawString("FPS: " + fps, 10, 10);

        g2d.dispose();
    }

    public BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void removeMonster(Monster monster) {
        monster.dead = true;
        monster.disappearing = true;
        monster = null;
}
}
