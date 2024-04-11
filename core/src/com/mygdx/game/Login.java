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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Utils.Settings;
import objects.Background;

public class Login implements Screen {

    Pixel_R6 game;

    Background bg;

    Stage stage;

    OrthographicCamera camera;

    // Añade un Skin
    Skin skin, skin_inputs;


    TextField Username, Password;

    Preferences preferences;


    public Login(Pixel_R6 game) {
        this.game = game;

        //AssetManager.load();

        preferences = Gdx.app.getPreferences("Pref");

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


        //TITULO
        // Carga el Skin
        skin = new Skin(Gdx.files.internal("skin_txt/arcade-ui.json"));

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
        float posX = (Gdx.graphics.getWidth() - titleLabel.getWidth()) / 2;

        // Calcula la posición Y en la parte superior de la pantalla
        float posY = Settings.GAME_HEIGHT - titleLabel.getHeight(); // Margen de 20 píxeles

        // Establece la posición del título
        titleLabel.setPosition(posX, posY); // Alinea el título en la parte superior y centrado

        // Añade el título al stage
        stage.addActor(titleLabel);


        //BOTONES
        // Cargar el Skin
        skin_inputs = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton.TextButtonStyle textButtonStyle = skin_inputs.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_inicio = new TextButton("Inicio Sesion", textButtonStyle);

        btn_inicio.setSize(200, 70);

        // Agregar un ClickListener al botón
        btn_inicio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                try {
                    // Obtener el texto ingresado por el usuario en el TextField
                    String nombreUsuario = Username.getText();
                    String PasswordUsuario = Password.getText();

                    // Imprimir el nombre de usuario por consola
                    System.out.println("Nombre de usuario: " + nombreUsuario);
                    System.out.println("Contraseña: " + PasswordUsuario);

                    // Crear un HashMap para almacenar los datos del usuario
                    JSONObject json = new JSONObject();
                    json.put("user", nombreUsuario);
                    json.put("pwd", PasswordUsuario);

                    System.out.println(json);


                    // Crear una solicitud HTTP POST
                    Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
                    httpRequest.setUrl("http://192.168.205.67:3168/login"); // URL de tu servidor
                    httpRequest.setHeader("Content-Type", "application/json");
                    String data = json.toString();
                    httpRequest.setContent(data);

                    // Enviar la solicitud al servidor
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            try {
                                System.out.println("Respuesta: " + httpResponse);

                                HttpStatus status = httpResponse.getStatus();

                                System.out.println("ANTES DEL IF: " + status.getClass());


                                // Obtener el contenido de la respuesta como una cadena
                                String responseString = httpResponse.getResultAsString();

                                // Imprimir la respuesta recibida desde el servidor
                                System.out.println("Respuesta del servidor: " + responseString);

                                // Analizar la respuesta como un objeto JSON
                                JSONObject jsonResponse = new JSONObject(responseString);

                                System.out.println("JSON: " + jsonResponse);

                                boolean valido = jsonResponse.getBoolean("auth");

                                System.out.println("Boolean: " + valido);

                                if (valido) {
                                    game.setLoggedIn(true, nombreUsuario);
                                    // Redirige a la pantalla principal
                                    Gdx.app.postRunnable(() -> {
                                        game.setScreen(new PantallaPrincipal(game, true));
                                    });

                                } else {

                                    Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);
                                    // Crea una instancia de Window con el estilo obtenido
                                    Window windowerror = new Window("ERROR", windowStyle);
                                    windowerror.getTitleLabel().setAlignment(Align.center);

                                    // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
                                    float windowErrorX = ((Gdx.graphics.getWidth() *0.93f) - windowerror.getWidth()) / 2;
                                    float windowErrorY = (Settings.GAME_HEIGHT - windowerror.getHeight()) / 2;

                                    // Establece la posición de la ventana en el centro de la pantalla
                                    windowerror.setPosition(windowErrorX, windowErrorY);

                                    windowerror.setSize(200,100);

                                    Label.LabelStyle labelStyle = skin_inputs.get("error", Label.LabelStyle.class);

                                    Label errorUser = new Label("Usuario no registrado", labelStyle);

                                    windowerror.add(errorUser);

                                    stage.addActor(windowerror);

                                    windowerror.setName("error_window");

                                    // Crear instancia del TextButton con el estilo obtenido del Skin
                                    TextButton btn_cerrar = new TextButton("CERRRAR", textButtonStyle);

                                    btn_cerrar.setPosition(windowErrorX + 40, windowErrorY - 50);

                                    stage.addActor(btn_cerrar);

                                    btn_cerrar.setName("btn_cerrar_windows");

                                    btn_cerrar.addListener(new ClickListener(){
                                        @Override
                                        public void clicked(InputEvent event, float x, float y) {
                                            stage.getRoot().findActor("error_window").remove();
                                            stage.getRoot().findActor("btn_cerrar_windows").remove();
                                        }
                                    });
                                    System.out.println("NO ESTA EN LA BASE");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failed(Throwable t) {
                            // Ocurrió un error al enviar la solicitud
                            System.out.println("Error al enviar la solicitud: " + t.getMessage());
                        }

                        @Override
                        public void cancelled() {
                            // La solicitud fue cancelada
                            System.out.println("Solicitud cancelada");
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_registrar = new TextButton("Registrar", textButtonStyle);

        btn_registrar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("PANTALLA REGISTRO");
                game.setScreen(new RegisterScreen(game));
            }
        });

        btn_registrar.setSize(200, 70);


        //INPUTS
        // Obtener el estilo del TextField del Skin
        TextField.TextFieldStyle textFieldStyle = skin_inputs.get("default", TextField.TextFieldStyle.class);

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


        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin_inputs.get("title", Label.LabelStyle.class);

        // Crear una instancia de Label con el texto "Username" y el estilo definido
        Label usernameLabel = new Label("Username", labelStyle);

        Label passwordLabel = new Label("Password", labelStyle);



        //VENTANA
        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("LOGIN", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Gdx.graphics.getWidth();
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

        // Agregar los TextField a la tabla, uno debajo del otro
        table.add(usernameLabel).pad(20);
        table.add(Username).pad(20).prefSize(250, 50).row(); // La función row() indica que se moverá a la siguiente fila después de este componente
        table.add(passwordLabel).pad(20);
        table.add(Password).pad(20).prefSize(250, 50).row();

        // Agregar la ventana al Stage
        stage.addActor(window);


        //POSICION BOTONES BAJO WINDOWS
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones

        // Establecer la posición de los botones
        btn_inicio.setPosition(windowX - 20, btnY); // Posición del botón "Inicio Sesión"
        btn_registrar.setPosition(windowX + 230, btnY); // Posición del botón "Registrar"

        // Agregar los botones al Stage
        stage.addActor(btn_inicio);
        stage.addActor(btn_registrar);

        //PARA INTRODUCIR DATOS
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
