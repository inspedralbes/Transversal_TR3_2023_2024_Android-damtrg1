package com.mygdx.game;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import Utils.Settings;
import io.socket.client.IO;
import io.socket.client.Socket;
import objects.Background;

public class CodigoSalaScreen implements Screen {

    Game game;
    TextField codigo;

    Background bg;

    Stage stage;

    Socket mSocket;
    OrthographicCamera camera;

    // Añade un Skin
    Skin skin, skin_inputs;
    ArrayList<String> usuarisSala = new ArrayList<>();
    ArrayList<Label> labelsUsuaris = new ArrayList<>();
    Label salaLabel;

    String salaId;
    Preferences preferences;

    public CodigoSalaScreen(Pixel_R6 game) {
                this.game = game;

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
        bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT, false);

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

        skin_inputs = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Añade el título al stage
        stage.addActor(titleLabel);

        try {
            mSocket = IO.socket("http://r6pixel.duckdns.org:3168");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();

        TextButton.TextButtonStyle textButtonStyle = skin_inputs.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_acceder = new TextButton("ENTRAR", textButtonStyle);

        btn_acceder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Create an HTTP request
                Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);

                // Construct the URL with query parameters
                String url = "http://r6pixel.duckdns.org:3168/unirSala";
                String username = preferences.getString("username");
                httpRequest.setUrl(url);
                httpRequest.setHeader("Content-Type", "application/json");
                JSONObject json = new JSONObject();
                json.put("user", username);
                json.put("sala", codigo.getText());
                json.put("equip", "EQUIP NO SELECCIONAT");
                json.put("nom_skin",LoadingScreen.nom_skin);
                httpRequest.setContent(json.toString());

                // Send the HTTP request
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        HttpStatus status = httpResponse.getStatus();
                        if (status.getStatusCode() == 200) {
                            // If the request was successful (status code 200)
                            String responseData = httpResponse.getResultAsString();
                            // Handle the response data here
                            JSONObject jsonResponse = new JSONObject(responseData);
                            if (jsonResponse.getBoolean("auth")) {
                                System.out.println("UNIDO A LA SALA");
                                mSocket.emit("userNuevo", json.toString());
                                Gdx.app.postRunnable(() -> {
                                    game.setScreen(new UnidoSalaScreen(game, json.getString("sala")));
                                });
                            }
                            System.out.println(responseData);
                        } else {
                            // If the request failed, handle the error
                            System.out.println("HTTP request failed with status code: " + status.getStatusCode());
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        // Handle the case where the HTTP request failed
                        t.printStackTrace();
                    }

                    @Override
                    public void cancelled() {
                        // Handle the case where the HTTP request was cancelled
                    }
                });


            }
        });

        btn_acceder.setSize(200, 70);


        TextField.TextFieldStyle textFieldStyle = skin_inputs.get("default", TextField.TextFieldStyle.class);

        // Crear una instancia de TextField con el estilo obtenido
        codigo = new TextField("Ingrese codigo", textFieldStyle);

        // Configurar el controlador de eventos para el TextField
        codigo.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (codigo.getText().equals("Ingrese codigo")) {
                    // Si lo es, borrar el texto
                    codigo.setText("");
                } else {
                    // Procesa los datos ingresados por el usuario
                    String userInput = codigo.getText();
                    // Aquí puedes hacer algo con los datos ingresados, como validación o procesamiento.
                    // Limpiar el TextField después de procesar los datos (opcional)
                    codigo.setText(userInput);
                }
                return true;
            }
        });


        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin_inputs.get("title", Label.LabelStyle.class);

        // Crear una instancia de Label con el texto "Username" y el estilo definido
        salaLabel = new Label("Codigo Sala", labelStyle);


        //VENTANA
        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("SALA", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Gdx.graphics.getWidth();
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 3.3f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 1.7f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);
        window.setMovable(false);
        window.setSize(1000, 500); // Establece el tamaño como desees

        Table table = new Table();
        table.setFillParent(false); // La tabla ocupará todo el espacio del padre, que es la ventana en este caso


        table.add(salaLabel);
        table.getCell(salaLabel).center();
        table.row();
        table.add(codigo).prefSize(250, 50).row();
        table.add(btn_acceder);

        window.add(table); // Agregar la tabla a la ventana

        // Agregar la ventana al Stage
        stage.addActor(window);
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones


        //PARA INTRODUCIR DATOS
        Gdx.input.setInputProcessor(stage);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_volver = new TextButton("Volver", textButtonStyle);

        btn_volver.setSize(200, 70);

        btn_volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaPrincipal(game, false));
            }
        });

        btn_volver.setPosition(windowX + 100, windowY - 100);

        stage.addActor(btn_volver);
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
        mSocket.disconnect();
    }
}
