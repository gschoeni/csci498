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
	
	public static void load(GLGame game){
		mainMenuTexture = new Texture(game, "MainMenu.png"); 
		backgroundTexture = new Texture(game, "background.png"); 
		foregroundTexture = new Texture(game, "SquirrelRight.png");
		
		backgroundRegion = new TextureRegion(backgroundTexture, 0, 0, 320, 480);
		mainMenuRegion = new TextureRegion(mainMenuTexture, 0, 0, 320, 480);
		squirrel = new TextureRegion(foregroundTexture, 0, 0, 300, 370);
		
	}
	
	public static void reload(){
		mainMenuTexture.reload();
		backgroundTexture.reload();
		foregroundTexture.reload();
	}
}
