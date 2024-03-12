package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    public static Texture imgFondo;

    public static TextureRegion backgorund;

    public static void load() {
        imgFondo = new Texture(Gdx.files.internal("fondo.jpg"));
        imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

        backgorund = new TextureRegion(imgFondo);

    }

    public static void dispose() {
        imgFondo.dispose();
    }
}
