package com.gregschoeninger.SquirrelScamper;


import com.badlogic.androidgames.framework.gl.TextureRegion;

public class Background {
	public static final int HEIGHT = 15;
	public static final int WIDTH = 10;
	public TextureRegion region1;
	public TextureRegion region2;
	
	public float region1_y = 0;
	public float region2_y = HEIGHT;
	
	public Background(TextureRegion r1, TextureRegion r2){
		this.region1 = r1;
		this.region2 = r2;
		region1_y = 0;
		region2_y = HEIGHT;
	}
	
	public void update(float velocity){
		region1_y -= velocity;
		region2_y -= velocity;
		
		if(region1_y <= -HEIGHT){
			region1_y = 0;
		}
		
		if(region2_y <= 0){
			region2_y = HEIGHT;
		}
	}

}
