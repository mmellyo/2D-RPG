
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class Mage extends Entity {
    GamePanel gamePanel;
    BufferedImage[] idleUpFrames;
    BufferedImage[] walkUpFrames;
    BufferedImage[] attackUpFrames;
    BufferedImage[] abilityUpFrames;
    BufferedImage[] hurtUpFrames;
    BufferedImage[] deathUpFrames;

    BufferedImage[] idleDownFrames;
    BufferedImage[] walkDownFrames;
    BufferedImage[] attackDownFrames;
    BufferedImage[] abilityDownFrames;
    BufferedImage[] hurtDownFrames;
    BufferedImage[] deathDownFrames;

    BufferedImage[] idleLeftFrames;
    BufferedImage[] walkLeftFrames;
    BufferedImage[] attackLeftFrames;
    BufferedImage[] abilityLeftFrames;
    BufferedImage[] hurtLeftFrames;
    BufferedImage[] deathLeftFrames;

    BufferedImage[] idleRightFrames;
    BufferedImage[] walkRightFrames;
    BufferedImage[] attackRightFrames;
    BufferedImage[] abilityRightFrames;
    BufferedImage[] hurtRightFrames;
    BufferedImage[] deathRightFrames;
    int frameIndex = 0;
    int frameCount = 8; // nbmr of frames in the idle and walk animations
    int frameDelay = 10; // Delay between frames
    int frameTimer = 0;
    int deathFrameDelay = 20;
    public boolean hurt = false;
    public boolean dead = false;
    public boolean disappearing = false;

    //death 
    int deathTimer = 0;
    final int deathDuration = 50;

    // aggro
    boolean aggro = false;
    int aggroRange = 0; //in pixels
    int aggroDuration = 180; // Aggro duration in frames (5s at 60 FPS)
    int aggroTimer = 0;

    //attak
    int attackCooldown = 60; //Cooldown duration in frames (1s at 60 FPS)
    int attackTimer = 0;
    int attackDamage = 2; //mage's attack
    int attackCount = 0; //Cnt for the nmbr of attacks

    int moveDelay = 0; // Delay cnt for movement
    final int moveDelayDuration = 30; // Delay duration in frames
    
    String direction = "down"; // Default
    int hp = 100; 

    //mana
    int mana = 100;
    int manaCost = 0; //each attack
    int abilityManaCost = 25; //cost for the ability
    int manaDamage = 20;

    //no mana msg
    boolean showMessage = false; // Flag to show message
    int messageTimer = 0; 
    final int messageDuration = 120; //(2s at 60 FPS)

     

    public Mage(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setDefaultValues();
        getMageImage();
    }

    public void setDefaultValues() {
        x = 200;
        y = 200;
        speed = 2;
        //Initialize
        hp = 100; 
        mana = 100;
        aggroRange = 8 * gamePanel.tileSize;

    }

    public void decreaseHp(int amount) {
        System.out.println("Mage hp: " + this.hp);
        this.hp -= amount;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public int getHp() {
        return hp;
    }

    public void getMageImage() {
    //will be overridden in subclass
    }

    public void update() {
        if (disappearing) return;
        // Check if the player is within aggro range
        int playerX = gamePanel.getPlayer().x;
        int playerY = gamePanel.getPlayer().y;
        int distanceX = Math.abs(playerX - x);
        int distanceY = Math.abs(playerY - y);

        if (distanceX <= aggroRange && distanceY <= aggroRange) {
            aggro = true;
            aggroTimer = aggroDuration; // Reset aggro timer
        }

        //aggro logic
        if (aggro) {
            if (aggroTimer > 0) {
                aggroTimer--;

                // Move towards the player
                if (playerX < x) {
                    x -= speed;
                    direction = "left";
                } else if (playerX > x) {
                    x += speed;
                    direction = "right";
                }

                if (playerY < y) {
                    y -= speed;
                    direction = "up";
                } else if (playerY > y) {
                    y += speed;
                    direction = "down";
                }

                // attack logic
                if (attackTimer > 0) {
                    attackTimer--;
                 } else if (mana >= manaCost && gamePanel.checkCollision(this, gamePanel.getPlayer())) {
                    attackTimer = attackCooldown; // Reset attack timer
                    attackCount++;
                    gamePanel.getPlayer().decreaseHp(attackDamage); // Decrease player's health by attack damage

                    //if mage attacked 5 times => use ability for bigger damage
                    if (attackCount % 5 == 0 && mana >= abilityManaCost) {
                        
                        gamePanel.getPlayer().decreaseHp(manaDamage); // Decrease player's health by attack damage

                        BufferedImage[] tempattackUpFrames = attackUpFrames;
                        BufferedImage[] tempattackDownFrames = attackDownFrames;
                        BufferedImage[] tempattackLeftFrames = attackLeftFrames;
                        BufferedImage[] tempattackRightFrames = attackRightFrames;

                        switch (direction) {
                            case "up":
                                attackUpFrames = abilityUpFrames;
                                break;
                            case "down":
                                attackDownFrames = abilityDownFrames;
                                break;
                            case "left":
                                attackLeftFrames = abilityLeftFrames;
                                break;
                            case "right":
                                attackRightFrames = abilityRightFrames;
                                break;
                        }
                        

                        // Schedule a reset back to normal attack frames after 500ms
                        new java.util.Timer().schedule(new java.util.TimerTask() {
                            @Override
                            public void run() {
                                switch (direction) {
                                case "up":
                                    attackUpFrames = tempattackUpFrames;
                                    break;
                                case "down":
                                    attackDownFrames = tempattackDownFrames;
                                    break;
                                case "left":
                                    attackLeftFrames = tempattackLeftFrames;
                                    break;
                                case "right":
                                    attackRightFrames = tempattackRightFrames;
                                    break;
                                 }
                            }
                         }, 1500); //delay before switching back to normal attack

                            mana -= abilityManaCost;
                      // Reset to defaukt attck after using ability
                      frameTimer = 0;
                      frameIndex = 0;
                      attackCount = 0; 

                      //if Not enough mana to use ability => display 'cant use abiliy'  
                    } else if (mana < abilityManaCost) {
                        showMessage = true;
                        messageTimer = messageDuration;
                        attackCount = 0;
                    }
                 }
             } else {
                aggro = false; // Stop following the player after aggro duration
            }
        } else {
            
            // Handle random movement logic
            if (moveDelay > 0) {
                moveDelay--;
            } else {
                Random rand = new Random();
                int move = rand.nextInt(100);
                if (move < 25) {
                    x -= speed;
                    direction = "left";
                } else if (move < 50) {
                    x += speed;
                    direction = "right";
                } else if (move < 75) {
                    y -= speed;
                    direction = "up";
                } else {
                    y += speed;
                    direction = "down";
                }
                moveDelay = moveDelayDuration; // Reset the movement delay
            }
        }

        // Prevent the mage from crossing the borders
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize())
            x = gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize();
        if (y > gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize)
            y = gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize;

        
        // death
        if (dead && !disappearing) {
            deathTimer++;
            
            if (deathTimer >= deathDuration) {
                disappearing = true;
            }
        }

        if (disappearing) {
            gamePanel.remove(Mage.this);
        }


        // Update frame index for animation
        frameTimer++;
        if (frameTimer >= frameDelay) {
            frameIndex = (frameIndex + 1) % frameCount;
            frameTimer = 0;
        }

        // Reset hurt state if animation is complete
        BufferedImage[] hurtFrames = null;
        switch (direction) {
            case "up":
                hurtFrames = hurtUpFrames;
                break;
            case "down":
                hurtFrames = hurtDownFrames;
                break;
            case "left":
                hurtFrames = hurtLeftFrames;
                break;
            case "right":
                hurtFrames = hurtRightFrames;
                break;
        }
        if (hurt && hurtFrames != null && frameIndex == hurtFrames.length - 1) {
            hurt = false;
        }

        // Reset dead state if animation is complete
        BufferedImage[] deathFrames = null;
        switch (direction) {
            case "up":
                deathFrames = deathUpFrames;
                
                break;
            case "down":
                deathFrames = deathDownFrames;
                
                break;
            case "left":
                deathFrames = deathLeftFrames;
                
                break;
            case "right":
                deathFrames = deathRightFrames;
               
                break;
        }
        if (dead && deathFrames != null && frameIndex == deathFrames.length - 1) {
            disappearing = true;
        }

        // Update message timer
        if (showMessage) {
            messageTimer--;
            if (messageTimer <= 0) {
                showMessage = false;
            }
        }
    }

    public void draw(Graphics2D g2d, int x, int y) {
        if (disappearing) {
            return; // Do not draw if the mage is disappearing
        }
        BufferedImage image = null;

        // Determine which image to draw based on mage state and direction
        switch (direction) {
            case "up":
                if (dead) {
                    image = deathUpFrames != null ? deathUpFrames[frameIndex % deathUpFrames.length] : null;
                } else if (hurt) {
                    image = hurtUpFrames != null ? hurtUpFrames[frameIndex % hurtUpFrames.length] : null;
                } else if (attackTimer > 0) {
                    image = attackUpFrames != null ? attackUpFrames[frameIndex % attackUpFrames.length] : null;
                } else {
                    image = walkUpFrames != null ? walkUpFrames[frameIndex % walkUpFrames.length] : null;
                }
            //image = walkUpFrames[frameIndex];
                break;
            case "down":
                if (dead) {
                    image = deathDownFrames != null ? deathDownFrames[frameIndex % deathDownFrames.length] : null;
                } else if (hurt) {
                    image = hurtDownFrames != null ? hurtDownFrames[frameIndex % hurtDownFrames.length] : null;
                } else if (attackTimer > 0) {
                    image = attackDownFrames != null ? attackDownFrames[frameIndex % attackDownFrames.length] : null;
                } else {
                    image = walkDownFrames != null ? walkDownFrames[frameIndex % walkDownFrames.length] : null;
                }
                // image = walkDownFrames[frameIndex];
                break;
            case "left":
                if (dead) {
                    image = deathLeftFrames != null ? deathLeftFrames[frameIndex % deathLeftFrames.length] : null;
                } else if (hurt) {
                    image = hurtLeftFrames != null ? hurtLeftFrames[frameIndex % hurtLeftFrames.length] : null;
                } else if (attackTimer > 0) {
                    image = attackLeftFrames != null ? attackLeftFrames[frameIndex % attackLeftFrames.length] : null;
                } else {
                    image = walkLeftFrames != null ? walkLeftFrames[frameIndex % walkLeftFrames.length] : null;
                }
              //image = walkLeftFrames[frameIndex];
                break;
            case "right":
                if (dead) {
                    image = deathRightFrames != null ? deathRightFrames[frameIndex % deathRightFrames.length] : null;
                } else if (hurt) {
                    image = hurtRightFrames != null ? hurtRightFrames[frameIndex % hurtRightFrames.length] : null;
                } else if (attackTimer > 0) {
                    image = attackRightFrames != null ? attackRightFrames[frameIndex % attackRightFrames.length] : null;
                } else {
                    image = walkRightFrames != null ? walkRightFrames[frameIndex % walkRightFrames.length] : null;
                }
                //image = walkRightFrames[frameIndex];
                break;
        }
        
        if (image == null) {
            //draw default rectangle in no image found
            g2d.setColor(Color.red);
            g2d.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);
        } else {
        int drawWidth = gamePanel.tileSize * 2; // Adjust the multiplier to change the mage's width
        int drawHeight = gamePanel.tileSize * 2; // Adjust the multiplier to change the mage's height
        g2d.drawImage(image, x, y, drawWidth, drawHeight, null);
        //g2d.drawImage(image, x, y, null);
        }

        // Draw health bar
        g2d.setColor(Color.red);
        int hpBarWidth = (int) ((double) hp / 100 * 200); // Calculate the width of the HP bar based on the current HP
        
        g2d.fillRect(x - gamePanel.tileSize / 2, y - gamePanel.tileSize, hpBarWidth, 10);
        g2d.setColor(Color.white);
        g2d.drawRect(x - gamePanel.tileSize / 2, y - gamePanel.tileSize, 200, 10); // Outline for the health bar
                                                                                              // position as needed
        

        // Draw mana bar
        g2d.setColor(Color.blue);
        int manaBarWidth = (int) ((double) mana / 100 * 200); // Calculate the width of the mana bar based on the current mana
        g2d.fillRect(x - gamePanel.tileSize / 2, y - gamePanel.tileSize + 15, manaBarWidth, 10); // Adjust the size and position as needed
        g2d.setColor(Color.white);
        g2d.drawRect(x - gamePanel.tileSize / 2, y - gamePanel.tileSize + 15, 200, 10); // Outline for the mana bar
        

        // Draw the 'no mana' message
        if (showMessage) {
            g2d.setColor(Color.black);
            g2d.setFont(g2d.getFont().deriveFont(18f));
            g2d.drawString("Can't use ability", x, y - 10);
        }

    }
}

