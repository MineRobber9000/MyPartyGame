package me.minerobber9000.partygame.game;

public class GameObject {
	
	int lives;
	boolean isAlive = true;
	String name = "OBJECT";
	
	public GameObject() {
		this.lives = 1;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void kill() {
		this.isAlive = false;
	}

}
