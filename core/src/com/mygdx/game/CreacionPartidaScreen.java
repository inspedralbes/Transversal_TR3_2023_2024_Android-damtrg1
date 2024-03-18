package com.mygdx.game;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import Utils.Settings;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import objects.Background;
import objects.Jugador;
import sun.font.TextLabel;

public class CreacionPartidaScreen implements Screen {

    Game game;

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
JSONObject json;
    Preferences preferences;

    public CreacionPartidaScreen(Game game){
        this.game = game;

        preferences = Gdx.app.getPreferences("Pref");
        for (int i = 0; i < 10; i++) {
            usuarisSala.add("NO PLAYER");
        }
// Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

// Construct the URL with query parameters
        String url = "http://r6pixel.dam.inspedralbes.cat:3169/crearSala";
        String username = preferences.getString("username");
        url += "?user=" + username;

        httpRequest.setUrl(url);
        httpRequest.setHeader("Content-Type", "application/json");

// Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();
                    // Handle the response data here
                    JSONObject json = new JSONObject(responseData);
                    salaId = json.getString("salaId");// Parse the JSON response string
                    JSONArray jsonArray = new JSONArray(json.getJSONArray("users"));

                    // Iterate through the JSON array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Get each element from the JSON array and add it to the list
                        String element = jsonArray.getString(i);
                        usuarisSala.set(i, element);
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
        float posX = (Settings.GAME_WIDTH - titleLabel.getWidth()) / 2;

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

        TextButton botonListo = new TextButton("LISTO", textButtonStyle);


        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin_inputs.get("title", Label.LabelStyle.class);

        // Crear una instancia de Label con el texto "Username" y el estilo definido
        salaLabel = new Label("Sala: " + salaId, labelStyle);


        //VENTANA
        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("SALA", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Settings.GAME_WIDTH;
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 3.3f);
        float windowY = ((gameHeight / 2) - window.getHeight() *1.7f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(1000, 500); // Establece el tamaño como desees

        Table table = new Table();
        table.setFillParent(false); // La tabla ocupará todo el espacio del padre, que es la ventana en este caso

        Label labelPlayer1 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer1);
        Label labelPlayer2 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer2);
        Label labelPlayer3 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer3);
        Label labelPlayer4 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer4);
        Label labelPlayer5 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer5);
        Label labelPlayer6 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer6);
        Label labelPlayer7 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer7);
        Label labelPlayer8 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer8);
        Label labelPlayer9 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer9);
        Label labelPlayer10 = new Label("PLAYER NULL", labelStyle);
        labelsUsuaris.add(labelPlayer10);


        table.add(salaLabel);
        table.getCell(salaLabel).center();
        table.row();
        table.add(labelPlayer1);
        table.add(labelPlayer2);
        table.add(labelPlayer3);
        table.add(labelPlayer4);
        table.add(labelPlayer5);
        table.row();
        table.add(labelPlayer6);
        table.add(labelPlayer7);
        table.add(labelPlayer8);
        table.add(labelPlayer9);
        table.add(labelPlayer10);



        window.add(table); // Agregar la tabla a la ventana

        // Agregar la ventana al Stage
        stage.addActor(window);
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones

        // Establecer la posición de los botones
        botonListo.setPosition(windowX-20, btnY); // Posición del botón "Inicio Sesión"

        // Agregar los botones al Stage
        stage.addActor(botonListo);

        //PARA INTRODUCIR DATOS
        Gdx.input.setInputProcessor(stage);






        try {
            mSocket = IO.socket("http://r6pixel.dam.inspedralbes.cat:3169");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("user", preferences.getString("username"));

        mSocket.on("userNuevo",  new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonString = (String) args[0];
                try {
                    JSONObject data = new JSONObject(jsonString);
                    String user = data.getString("user");
                    if(!user.equals(preferences.getString("username"))){
                        int contador = 0;
                        for (String usuari: usuarisSala
                             ) {
                            if(!usuari.equals("NO PLAYER")){
                                contador++;
                            }
                        }
                        usuarisSala.set(contador, user);
                    }
                    System.out.println("Nuevo usuario: " + user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
        salaLabel.setText("Sala: " + salaId );
        for(int i = 0; i < labelsUsuaris.size(); i++){
            labelsUsuaris.get(i).setText(usuarisSala.get(i));
        }

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
