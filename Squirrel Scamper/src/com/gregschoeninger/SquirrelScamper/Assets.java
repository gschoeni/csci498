package com.gregschoeninger.SquirrelScamper;

import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.impl.GLGame;

public class Assets {
	public static Texture mainMenuTexture;
	public static Texture backgroundTexture;
	public static Texture foregroundTexture;
	
	public static TextureRegion mainMenuRegion;
	public static TextureRegion backgroundRegion;
	public static TextureRegion squirrel;
	public static TextureRegion readyText;
	public static TextureRegion pauseButton;
	
	public static void load(GLGame game){
		mainMenuTexture = new Texture(game, "MainMenu.png"); 
		backgroundTexture = new Texture(game, "background.png"); 
		foregroundTexture = new Texture(game, "ForegroundSprite.png");
		
		backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 320, 480);
		mainMenuRegion = new TextureRegion(mainMenuTexture, 0, 0, 320, 480);
		squirrel = new TextureRegion(foregroundTexture, 0, 0, 100, 150);
		readyText = new TextureRegion(foregroundTexture, 100, 0, 200, 50);
		pauseButton = new TextureRegion(foregroundTexture, 100, 50, 50, 50);
		
	}
	
	public static void reload(){
		mainMenuTexture.reload();
		backgroundTexture.reload();
		foregroundTexture.reload();
	}
}
