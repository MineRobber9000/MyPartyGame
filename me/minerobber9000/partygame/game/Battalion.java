package me.minerobber9000.partygame.game;

public class Battalion extends Troop {
	
	int soldiers = 0;
	
	public Battalion(int amt) {
		this.soldiers = amt;
		cost = 1000 * amt;
		health = amt * 10;
	}
	
	public void recalculateHealth() {
		health = soldiers * 10;
		if (health <= 0) {
			this.kill();
		}
	}
	
}
