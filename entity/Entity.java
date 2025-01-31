package entity;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public BufferedImage idle, walk, attack1;
    public boolean isMoving, isAttacking;
    public int direction;
    public boolean dead;
    public boolean disappearing;

}

