package com.gregschoeninger.SquirrelScamper;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	
	public WorldRenderer(GLGraphics g, SpriteBatcher b, World w){
		this.glGraphics = g;
		this.batcher = b;
		this.world = w;
		this.cam = new Camera2D(g, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	}
	
	public void render(){
		cam.setViewportAndMatrices();
		renderBackground();
		renderForeground();
	}
	
	private void renderBackground(){
		batcher.beginBatch(Assets.backgroundTexture);
		batcher.drawSprite(cam.position.x, cam.position.y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();
	}
	
	private void renderForeground(){
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.foregroundTexture);
		renderSquirrel();
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderSquirrel(){
		if(world.squirrel.velocity.x < 0)
			batcher.drawSprite(world.squirrel.position.x, world.squirrel.position.y, Squirrel.SQUIRREL_WIDTH, Squirrel.SQUIRREL_HEIGHT, Assets.squirrelLeft);
		else 
			batcher.drawSprite(world.squirrel.position.x, world.squirrel.position.y, Squirrel.SQUIRREL_WIDTH, Squirrel.SQUIRREL_HEIGHT, Assets.squirrelRight);
	}
	
	

}
