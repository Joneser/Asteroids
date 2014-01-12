package com.neet.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.HighscoreManager;
import com.neet.managers.Score;

public class HighscoreState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private HighscoreManager hm;
	
	private int currentChoice = 0;
	
	private BitmapFont font;
	
	private ArrayList<Asteroid> asteroids;
		
	private int level;

	public HighscoreState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		hm = new HighscoreManager();
		
		// set font
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		font = gen.generateFont(20);		
		
		asteroids = new ArrayList<Asteroid>();
				
		level = 1;
		spawnAsteroids();
		
	}
	
	private void spawnAsteroids() {
		asteroids.clear();
		
		int numToSpawn = 4 + level - 1;
		
		for(int i = 0; i < numToSpawn; i++) {
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}	
	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		// update asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
		}
		
	}

	@Override
	public void draw() {
		sb.setColor(1, 1, 1, 1);
		sb.begin();
				
		ArrayList<Score> scores;
		scores = hm.getScores();
		
		int max = 8;
		
		int index = scores.size() - 1;
		
		if(index > max) {
			index = max - 1;
		}
		int x = -1;

		while (index != x) {
		    font.draw(sb, scores.get(index).getNaam() + ": " + scores.get(index).getScore(), 180, 350 - index * 30);
		    index--;
		}		
		
		font.draw(sb, "->", 120, 50);
		font.draw(sb, "Back to Main Menu", 160, 50);
		sb.end();
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(0);
		}
	}

	@Override
	public void handleInput() {
		
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
