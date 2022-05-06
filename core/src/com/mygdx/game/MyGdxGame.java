package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	//Область отрисовки
	SpriteBatch batch;
	Hero hero;
	Monster monster;

	public Hero getHero() {
		return hero;
	}

	//Метод выполняется сразу при запуске приложения
	@Override
	public void create() {
		batch = new SpriteBatch();
		hero = new Hero();
		monster = new Monster(this);
	}

	//цикл приложения, который обновляется какое-то кол-во кадров в единицу времени
	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		//RGBA ,R - red ,G -green ,B-blue ,A-alpha прозрачность
		//Например - 0,0,0 - black 1,1,1 - white
		//цвета можно посмотреть в пайнте (они в байтах) и разделить значение на 255
		ScreenUtils.clear(0, 0.5f, 0, 0.1f);
		batch.begin();
		//между бегином и эндом описываем всю отрисовку
		hero.render(batch);
		monster.render(batch);
		batch.end();
	}

	public void update(float dt) {
		hero.update(dt);
		monster.update(dt);
		float dst = (float) Math.sqrt((hero.getX() - monster.getX()) * (hero.getX() - monster.getX()) + (hero.getY() - monster.getY()) * (hero.getY() - monster.getY()));
		if (dst < 40) {
			hero.takeDamage(dt * 10.0f);
		}
	}

	//Освобождение ресурсов, например если у нас в игре будет несколько уровней
	@Override
	public void dispose() {
		batch.dispose();
	}
}
