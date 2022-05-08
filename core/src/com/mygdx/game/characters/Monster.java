package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public class Monster extends GameCharacter {

    private float activityRadius;
    private float moveTimer;

    public Monster(GameScreen game) {
        this.texture = new Texture("Monster.png");
        this.textureHp = new Texture("bar.png");
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!game.getMap().isCellPassable(position)) {
            this.position.set(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }
        this.direction = new Vector2(0, 0);
        this.temp = new Vector2(0, 0);
        this.speed = 100.0f;
        this.activityRadius = 300.0f;
        this.maxHp = 40.0f;
        this.hp = this.maxHp;
        this.game = game;
        this.weapon = new Weapon("Sword", 100.0f, 0.8f, 10);
    }

    @Override
    public void update(float dt) {
        damageEffectTimer -= dt;
        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }
        float dst = game.getHero().getPosition().dst(position);
        if (dst < activityRadius) {
            /*вычитаем один вектор из другого - получаем направление к герою,
            если вычитать наоборот направление будет от героя*/
            direction.set(game.getHero().getPosition()).sub(this.position).nor();
        } else {

            moveTimer -= dt;
            if (moveTimer < 0.0f) {
                //обновление таймера
                moveTimer = MathUtils.random(3.0f, 5.0f);
                //Выбор нового вектора движения
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
                //Нормировка векторов, чтобы вектора привести к единице
                direction.nor();
            }
        }
        temp.set(position).mulAdd(direction, speed * dt);
        if (game.getMap().isCellPassable(temp)) {
            position.set(temp);
        }
        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                game.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounds();
    }
}
