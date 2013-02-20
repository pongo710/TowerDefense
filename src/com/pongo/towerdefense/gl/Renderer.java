package com.pongo.towerdefense.gl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

import com.pongo.towerdefense.Input;
import com.pongo.towerdefense.TouchMode;
import com.pongo.towerdefense.TowerDefense;
import com.pongo.towerdefense.model.Enemy;
import com.pongo.towerdefense.model.GameField;
import com.pongo.towerdefense.model.Richtung;
import com.pongo.towerdefense.model.Tower;
import com.pongo.towerdefense.tools.Mesh;
import com.pongo.towerdefense.tools.Mesh.PrimitiveType;

public class Renderer {

	private Mesh enemy;
	private Mesh tower;
	private Input input;
	private int screenX;
	private int screenY;
	private int scrollCounter;
	
	public Renderer(GL10 gl, TowerDefense activity) {
		
		this.input = activity.input;
		screenX = 0;
		screenY = 0;
		enemy = new Mesh(gl, 3, false, false, false);
		enemy.vertex(0, 0, 0);
		enemy.vertex(50, 0, 0);
		enemy.vertex(25, 50, 0);
		
		tower = new Mesh(gl, 3, false, false, false);
		tower.vertex(0, 0, 0);
//		tower.color(0, 1, 0, 1);
		tower.vertex(50, 0, 0);
//		tower.color(0, 1, 0, 1);
		tower.vertex(25, 100, 0);
//		tower.color(0, 1, 0, 1);

	}

	public void render(GL10 gl, TowerDefense activity, GameField field) {
		//gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, activity.getWidth(), activity.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0, activity.getWidth(), 0, activity.getHeight());

		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glLoadIdentity();
		if(input.touchMode == TouchMode.Scroll || input.touchMode == TouchMode.EndScroll){
			screenX += input.prevTouchX - input.touchX;
			screenY += input.prevTouchY - input.touchY;
			if(input.touchMode == TouchMode.EndScroll){
				input.touchMode = TouchMode.No;
			}
		}
		GLU.gluLookAt( gl, screenX, screenY, 1, screenX, screenY, 0, 0, 1, 0 );
		
		
		renderEnemies(gl, field.getWalkingEnemies());
		renderTower(gl, field.getTower());
		renderFont(gl);
	}

	private void renderFont(GL10 gl) {
		
		
	}

	private void renderEnemies(GL10 gl, ArrayList<Enemy> enemies) {
		for(Enemy actualEnemy: enemies){
			gl.glPushMatrix();
			gl.glTranslatef(actualEnemy.actualPosition.x, actualEnemy.actualPosition.y, actualEnemy.actualPosition.z);
			
			if(actualEnemy.richtung == Richtung.Osten){
				gl.glRotatef(-90, 0, 0, 1);
			} else if(actualEnemy.richtung == Richtung.Sueden){
				
				gl.glRotatef(180, 0, 0, 1);
			} else if(actualEnemy.richtung == Richtung.Westen){
				
				gl.glRotatef(90, 0, 0, 1);
			}
			enemy.render(PrimitiveType.Triangles);
			gl.glPopMatrix();
		}
		
	}

	private void renderTower(GL10 gl, ArrayList<Tower> towerList) {
		for(Tower actualTower: towerList){
			gl.glPushMatrix();
			gl.glTranslatef(actualTower.position.x, actualTower.position.y, actualTower.position.z);
			tower.render(PrimitiveType.Triangles);
			gl.glPopMatrix();
		}
	}
	public void dispose(){
		enemy.dispose();
		tower.dispose();
	}
}
