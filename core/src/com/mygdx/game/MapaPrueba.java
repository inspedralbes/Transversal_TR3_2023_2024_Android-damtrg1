package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Utils.Settings;
import helpers.InputHandlerGameScreen;
import objects.Background;
import objects.Jugador;

public class MapaPrueba implements Screen {

    Pixel_R6 game;

    Background bg;

    Jugador jugador;

    public Jugador getJugador() {
        return jugador;
    }

    Stage stage;

    OrthographicCamera camera;

    OrthogonalTiledMapRenderer renderer;


    public MapaPrueba(Pixel_R6 game) {
        this.game = game;

        AssetManager.load();

        // Creem la càmera de les dimensions del joc
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        // Posant el paràmetre a true configurem la càmera perquè
        // faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(false);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        //CONFIGURACION DEL FONDO
        bg = new Background(0, 0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        jugador = new Jugador(Settings.JUGADOR_STARTX, Settings.JUGADOR_STARTY, Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT);

        //AÑADIMOS EL FONDO AL STAGE
        //stage.addActor(bg);
        stage.addActor(jugador);

        Gdx.input.setInputProcessor(stage);

        renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledMap);

        Gdx.input.setInputProcessor(new InputHandlerGameScreen(this));

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        renderer.setView(camera);
        renderer.render();

        stage.draw();
        stage.act(delta);



    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
