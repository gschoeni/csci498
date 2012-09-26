package com.gregschoeninger.SquirrelScamper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.badlogic.androidgames.framework.math.OverlapTester;

public class World {
	public static final float TREE_WIDTH = 8;
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_READY = 1;
	
	public final Squirrel squirrel;
	public List<Acorn> acorns;
	public List<Bird> birds;
	public List<Hole> holes;
	
	public final Random rand;
	public int score;
	public int state;
	
	public World(){
		this.squirrel = new Squirrel(5, 2);
		this.acorns = new ArrayList<Acorn>();
		this.birds = new ArrayList<Bird>();
		this.holes = new ArrayList<Hole>();
		
		rand = new Random();
		generateWorld();
		this.score = 0;
		this.state = WORLD_STATE_READY;
	}
	
	private void generateWorld(){
		float y = 3;
		
		while(y < WORLD_HEIGHT){
			if (rand.nextFloat() < 0.1f) {
				float x = rand.nextFloat() * (WORLD_WIDTH - Acorn.WIDTH) + Acorn.WIDTH / 2;
				Acorn acorn = new Acorn(x, y);
				acorns.add(acorn);
			}
			
			if (rand.nextFloat() < 0.05f) {
				float x = rand.nextFloat() * (WORLD_WIDTH - Bird.WIDTH) + Bird.WIDTH / 2;
				Bird bird = new Bird(x, y);
				birds.add(bird);
			}
			
			if (rand.nextFloat() < 0.01f) {
				float x = rand.nextFloat() * (TREE_WIDTH - Hole.WIDTH) + Hole.WIDTH / 2 + 1;
				Hole hole = new Hole(x, y);
				holes.add(hole);
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
		checkSquirrelAcornCollisions();
		checkBirdSquirrelCollisions();
		checkSquirrelHoleCollisions();
		checkFiringAcornBirdCollisions();
	}
	
	private void checkSquirrelAcornCollisions(){
		for (int i = 0; i < acorns.size(); i++) {
			Acorn a = acorns.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, a.bounds)) {
				squirrel.acorns.add(new Acorn(squirrel.position.x, squirrel.position.y));
				acorns.remove(i);
				score += Acorn.SCORE;
				break; 
			}
		}
	}
	
	private void checkBirdSquirrelCollisions(){
		for (int i = 0; i < birds.size(); i++) {
			Bird b = birds.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, b.bounds)) {
				birds.remove(i);
				
				//Bird.SCORE is negative
				if(score >  Bird.SCORE * -1){
					score += Bird.SCORE;
				} else {
					score = 0;
				}
				
				break; 
			}
		}
	}
	
	private void checkSquirrelHoleCollisions(){
		for (int i = 0; i < holes.size(); i++) {
			Hole h = holes.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, h.bounds)) {
				restart(0);
				break; 
			}
		}
	}
	
	private void checkFiringAcornBirdCollisions(){
		for (int i = 0; i < squirrel.firingAcorns.size(); i++) {
			Acorn a = squirrel.firingAcorns.get(i);
			for (int j = 0; j < birds.size(); j++) {
				Bird b = birds.get(j);
				if (OverlapTester.overlapRectangles(a.bounds, b.bounds)) {
					birds.remove(j);
					squirrel.firingAcorns.remove(i);
					break;
				}
			}
		}
	}
	
	private void checkRestart(){
		if(squirrel.position.y > WORLD_HEIGHT){
			restart(0);
		}
	}
	
	private void restart(int level){
		state = WORLD_STATE_READY;
		score = 0;
		squirrel.position.y = 0;
		generateWorld();
	}
}
