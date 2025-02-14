package main;

import entity.AIMage;
import entity.Entity;
import entity.Player;
import entity.SkeletonKing;
import entity.Warrior;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
    final int maxMapCol = 40;
    final int maxMapRow = 50;

    int totalCols = maxMapCol + 8;
    int totalRows = maxMapRow + 8;

    final int borderThickness = 6; 




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
    AIMage AIMage;
    SkeletonKing skeletonKing;
    Thread gameThread;
    KeyHandler keyHandler;

    BufferedImage potionImage;
    BufferedImage grassTile;
    BufferedImage waterTile;
    BufferedImage castleImage;
    BufferedImage darkWaterTileL;  //with grass in left
    BufferedImage endGrassTileD;
    BufferedImage castle;
    BufferedImage rockWallTileB;  //with water at bottom
    BufferedImage endWaterTile;
    BufferedImage waterGrassTileL; 
    BufferedImage waterGrasstileR;
    BufferedImage endRockWallTileR;
    BufferedImage endWallwaterTileR;
    BufferedImage rockWall;
    BufferedImage endWallGrassTileD;
    BufferedImage endGrasstileRU;
    BufferedImage waterGrasstileD;
    BufferedImage bridgeTile;
    BufferedImage verticalBridgeTile;
    BufferedImage bridgeWoodTile;
    BufferedImage waterGrasstileDL;
    BufferedImage waterGrasstileU; //§§§§§§§§§§§§§§§§§§§§§§§§

    int[][] mapTileNum;
    int[][] playablemapTileNum;

    int potionX, potionY;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Initialize player and mage positions
        int initialX = 70 * tileSize; // 20 tiles from the left
        int initialY = 90 * tileSize; //  40 tiles from the top

        player = new Warrior(this, keyHandler);
        player.x = initialX;
        player.y = initialY;

        AIMage = new AIMage(this);
        AIMage.x = initialX + tileSize; // Spawn the mage next to the player
        AIMage.y = initialY;

        skeletonKing = new SkeletonKing(this);

        loadPotionImage();
        loadTileSet();
        loadCastleImage();
        generateMap();
        //generatePlayableMap();
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
        AIMage.update();

        // Prevent the player from crossing the borders
        if (player.x < 8 * tileSize )
            player.x =  8 * tileSize ;
        if (player.y < 8 * tileSize )
            player.y =  8 * tileSize ;
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
                skeletonKing.decreaseHp(20);
                skeletonKing.hurt = true;
                player.setAttackRegistered(true); // Register the attack
                if (skeletonKing.getHp() <= 0) {
                    skeletonKing.dead = true;
                }
            }//ability
            if (player.isAttacking3() && checkCollision(player, skeletonKing)  && !player.isAttackRegistered()) {
                skeletonKing.decreaseHp(35);
                skeletonKing.hurt = true;
                player.setAttackRegistered(true); // Register the attack
                if (skeletonKing.getHp() <= 0) {
                    skeletonKing.dead = true;
                }
            }
           // player.setAttacking(false);
        }

        // Check for player attack on mage
        if (!AIMage.dead) {
            if (player.isAttacking() && checkCollision(player, AIMage)  && !player.isAttackRegistered()) {
                AIMage.decreaseHp(4); 
                AIMage.hurt = true;
                player.setAttackRegistered(true); // Register the attack
                
                if (AIMage.getHp() <= 0) {
                    AIMage.dead = true;
                }
            
            }
            //ability
            if (player.isAttacking3() && checkCollision(player, AIMage) && !player.isAttackRegistered() ) {
                AIMage.decreaseHp(25);
                AIMage.hurt = true;
                player.setAttackRegistered(true); 
                
                if (AIMage.getHp() <= 0) {
                    AIMage.dead = true;
                }
           
            }

        }

        // Check skeleton king attack on player
        if (!player.isDead()) {
            if (skeletonKing.isAttacking() && checkCollision(player, skeletonKing) && !skeletonKing.isAttackRegistered() ) {
                
                //debug
                System.out.println("player hurt BEFORE the 'set' is : " + player.isHurtByMonster());

                player.setHurtByMonster(true);
                skeletonKing.setAttackRegistered(false);

                //debug
                System.out.println("player hurt AFTER the 'set' is : " + player.isHurtByMonster());

            }
            if (player.isHurtByMonster() && player.getFrameIndex() == 4) {
                System.out.println("Forcing hurt to false.");
                player.setHurtByMonster(false);
                
            }
        }

        //hp = 0 death logic
        if (player.getHp() <= 0) {
            player.setDead(true);
        }

        //debug
        System.out.println("AFTER COLLISION : " + player.isHurtByMonster());

    }

    public void loadPotionImage() {
        potionImage = loadImage("/res/icons/potions.png");
    }

    public void loadTileSet() {
        BufferedImage tileSet = loadImage("/res/tiles/htiles1.png");
        int tileWidth = 32;
        int tileHeight = 32;
        grassTile = tileSet.getSubimage(20 * tileWidth, 0 * tileHeight, tileWidth, tileHeight);
        waterTile = tileSet.getSubimage(27 * tileWidth, 6 * tileHeight, tileWidth, tileHeight);  //w 864 h 192
        endGrassTileD= tileSet.getSubimage(23 * tileWidth, 10 * tileHeight, tileWidth, tileHeight); //w 733 h 319
        darkWaterTileL = tileSet.getSubimage(26 * tileWidth, 4 * tileHeight, tileWidth, tileHeight); //w 832 h 128
        waterGrassTileL = tileSet.getSubimage(26 * tileWidth, 5 * tileHeight, tileWidth, tileHeight); 
        endWallGrassTileD = tileSet.getSubimage(23 * tileWidth, 13 * tileHeight, tileWidth, tileHeight); 
        rockWall  = tileSet.getSubimage(26 * tileWidth, 7 * tileHeight, tileWidth, tileHeight); 
        endGrasstileRU = tileSet.getSubimage(21 * tileWidth, 8 * tileHeight, tileWidth, tileHeight);
        waterGrasstileD = tileSet.getSubimage(29 * tileWidth, 10 * tileHeight, tileWidth, tileHeight);
        bridgeWoodTile = tileSet.getSubimage(22 * tileWidth, 21 * tileHeight, tileWidth, tileHeight);
        bridgeTile = tileSet.getSubimage(22 * tileWidth, 20 * tileHeight, tileWidth, tileHeight);
        verticalBridgeTile = tileSet.getSubimage(20 * tileWidth, 19 * tileHeight, tileWidth, tileHeight);
        waterGrasstileDL = tileSet.getSubimage(28 * tileWidth, 10 * tileHeight, tileWidth, tileHeight);
        waterGrasstileR = tileSet.getSubimage(27 * tileWidth, 5 * tileHeight, tileWidth, tileHeight);
        waterGrasstileU = tileSet.getSubimage(27 * tileWidth, 5 * tileHeight, tileWidth, tileHeight); 

        BufferedImage tileSet2 = loadImage("/res/tiles/htiles2.png");
        rockWallTileB = tileSet2.getSubimage(23 * tileWidth, 12 * tileHeight, tileWidth, tileHeight); //w 736 h 384
        endWaterTile = tileSet2.getSubimage(23 * tileWidth, 13 * tileHeight, tileWidth, tileHeight);
        
        endRockWallTileR = tileSet2.getSubimage(24 * tileWidth, 14 * tileHeight, tileWidth, tileHeight);
        endWallwaterTileR = tileSet2.getSubimage(24 * tileWidth, 15 * tileHeight, tileWidth, tileHeight);
    }

    public void loadCastleImage() {  // w 256 h 336
        BufferedImage cset = loadImage("/res/tiles/Castle.png");
        int castleX = 198; // x-coordinate of the top-left corner of the castle
        int castleY = 0; // y-coordinate of the top-left corner of the castle
        int castleWidth = 116; // width of the castle
        int castleHeight = 224; // height of the castle
        castleImage = cset.getSubimage(castleX, castleY, castleWidth, castleHeight);
    }

    // input : angle's grass tile 
    public void AngleBottomRightTiles(int colOffset, int rowOffset) {
        mapTileNum[colOffset][rowOffset] = 2; // end grass tile at down
        mapTileNum[colOffset][rowOffset + 1] = 41; // end rock wall from RIGHT 
        mapTileNum[colOffset][rowOffset + 2] = 411; // the water under it
    }

    // input : first grass tile 
    public void RockWallTiles(int colOffset, int rowOffset, boolean withGrass, boolean midWall) {
        if (!midWall) {
            mapTileNum[colOffset][rowOffset] = 2; // end grass tile at down
            mapTileNum[colOffset][rowOffset + 1] = 4; // rock wall tile water at bottom

                if (withGrass) {
                    mapTileNum[colOffset][rowOffset + 2] = 3; // dark water tile (with end grass in left)
                } else {
                    mapTileNum[colOffset][rowOffset + 2] = 11; // end water
                }
            
        } else {
            mapTileNum[colOffset][rowOffset] = 2; // end grass tile at down
            mapTileNum[colOffset][rowOffset + 1] = 40; // rock wall
            mapTileNum[colOffset][rowOffset + 2] = 40; // rock wall
            mapTileNum[colOffset][rowOffset + 3] = 401; // rock wall with grass at bottom

        }
    }



    public void generateMap() {
        mapTileNum = new int[totalCols][totalRows];
        int col,row;

        //BASE
        // 0 = Grass tile
        // 1 = Water tile
        
        // END GRASS
        // 2 = end grass tile at DOWN
        // 22 =  end grass at UP & RIGHT

        // WATER WITH END GRASS
        // 5 = water with grass at LEFT
        // 51 = water with gras at DOWN
        // 52 = water with grass at RIGHT
        // 53 = water with grass at UP
        // 510 = water with grass at DOWN & LEFT

        

        // DARK WATER
        // 3 = dark water tile (with end grass in left)

        // ROCK WALL
        // 40 = rock wall
        // 401 = rock wall with grass at bottom
        // 4 = rock wall with watter at bottom
        // 41 = end rock wall from RIGHT with water 
        // 411 = is water (the water under the right end wall)

        //BRIDGE
        //9 = wood of bridge
        //91 =bridge
        //911 = vertical bridge

        // 11 = end water


        for ( col = 0; col < totalCols; col++) {
            for ( row = 0; row < totalRows; row++) {

               //rightmost outer edges to water tiles
                if ( col >= totalCols - 11 || row >= totalRows - 8) {

                    //BRIDGE line
                    if (row == maxMapRow - 4) {
                        mapTileNum[col ][maxMapRow - 4] = 91; //bridge      
                    } else {
                        mapTileNum[col][row] = 1; //Water tile
                    }

                } else // Grass tile for playable map + leftmost outer
                {
                    mapTileNum[col][row] = 0; 
                }

                // end grass tiles botttommost line (playable)
                if (col < totalCols - 13 && row >= 8 || row == totalRows - 9) {
                    RockWallTiles(col,maxMapRow,false,false);
                }

                

                //GANERATE LEFT OUTER MAP
                //end grass tiles botttommost line (leftmost outer)
                if (col < 8 && col < totalCols - 8  /*&&row < 8 || row == totalRows - 8*/) {
                    mapTileNum[col][maxMapRow] = 2;  //end grass tile 
                }


                //GANERATE RIGHIT OUTER MAP
                // lil mountain
                if (col > maxMapCol - 4 && row >= maxMapRow -  0.0625*maxMapRow) { // 1/16 maxrow
                    mapTileNum[col][row] = 1; //Water tile

                    if (row <= maxMapRow - 3) {
                    mapTileNum[maxMapCol - 3][row] = 5; //Water with grass at right tile 
                    }
                }
                
                //MID WALL   96 124 (starts maxrow - 14 * - 13)
                if (col <= maxMapCol - 4) {
                RockWallTiles(col, totalCols - 17  ,false,true);
                } else {
                    RockWallTiles(col, totalCols - 17  ,false,true);
                }
            }
        }


        //rightmost end (lil mountain lol)
        RockWallTiles(totalCols - 13, maxMapRow - 3, true,false);
        AngleBottomRightTiles(totalCols - 12 , maxMapRow - 3 );
        mapTileNum[totalCols - 11][maxMapRow - 5] = 5; // water with grass at left (upon bridge)




        //CASTLE SURROUNDINGS

        //vertical bridge to castle
        for (int i = 6; i <= 10; i++) {
            mapTileNum[totalCols - 16][maxMapRow - i] = 911;
        }
        

        // handling water around castle
        int[] waterCols = { 12, 13, 14, 15,   17, 18, 19};
        for (int waterCol : waterCols) {
            mapTileNum[totalCols - waterCol][maxMapRow - 6] = 51; // water with gras at DOWN
            mapTileNum[totalCols - waterCol][maxMapRow - 15] = 51; // water with gras at UP

            for (int i = 7; i <= 16; i++) {  
                mapTileNum[totalCols - waterCol][maxMapRow - i] = 1; // water
                mapTileNum[totalCols - 20][maxMapRow - i] = 5; // water with grass at left
            }
        }                 
        mapTileNum[totalCols - 20][maxMapRow - 6] = 510; // the angle : water with grass at DOWN & LEFT



        //bottommost
        AngleBottomRightTiles(totalCols - 14 , maxMapRow);
        mapTileNum[totalCols - 13][maxMapRow] = 5; // water with grass at left
 
    } 

    // public void generatePlayableMap() {
    //     playablemapTileNum = new int[maxMapCol][maxMapRow];

    //     for (int col = 0; col < maxMapCol; col++) {
    //         for (int row = 0; row < maxMapRow; row++) {
    //             playablemapTileNum[col][maqqxMapRow] = 2;  //end grass tile


    //         }ssssss
    //     }
    // }

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

        //DEBUG1 : how long it takes to draw
        long drawStart =0;
        if (keyHandler.checkDrawTime == true) {
        drawStart = System.nanoTime();  
        }

        //DEBUG2 :position
        if (keyHandler.PlayerPos == true) {
        drawStart = System.nanoTime();  
        }

        // Calculate camera position
        int cameraX = player.x - screenWidth / 2 + tileSize / 2;
        int cameraY = player.y - screenHeight / 2 + tileSize / 2;

        // Draw the map
        for (int col = 0; col < totalCols; col++) {
            for (int row = 0; row < totalRows; row++) {
                int drawX = col * tileSize - cameraX;
                int drawY = row * tileSize - cameraY;

                // Only draw tiles that are visible on the screen
                if (drawX + tileSize > 0 && drawX < screenWidth && drawY + tileSize > 0 && drawY < screenHeight) {
                    BufferedImage tileImage = null;

                    switch (mapTileNum[col][row]) {
                        case 0 :
                            tileImage = grassTile;
                            break;
                        
                        case 1 :
                            tileImage = waterTile;
                        break;

                        case 2 :
                            tileImage = endGrassTileD;
                        break;

                        case 22 :
                            tileImage = endGrasstileRU;
                        break;

                        case 11 :
                            tileImage = endWaterTile;
                        break;

                        case 3 :
                            tileImage = darkWaterTileL;
                        break;

                        case 40 :
                            tileImage = rockWall;
                        break;

                        case 401 :
                            tileImage = endWallGrassTileD;
                        break;
                        
                        case 4 :
                            tileImage = rockWallTileB;
                        break;
                        
                        case 41 :
                        tileImage = endRockWallTileR;
                        break;

                        case 411 :
                        tileImage = endWallwaterTileR;
                        break;

                        case 5 :
                            tileImage = waterGrassTileL;
                        break;

                        case 51 :
                            tileImage = waterGrasstileD;
                        break;

                        case 52 :
                            tileImage = waterGrasstileR;
                        break;

                        case 510 :
                            tileImage = waterGrasstileDL;
                        break;

                        case 53 :
                            tileImage = waterGrasstileU;
                        break;

                        case 9 :
                            tileImage = bridgeWoodTile;
                        break;

                        case 91 :
                            tileImage = bridgeTile;
                        break; 

                        case 911 :
                            tileImage = verticalBridgeTile;
                        break;
                        default:
                            //throw new AssertionError();
                    }
                    g2d.drawImage(tileImage, drawX, drawY, tileSize, tileSize, null);
                }
            }
        }

        // Draw the castle image at a specific position
        if (castleImage != null) {
            int castleX = 31 * tileSize; // Eg (10 tiles from the left)
            int castleY = 31 * tileSize; // Eg (10 tiles from the top)
            int scaledWidth = castleImage.getWidth() * 2;
            int scaledHeight = castleImage.getHeight() * 2;
            // g2d.drawImage(castleImage, castleDrawX - cameraX, castleDrawY - cameraY, scaledWidth, scaledHeight, null);
            g2d.drawImage(castleImage, castleX - cameraX , castleY - cameraY,scaledWidth, scaledHeight, null);
        }

        // Draw the player
        player.draw(g2d);

        // Draw the skeleton king
        int drawSkeletonKingX = skeletonKing.x - cameraX;
        int drawSkeletonKingY = skeletonKing.y - cameraY;
        skeletonKing.draw(g2d, drawSkeletonKingX, drawSkeletonKingY);
    
        // draw aimage 
        int drawAIMageX = AIMage.x - cameraX;
        int drawAIMageY = AIMage.y - cameraY;
        AIMage.draw(g2d, drawAIMageX, drawAIMageY);
        

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

        //DEBUG1
        if (keyHandler.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2d.setColor(Color.black);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("Draw time :" + passed, 10, 400);
            System.out.println("draw time : " + passed);
        }

        //DEBUG2 
        if (keyHandler.PlayerPos == true) {
            g2d.setColor(Color.black);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("Player position: x = " + getPlayer().x / tileSize  + " tiles, y = " + getPlayer().y / tileSize + " tiles", 10, 420);
        }
            
        g2d.dispose();

    }

    public BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            System.out.println("Attempting to load image from path: " + path);
            image = ImageIO.read(getClass().getResourceAsStream(path));
            //DEBUG
            if (image == null) {
                throw new IOException("Image not found: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }



    public void remove(Entity e) {
        e.dead = true;
        e.disappearing = true;
        e = null;
    }

    public void removePlayer(Player p) {
        p.setDead(true);
        p.disappearing = true;
        p = null;
    }
}
/*public void remove(Entity e) {
        e.dead = true;
        e.disappearing = true;
        e = null;
    } */