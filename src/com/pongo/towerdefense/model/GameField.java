package com.pongo.towerdefense.model;

import java.util.ArrayList;

import com.pongo.towerdefense.Input;
import com.pongo.towerdefense.TouchMode;

public class GameField {

	private ArrayList<Enemy> waitingEnemies;
	private ArrayList<Enemy> walkingEnemies;
	private ArrayList<Enemy> lostEnemies;
	private ArrayList<Enemy> deadEnemies;
	private boolean startEnemies;
	private ArrayList<Tower> tower;
	private float totalTime;
	private float enemyCounter;
	public boolean isTouched;
	private Input input;
	public int screenX;
	public int screenY;

	public GameField(ArrayList<Enemy> enemies, Input input) {
		this.startEnemies = false;
		this.waitingEnemies = enemies;
		this.walkingEnemies = new ArrayList<Enemy>();
		this.lostEnemies = new ArrayList<Enemy>();
		this.tower = new ArrayList<Tower>();
		this.deadEnemies = new ArrayList<Enemy>();
		this.totalTime = 0;
		this.enemyCounter = 0;
		this.input = input;

	}

	public void startEnemies() {
		startEnemies = true;
	}

	public void addTower(Tower tower) {
		this.tower.add(tower);
	}

	public void startAction(float deltaTime) {
		totalTime += deltaTime;

		if (input.touchMode == TouchMode.EndTip) {
			tower.add(new Aussichtsturm(new Vector(input.firstTouchX+screenX,
					input.firstTouchY+screenY, 0)));
			input.touchMode = TouchMode.No;
		}
		if(input.touchMode == TouchMode.Scroll || input.touchMode == TouchMode.EndScroll){
			screenX += input.prevTouchX - input.touchX;
			screenY += input.prevTouchY - input.touchY;
			if(input.touchMode == TouchMode.EndScroll){
				input.touchMode = TouchMode.No;
			}
		}
		moveEnemies(deltaTime);
		fireTowers(deltaTime);
	}

	private void fireTowers(float deltaTime) {
		for (Tower actualTower : tower) {
			Enemy enemy = actualTower.update(walkingEnemies, deltaTime);
			if (enemy != null) {
				walkingEnemies.remove(enemy);
				deadEnemies.add(enemy);
			}
		}

	}

	private void moveEnemies(float deltaTime) {
		if (!walkingEnemies.isEmpty()) {
			ArrayList<Enemy> toDelete = new ArrayList<Enemy>();
			for (Enemy enemy : walkingEnemies) {
				boolean last = enemy.update(deltaTime, true);
				if (last) {
					toDelete.add(enemy);
					lostEnemies.add(enemy);
					enemy.onField = false;
				}
			}
			if (!toDelete.isEmpty()) {
				for (Enemy enemy : toDelete) {
					walkingEnemies.remove(enemy);
				}
			}
		}

		if (startEnemies == true && waitingEnemies.size() > 0
				&& totalTime > enemyCounter * 3) {
			Enemy enemy = waitingEnemies.remove(0);
			enemy.update(deltaTime, true);
			walkingEnemies.add(enemy);
			enemyCounter++;
		}
	}

	public ArrayList<Enemy> getWalkingEnemies() {
		// TODO Auto-generated method stub
		return walkingEnemies;
	}

	public ArrayList<Tower> getTower() {
		// TODO Auto-generated method stub
		return tower;
	}
}
