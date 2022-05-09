package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.weapon.Weapon;

/*
 * Класс описание игровых персонажей
 */
public abstract class GameCharacter {
    Texture texture;
    Texture textureHp;
    float hp, maxHp;
    Vector2 position;
    Vector2 direction;
    Vector2 temp;
    StringBuilder stringHelper;
    float speed;
    GameScreen game;
    float damageEffectTimer;
    float attackTimer;
    Weapon weapon;
    TextureRegion[] regions;
    float animationTimer;
    float secondsPerFrame;

    public GameCharacter() {
        temp = new Vector2(0.0f, 0.0f);
        stringHelper = new StringBuilder();
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch, BitmapFont font24) {
        if (damageEffectTimer > 0.0f) {
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }
        //todo необходимо сдвинуть центр в соответствии с текстурой персонажа
        int frameIndex = (int) (animationTimer / secondsPerFrame) % regions.length;
        batch.draw(regions[frameIndex], position.x - 40.0f, position.y - 40.0f);
        batch.setColor(1, 1, 1, 1);

        batch.setColor(0, 0, 0, 1);
        batch.draw(textureHp, position.x - 52.0f, position.y + 165.0f - 52.0f + (int) (MathUtils.sin(animationTimer * 10) * 15 * damageEffectTimer), 124.0f, 25.0f);
        batch.setColor(1, 0, 0, 1);

        batch.draw(textureHp, position.x - 50.0f, position.y + 165.0f - 50.0f + (int) (MathUtils.sin(animationTimer * 10) * 15 * damageEffectTimer), 0, 0, hp / maxHp * 120.0f, 20.0f, 1, 1, 0, 0, 0, 0, 0, false, false);
        batch.setColor(1, 1, 1, 1);
        stringHelper.setLength(0);
        stringHelper.append((int) hp);
        font24.draw(batch, stringHelper, position.x - 50.0f, position.y + 165.0f - 25.0f + (int) (MathUtils.sin(animationTimer * 10) * 15 * damageEffectTimer), 120.0f, 1, false);
    }

    public abstract void update(float dt);

    public void checkScreenBounds() {
        if (position.x > 1280.0f) {
            position.x = 1280.0f;
        }
        if (position.x < 0.0f) {
            position.x = 0.0f;
        }
        if (position.y > 720.0f) {
            position.y = 720.0f;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
        }
    }

    public void takeDamage(float amount) {
        hp -= amount;
        damageEffectTimer += 0.5f;
        if (damageEffectTimer > 1.0f) {
            damageEffectTimer = 1.0f;
        }
    }

    public void moveForward(float dt) {
        if (game.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt))) {
            position.set(temp);
        } else if (game.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt).set(temp.x, position.y))) {
            position.set(temp);
        } else if (game.getMap().isCellPassable(temp.set(position).mulAdd(direction, speed * dt).set(position.x, temp.y))) {
            position.set(temp);
        }
    }
}
