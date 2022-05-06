package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public class Hero {
    private Texture texture;
    private Texture textureHp;
    private float hp, maxHp;
    private float x;
    private float y;
    private float speed;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Hero() {
        texture = new Texture("Hero.png");
        textureHp = new Texture("Hero.png");
        x = 200.0f;
        y = 200.0f;
        speed = 250.0f;
        maxHp = 100;
        hp = maxHp;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
        batch.setColor(1, 0, 0, 1);
        batch.draw(textureHp, x, y + 165, 0, 0, hp, 20,
                1, 1, 0, 0, 0, 0, 0, false, false);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * dt;
        }
    }

    public void takeDamage(float amount) {
        hp -= amount;
    }
}
