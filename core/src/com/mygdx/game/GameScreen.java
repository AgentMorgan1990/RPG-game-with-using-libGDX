package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.characters.GameCharacter;
import com.mygdx.game.characters.Hero;
import com.mygdx.game.characters.Monster;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.ItemsEmitter;
import com.mygdx.game.map.Map;
import com.mygdx.game.texts.TextEmitter;

import java.util.*;

/*
 * Класс с основной игровой логикой
 */
public class GameScreen {
    private SpriteBatch batch;
    //Шрифты создаются с помощью программы Hiero с сайтв libGDX
    private BitmapFont font24;
    private com.mygdx.game.map.Map map;
    private ItemsEmitter itemsEmitter;
    private Comparator<GameCharacter> drawOrderComparator;
    private List<GameCharacter> allCharacters;
    private List<Monster> allMonsters;
    private TextEmitter textEmitter;
    private Stage stage;
    private boolean paused;
    private Music music;
    private Sound sound;
    private Float spawnTimer;

    public TextEmitter getTextEmitter() {
        return textEmitter;
    }

    public Hero getHero() {
        return hero;
    }

    public List<Monster> getAllMonsters() {
        return allMonsters;
    }

    public com.mygdx.game.map.Map getMap() {
        return map;
    }

    private Hero hero;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public void create() {
        map = new Map();
        itemsEmitter = new ItemsEmitter();
        textEmitter = new TextEmitter();
        //Метод выполняется сразу при запуске приложения
        batch = new SpriteBatch();
        allCharacters = new ArrayList<>();
        allMonsters = new ArrayList<>();
        hero = new Hero(this);
        spawnTimer = 0.0f;
        allCharacters.addAll(Arrays.asList(hero,
                new Monster(this),
                new Monster(this),
                new Monster(this),
                new Monster(this),
                new Monster(this)
        ));

        for (int i = 0; i < allCharacters.size(); i++) {
            if (allCharacters.get(i) instanceof Monster) {
                allMonsters.add((Monster) allCharacters.get(i));
            }
        }
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));

        //todo создание игровой интерфейса, нужно вынести с отдельный класс
        stage = new Stage();
        Skin skin = new Skin();
        skin.add("simpleButton", new Texture("Button.png"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        TextButton pausedButton = new TextButton("Pause", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pausedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;

            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        Group menuGroup = new Group();
        menuGroup.addActor(pausedButton);
        menuGroup.addActor(exitButton);
        exitButton.setPosition(150.0f, 0.0f);
        menuGroup.setPosition(1000.0f, 600.0f);
        //сделать кнопки невидимыми
        //menuGroup.setVisible(false);
        stage.addActor(menuGroup);
        Gdx.input.setInputProcessor(stage);

        drawOrderComparator = new Comparator<GameCharacter>() {
            @Override
            public int compare(GameCharacter o1, GameCharacter o2) {
                return (int) (o2.getPosition().y - o1.getPosition().y);
            }
        };

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();

//        sound = Gdx.audio.newSound(Gdx.files.internal("Boom.mp3"));
//        sound.play();
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
        textEmitter.render(batch, font24);
        hero.renderHUD(batch, font24);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        if (!paused) {
            spawnTimer += dt;
            if (spawnTimer > 5.0f) {
                Monster monster = new Monster(this);
                allCharacters.add(monster);
                allMonsters.add(monster);
                spawnTimer = 0.0f;
            }
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
            textEmitter.update(dt);
            itemsEmitter.update(dt);
        }
        stage.act(dt);
    }
}
