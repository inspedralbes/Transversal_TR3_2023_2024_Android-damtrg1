package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetManager {

    public static Texture imgFondo, imgCuadrado;

    public static TextureRegion background;

    public static Color grisTransparente;

    public static BitmapFont font;

    public static TiledMap tiledMap;

    public static Texture spritesheet_joc;

    public static TextureRegion jugador;
    public static Sprite jugadorSprite;

    public static void load() {
        imgFondo = new Texture(Gdx.files.internal("fondo2.jpg"));
        imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

        background = new TextureRegion(imgFondo);


        grisTransparente = new Color(115/255f,115/255f,115/255f, 150/255f);
        Pixmap pixmapgrisTransparente = new Pixmap(500, 400, Pixmap.Format.RGBA8888);
        pixmapgrisTransparente.setColor(grisTransparente);
        pixmapgrisTransparente.fill();
        imgCuadrado = new Texture(pixmapgrisTransparente);

        tiledMap = new TmxMapLoader().load("mapaPrueba.tmx");

        spritesheet_joc = new Texture(Gdx.files.internal("swat_sprite.png"));
        spritesheet_joc.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        jugador = new TextureRegion(spritesheet_joc, 38, 1, 46, 38);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite = new Sprite(jugador);

        FileHandle fontFile = Gdx.files.internal("default.fnt"); // Assuming your font is in the "fonts" folder
        font = new BitmapFont(fontFile);
    }

    public static void dispose() {
        imgFondo.dispose();
        imgCuadrado.dispose();
    }
}
