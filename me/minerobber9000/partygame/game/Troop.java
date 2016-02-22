package me.minerobber9000.partygame.game;

public class Troop extends GameObject {
	
	int cost;
	int atk;
	int def;
	int health;
	
	public Troop() {
		super();
		cost = 1;
		atk = 0;
		def = 1;
		health = 0;
	}
	
	public void attack(Troop t) {
		t.takeHit(atk);
	}
	
	public void takeHit(int amt) {
		health -= ((amt-1) / def) - 1;
		if (health <= 0) {
			this.kill();
		}
	}
	
}
