package com.mygdx.game.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.Weapon;

public abstract class GameCharacter {
    Texture texture;
    Texture textureHp;
    float hp, maxHp;
    Vector2 position;
    float speed;
    GameScreen game;
    float damageEffectTimer;
    float attackTimer;
    Weapon weapon;

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        if (damageEffectTimer > 0.0f) {
            batch.setColor(1, 1 - damageEffectTimer, 1 - damageEffectTimer, 1);
        }
        //todo необходимо сдвинуть центр в соответствии с текстурой персонажа
        batch.draw(texture, position.x - 40.0f, position.y - 40.0f);
        batch.setColor(1, 1, 1, 1);

        batch.setColor(0, 0, 0, 1);
        batch.draw(textureHp, position.x - 52.0f, position.y + 165.0f - 52.0f, 124.0f, 25.0f);
        batch.setColor(1, 0, 0, 1);

        batch.draw(textureHp, position.x - 50.0f, position.y + 165.0f - 50.0f, 0, 0, hp / maxHp * 120.0f, 20.0f, 1, 1, 0, 0, 0, 0, 0, false, false);
        batch.setColor(1, 1, 1, 1);

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

}
