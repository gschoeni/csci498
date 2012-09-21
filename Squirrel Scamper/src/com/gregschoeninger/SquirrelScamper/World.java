package com.gregschoeninger.SquirrelScamper;

public class World {
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	
	public final Squirrel squirrel;
	
	public World(){
		this.squirrel = new Squirrel(5, 2);
	}

	public void update(float deltaTime, float accelX){
		updateSquirrel(deltaTime, accelX);
		//going to update more stuff here, just testing mr squirrel
	}
	
	public void updateSquirrel(float deltaTime, float accelX){
		squirrel.velocity.x = -accelX / 10 * squirrel.MOVE_VELOCITY;
		squirrel.update(deltaTime);
	}
}
