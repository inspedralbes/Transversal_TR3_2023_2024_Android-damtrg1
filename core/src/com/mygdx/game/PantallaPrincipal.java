package com.mygdx.game;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import Utils.Settings;
import io.socket.client.IO;
import io.socket.client.Socket;
import objects.Background;
import io.socket.client.IO;
import io.socket.client.Socket;

public class PantallaPrincipal implements Screen {

    Pixel_R6 game;

    Background bg;

    static Stage stage;

    OrthographicCamera camera;

    Skin skin;
    static Skin skin_txt;
    JSONArray noticies = null;
    boolean creat =false;

    Preferences preferences;

    Socket mSocket;

    Window popupWindow;

    ArrayList<Label> llistaTitols;
    ArrayList<Label> llistaDescripcio = new ArrayList<>();
    ArrayList<Image> llistaImatges = new ArrayList<>();




    public PantallaPrincipal(Pixel_R6 game, boolean nou) {
        this.game = game;

        AssetManager.load();


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

        if(nou) {
            // Create a GET request to fetch the image
            Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
            httpRequest.setUrl("http://r6pixel.duckdns.org:3169/logged/" + preferences.getString("username"));
            httpRequest.setHeader("Content-Type", "application/json");

            // Send the request
            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    HttpStatus status = httpResponse.getStatus();
                    if (status.getStatusCode() == 200) {
                        // If the request was successful (status code 200)
                        String responseData = httpResponse.getResultAsString();
                        JSONObject json = new JSONObject(responseData);
                        if (json.getBoolean("auth")) {
                            Gdx.app.postRunnable(() -> {
                                game.setScreen(new Login(game));
                            });
                        }
                    }

                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void cancelled() {

                }
            });

            try {
                mSocket = IO.socket("http://r6pixel.duckdns.org:3169");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            mSocket.connect();
            JSONObject jsonSocket = new JSONObject();
            jsonSocket.put("user", preferences.getString("username"));
            mSocket.emit("loggedIn", jsonSocket.toString());
        }

        // Create a GET request to fetch the image
        Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest2.setUrl("http://r6pixel.duckdns.org:3169/getMonedes/" + preferences.getString("username"));
        httpRequest2.setHeader("Content-Type", "application/json");

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();
                    JSONObject json = new JSONObject(responseData.substring(1, responseData.length() - 1));
                    preferences.putString("monedas", "" +json.get("monedas"));
                    preferences.flush();
                }

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });




                //CONFIGURACION DEL FONDO
        bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT);

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
        float posX = (Gdx.graphics.getWidth() - titleLabel.getWidth()) / 2;

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
                mSocket.emit("sesionCerrada");
                game.setScreen(new Login(game));
                game.setLoggedIn(false, "");
            }
        });

        //TAMAÑO DEL BOTON
        btn_play.setSize(250, 70);
        btn_settings.setSize(250, 70);
        btn_log_out.setSize(250, 70);

        // Calcula las coordenadas X e Y para colocar los botones en el medio de la pantalla
        float btnX = (Gdx.graphics.getWidth() - btn_play.getWidth()) / 2;
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
                game.setScreen(new ShopScreen(game));
            }
        });

        TextButton btn_news = new TextButton("TENDA", textButtonStyle);
        btn_news.setSize(70,70);
        btn_news.setPosition(0 + btn_tenda.getWidth(), Settings.GAME_HEIGHT - btn_news.getHeight());

        btn_news.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNewsWindows();
            }
        });


        stage.addActor(btn_news);
        stage.addActor(btn_tenda);
        stage.addActor(btn_play);
        stage.addActor(btn_settings);
        stage.addActor(btn_log_out);


        //USERNAME
        // Obtener el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titlestyleUser = skin.get("title", Label.LabelStyle.class);


        Label titUssername = new Label(preferences.getString("username"), titlestyleUser);

        // Calcular la posición X para que el Label esté a la derecha de la pantalla
        float posX2 = Gdx.graphics.getWidth() - titUssername.getWidth();

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


        if(creat){
            for(int i = 0; i < noticies.length(); i++){
                JSONObject json = noticies.getJSONObject(i);
                System.out.println(noticies);
                llistaTitols.get(i).setText(json.getString("title"));
                llistaDescripcio.get(i).setText(json.getString("Description"));
            }
        }



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


    private void showNewsWindows(){
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Label.LabelStyle labelDescripcioStyle = skin.get(Label.LabelStyle.class);
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://r6pixel.duckdns.org:3169/getBroadcastNews");
        httpRequest.setHeader("Content-Type", "application/json");

        Label.LabelStyle titleLabelStyle = skin_txt.get("title", Label.LabelStyle.class);

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();
                    noticies = new JSONArray(responseData);
                    System.out.println(noticies.length());
                    for(int i = 0; i < noticies.length(); i++){
                        System.out.println(i);
                        Label titol = new Label("" ,titleLabelStyle);
                        llistaTitols.add(titol);
                        Label desc = new Label("", labelDescripcioStyle);
                        llistaDescripcio.add(desc);
                        Image img = new Image();
                        llistaImatges.add(img);
                        System.out.println("AAAAAAAAAAAAAAAAAAAa");
                    }
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                    creat = true;
                }

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });


        popupWindow = new Window("NOTCIIES", skin);
        popupWindow.getTitleLabel().setAlignment(Align.center);

        popupWindow.setSize(Settings.GAME_WIDTH * 0.75f, Settings.GAME_HEIGHT*0.75f);
        popupWindow.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()) / 2);
        popupWindow.setMovable(false); // Make the window non-movable
        popupWindow.setModal(true); // Make the window modal (blocks input to other actors)

        ScrollPane sp = new ScrollPane(popupWindow);

        sp.setSize(Settings.GAME_WIDTH * 0.75f, Settings.GAME_HEIGHT*0.75f);
        sp.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()) / 2);

        stage.addActor(sp);
    }
}
