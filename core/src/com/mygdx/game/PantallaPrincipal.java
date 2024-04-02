package com.mygdx.game;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;

import Utils.Settings;
import objects.Background;

public class PantallaPrincipal implements Screen {

    Pixel_R6 game;

    Background bg;

    Stage stage;

    OrthographicCamera camera;

    Skin skin, skin_txt;

    Preferences preferences;


    public PantallaPrincipal(Pixel_R6 game) {

        this.game = game;

        AssetManager.load();

        preferences = Gdx.app.getPreferences("Pref");

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

        //AÑADIMOS EL FONDO AL STAGE
        stage.addActor(bg);


        // Carga el Skin
        skin_txt = new Skin(Gdx.files.internal("skin_txt/arcade-ui.json"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


        //TITULO
        // Registro del color blanco en el Skin
        Color blanco = Color.WHITE;
        skin_txt.add("white", blanco);

        // Obtén el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titleLabelStyle = skin_txt.get("title", Label.LabelStyle.class);

        // Cambia el color del texto a blanco
        titleLabelStyle.fontColor = blanco;

        // Cambia el tamaño de la fuente del título
        BitmapFont titleFont = skin_txt.getFont("title"); // Obtiene la fuente del título
        titleFont.getData().setScale(2.0f); // Establece el tamaño de la fuente (cambia el valor como desees)


        // Crea una instancia de Label con el texto "R6 PIXEL" y el nuevo estilo
        Label titleLabel = new Label("R6 PIXEL", titleLabelStyle);


        // Calcula la posición X centrada en la pantalla
        float posX = (Settings.GAME_WIDTH - titleLabel.getWidth()) / 2;

        // Calcula la posición Y en la parte superior de la pantalla
        float posY = Settings.GAME_HEIGHT - titleLabel.getHeight(); // Margen de 20 píxeles

        // Establece la posición del título
        titleLabel.setPosition(posX, posY); // Alinea el título en la parte superior y centrado

        // Añade el título al stage
        stage.addActor(titleLabel);


        //BOTONES
        TextButton.TextButtonStyle textButtonStyle = skin.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_play = new TextButton("PLAY", textButtonStyle);

        btn_play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectPlayScreen(game));
            }
        });

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_settings = new TextButton("SETTINGS", textButtonStyle);

        btn_settings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ScreenSettings(game));
            }
        });

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_log_out = new TextButton("CERRAR SESION", textButtonStyle);

        btn_log_out.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Login(game));
                game.setLoggedIn(false, "");
            }
        });

        //TAMAÑO DEL BOTON
        btn_play.setSize(250, 70);
        btn_settings.setSize(250, 70);
        btn_log_out.setSize(250, 70);

        // Calcula las coordenadas X e Y para colocar los botones en el medio de la pantalla
        float btnX = (Settings.GAME_WIDTH - btn_play.getWidth()) / 2;
        float btnY = (Settings.GAME_HEIGHT - btn_play.getHeight()) / 2;

        // Establece la posición de los botones
        btn_play.setPosition(btnX, btnY + 60); // Posición del botón "PLAY"
        btn_settings.setPosition(btnX, btnY - 70); // Posición del botón "SETTINGS"
        btn_log_out.setPosition(btnX, btnY - 200);


        TextButton btn_tenda = new TextButton("TENDA", textButtonStyle);
        btn_tenda.setSize(70,70);
        btn_tenda.setPosition(0, Settings.GAME_HEIGHT - btn_tenda.getHeight());

        btn_tenda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ScreenSettings(game));
            }
        });

        stage.addActor(btn_tenda);
        stage.addActor(btn_play);
        stage.addActor(btn_settings);
        stage.addActor(btn_log_out);


        //USERNAME
        // Obtener el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titlestyleUser = skin.get("title", Label.LabelStyle.class);


        Label titUssername = new Label(preferences.getString("username"), titlestyleUser);

        // Calcular la posición X para que el Label esté a la derecha de la pantalla
        float posX2 = Settings.GAME_WIDTH - titUssername.getWidth();

        // Calcular la posición Y para que el Label esté en la parte superior de la pantalla
        float posY2 = Settings.GAME_HEIGHT - titUssername.getHeight();

        // Establecer la posición del Label
        titUssername.setPosition(posX2, posY2);

        stage.addActor(titUssername);


        //CONTROLAR INPUTS
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
