package com.mygdx.game.texts;

import com.badlogic.gdx.math.Vector2;

/*
 * Класс всплывающего текста
 */
public class FlyingText {

    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private StringBuilder text;
    private float timeMax;
    private boolean active;

    public StringBuilder getText() {
        return text;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.text = new StringBuilder();
        this.timeMax = 5.0f;
        this.time = 0.0f;
        this.active = false;
    }

    public void setup(float x, float y, StringBuilder text) {
        this.position.set(x, y);
        this.velocity.set(20, 40);
        this.text.setLength(0);
        this.text.append(text);
        this.time = 0.0f;
        this.active = true;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > timeMax) {
            deactivate();
        }
    }
}
