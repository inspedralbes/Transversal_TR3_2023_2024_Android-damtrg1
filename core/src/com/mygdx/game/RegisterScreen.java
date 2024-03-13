package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Utils.Settings;
import objects.Background;

public class RegisterScreen implements Screen {

    Stage stage;

    OrthographicCamera camera;

    Background bg;
    private Skin skin, skin_windows;
    Pixel_R6 game;

    TextField Username, Password, Mail, FechaNA;


    public RegisterScreen(Pixel_R6 game) {

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
        Gdx.input.setInputProcessor(stage);

        bg = new Background(0, 0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        //AÑADIMOS EL FONDO AL STAGE
        stage.addActor(bg);



        //TITULO
        // Carga el Skin
        skin = new Skin(Gdx.files.internal("skin_txt/arcade-ui.json"));
        skin_windows = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Registro del color blanco en el Skin
        Color blanco = Color.WHITE;
        skin.add("white", blanco);

        // Obtén el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titleLabelStyle = skin.get("title", Label.LabelStyle.class);

        // Cambia el color del texto a blanco
        titleLabelStyle.fontColor = blanco;

        // Cambia el tamaño de la fuente del título
        BitmapFont titleFont = skin.getFont("title"); // Obtiene la fuente del título
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


        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin_windows.get("title", Label.LabelStyle.class);

        // Crear una instancia de Label con el texto "Username" y el estilo definido
        Label usernameLabel = new Label("Username", labelStyle);

        Label passwordLabel = new Label("Password", labelStyle);

        Label MailLabel = new Label("Correo", labelStyle);

        Label fecha_nacimento = new Label("Fecha", labelStyle);



        //INPUTS
        // Obtener el estilo del TextField del Skin
        TextField.TextFieldStyle textFieldStyle = skin_windows.get("default", TextField.TextFieldStyle.class);

        // Crear una instancia de TextField con el estilo obtenido
        Username = new TextField("Ingrese nombre", textFieldStyle);

        // Configurar el controlador de eventos para el TextField
        Username.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (Username.getText().equals("Ingrese nombre")) {
                    // Si lo es, borrar el texto
                    Username.setText("");
                } else {
                    // Procesa los datos ingresados por el usuario
                    String userInput = Username.getText();
                    // Aquí puedes hacer algo con los datos ingresados, como validación o procesamiento.
                    // Limpiar el TextField después de procesar los datos (opcional)
                    Username.setText(userInput);
                }
                return true;
            }
        });

        // Crear una instancia de TextField con el estilo obtenido
        Password = new TextField("Ingrese Password", textFieldStyle);

        // Configurar el controlador de eventos para el TextField
        Password.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (Password.getText().equals("Ingrese Password")) {
                    // Si lo es, borrar el texto
                    Password.setText("");
                } else {
                    Password.setPasswordMode(true);
                    Password.setPasswordCharacter('*');
                    // Procesa los datos ingresados por el usuario
                    String passwordInput = Password.getText();
                    // Aquí puedes hacer algo con los datos ingresados, como validación o procesamiento.
                    // Limpiar el TextField después de procesar los datos (opcional)
                    Password.setText(passwordInput);
                }
                return true;
            }
        });

        Mail =  new TextField("Ingrese su correo", textFieldStyle);

        Mail.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (Mail.getText().equals("Ingrese Password")) {
                    // Si lo es, borrar el texto
                    Mail.setText("");
                } else {
                    // Procesa los datos ingresados por el usuario
                    String passwordInput = Mail.getText();
                    // Aquí puedes hacer algo con los datos ingresados, como validación o procesamiento.
                    // Limpiar el TextField después de procesar los datos (opcional)
                    Mail.setText(passwordInput);
                }
                return true;
            }
        });

        FechaNA =  new TextField("Ingrese fecha de nacimiento", textFieldStyle);
        FechaNA.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (FechaNA.getText().equals("Ingrese Password")) {
                    // Si lo es, borrar el texto
                    FechaNA.setText("");
                } else {
                    // Procesa los datos ingresados por el usuario
                    String passwordInput = FechaNA.getText();
                    // Aquí puedes hacer algo con los datos ingresados, como validación o procesamiento.
                    // Limpiar el TextField después de procesar los datos (opcional)
                    FechaNA.setText(passwordInput);
                }
                return true;
            }
        });



        //VENTANA
        Window.WindowStyle windowStyle = skin_windows.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("Registrar", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Settings.GAME_WIDTH;
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 1.5f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 1.5f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(400, 400); // Establece el tamaño como desees

        Table table = new Table();
        table.setFillParent(false); // La tabla ocupará todo el espacio del padre, que es la ventana en este caso
        window.add(table); // Agregar la tabla a la ventana

        // Agregar la ventana al Stage
        stage.addActor(window);

        table.add(usernameLabel).pad(5).height(50);
        table.add(Username).pad(5).prefSize(250, 50).row(); // La función row() indica que se moverá a la siguiente fila después de este componente
        table.add(passwordLabel).pad(5).height(50);
        table.add(Password).pad(5).prefSize(250, 50).row();
        table.add(MailLabel).pad(5).height(50);
        table.add(Mail).pad(5).prefSize(250,50).row();
        table.add(fecha_nacimento).pad(5).height(50);
        table.add(FechaNA).pad(5).prefSize(250,50).row();



        //BOTONES
        TextButton.TextButtonStyle textButtonStyle = skin_windows.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_registrarse = new TextButton("Registrarse", textButtonStyle);


        // Agregar un ClickListener al botón
        btn_registrarse.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("PANTALLA JUEGO");
                game.setScreen(new GameSceen(game));
            }
        });

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_volver = new TextButton("Volver", textButtonStyle);

        btn_volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("PANTALLA PRINCIPAL");
                game.setScreen(new Login(game));
            }
        });


        //POSICION BOTONES BAJO WINDOWS
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones

        // Establecer la posición de los botones
        btn_registrarse.setPosition(windowX + 10, btnY); // Posición del botón "Inicio Sesión"
        btn_volver.setPosition(windowX + 270, btnY); // Posición del botón "Registrar"

        // Agregar los botones al Stage
        stage.addActor(btn_registrarse);
        stage.addActor(btn_volver);

        //PARA INTRODUCIR DATOS
        Gdx.input.setInputProcessor(stage);

        /*
        cuadratFons = new Image(AssetManager.imgCuadrado);
        cuadratFons.setPosition(Settings.GAME_WIDTH / 2 - cuadratFons.getWidth() / 2, Settings.GAME_HEIGHT / 2 - cuadratFons.getHeight() / 2);

        stage.addActor(cuadratFons);


        Table table = new Table();
        table.setFillParent(true);

        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = AssetManager.font;
        tfs.fontColor = Color.BLACK; // Set a valid font color
        tfs.cursor = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("cursor.png")))); // Set a valid cursor


        // Create text fields
        usernameField = new TextField("", tfs);
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
*/

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
