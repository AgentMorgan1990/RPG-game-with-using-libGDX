package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.characters.Hero;
import com.mygdx.game.characters.Monster;

public class GameScreen {
    private SpriteBatch batch;

    public Hero getHero() {
        return hero;
    }

    public Monster getMonster() {
        return monster;
    }

    private Hero hero;
    private Monster monster;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public void create() {
        //Метод выполняется сразу при запуске приложения
        batch = new SpriteBatch();
        hero = new Hero(this);
        monster = new Monster(this);

    }

    public void render() {
        //цикл приложения, который обновляется какое-то кол-во кадров в единицу времени
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);

        /* RGBA ,R - red ,G -green ,B-blue ,A-alpha прозрачность,например - 0,0,0 - black 1,1,1 - white.
        Цвета можно посмотреть в пэйнте (они в байтах) и разделить значение на 255 */

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
    }
}
