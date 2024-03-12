package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Utils.Settings;
import objects.Background;

public class RegisterScreen implements Screen {

    Stage stage;

    OrthographicCamera camera;

    Image cuadratFons;

    Background bg;
    private TextField usernameField;
    private TextField passwordField;
    private Skin skin;


    public RegisterScreen() {
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
        Gdx.input.setInputProcessor(stage);

        bg = new Background(0, 0, Settings.GAME_WIDTH , Settings.GAME_HEIGHT);

        //AÑADIMOS EL FONDO AL STAGE
        stage.addActor(bg);

        cuadratFons = new Image(AssetManager.imgCuadrado);
        cuadratFons.setPosition(Settings.GAME_WIDTH/2 - cuadratFons.getWidth()/2,Settings.GAME_HEIGHT/2 - cuadratFons.getHeight()/2);

        stage.addActor(cuadratFons);


        Table table = new Table();
        table.setFillParent(true);

        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = AssetManager.font;
        tfs.fontColor = Color.BLACK; // Set a valid font color
        tfs.cursor = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("cursor.png")))); // Set a valid cursor


        // Create text fields
        usernameField = new TextField("",tfs);
        passwordField = new TextField("", tfs);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        Label.LabelStyle ls = new Label.LabelStyle();
        ls.font = AssetManager.font;

        // Create labels
        Label usernameLabel = new Label("Username:", ls);
        Label passwordLabel = new Label("Password:", ls);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = AssetManager.font;

        // Create buttons
        TextButton loginButton = new TextButton("Register", tbs);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Handle login logic here
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }
        });

        // Add elements to the table
        table.add(usernameLabel).pad(10);
        table.add(usernameField).width(200).pad(10);
        table.row();
        table.add(passwordLabel).pad(10);
        table.add(passwordField).width(200).pad(10);
        table.row();
        table.add(loginButton).colspan(2).pad(10);
        stage.addActor(table);



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

    }
}
