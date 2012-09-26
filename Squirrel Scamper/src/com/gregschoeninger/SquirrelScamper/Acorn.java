package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.DynamicGameObject;

public class Acorn extends DynamicGameObject {
	public static final float WIDTH = 0.88f;
	public static final float HEIGHT = 1.0f;
	public static final int SCORE = 10;
	public static final float FIRE_VELOCITY = 0.03f;
	public boolean firing = false;
	
	public Acorn(float x, float y){
		super(x, y, WIDTH, HEIGHT);
	}
	
	public void update(float deltaTime){
		if(firing){
			velocity.y += FIRE_VELOCITY;
			position.add(velocity.x, velocity.y); 
			bounds.lowerLeft.set(position).sub(WIDTH / 2, HEIGHT / 2);
		}
	}
	
	public void fire(float x, float y){
		firing = true;
		
		this.position.x = x;
		this.position.y = y;
	}

}
