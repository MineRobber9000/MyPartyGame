package me.minerobber9000.partygame.game;

import java.util.*;

public class Player extends GameObject{
	
	private int score;
	private int balance;
	ArrayList<Tank> tanks = new ArrayList<Tank>(); 
	Battalion b;
	
	public Player(int startingLives) {
		this.score = 0;
		this.name = "Player";
		this.lives = startingLives;
		this.balance = 100000000;
	}
	
	public boolean isDead() {
		lives--;
		if (lives == 0) {
			this.kill();
			return true;
		} else {
			return false;
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int i) {
		score += i;
	}
	
	public void removeScore(int i) {
		score -= i;
		if (score <= 0) {
			score = 0;
		}
	}
	
	public int getMoney() {
		return balance;
	}
	
	public void pay(int i) {
		balance -= i;
	}
	
	public void paidBack(int i) {
		balance += i;
	}
	
}
