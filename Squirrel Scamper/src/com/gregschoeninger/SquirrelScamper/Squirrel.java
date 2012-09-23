package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.DynamicGameObject;

public class Squirrel extends DynamicGameObject {
	public static final float SQUIRREL_WIDTH = 2.0f;
	public static final float SQUIRREL_HEIGHT = 2.5f;
	public final float VELOCITY_X = 40;
	public final float VELOCITY_Y = 0.1f;
	
	public Squirrel(float x, float y) {
		super(x, y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT);
	}
	
	public void update(float deltaTime){
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		float halfSquirrelWidth = SQUIRREL_WIDTH / 2;
		if(position.x < halfSquirrelWidth)
			position.x = halfSquirrelWidth;
		
		if(position.x > World.WORLD_WIDTH - halfSquirrelWidth)
			position.x = World.WORLD_WIDTH - halfSquirrelWidth;
		
		this.position.y += VELOCITY_Y;
	}

}
