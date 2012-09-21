package com.gregschoeninger.SquirrelScamper;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.gl.Camera2D;
import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.impl.GLScreen;
import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;
import com.badlogic.androidgames.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {
	Camera2D guiCam;
	SpriteBatcher batcher;
	Rectangle playBounds;
	Vector2 touchPoint;
	
	public MainMenuScreen(Game game){
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 100);
		playBounds = new Rectangle(0, 390, 320, 50);
		touchPoint = new Vector2();
	}
	
	@Override
	public void update(float deltaTime){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		for(int i = 0; i < touchEvents.size(); i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP)
				continue;
			
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(OverlapTester.pointInRectangle(playBounds, touchPoint)){
				//Play Click Sound
				Log.d("touch event", "overlap! "+touchPoint.x + " " + touchPoint.y);
				game.setScreen(new GameScreen(game));
				return;
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		batcher.beginBatch(Assets.mainMenuTexture);
		batcher.drawSprite(160, 240, 320, 480, Assets.mainMenuRegion);
		batcher.endBatch();
		
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
