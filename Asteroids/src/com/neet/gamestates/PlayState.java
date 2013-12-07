package com.neet.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.entities.Bullet;
import com.neet.entities.Player;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;

public class PlayState extends GameState {
	
	private ShapeRenderer sr;
	
	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		
	}

	public void init() {
		
		sr = new ShapeRenderer();
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets);
		
		asteroids = new ArrayList<Asteroid>();
		
		level = 1;
		spawnAsteroids();

	}
	
	private void spawnAsteroids() {
		asteroids.clear();
		
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		
		for(int i = 0; i < numToSpawn; i++) {
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			float dx = x - player.getX();
			float dy = y - player.getY();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			while(dist < 100) {
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				
				dx = x - player.getX();
				dy = y - player.getY();
				dist = (float) Math.sqrt(dx * dx + dy * dy);
			}
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
			
		}
		
		
	}

	public void update(float dt) {
		
		// get user input
		handleInput();
		
		// update player
		player.update(dt);
		
		// update player bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}
		
		// update asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
		}
	}

	public void draw() {
		// draw player
		player.draw(sr);
		
		// draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(sr);
		}
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
	}

	public void handleInput() {
		player.setLeft(GameKeys.isDown(GameKeys.LEFT));
		player.setRight(GameKeys.isDown(GameKeys.RIGHT));
		player.setUp(GameKeys.isDown(GameKeys.UP));
		if(GameKeys.isPressed(GameKeys.SPACE)) {
			player.shoot();
		}
	}

	public void dispose() {

	}

}
