package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
    //Область отрисовки
    SpriteBatch batch;
    //Основной игровой экран
    GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gameScreen = new GameScreen(batch);
        gameScreen.create();
    }

    @Override
    public void render() {
        gameScreen.render();
    }

    //Освобождение ресурсов, например если у нас в игре будет несколько уровней
    @Override
    public void dispose() {
        batch.dispose();
    }
}
