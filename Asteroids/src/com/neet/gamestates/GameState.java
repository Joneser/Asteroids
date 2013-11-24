package com.neet.gamestates;

import com.neet.managers.GameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update(float dt);
	public abstract void draw();
	public abstract void hanedleInput();
	public abstract void dispose();

}
