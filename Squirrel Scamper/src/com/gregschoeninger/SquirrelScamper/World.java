package com.gregschoeninger.SquirrelScamper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.androidgames.framework.math.OverlapTester;

public class World {
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_READY = 1;
	
	public final Squirrel squirrel;
	public List<Acorn> acorns;
	public List<Bird> birds;
	
	public final Random rand;
	public int score;
	public int state;
	
	public World(){
		this.squirrel = new Squirrel(5, 2);
		this.acorns = new ArrayList<Acorn>();
		this.birds = new ArrayList<Bird>();
		
		rand = new Random();
		generateWorld();
		this.score = 0;
		this.state = WORLD_STATE_READY;
	}
	
	private void generateWorld(){
		float y = 3;
		
		while(y < WORLD_HEIGHT){
			if (rand.nextFloat() > 0.6f) {
				float x = rand.nextFloat() * (WORLD_WIDTH - Acorn.WIDTH) + Acorn.WIDTH / 2;
				Acorn acorn = new Acorn(x, y);
				acorns.add(acorn);
			}
			
			if (rand.nextFloat() < 0.1f) {
				float x = rand.nextFloat() * (WORLD_WIDTH - Bird.WIDTH) + Bird.WIDTH / 2;
				Bird bird = new Bird(x, y);
				birds.add(bird);
			}
			y += rand.nextFloat() * 3;
		}
	}
	

	public void update(float deltaTime, float accelX){
		if(state == WORLD_STATE_RUNNING){
			updateSquirrel(deltaTime, accelX);
			updateBirds(deltaTime);
			checkCollisions();
			checkRestart();
		}
	}
	
	private void updateSquirrel(float deltaTime, float accelX){
		squirrel.velocity.x = -accelX * squirrel.VELOCITY_X;
		squirrel.update(deltaTime);
	}
	
	private void updateBirds(float deltaTime){
		for(int i = 0; i < birds.size(); i++){
			Bird b = birds.get(i);
			b.update(deltaTime);
		}
	}
	
	private void checkCollisions(){
		for (int i = 0; i < acorns.size(); i++) {
			Acorn a = acorns.get(i);
			if (a.position.y > squirrel.position.y) {
				if (OverlapTester.overlapRectangles(squirrel.bounds, a.bounds)) {
					acorns.remove(i);
					score += Acorn.SCORE;
					break; 
				}
			} 
		}
	}
	
	private void checkRestart(){
		if(squirrel.position.y > WORLD_HEIGHT){
			state = WORLD_STATE_READY;
			score = 0;
			squirrel.position.y = 0;
			generateWorld();
		}
	}
}
