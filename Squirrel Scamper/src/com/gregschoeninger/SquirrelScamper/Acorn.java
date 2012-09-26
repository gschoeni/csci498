package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.DynamicGameObject;

public class Acorn extends DynamicGameObject {
	public static final float WIDTH = 0.88f;
	public static final float HEIGHT = 1.0f;
	public static final int SCORE = 10;
	public static final float FIRE_VELOCITY = 0.3f;
	public boolean firing = false;
	
	public Acorn(float x, float y){
		super(x, y, WIDTH, HEIGHT);
	}
	
	public void update(){
		if(firing){
			this.position.y += FIRE_VELOCITY;
		}
	}
	
	public void fire(float x, float y){
		firing = true;
		this.position.x = x;
		this.position.y = y;
	}

}
