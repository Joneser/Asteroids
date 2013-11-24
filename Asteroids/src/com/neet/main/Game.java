package com.neet.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public void create() {
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
	}
	
	public void render() {
		
		// Clear screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
	}
	
	public void resize(int width, int height) {
		
	}  
	
	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	public void dispose() {
		
	}

}
