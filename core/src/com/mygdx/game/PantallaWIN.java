package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;

import Utils.Settings;
import objects.Background;

public class PantallaWIN implements Screen {

    Pixel_R6 game;

    Background bg;

    static Stage stage;

    OrthographicCamera camera;

    Skin skin_inputs;

    Table table;

    ArrayList<Label> ganadores = new ArrayList<>();
    ArrayList<Label> perdedores = new ArrayList<>();

    public PantallaWIN(Pixel_R6 game) {
        this.game = game;

        // Creem la càmera de les dimensions del joc
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Settings.GAME_HEIGHT);

        // Posant el paràmetre a true configurem la càmera perquè
        // faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(false);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Settings.GAME_HEIGHT, camera);


        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        //CONFIGURACION DEL FONDO
        bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT);

        //AÑADIMOS EL FONDO AL STAGE
        stage.addActor(bg);

        // Cargar el Skin
        skin_inputs = new Skin(Gdx.files.internal("skin/uiskin.json"));


        // Obtén el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titleLabelStyle = skin_inputs.get("title", Label.LabelStyle.class);


        Label titulo = new Label("GANDORES", titleLabelStyle);

        // Calcula la posición X centrada en la pantalla
        float posX = (Gdx.graphics.getWidth() - titulo.getWidth()) / 2;

        // Calcula la posición Y en la parte superior de la pantalla
        float posY = Settings.GAME_HEIGHT - titulo.getHeight(); // Margen de 20 píxeles

        // Establece la posición del título
        titulo.setPosition(posX, posY); // Alinea el título en la parte superior y centrado

        // Añade el título al stage
        stage.addActor(titulo);


        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("", windowStyle);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Gdx.graphics.getWidth();
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 3.3f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 2.3f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(1000, 600); // Establece el tamaño como desees

        table = new Table();

        Table ganadors = new Table();
        Table perdedors = new Table();

        for (int i = 0; i <= 5; i++) {
            Label wins = new Label("GANADORES " + i, skin_inputs);
            ganadores.add(wins);
            ganadors.add(wins);
        }

        for (int i = 0; i <= 5; i++) {
            Label perduts = new Label("PERDEDORES " + i, skin_inputs);
            perdedores.add(perduts);
            perdedors.add(perduts);
        }

        Label tituloGanador = new Label("GANADORES", skin_inputs);
        Label tituloPerdedores = new Label("PERDEDORES", skin_inputs);


        window.add(tituloGanador).row();
        window.add(ganadors).row();
        window.add(tituloPerdedores).row();
        window.add(perdedors);

        stage.addActor(window);

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
