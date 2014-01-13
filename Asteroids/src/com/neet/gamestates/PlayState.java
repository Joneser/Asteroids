package com.neet.gamestates;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.entities.Bullet;
import com.neet.entities.Particle;
import com.neet.entities.Player;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.HighscoreManager;

public class PlayState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private HighscoreManager hm;
	
	private BitmapFont font;
	private FreeTypeFontGenerator gen;
	
	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	
	private ArrayList<Particle> particles;
	
	private String[] alpha = {
			" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "."
	};
	
	private String[] playerName = { "A", "A", "A" };
	
	private int playerNameIndex = 0;
	private int alphaIndex = 1;
	
	private int level;
	private boolean isOver = false;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		
	}

	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		hm = new HighscoreManager();
		
		// set font
		gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets);
		
		asteroids = new ArrayList<Asteroid>();
		
		particles = new ArrayList<Particle>();
		
		level = 1;
		spawnAsteroids();

	}
	
	private void createParticles(float x, float y) {
		for(int i = 0; i < 6; i++) {
			particles.add(new Particle(x, y));
		}
	}
	
	private void splitAsteroids(Asteroid a) {
		createParticles(a.getX(), a.getY());
		if(a.getType() == Asteroid.LARGE) {
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
		}
		if(a.getType() == Asteroid.MEDIUM) {
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));
		}
	}
	
	
	private void spawnAsteroids() {
		asteroids.clear();
		
		int numToSpawn = 4 + level - 1;
		
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
		
		// next level
		if(asteroids.size() == 0) {
			level++;
			spawnAsteroids();
		}
		
		// update player
		player.update(dt);
		if(player.isDead()) {
			player.reset();
			player.loseLife();
			if(player.getLives() == 0) {				
				isOver = true;
			}
			return;
		}
		
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
		
		// update particles
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update(dt);
			if(particles.get(i).shouldRemove()) {
				particles.remove(i);
				i--;
			}
		}
		
		// check collisions
		checkCollisions();
	}
	
	private void checkCollisions() {
		
		// player - asteroid collision
		if(!player.isHit()) {
			for(int i = 0; i < asteroids.size(); i++) {
				Asteroid a = asteroids.get(i);
				if(a.intersects(player)) {
					player.hit();
					asteroids.remove(i);
					i--;
					splitAsteroids(a);
					break;
				}
			}
		}

		// bullet - asteroid collisions
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				if(a.contains(b.getX(), b.getY())) {
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					player.incrementScore(a.getScore());
					
					break;
				}
			}
		}
		
		
	}

	public void draw() {
		// draw player
		if(!isOver) {
			player.draw(sr);	
		}
		
		// draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(sr);
		}
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
		// draw particles
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).draw(sr);
		}
		
		// draw score
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		font = gen.generateFont(20);
		font.draw(sb, Long.toString(player.getScore()), 40, 390);
		if(isOver) {
			font = gen.generateFont(50);
			for(int i = 0; i < playerName.length; i++) {
				font.draw(sb, playerName[i], 140 + 100 * i, 300);
			}
		}
		sb.end();
		
	}

	public void handleInput() {
		if(!isOver) {
			player.setLeft(GameKeys.isDown(GameKeys.LEFT));
			player.setRight(GameKeys.isDown(GameKeys.RIGHT));
			player.setUp(GameKeys.isDown(GameKeys.UP));
			if(GameKeys.isPressed(GameKeys.SPACE)) {
				player.shoot();
			}	
		} else {
			// Handling highscore name
			if(GameKeys.isPressed(GameKeys.LEFT)) {
				playerNameIndex--;
				if(playerNameIndex < 0) {
					playerNameIndex = 2;
				}
				alphaIndex = Arrays.asList(alpha).indexOf(playerName[playerNameIndex]);
			}
			if(GameKeys.isPressed(GameKeys.RIGHT)) {
				playerNameIndex++;
				if(playerNameIndex > 2) {
					playerNameIndex = 0;
				}
				alphaIndex = Arrays.asList(alpha).indexOf(playerName[playerNameIndex]);
			}
			if(GameKeys.isPressed(GameKeys.UP)) {
				alphaIndex++;
				if(alphaIndex > 27) {
					alphaIndex = 0;
				}
				playerName[playerNameIndex] = alpha[alphaIndex];
			}
			if(GameKeys.isPressed(GameKeys.DOWN)) {
				alphaIndex--;
				if(alphaIndex < 0) {
					alphaIndex = 27;
				}
				playerName[playerNameIndex] = alpha[alphaIndex];
			}
			if(GameKeys.isPressed(GameKeys.ENTER)) {
				// On pressing enter, save the name and highschore, and navigate to the highscores screen
				hm.addScore(playerName[0] + " " + playerName[1] + " " + playerName[2], player.getScore());
				gsm.setState(2);
			}
		}
	}

	public void dispose() {

	}

}
