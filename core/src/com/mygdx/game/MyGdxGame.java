package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
/*
 * основной класс игры
 */
public class MyGdxGame extends ApplicationAdapter {


    //todo list with ideas
    /*
    Доработать игровой интерфейс, продумать и добавить кноки для работы
    Добавить возможность убийства персонажа
    При смерти персонажа останавливать игру
    Возможность перезапуска игры, если погибли
    Система прокачки уровней, например возможность выбора увеличения урона, скорости атаки
    Возможность выбора оружия -> добавить кнопки для смены оружия
    Возможность покупки нового оружия
    Система новых уровней в игры
    Добавить несколько разных монстров
    * Добавить анимацию персонажу при движении, атаке, смерти
    * Добавить монстрам персонажу при движении, атаке, смерти
    * Добавить звуковые эффекты при попадании по персонажу, по монстру

    */

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
