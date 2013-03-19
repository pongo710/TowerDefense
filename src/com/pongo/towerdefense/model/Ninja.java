package com.pongo.towerdefense.model;

import java.util.ArrayList;

import com.pongo.towerdefense.tools.Vector;

public class Ninja extends Enemy {
	public static final int SPEED = 200;
	public static final int LIFE = 100;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 25;
	

	public Ninja(ArrayList<Vector> route, Richtung richtung) {
		super(route, SPEED, LIFE, richtung, HEIGHT, WIDTH);

	}

}