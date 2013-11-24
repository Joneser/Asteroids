package com.neet.gamestates;

import com.neet.managers.GameStateManager;

public class PlayState extends GameState {
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		init();
	}

	public void init() {

	}

	public void update(float dt) {
		System.out.println("Play state updating");
	}

	public void draw() {
		System.out.println("Play state drawing");
	}

	public void hanedleInput() {

	}

	public void dispose() {

	}

}
