package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.GamePanel;

public class Monster extends Entity {
    GamePanel gamePanel;
    public Monster(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        setDefaultValues();
        getMonsterImage();
    }
    int attackCooldown = 60; // Cooldown duration in frames (1 second at 60 FPS)
    int attackTimer = 0;
    int attackDamage = 10; // Damage dealt by the monster's attack
    int manaCost = 10; // Mana cost for each attack

    BufferedImage[] attackUpFrames;
    BufferedImage[] attackDownFrames;
    BufferedImage[] attackLeftFrames;
    BufferedImage[] attackRightFrames;
    BufferedImage[] attackFrames; // Declare attackFrames variable
    
    BufferedImage[] idleUpFrames;
    BufferedImage[] walkUpFrames;
    BufferedImage[] hurtUpFrames;
    BufferedImage[] deathUpFrames;
    BufferedImage[] idleDownFrames;
    BufferedImage[] walkDownFrames;
    BufferedImage[] hurtDownFrames;
    BufferedImage[] deathDownFrames;
    BufferedImage[] idleLeftFrames;
    BufferedImage[] walkLeftFrames;
    BufferedImage[] hurtLeftFrames;
    BufferedImage[] deathLeftFrames;
    BufferedImage[] idleRightFrames;
    BufferedImage[] walkRightFrames;
    BufferedImage[] hurtRightFrames;
    BufferedImage[] deathRightFrames;
    int frameIndex = 0;
    int frameCount = 8; // Number of frames in the idle and walk animations
    int frameDelay = 10; // Delay between frames
    int frameTimer = 0;
    int deathFrameDelay = 20;
    public boolean hurt = false;
    public boolean dead = false;
    public boolean disappearing = false;
    // aggro
    boolean aggro = false;
    int aggroRange = 0; // Aggro range in pixels
    int aggroDuration = 180; // Aggro duration in frames (5 seconds at 60 FPS)
    int aggroTimer = 0;

    int deathTimer = 0;
    final int deathDuration = 50;

    String direction = "down"; // Default direction
    int hp = 100; // Default health
    int mana = 100; // Default mana
    int moveDelay = 0; // Delay counter for movement
    final int moveDelayDuration = 30; // Delay duration in frames

    boolean attacking = false;
    boolean attackRegistered = false; 
    
    public void setDefaultValues() {
        x = 200;
        y = 200;
        speed = 2;
        hp = 100; // Initialize health
        mana = 100; // Initialize mana
        aggroRange = 2 * gamePanel.tileSize;
    }

    public void decreaseHp(int amount) {
        System.out.println("Monster hp: " + this.hp);
        this.hp -= amount;
        if (this.hp < 0) {
            this.hp = 0;
        }

    }

    public int getHp() {

        return hp;

    }

    public boolean isAttacking(){
        return attacking;
    }

    public boolean isAttackRegistered() {

        return attackRegistered;
    }

    public void setAttackRegistered(boolean attackRegistered) {
        this.attackRegistered = attackRegistered;
    }

    public void getMonsterImage() {
        // This method will be overridden in subclasses
    }

    public void update() {
        if(disappearing) return;
        // Check if the player is within aggro range
        int playerX = gamePanel.getPlayer().x;
        int playerY = gamePanel.getPlayer().y;
        int distanceX = Math.abs(playerX - x);
        int distanceY = Math.abs(playerY - y);

        if (distanceX <= aggroRange && distanceY <= aggroRange) {
            aggro = true;
            aggroTimer = aggroDuration; // Reset aggro timer
        }

        // Handle aggro logic
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

                // Handle attack logic
                if (attackTimer > 0) {
                    attacking = true;
                    //gamePanel.getPlayer().setHurt(true);
                    //frameIndex = 0; // Reset frame index for hurt animation
                    attackTimer--;
                } else if (mana >= manaCost && gamePanel.checkCollision(this, gamePanel.getPlayer())) {
                    attacking = true;
                    //frameIndex = 0; // Reset frame index for hurt animation
                    attackTimer = attackCooldown; // Reset attack timer
                    mana -= manaCost; // Consume mana
                    gamePanel.getPlayer().decreaseHp(attackDamage); // Decrease player's health by attack damage
                }
            } else {
                aggro = false; // Stop following the player after aggro duration
                attacking = false;
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

        // Prevent the monster from crossing the borders
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize())
            x = gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize();
        if (y > gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize)
            y = gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize;

        // Handle death timer
        if (dead && !disappearing) {
            deathTimer++;
            
            if (deathTimer >= deathDuration) {
                disappearing = true;
            }
        }

        // Remove the monster from the game if it is disappearing
        if (disappearing) {
            gamePanel.remove(Monster.this);
        }


        // Update frame index for animation
        frameTimer++;
        if (dead) {
            if (frameTimer >= deathFrameDelay) {
                frameIndex = (frameIndex + 1) % frameCount;
                frameTimer = 0;
            }
        } else {
            if (frameTimer >= frameDelay) {
                frameIndex = (frameIndex + 1) % frameCount;
                frameTimer = 0;
            }
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
    }

    public void draw(Graphics2D g2d, int drawX, int drawY) {
       
        if (disappearing) {
            return; // Do not draw if the monster is disappearing
        }
        BufferedImage image = null;

        // Determine which image to draw based on monster state and direction
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
                break;
        }

        // Draw the selected image
        if (image != null) {
            int drawWidth = gamePanel.tileSize * 2; // Adjust the multiplier to make the monster bigger
            int drawHeight = gamePanel.tileSize * 2; // Adjust the multiplier to make the monster bigger
            g2d.drawImage(image, drawX - drawWidth / 2, drawY - drawHeight / 2, drawWidth, drawHeight, null);
        } else {
            // Fallback to a default rectangle if no image is available
            g2d.setColor(Color.red);
            g2d.fillRect(drawX, drawY, gamePanel.tileSize, gamePanel.tileSize);
        }

        // Draw health bar
        g2d.setColor(Color.red);
        int hpBarWidth = (int) ((double) hp / 100 * 200); // Calculate the width of the HP bar based on the current HP
        
        g2d.fillRect(drawX - gamePanel.tileSize / 2, drawY - gamePanel.tileSize, hpBarWidth, 10); // Adjust the size and position as needed
        g2d.setColor(Color.white);
        g2d.drawRect(drawX - gamePanel.tileSize / 2, drawY - gamePanel.tileSize, 200, 10); // Outline for the health bar
                                                                                              // position as needed
        

        // Draw mana bar
        g2d.setColor(Color.blue);
        int manaBarWidth = (int) ((double) mana / 100 * 200); // Calculate the width of the mana bar based on the current mana
        g2d.fillRect(drawX - gamePanel.tileSize / 2, drawY - gamePanel.tileSize + 15, manaBarWidth, 10); // Adjust the size and position as needed
        g2d.setColor(Color.white);
        g2d.drawRect(drawX - gamePanel.tileSize / 2, drawY - gamePanel.tileSize + 15, 200, 10); // Outline for the mana bar
        
        // Draw the solid portion as red
       
    }
}