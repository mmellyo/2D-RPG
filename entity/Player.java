package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity implements MouseListener {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    BufferedImage[] idleUpFrames;
    BufferedImage[] walkUpFrames;
    BufferedImage[] attackUpFrames;
    BufferedImage[] attack3UpFrames;
    BufferedImage[] hurtUpFrames;
    BufferedImage[] deathUpFrames;


    BufferedImage[] idleDownFrames;
    BufferedImage[] walkDownFrames;
    BufferedImage[] attackDownFrames;
    BufferedImage[] attack3DownFrames;
    BufferedImage[] hurtDownFrames;
    BufferedImage[] deathDownFrames;

    BufferedImage[] idleLeftFrames;
    BufferedImage[] walkLeftFrames;
    BufferedImage[] attackLeftFrames;
    BufferedImage[] attack3LeftFrames;
    BufferedImage[] hurtLeftFrames;
    BufferedImage[] deathLeftFrames;

    BufferedImage[] idleRightFrames;
    BufferedImage[] walkRightFrames;
    BufferedImage[] attackRightFrames;
    BufferedImage[] attack3RightFrames;
    BufferedImage[] hurtRightFrames;
    BufferedImage[] deathRightFrames;

    int frameIndex = 0;
    int frameCount = 8; // Number of frames in the idle and walk animations
    int frameDelay = 10; // Delay between frames
    int frameTimer = 0;
    int deathFrameDelay = 20; // Define the deathFrameDelay variable

    boolean attacking = false;
    boolean attacking3 = false;
    boolean attackRegistered = false; // Flag 
    boolean hurt = false; // Flag to indicate if the player is hurt

    boolean dead = false;
    int hurtTimer = 0; // Timer for hurt state
    final int hurtDuration = 30; // Duration for hurt state

    String direction = "down"; // Default direction
    int mana = 100; // Default mana
    
    int deathTimer = 0;
    final int deathDuration = 40;

    int hp = 100; 
    final int maxHp = 100;

    final int messageDuration = 60; //(1s at 60 FPS)
    boolean abilityWhileAttakMessage = false;
    int messageTimer = messageDuration;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        setDefaultValues();
        getPlayerImage();
        gamePanel.addMouseListener(this);
    }
   public void decreaseHp(int amount) {
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void increaseHp(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public void setDead(boolean dead) {

        this.dead = dead;

    }

    public boolean isDead() {

        return this.dead;

    }



    
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        mana = 100; 
    }

    public void getPlayerImage() {
        // This method will be overridden in subclasses
    }
    public boolean isAttacking() {

        return attacking;
    
    }
    public boolean isAttacking3() {

        return attacking3;
    
    }
    public boolean isAttackRegistered() {
        return attackRegistered;
    }



    public void setHurtByMonster(boolean hurt) {
        // if (hurt) {
        //     frameIndex = 0;
        //     frameTimer = 0;
        //     hurtTimer = 0;
        // }
        this.hurt = hurt;
    }

    public boolean isHurtByMonster() {

        return this.hurt;


    }

    public int getFrameIndex() {

        return frameIndex;
    }

   

    public void setAttackRegistered(boolean attackRegistered) {
        this.attackRegistered = attackRegistered;
    }

    // public void hurt(int damage) {
    //     if (!dead) {
    //         decreaseHp(damage);
    //         hurt = true;
    //         frameIndex = 0; // Reset frame index for hurt animation
    //         if (hp <= 0) {
    //             dead = true;
    //             frameIndex = 0; // Reset frame index for death animation
    //         }
    //     }
    // }

    public void update() {


        if (keyHandler.leftPressed) {
            x -= speed;
            direction = "left";
        } else if (keyHandler.rightPressed) {
            x += speed;
            direction = "right";
        } else if (keyHandler.upPressed) {
            y -= speed;
            direction = "up";
        } else if (keyHandler.downPressed) {
            y += speed;
            direction = "down";
        }


        // Prevent the player from crossing the borders
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize()) x = gamePanel.getMaxMapCol() * gamePanel.getTileSize() - gamePanel.getTileSize();
        if (y > gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize) y = gamePanel.getMaxMapRow() * gamePanel.tileSize - gamePanel.tileSize;

        // Handle death timer
        if (dead && !disappearing) {
            //debug
            System.out.println("death timeer .......................: " +deathTimer);
            
            deathTimer++;
            
            if (deathTimer >= deathDuration) {
                disappearing = true;
            }
        }
        if (disappearing) {
            gamePanel.removePlayer(this);
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
        
        // Hurt animation handling
        if (hurt) {//debug DONE
           // System.out.println("1////////// Hurt animation is running. Frame: " + frameIndex);
            BufferedImage[] hurtFrames = null;
            switch (direction) {
                case "up": hurtFrames = hurtUpFrames; break;
                case "down": hurtFrames = hurtDownFrames; break;
                case "left": hurtFrames = hurtLeftFrames; break;
                case "right": hurtFrames = hurtRightFrames; break;
            }if (hurtFrames != null && frameIndex == hurtFrames.length - 1) {
                hurt = false;
            }           
        }
        
        

        // Reset dead state if animation is complete
        BufferedImage[] deathFrames = null;
        if(dead) {
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


        // Reset attacking state if animation is complete
        BufferedImage[] attackFrames = null;
        switch (direction) {
            case "up":
                attackFrames = attackUpFrames;
                break;
            case "down":
                attackFrames = attackDownFrames;
                break;
            case "left":
                attackFrames = attackLeftFrames;
                break;
            case "right":
                attackFrames = attackRightFrames;
                break;
        }
        if (attacking && attackFrames != null && frameIndex == attackFrames.length - 1) {
            attacking = false;
            attackRegistered = false; // Reset the attack registered flag
        }



        // Reset attacking3 state if animation is complete
        BufferedImage[] attack3Frames = null;
        switch (direction) {
            case "up":
                attack3Frames = attack3UpFrames;
                break;
            case "down":
                attack3Frames = attack3DownFrames;
                break;
            case "left":
                attack3Frames = attack3LeftFrames;
                break;
            case "right":
                attack3Frames = attack3RightFrames;
                break;
        }
        if (attacking3 && attack3Frames != null && frameIndex == attack3Frames.length - 1) {
            attacking3 = false;
            attackRegistered = false; // Reset the attack registered flag
        }   

        // Update message timer
        if (abilityWhileAttakMessage) {
            messageTimer--;
            if (messageTimer <= 0) {
                abilityWhileAttakMessage = false;
            }
        }
        
    }

    

    public boolean checkCollisionWithSkeletonKing() {
        int playerWidth = gamePanel.tileSize;
        int playerHeight = gamePanel.tileSize;
        int skeletonKingWidth = gamePanel.tileSize;
        int skeletonKingHeight = gamePanel.tileSize;

        SkeletonKing skeletonKing = gamePanel.getSkeletonKing();
        return x < skeletonKing.x + skeletonKingWidth && x + playerWidth > skeletonKing.x &&
               y < skeletonKing.y + skeletonKingHeight && y + playerHeight > skeletonKing.y;
    }

    public void updateMana(int amount) {
        mana = Math.min(mana + amount, 100); // Increase mana by the specified amount, up to a maximum of 100
    }


    

    public void draw(Graphics2D g2d) {
        if (disappearing) {
            return; // Do not draw if the player is disappearing
        }
        BufferedImage image = null;
        // Determine which image to draw based on player state and direction
        switch (direction) {
            case "up":
            if (dead) {
                image = deathUpFrames != null ? deathUpFrames[frameIndex % deathUpFrames.length] : null;  // ensures that the frame index wraps around if it exceeds the number of frames available
            } else if (hurt) {
                image = hurtUpFrames != null ? hurtUpFrames[frameIndex % hurtUpFrames.length] : null;
            } else if (attacking) {
                image = attackUpFrames != null ? attackUpFrames[frameIndex % attackUpFrames.length] : null;
            } else if (attacking3) {
                image = attack3UpFrames != null ? attack3UpFrames[frameIndex % attack3UpFrames.length] : null;
            } else if (!keyHandler.leftPressed && !keyHandler.rightPressed && !keyHandler.upPressed && !keyHandler.downPressed) {
                image = idleUpFrames != null ? idleUpFrames[frameIndex % idleUpFrames.length] : null;
            } else {
                image = walkUpFrames != null ? walkUpFrames[frameIndex % walkUpFrames.length] : null;
            }
            break;
        case "down":
            if (dead) {
                image = deathDownFrames != null ? deathDownFrames[frameIndex % deathDownFrames.length] : null;
            } else if (hurt) {
                image = hurtDownFrames != null ? hurtDownFrames[frameIndex % hurtDownFrames.length] : null;
            } else if (attacking) {
                image = attackDownFrames != null ? attackDownFrames[frameIndex % attackDownFrames.length] : null;
            } else if (attacking3) {
                image = attack3DownFrames != null ? attack3DownFrames[frameIndex % attack3DownFrames.length] : null;
            } else if (!keyHandler.leftPressed && !keyHandler.rightPressed && !keyHandler.upPressed && !keyHandler.downPressed) {
                image = idleDownFrames != null ? idleDownFrames[frameIndex % idleDownFrames.length] : null;
            } else {
                image = walkDownFrames != null ? walkDownFrames[frameIndex % walkDownFrames.length] : null;
            }
            break;
        case "left":
            if (dead) {
                image = deathLeftFrames != null ? deathLeftFrames[frameIndex % deathLeftFrames.length] : null;
            } else if (hurt) {
                image = hurtLeftFrames != null ? hurtLeftFrames[frameIndex % hurtLeftFrames.length] : null;
            } else if (attacking) {
                image = attackLeftFrames != null ? attackLeftFrames[frameIndex % attackLeftFrames.length] : null;
            } else if (attacking3) {
                image = attack3LeftFrames != null ? attack3LeftFrames[frameIndex % attack3LeftFrames.length] : null;
            } else if (!keyHandler.leftPressed && !keyHandler.rightPressed && !keyHandler.upPressed && !keyHandler.downPressed) {
                image = idleLeftFrames != null ? idleLeftFrames[frameIndex % idleLeftFrames.length] : null;
            } else {
                image = walkLeftFrames != null ? walkLeftFrames[frameIndex % walkLeftFrames.length] : null;
            }
            break;
        case "right":
            if (dead) {
                image = deathRightFrames != null ? deathRightFrames[frameIndex % deathRightFrames.length] : null;
            } else if (hurt) {
                image = hurtRightFrames != null ? hurtRightFrames[frameIndex % hurtRightFrames.length] : null;
            } else if (attacking) {
                image = attackRightFrames != null ? attackRightFrames[frameIndex % attackRightFrames.length] : null;
            } else if (attacking3) {
                image = attack3RightFrames != null ? attack3RightFrames[frameIndex % attack3RightFrames.length] : null;
            } else if (!keyHandler.leftPressed && !keyHandler.rightPressed && !keyHandler.upPressed && !keyHandler.downPressed) {
                image = idleRightFrames != null ? idleRightFrames[frameIndex % idleRightFrames.length] : null;
            } else {
                image = walkRightFrames != null ? walkRightFrames[frameIndex % walkRightFrames.length] : null;
            }
            break;
        }

        // Draw the selected image
        if (image != null) {
            int drawWidth = gamePanel.tileSize * 2; //make the character bigger
            int drawHeight = gamePanel.tileSize * 2; // character bigger
            g2d.drawImage(image, gamePanel.getScreenWidth() / 2 - drawWidth / 2, gamePanel.getScreenHeight() / 2 - drawHeight / 2, drawWidth, drawHeight, null);
        } else {
            // DEBUG : default rectangle if no image is available
            g2d.setColor(Color.white);
            g2d.fillRect(gamePanel.getScreenWidth() / 2 - gamePanel.tileSize / 2, gamePanel.getScreenHeight() / 2 - gamePanel.tileSize / 2, gamePanel.tileSize, gamePanel.tileSize);
        }
        // Draw health bar
        g2d.setColor(Color.red);
        g2d.fillRect(10, 10, hp * 2, 10); // Adjust the size and position as needed
        g2d.setColor(Color.white);
        g2d.drawRect(10, 10, 200, 10); // Outline for the health bar

        // Draw mana bar
        g2d.setColor(Color.blue);
        g2d.fillRect(10, 30, mana * 2, 10); // Adjust the size and position as needed
        g2d.setColor(Color.white);
        g2d.drawRect(10, 30, 200, 10); // Outline for the mana bar

        // Draw the 'no mana' message
        if (abilityWhileAttakMessage) {
            g2d.setColor(Color.black);
            g2d.setFont(g2d.getFont().deriveFont(18f));
            g2d.drawString("Can't attack while hurt", 10, 60);
        }
    }


    


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 ) {
            if(!isHurtByMonster()) {
                attacking = true;
                frameIndex = 0; // Reset frame index for attack animation
                attackRegistered = false; // Reset the attack registered flag 
            } else { 
                System.out.println("player is hurt by monster !!!!!!!!!!!!!!!!!!! cant attack !!!!!!!!!!!!!!!!!!!!!!!!");
                abilityWhileAttakMessage = true;
                return;
            }
            
        } else if (e.getButton() == MouseEvent.BUTTON3 ) {
            if(!isHurtByMonster()) {
                if (mana >= 20) {
                    attacking3 = true;
                    frameIndex = 0; // Reset frame index for attack3 animation
                    attackRegistered = false; // Reset the attack registered flag
                    mana -= 20; // Decrease mana by 20
                    
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void setAttacking(boolean b) {
        
        attacking = attacking3 = b;
    }
}