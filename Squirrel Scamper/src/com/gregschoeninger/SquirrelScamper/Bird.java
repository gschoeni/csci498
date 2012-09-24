package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.DynamicGameObject;

public class Bird extends DynamicGameObject {
	public static final float WIDTH = 2;
	public static final float HEIGHT = 1.2f;
	public static final float VELOCITY = 3f;
	public static final int SCORE = -100;
	
	float stateTime = 0;
	
	public Bird(float x, float y){
		super(x, y, WIDTH, HEIGHT);
		velocity.set(VELOCITY, 0);
	}
	
	public void update(float deltaTime){
		position.add(velocity.x * deltaTime, velocity.y * deltaTime); 
		bounds.lowerLeft.set(position).sub(WIDTH / 2, HEIGHT / 2);
		if(position.x < WIDTH / 2 ) { 
			position.x = WIDTH / 2;
			velocity.x = VELOCITY; 
		}
		if(position.x > World.WORLD_WIDTH - WIDTH / 2) { 
			position.x = World.WORLD_WIDTH - WIDTH / 2;
			velocity.x = -VELOCITY; 
		}
		stateTime += deltaTime;
	}
}
