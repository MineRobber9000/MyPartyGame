package me.minerobber9000.partygame.game;

import java.util.*;

public class Tank extends Troop {
	
	Random r = new Random();
	static int cost = 10000;
	
	public Tank() {
		atk = 100000;
		def = 1000;
		lives = 1;
		health = 100000;
	}
	
	public void attack(Troop t) {
		if (r.nextInt(11) == 5) {
			t.takeHit(atk * (r.nextInt(4) + 1));
		} else {
			super.attack(t);
		}
	}
	
}
