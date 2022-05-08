package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Item;
import com.mygdx.game.Weapon;

public class Hero extends GameCharacter {
    private int coins;
    private int level;
    private int exp;
    private int[] expTo = {0, 0, 100, 300, 600, 1000, 5000};

    public Hero(GameScreen gameScreen) {
        this.game = gameScreen;
        this.level = 1;
        this.texture = new Texture("Hero.png");
        this.textureHp = new Texture("bar.png");
        this.position = new Vector2(300.0f, 300.0f);
        while (!game.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }
        this.speed = 250.0f;
        this.maxHp = 100;
        this.hp = this.maxHp;
        this.weapon = new Weapon("Spear", 150.0f, 0.5f, 5.0f);
        this.direction = new Vector2(0.0f, 0.0f);
        this.temp = new Vector2(0.0f, 0.0f);
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        //todo оптимизировать через stringBuilder
        font24.draw(batch, "Knight: Hero\nCoins: " + coins + "\nLevel: " + level + "\nExp: " + exp + "/" + expTo[level + 1], 20, 700);
    }

    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
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
        temp.set(position).mulAdd(direction, speed * dt);
        if (game.getMap().isCellPassable(temp)) {
            position.mulAdd(direction, speed * dt);
        }
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
                coins += MathUtils.random(2, 5);
                break;
        }
        it.deactivate();
    }
}
