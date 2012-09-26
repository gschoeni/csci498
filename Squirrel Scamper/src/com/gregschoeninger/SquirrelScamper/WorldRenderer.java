package com.gregschoeninger.SquirrelScamper;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLGraphics;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	Background background;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	
	public WorldRenderer(GLGraphics g, SpriteBatcher b, World w){
		this.glGraphics = g;
		this.batcher = b;
		this.world = w;
		this.cam = new Camera2D(g, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.background = new Background(Assets.backgroundRegion1, Assets.backgroundRegion2);
	}
	
	public void render(){
		//climb with the squirrel
		if(world.squirrel.position.y + 5.5f > cam.position.y)
			cam.position.y = world.squirrel.position.y + 5.5f;
		
		//make sure we reset back to him on restart
		if(world.squirrel.position.y + 5.0f < cam.position.y)
			cam.position.y = world.squirrel.position.y + 5.5f;
		
		cam.setViewportAndMatrices();
		renderBackground();
		renderForeground();
	}
	
	private void renderBackground(){
		batcher.beginBatch(Assets.backgroundTexture);
		if(world.state == World.WORLD_STATE_RUNNING){
			background.update();
		}
		
		batcher.drawSprite(cam.position.x, cam.position.y+background.region1_y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, background.region1);
		batcher.drawSprite(cam.position.x, cam.position.y+background.region2_y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, background.region2);
		batcher.endBatch();
	}
	
	private void renderForeground(){
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.foregroundTexture);
		renderHoles();
		renderSquirrel();
		renderAcorns();
		renderBirds();
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void renderSquirrel(){
		if(world.squirrel.velocity.x < 0)
			batcher.drawSprite(world.squirrel.position.x, world.squirrel.position.y, Squirrel.SQUIRREL_WIDTH, Squirrel.SQUIRREL_HEIGHT, Assets.squirrelLeft);
		else 
			batcher.drawSprite(world.squirrel.position.x, world.squirrel.position.y, Squirrel.SQUIRREL_WIDTH, Squirrel.SQUIRREL_HEIGHT, Assets.squirrelRight);
	}
	
	private void renderAcorns(){
		for(int i = 0; i < world.acorns.size(); i++){
			Acorn a = world.acorns.get(i);
			batcher.drawSprite(a.position.x, a.position.y, Acorn.WIDTH, Acorn.HEIGHT, Assets.acorn);
		}
		
		//render any acorns that are being fired
		for(int i = 0; i < world.squirrel.firingAcorns.size(); i++){
			Acorn a = world.squirrel.firingAcorns.get(i);
			if(a.firing){
				a.update();
				batcher.drawSprite(a.position.x, a.position.y, Acorn.WIDTH, Acorn.HEIGHT, Assets.acorn);
			}
		}
	}

	private void renderHoles(){
		for(int i = 0; i < world.holes.size(); i++){
			Hole h = world.holes.get(i);
			batcher.drawSprite(h.position.x, h.position.y, Hole.WIDTH, Hole.HEIGHT, Assets.hole);
		}
	}
	
	
	private void renderBirds(){
		for(int i = 0; i < world.birds.size(); i++){
			Bird b = world.birds.get(i);
			float side = b.velocity.x < 0 ? 1 : -1;
			batcher.drawSprite(b.position.x, b.position.y, Bird.WIDTH * side, Bird.HEIGHT, Assets.bird.getKeyFrame(b.stateTime, Animation.ANIMATION_LOOPING));
		}
	}
	
	
	

}
