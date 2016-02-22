package me.minerobber9000.partygame.game;

import java.io.*;
import java.util.*;

public class GameInstance {
	
	Player player;
	Enemy enemy;
	PrintStream out;
	boolean memes = false;
	Random r = new Random();
	int[] scores = new int[5];
	
	public GameInstance() {
		player = new Player(5);
		out = System.out;
	}
	
	public GameInstance(boolean memes) {
		player = new Player(5);
		out = System.out;
		this.memes = memes;
	}
	
	public void begin() {
		Scanner input = new Scanner(System.in);
		out.println("Hello!");
		boolean notOpt = true;
		while (notOpt) {
			int choice = choose(input, "Start the game", "Learn about the game", "Exit");
			switch (choice) {
			case 1: startGame();
					notOpt = false;
					break;
			case 2: about();
					break;
			case 3: System.exit(0);
					break;
			default:out.println("Invalid choice!");
					break;
			}
		}
		input.close();
	}
	
	public void startGame() {
		Scanner input = new Scanner(System.in);
		out.println("What should we call you?");
		out.println("Name:");
		String n = input.nextLine();
		if (!n.equals("")) {
			player.setName(n);
		}
		out.println("And who, exactly, are you fighting against?");
		n = input.nextLine();
		if (!n.equals("")) {
			enemy.setName(n);
		}
		//If "end" is true, then the game has ended.
		boolean end = false;
		//If "lost" is true, that means we lost. (oh noes!)
		boolean lost = false;
		//If "snark" is true, we get to be an asshole, because the player was a dick, and tried to send out more tanks then they had.
		boolean snark = false;
		//The "stage" variable stores where we are in the game.
		String stage = "planning1";
		//The "life" variable stores which life we are on. (1-5)
		int life = 1;
		//The "battalion" and "tanks" variables store whether we have a battalion or not, or tanks or not, respectively.
		boolean battalion = true;
		boolean tanks = true;
		//The following variables store the same things, but for the enemy.
		boolean eBattalion = true;
		boolean eTanks = true;
		while (!end) {
			if (stage.equals("planning1")) {
				out.println("How many tanks will you buy? Each tank costs " + Tank.cost + " dollars.");
				int tankAmount = input.nextInt();
				if ((tankAmount * Tank.cost) > player.getMoney()) {
					//The player can't afford that many tanks!
					out.println(memes ? "INSUFFICIENT FUNDS!" : "Sorry, you can't afford that many tanks!");
				} else {
					//Buy the amount of tanks the player wanted.
					int iter = 0;
					while (iter < tankAmount) {
						iter++;
						player.tanks.add(new FriendlyTank());
					}
					player.pay(tankAmount * Tank.cost);
					stage = "planning2";
				}
			}
			if (stage.equals("planning2")) {
				out.println("How many soldiers do you want to hire? Each costs 1000 dollars.");
				int amt = input.nextInt();
				if (amt*1000 > player.getMoney()) {
					//The player can't afford that many soldiers!
					out.println(memes ? "INSUFFICIENT FUNDS!" : "Sorry, you can't afford that many soldiers!");
				} else {
					if (player.b.getClass() == Battalion.class) {
						player.b.soldiers += amt;
					}
					player.b = new Battalion(amt);
					player.pay(player.b.cost);
					stage = "battle";
				}
			}
			if (stage.equals("battle")) {
				String enemyStatus = "The enemy has ";
				if (eTanks) {
					enemyStatus += enemy.tanks.size() + " tanks";
				}
				if (!eTanks && eBattalion) {
					enemyStatus += enemy.b.soldiers + " soldiers";
				}
				if (eTanks && eBattalion) {
					enemyStatus += " and " + enemy.b.soldiers + " soldiers";
				}
				enemyStatus += ".";
				out.println(enemyStatus);
				int c = 0;
				if (battalion && tanks) {
					out.println("How will you attack?");
					c = choose(input, "Tank battle", "Battalion siege");
				} else {
					if (tanks) {
						c = 1;
					} else {
						if (battalion) {
							c = 2;
						}
					}
				}
				switch (c) {
				case 1:	out.println("How many tanks will you send?");
						int amt = input.nextInt();
						if (amt > player.tanks.size()) {
							out.println("You don't have that many tanks!");
							out.println("How many tanks will you send?");
							amt = input.nextInt();
							if (amt > player.tanks.size()) {
								out.println("Fine. You lose 3 tanks.");
								for (int i = 0; i < 3; i++) {
									player.tanks.remove(i);
									if (player.tanks.size() == 0) {
										out.println("You ran out of tanks.");
										tanks = false;
										if (!battalion) {
											out.println("You lose. Serves you right!");
											scores[life] = player.getScore()+1;
											if (life == 5) {
												out.println("Game over!");
												lost = true;
												end = true;
												snark = true;
												break;
											}
										}
									}
								}
							}
						}
						int enemyAmt = r.nextInt(enemy.tanks.size()+1);
						int amountToAttack = enemyAmt;
						for (int a = 0; a < amt; a++) {
							int attackee = r.nextInt(enemyAmt+1);
							player.tanks.get(a).attack(enemy.tanks.get(attackee));
							if (!enemy.tanks.get(attackee).isAlive()) {
								player.addScore(1000);
								out.println("You defeated an enemy tank!");
								enemy.tanks.remove(attackee);
							} else {
								enemy.tanks.get(attackee).attack(player.tanks.get(a));
								amountToAttack--;
								if (!player.tanks.get(a).isAlive()) {
									player.removeScore(100);
									out.println("Oh no! One of your tanks was defeated!");
									player.tanks.remove(a);
								}
							}
						}
						if (!player.b.isAlive()) {
							enemy.b.soldiers -= 3;
							enemy.b.recalculateHealth();
							if (!enemy.b.isAlive()) {
								
							}
						}
						for (int a = 0; a < amountToAttack; a++) {
							int attackee = r.nextInt(amt+1);
							enemy.tanks.get(a).attack(player.tanks.get(attackee));
							if (!player.tanks.get(attackee).isAlive()) {
								player.removeScore(100);
								out.println("Oh no! One of your tanks was defeated!");
								player.tanks.remove(a);
							} else {
								player.tanks.get(attackee).attack(player.tanks.get(a));
								if (!player.tanks.get(a).isAlive()) {
									player.addScore(1000);
									out.println("You defeated an enemy tank!");
									enemy.tanks.remove(attackee);
								}
							}
						}
						if (player.tanks.size() == 0) {
							tanks = false;
							out.println("Oh no! You don't have any tanks left!");
						}
						if (enemy.tanks.size() == 0) {
							eTanks = false;
							out.println("The enemy's tanks were defeated!" + (tanks ? " You don't need those tanks anymore!" : ""));
							if (tanks && (battalion && eBattalion)) {
								player.paidBack(player.tanks.size() * Tank.cost);
								out.println("Do you wish to buy some more soldiers?(y/n)");
								String choice = input.nextLine();
								if (choice.equalsIgnoreCase("y")) {
									stage = "planning2";
								}
							}
						}
						break;
				case 2:	int amtDefeatedP = r.nextInt(player.b.soldiers+1);
						int amtDefeatedE = r.nextInt(enemy.b.soldiers+1);
						player.b.soldiers -= amtDefeatedP;
						enemy.b.soldiers -= amtDefeatedE;
						player.b.recalculateHealth();
						enemy.b.recalculateHealth();
						if (enemy.b.isAlive()) {
							if (!player.b.isAlive()) {
								out.println("Oh no! Your battalion was defeated!");
								battalion = false;
							}
						} else {
							eBattalion = false;
							if (!player.b.isAlive()) {
								out.println("Your battalion was defeated, but so was the enemy's!");
								battalion = false;
							} else {
								out.println("The enemy's battalion has been defeated! All of your soldiers can go home!");
								battalion = false;
							}
						}
				}
			}
		}
		out.println((lost ? (snark ? "You lose! Haha!" : "Aww... you lost. Better luck next time!") : "Yay! You win!"));
		out.println("Your scores:");
		int prog = 1;
		for (int i : scores) {
			if (i == 0) {
				out.println(prog + ": N/A");
			}
		}
		input.close();
	}

	public void about() {
		out.println("This is a Army adventure game in which you lead an army against an enemy.");
		out.println("The enemy is indescript. You choose what the enemy is called, and what your army is called.");
		out.println("This is to avoid accidental indoctrination.");
	}
	
	public int choose(Scanner s, String... choice) {
		int ret;
		int prog = 1;
		out.println("Pick an option:");
		for (String str : choice) {
			out.println("[" + prog + "] " + str);
			prog++;
		}
		out.print("Pick: ");
		ret = s.nextInt();
		return ret;
	}
	
}
