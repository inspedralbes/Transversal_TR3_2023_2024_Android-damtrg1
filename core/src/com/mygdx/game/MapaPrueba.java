package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.net.URISyntaxException;

import Utils.Settings;
import helpers.InputHandlerGameScreen;
import objects.Background;
import objects.Jugador;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class MapaPrueba implements Screen {

    Pixel_R6 game;

    Background bg;

    Jugador jugador;

    public Jugador getJugador() {
        return jugador;
    }

    Stage stage;

    Batch batch;

    OrthographicCamera camera;

    OrthogonalTiledMapRenderer renderer;

    Socket mSocket;


    public MapaPrueba(Pixel_R6 game) {
        this.game = game;

        AssetManager.load();

        // Creem la càmera de les dimensions del joc
        camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        // Posant el paràmetre a true configurem la càmera perquè
        // faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(false );

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        //CONFIGURACION DEL FONDO
        //bg = new Background(0, 0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        jugador = new Jugador(Settings.JUGADOR_STARTX, Settings.JUGADOR_STARTY, Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT);
        camera.update();

        //AÑADIMOS EL FONDO AL STAGE
        //stage.addActor(bg);
        stage.addActor(jugador);

        batch = stage.getBatch();

        camera.zoom=0.5f;

        Gdx.input.setInputProcessor(stage);

        renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledMap);

        Gdx.input.setInputProcessor(new InputHandlerGameScreen(this));

        try {
            mSocket = IO.socket("http://r6pixel.dam.inspedralbes.cat:3169");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        camera.position.set(jugador.getPosition().x , jugador.getPosition().y , 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

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
