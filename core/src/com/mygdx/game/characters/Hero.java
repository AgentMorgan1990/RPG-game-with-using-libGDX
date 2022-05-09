package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.items.Item;
import com.mygdx.game.weapon.Weapon;

/*
 * Класс описание главного героя
 */
public class Hero extends GameCharacter {
    private int coins;
    private String name;
    private int level;
    private int exp;
    private int[] expTo = {0, 0, 100, 300, 600, 1000, 5000};


    public Hero(GameScreen gameScreen) {
        this.name = "Hero";
        this.game = gameScreen;
        this.level = 1;
        this.texture = new Texture("Animation_hero.png");
        this.regions = new TextureRegion(texture).split(113, 113)[0];
        this.textureHp = new Texture("bar.png");
        this.position = new Vector2(300.0f, 300.0f);
        while (!game.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0.0f, 1280.0f), MathUtils.random(0.0f, 720.0f));
        }
        this.speed = 250.0f;
        this.maxHp = 100.0f;
        this.hp = this.maxHp;
        this.weapon = new Weapon("Spear", 150.0f, 0.5f, 5.0f);
        this.direction = new Vector2(0.0f, 0.0f);
        this.secondsPerFrame = 0.1f;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        stringHelper.setLength(0);
        stringHelper
                .append("Knight:").append(name).append("\n")
                .append("Coins: ").append(coins).append("\n")
                .append("Level: ").append(level).append("\n")
                .append("Exp: ").append(exp).append("/").append(expTo[level + 1]);
        font24.draw(batch, stringHelper, 20.0f, 700.0f);
    }

    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
        animationTimer += dt;
        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }

        Monster nearestMonster = null;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < game.getAllMonsters().size(); i++) {
            float dst = game.getAllMonsters().get(i).getPosition().dst(position);
            if (dst < minDist) {
                minDist = dst;
                nearestMonster = game.getAllMonsters().get(i);
            }
        }

        if (nearestMonster != null && minDist < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                nearestMonster.takeDamage(weapon.getDamage());
            }
        }
        direction.set(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction.x = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction.y = 1.0f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction.y = -1.0f;
        }
        moveForward(dt);
        checkScreenBounds();
    }

    public void killMonster(Monster monster) {
        exp += monster.maxHp * 5;
        //todo эту логику надо вынести в другое место, так как повышение уровня может быть не только за убийство монстров
        if (exp >= expTo[level + 1]) {
            level++;
            exp -= expTo[level];
            maxHp += 10;
            hp = maxHp;
        }
    }

    public void useItem(Item it) {
        switch (it.getType()) {
            case COINS:
                int amount = MathUtils.random(2, 5);
                coins += amount;
                stringHelper.setLength(0);
                stringHelper.append("coins ").append("+").append(amount);
                game.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                break;
            case MEDS:
                hp += 5;
                stringHelper.setLength(0);
                stringHelper.append("hp ").append("+5");
                game.getTextEmitter().setup(it.getPosition().x, it.getPosition().y, stringHelper);
                if (hp > maxHp) {
                    hp = maxHp;
                }
                break;
        }
        it.deactivate();
    }
}
