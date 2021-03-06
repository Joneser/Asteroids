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

public class MenuState extends GameState   {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private int currentChoice = 0;
	
	private String[] options = {
			"Play", "Highscores", "Quit"
	};
	
	private BitmapFont font;
	
	private ArrayList<Asteroid> asteroids;
		
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		// set font
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		font = gen.generateFont(20);		
		
		asteroids = new ArrayList<Asteroid>();
				
		spawnAsteroids();
		
	}
	
	private void spawnAsteroids() {
		asteroids.clear();
		
		for(int i = 0; i < 4; i++) {
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
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				font.draw(sb, "->", 170, 240 - i * 30);
			}
			
			font.draw(sb, options[i], 210, 240 - i * 30);
		}
		sb.end();
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(1);
		}
		
		if(currentChoice == 1) {
			gsm.setState(2);
		}
		
		if(currentChoice == 2) {
			Gdx.app.exit();
		}
	}

	@Override
	public void handleInput() {
		
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}
		
		if(GameKeys.isPressed(GameKeys.DOWN)) {
			currentChoice++;
			if(currentChoice > 2) {
				currentChoice = 0;
			}
		}
		
		if(GameKeys.isPressed(GameKeys.UP)) {
			currentChoice--;
			if(currentChoice < 0) {
				currentChoice = 2;
			}
		}
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	
}
