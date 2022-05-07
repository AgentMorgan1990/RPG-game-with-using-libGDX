package com.mygdx.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public class Hero extends GameCharacter {

    public Hero(GameScreen gameScreen) {
        this.game = gameScreen;
        this.texture = new Texture("Hero.png");
        this.textureHp = new Texture("bar.png");
        this.position = new Vector2(200, 200);
        this.speed = 250.0f;
        this.maxHp = 100;
        this.hp = this.maxHp;
        this.weapon = new Weapon("Spear", 150.0f, 0.5f, 5.0f);
    }

    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }
        float dst = game.getMonster().getPosition().dst(position);
        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer > weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                game.getMonster().takeDamage(weapon.getDamage());
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= speed * dt;
        }
        checkScreenBounds();
    }
}
