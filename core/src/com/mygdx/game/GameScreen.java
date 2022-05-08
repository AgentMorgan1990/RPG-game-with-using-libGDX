package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Hero;
import com.mygdx.game.characters.Monster;

import java.util.*;

public class GameScreen {
    private SpriteBatch batch;
    //Шрифты создаются с помощью программы Hiero с сайтв libGDX
    private BitmapFont font24;
    private Map map;
    private ItemsEmitter itemsEmitter;
    private Comparator<GameCharacter> drawOrderComparator;
    private List<GameCharacter> allCharacters;
    private List<Monster> allMonsters;

    public Hero getHero() {
        return hero;
    }

    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    public Map getMap() {
        return map;
    }

    private Hero hero;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public void create() {
        map = new Map();
        itemsEmitter = new ItemsEmitter();
        //Метод выполняется сразу при запуске приложения
        batch = new SpriteBatch();
        allCharacters = new ArrayList<>();
        allMonsters = new ArrayList<>();
        hero = new Hero(this);
        allCharacters.addAll(Arrays.asList(hero,
                new Monster(this),
                new Monster(this)
        ));
        for (int i = 0; i < allCharacters.size(); i++) {
            if (allCharacters.get(i) instanceof Monster) {
                allMonsters.add((Monster) allCharacters.get(i));
            }
        }
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        drawOrderComparator = new Comparator<GameCharacter>() {
            @Override
            public int compare(GameCharacter o1, GameCharacter o2) {
                return (int) (o2.getPosition().y - o1.getPosition().y);
            }
        };
    }

    public void render() {
        //цикл приложения, который обновляется какое-то кол-во кадров в единицу времени
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);

        /* RGBA ,R - red ,G -green ,B-blue ,A-alpha прозрачность,например - 0,0,0 - black 1,1,1 - white.
        Цвета можно посмотреть в пэйнте (они в байтах) и разделить значение на 255 */

        ScreenUtils.clear(0, 0.5f, 0, 0.1f);
        //между бегином и эндом описываем всю отрисовку
        batch.begin();
        map.render(batch);

        Collections.sort(allCharacters, drawOrderComparator);
        for (int i = 0; i < allCharacters.size(); i++) {
            allCharacters.get(i).render(batch, font24);
        }
        itemsEmitter.render(batch);
        hero.renderHUD(batch, font24);
        batch.end();
    }

    public void update(float dt) {
        for (int i = 0; i < allCharacters.size(); i++) {
            allCharacters.get(i).update(dt);
        }
        for (int i = 0; i < allMonsters.size(); i++) {
            Monster currentMonster = allMonsters.get(i);
            if (!currentMonster.isAlive()) {
                allMonsters.remove(currentMonster);
                allCharacters.remove(currentMonster);
                itemsEmitter.generateRandomItem(currentMonster.getPosition().x, currentMonster.getPosition().y, 10, 0.6f);
                hero.killMonster(currentMonster);
            }
        }
        for (int i = 0; i < itemsEmitter.getItems().length; i++) {
            Item it = itemsEmitter.getItems()[i];
            if (it.isActive()) {
                float dst = hero.getPosition().dst(it.getPosition());
                if (dst < 24.0f) {
                    hero.useItem(it);
                }
            }

        }
        itemsEmitter.update(dt);
    }
}
