package com.neet.managers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Score implements Serializable {

	private long score;
	private String naam;
	
	public long getScore() {
		return score;
	}
	
	public String getNaam() {
		return naam;
	}
	
	public Score(String naam, long score) {
		this.score = score;
		this.naam = naam;
	}
}
