package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.gl.Animation;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	public static Texture mainMenuTexture;
	public static Texture backgroundTexture;
	public static Texture foregroundTexture;
	
	public static TextureRegion mainMenuRegion;
	public static TextureRegion backgroundRegion1;
	public static TextureRegion backgroundRegion2;
	
	public static TextureRegion squirrelRight;
	public static TextureRegion squirrelLeft;
	public static TextureRegion acorn;
	
	public static Animation bird;
	
	
	public static TextureRegion readyText;
	public static TextureRegion pauseButton;
	
	public static void load(GLGame game){
		mainMenuTexture = new Texture(game, "MainMenu.png"); 
		backgroundTexture = new Texture(game, "FullBackground.png"); 
		foregroundTexture = new Texture(game, "ForegroundSprite.png");
		
		mainMenuRegion = new TextureRegion(mainMenuTexture, 0, 0, 320, 480);
		
		backgroundRegion1 = new TextureRegion(backgroundTexture, 0, 0, 320, 480);
		backgroundRegion2 = new TextureRegion(backgroundTexture, 0, 0, 320, 480);
		
		squirrelRight = new TextureRegion(foregroundTexture, 0, 0, 123, 140);
		squirrelLeft = new TextureRegion(foregroundTexture, 0, 140, 123, 140);
		readyText = new TextureRegion(foregroundTexture, 125, 0, 175, 50);
		pauseButton = new TextureRegion(foregroundTexture, 125, 50, 50, 50);
		acorn = new TextureRegion(foregroundTexture, 125, 140, 40, 45);
		
		bird = new Animation(0.1f,
				new TextureRegion(foregroundTexture, 165, 50, 190, 153), 
				new TextureRegion(foregroundTexture, 165, 203, 190, 153), 
				new TextureRegion(foregroundTexture, 165, 356, 190, 153), 
				new TextureRegion(foregroundTexture, 165, 509, 190, 153));
		
		
	}
	
	public static void reload(){
		mainMenuTexture.reload();
		backgroundTexture.reload();
		foregroundTexture.reload();
	}
}
