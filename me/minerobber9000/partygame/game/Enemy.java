package me.minerobber9000.partygame.game;

import java.util.*;

public class Enemy extends GameObject {
	
	ArrayList<Tank> tanks = new ArrayList<Tank>();
	Battalion b;
	
	public Enemy() {
		this.name = "Enemy";
		this.lives = 1;
		for (int i = 0; i < 8000; i++) {
			tanks.add(new EnemyTank());
		}
		b = new Battalion(1000);
	}
	
}
