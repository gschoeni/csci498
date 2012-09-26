package com.gregschoeninger.SquirrelScamper;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.BigInt;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class GameScreen extends GLScreen {
	
	Camera2D guiCam;
	SpriteBatcher batcher;
	World world;
	WorldRenderer renderer;
	Vector2 touchPoint;
	Rectangle pauseBounds;
	Rectangle fireBounds;
	Rectangle readyBounds;
	BigInt lastScore;

	NumberRenderer numberRenderer;
	
	public GameScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 1000);
		world = new World();
		touchPoint = new Vector2();
		renderer = new WorldRenderer(glGraphics, batcher, world);
		numberRenderer = new NumberRenderer(Assets.foregroundTexture, 300, 0, 35, 50);
		
		pauseBounds = new Rectangle(20, 400, 50, 50);
		readyBounds = new Rectangle(0, 200, 320, 100);
		fireBounds = new Rectangle(210, 0, 110, 100);
		
		lastScore = new BigInt(0);
	}

	@Override
	public void update(float deltaTime) {
		//we limit deltaTime to 0.1f because of a bug in ByteBuffers on android 1.5 that would make
		//our game be interrupted by the garbage collector and not run as smooth (see page 477)
		if(deltaTime > 0.1f) 
			deltaTime = 0.1f;
		switch(world.state) { 
			case World.WORLD_STATE_READY:
				updateReady();
				break;
			case World.WORLD_STATE_RUNNING:
				updateRunning(deltaTime);
				break;
		}
	}
	
	private void updateReady(){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
				continue;
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(OverlapTester.pointInRectangle(readyBounds, touchPoint)){
				world.state = World.WORLD_STATE_RUNNING;
				return;
			}
		}
	}
	
	private void updateRunning(float deltaTime){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
				continue;
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)){
				world.state = World.WORLD_STATE_READY;
				return;
			}
			
			if(OverlapTester.pointInRectangle(fireBounds, touchPoint)){
				world.squirrel.fireAcorn();
				return;
			}
		}
		
		world.update(deltaTime, game.getInput().getAccelX());
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.foregroundTexture);
		
		guiCam.setViewportAndMatrices();
		
		switch(world.state){
			case World.WORLD_STATE_READY:
				presentReady();
				break;
			case World.WORLD_STATE_RUNNING:
				presentRunning();
				break;
		}
		
		
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	private void presentReady(){
		batcher.drawSprite(160, 240, 160, 32, Assets.readyText);
	}
	
	private void presentRunning(){
		//Draw the pause button
		batcher.drawSprite(pauseBounds.lowerLeft.x+10, 
							pauseBounds.lowerLeft.y + pauseBounds.height, 
							pauseBounds.width, 
							pauseBounds.height, 
							Assets.pauseButton);
		
		//Draw the fire button
		batcher.drawSprite(250, 
							20, 
							110, 
							50, 
							Assets.fireButton);
		
		
		//Draw the score
		if(world.score != lastScore.intValue){
			lastScore = new BigInt(world.squirrel.acorns.size()); 
		}
		
		
		numberRenderer.drawNumber(batcher, lastScore, 20, 20);
	}
	
	
	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
