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

import objects.Jugador;

public class AssetManager {

    public static Sprite[] imatgesJugador;
    public static Texture imgFondo, imgCuadrado, mapaCastillo, mapaMazmorra;

    public static TextureRegion background;

    public static Color grisTransparente;

    public static BitmapFont font;

    public static TiledMap tiledMazmorra, tiledCastillo;

    public static Texture spritesheet_joc_dreta;
    public static Texture spritesheet_joc_avall;
    public static Texture spritesheet_joc_esquerra;
    public static Texture spritesheet_joc_amunt;

    public static TextureRegion jugador_dreta;
    public static Sprite jugadorSprite_dreta;
    public static TextureRegion jugador_avall;
    public static Sprite jugadorSprite_avall;
    public static TextureRegion jugador_esquerra;
    public static Sprite jugadorSprite_esquerra;
    public static TextureRegion jugador_amunt;
    public static Sprite jugadorSprite_amunt;

    public static void load() {
        imgFondo = new Texture(Gdx.files.internal("fondo2.jpg"));
        imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

        background = new TextureRegion(imgFondo);

        //MAPAS
        mapaCastillo = new Texture(Gdx.files.internal("mapas/mapes/IMGmapas/mapacastillo.jpg"));
        mapaMazmorra = new Texture(Gdx.files.internal("mapas/mapes/IMGmapas/mapamazmorra.jpg"));


        grisTransparente = new Color(115/255f,115/255f,115/255f, 150/255f);
        Pixmap pixmapgrisTransparente = new Pixmap(500, 400, Pixmap.Format.RGBA8888);
        pixmapgrisTransparente.setColor(grisTransparente);
        pixmapgrisTransparente.fill();
        imgCuadrado = new Texture(pixmapgrisTransparente);

        tiledMazmorra = new TmxMapLoader().load("mapas/mapes/mapaMasmorra/mapaMazmorra.tmx");
        tiledCastillo = new TmxMapLoader().load("mapas/mapes/mapaCastell/mapaCastell.tmx");

        spritesheet_joc_dreta = new Texture(Gdx.files.internal("swat_sprite_dreta.png"));
        spritesheet_joc_dreta.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_avall = new Texture(Gdx.files.internal("swat_sprite_avall.png"));
        spritesheet_joc_avall.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_esquerra = new Texture(Gdx.files.internal("swat_sprite_esquerra.png"));
        spritesheet_joc_esquerra.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_amunt = new Texture(Gdx.files.internal("swat_sprite_amunt.png"));
        spritesheet_joc_amunt.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        jugador_dreta = new TextureRegion(spritesheet_joc_dreta, 47, 8, 31, 23);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_dreta = new Sprite(jugador_dreta);

        jugador_avall = new TextureRegion(spritesheet_joc_avall, 426, 42, 31, 38);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_avall = new Sprite(jugador_avall);
        jugador_esquerra = new TextureRegion(spritesheet_joc_esquerra, 485, 427, 36, 30);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_esquerra = new Sprite(jugador_esquerra);
        jugador_amunt = new TextureRegion(spritesheet_joc_amunt, 7, 484, 26, 38);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_amunt = new Sprite(jugador_amunt);

        FileHandle fontFile = Gdx.files.internal("default.fnt"); // Assuming your font is in the "fonts" folder
        font = new BitmapFont(fontFile);

    }

    public static void dispose() {
        imgFondo.dispose();
        imgCuadrado.dispose();
    }
}
