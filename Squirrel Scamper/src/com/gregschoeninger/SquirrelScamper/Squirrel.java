package com.gregschoeninger.SquirrelScamper;

import java.util.ArrayList;
import java.util.List;


import android.util.Log;

import com.badlogic.androidgames.framework.DynamicGameObject;

public class Squirrel extends DynamicGameObject {
	public static final float SQUIRREL_WIDTH = 2.0f;
	public static final float SQUIRREL_HEIGHT = 2.5f;
	public final float VELOCITY_X = 4;
	
	public final float VELOCITY_Y = 0.08f;
	public final float SPEEDING_VELOCITY_Y = 0.15f;
	
	public List<Acorn> acorns;
	public List<Acorn> firingAcorns;
	
	float stateTime = 0;
	boolean speeding = false;
	
	public Squirrel(float x, float y) {
		super(x, y, SQUIRREL_WIDTH, SQUIRREL_HEIGHT);
		this.acorns = new ArrayList<Acorn>();
		this.firingAcorns = new ArrayList<Acorn>();
	}
	
	public void update(float deltaTime){
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
		
		float halfSquirrelWidth = SQUIRREL_WIDTH / 2;
		if(position.x < halfSquirrelWidth)
			position.x = halfSquirrelWidth;
		
		if(position.x > World.WORLD_WIDTH - halfSquirrelWidth)
			position.x = World.WORLD_WIDTH - halfSquirrelWidth;
		
		if(!speeding){
			this.position.y += VELOCITY_Y;
		} else {
			this.position.y += SPEEDING_VELOCITY_Y;
			stateTime += deltaTime;
			if(stateTime > 5){
				speeding = false;
				stateTime = 0;
			}
		}
		
	}
	
	public void fireAcorn(){
		if(acorns.size() > 0){
			int i = acorns.size() - 1;
			
			//add to squirrels firing acorns and remove from his acorns
			firingAcorns.add(new Acorn(acorns.get(i).position.x, acorns.get(i).position.y));
			acorns.remove(i);
			
			//fire the acorn
			Acorn acornToBeFired = firingAcorns.get(firingAcorns.size() - 1);
			acornToBeFired.fire(this.position.x, this.position.y+SQUIRREL_HEIGHT/2);
		}
	}
	
	public void speedUp(){
		this.speeding = true;
	}

}
