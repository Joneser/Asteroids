package com.neet.managers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighscoreManager {
	
	private ArrayList<Score> scores = new ArrayList<Score>();
	
	private static final String HIGHSCORE_FILE = "score.dat";
	
	ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;
	
	public ArrayList<Score> getScores() {
		loadScoreFile();
		sort();
		return scores;
	}
	
	private void sort() {
		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(scores, comparator);
	}
	
	public void addScore(String name, long score) {
		loadScoreFile();
		scores.add(new Score(name, score));
		updateScoreFile();
	}
	
	@SuppressWarnings("unchecked")
	public void loadScoreFile() {
		try {
			inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
			scores = (ArrayList<Score>) inputStream.readObject();
		} catch (FileNotFoundException e) {
            System.out.println("[Laad] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Laad] CNF Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Laad] IO Error: " + e.getMessage());
            }
		}
	}
	
	public void updateScoreFile() {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
	}

}
