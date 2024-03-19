package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import Utils.Sala;
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
    int numJugador;

    Jugador jugador;

    public Jugador getJugador() {
        return jugador;
    }

    Stage stage;

    Batch batch;

    OrthographicCamera camera;

    OrthogonalTiledMapRenderer renderer;

    Socket mSocket;

    Preferences preferences;

    Skin skin;

    Touchpad touchpad;
    ArrayList<Jugador> jugadors = new ArrayList<>();

    public MapaPrueba(Pixel_R6 game, Sala sala) {
        preferences = Gdx.app.getPreferences("Pref");

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

        int contador = 0;
        for (String usuari: sala.getUsers()) {
            Jugador player = new Jugador(Settings.JUGADOR_STARTX, Settings.JUGADOR_STARTY, Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari);
            jugadors.add(player);
            stage.addActor(player);
            if(usuari.equals(preferences.getString("username"))){
                numJugador = contador;
            }
            contador++;
        }
        camera.update();

        //AÑADIMOS EL FONDO AL STAGE
        //stage.addActor(bg);

        batch = stage.getBatch();

        camera.zoom=0.5f;


        renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledMap);

        Gdx.input.setInputProcessor(new InputHandlerGameScreen(this));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Touchpad.TouchpadStyle touchpadStyle = skin.get("default", Touchpad.TouchpadStyle.class);
        touchpad = new Touchpad(0, touchpadStyle);
        touchpad.setBounds(40, 225, 100, 100); // Establecer posición y tamaño del Touchpad
        stage.addActor(touchpad); // Agregar el Touchpad al Stage

        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Actualizar la posición de la cámara para que siga al jugador
        camera.position.set(jugadors.get(numJugador).getPosition().x, jugadors.get(numJugador).getPosition().y, 0);

        // Limitar la posición de la cámara para que no se salga del mapa
        float halfWidth = camera.viewportWidth * camera.zoom / 2;
        float halfHeight = camera.viewportHeight * camera.zoom / 2;
        float minX = halfWidth;
        float minY = halfHeight;
        float maxX = AssetManager.tiledMap.getProperties().get("width", Integer.class) * AssetManager.tiledMap.getProperties().get("tilewidth", Integer.class) - halfWidth;
        float maxY = AssetManager.tiledMap.getProperties().get("height", Integer.class) * AssetManager.tiledMap.getProperties().get("tileheight", Integer.class) - halfHeight;

        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);

        // Actualizar la cámara
        camera.update();

        // Actualizar la posición del Touchpad en relación con la cámara
        float touchpadX = camera.position.x - camera.viewportWidth / 2 + 10; // Ajustar la posición del Touchpad en X
        float touchpadY = camera.position.y - camera.viewportHeight / 2 + 10; // Ajustar la posición del Touchpad en Y

        // Establecer límites para el Touchpad
        touchpadX = MathUtils.clamp(touchpadX, 0, Gdx.graphics.getWidth() - touchpad.getWidth());
        touchpadY = MathUtils.clamp(touchpadY, 0, Gdx.graphics.getHeight() - touchpad.getHeight());

        // Establecer límites para el Touchpad
        touchpadX = MathUtils.clamp(touchpadX, camera.position.x - minX, Gdx.graphics.getWidth() - touchpad.getWidth());
        touchpadY = MathUtils.clamp(touchpadY, camera.position.y - 200, Gdx.graphics.getHeight() - touchpad.getHeight());


        touchpad.setPosition(touchpadX, touchpadY);

        // Dibujar el mapa
        renderer.setView(camera);
        renderer.render();

        // Dibujar y actualizar el stage
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

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
