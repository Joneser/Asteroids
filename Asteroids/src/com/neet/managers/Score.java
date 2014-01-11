package com.neet.managers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Score implements Serializable {

	private int score;
	private String naam;
	
	public int getScore() {
		return score;
	}
	
	public String getNaam() {
		return naam;
	}
	
	public Score(String naam, int score) {
		this.score = score;
		this.naam = naam;
	}
}
