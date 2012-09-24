package com.gregschoeninger.SquirrelScamper;


import com.badlogic.androidgames.framework.gl.SpriteBatcher;
import com.badlogic.androidgames.framework.gl.Texture;
import com.badlogic.androidgames.framework.gl.TextureRegion;
import com.badlogic.androidgames.framework.math.BigInt;

public class NumberRenderer {
	 public final Texture texture;
	    public final int glyphWidth;
	    public final int glyphHeight;
	    public final TextureRegion[] numbers = new TextureRegion[10];   
	    
	    public NumberRenderer(Texture texture, int offsetX, int offsetY, int glyphWidth, int glyphHeight) {        
	        this.texture = texture;
	        this.glyphWidth = glyphWidth;
	        this.glyphHeight = glyphHeight;
	        int x = offsetX;
	        int y = offsetY;
	        for(int i = 0; i < 10; i++) {
	        	numbers[i] = new TextureRegion(texture, x, y, glyphWidth, glyphHeight);
	            x += glyphWidth;
	        }        
	    }
	    
	    public void drawNumber(SpriteBatcher batcher, BigInt num, float x, float y) {
	        for(int i = num.size - 1; i >= 0; i--) {
	            TextureRegion glyph = numbers[num.digits[i]];
	            batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
	            x += glyphWidth;
	        }
	    }
}
