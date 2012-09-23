package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.GameObject;

public class Acorn extends GameObject {
	public static final float WIDTH = 0.75f;
	public static final float HEIGHT = 1.0f;
	public static final int SCORE = 10;
	
	public Acorn(float x, float y){
		super(x, y, WIDTH, HEIGHT);
	}

}
