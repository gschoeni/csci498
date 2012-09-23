package com.gregschoeninger.SquirrelScamper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.math.OverlapTester;

public class World {
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	
	public final Squirrel squirrel;
	public List<Acorn> acorns;
	
	public final Random rand;
	
	public World(){
		this.squirrel = new Squirrel(5, 2);
		this.acorns = new ArrayList<Acorn>();
		rand = new Random();
		generateWorld();
	}
	
	private void generateWorld(){
		float y = 0;
		
		while(y < WORLD_HEIGHT){
			if (rand.nextFloat() > 0.6f) {
				float x = rand.nextFloat() * (WORLD_WIDTH - Acorn.WIDTH) + Acorn.WIDTH / 2;
				Acorn acorn = new Acorn(x, y);
				acorns.add(acorn);
			}
			y += rand.nextFloat() * 3;
		}
	}
	

	public void update(float deltaTime, float accelX){
		updateSquirrel(deltaTime, accelX);
		checkCollisions();
	}
	
	public void updateSquirrel(float deltaTime, float accelX){
		squirrel.velocity.x = -accelX / 10 * squirrel.VELOCITY_X;
		squirrel.update(deltaTime);
	}
	
	private void checkCollisions(){
		for (int i = 0; i < acorns.size(); i++) {
			Acorn a = acorns.get(i);
			if (a.position.y > squirrel.position.y) {
				if (OverlapTester.overlapRectangles(squirrel.bounds, a.bounds)) {
					acorns.remove(i);
					break; 
				}
			} 
		}
	}
}
