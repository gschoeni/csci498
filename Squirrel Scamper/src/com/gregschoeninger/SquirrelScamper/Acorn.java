package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.GameObject;

public class Acorn extends GameObject {
	public static final float WIDTH = 0.88f;
	public static final float HEIGHT = 1.0f;
	public static final int SCORE = 10;
	public static final float FIRE_VELOCITY = 0.5f;
	public boolean firing = false;
	
	public Acorn(float x, float y){
		super(x, y, WIDTH, HEIGHT);
	}
	
	public void fire(float y){
		firing = true;
		while(firing){
			this.position.y += y + FIRE_VELOCITY;
		}
	}

}
