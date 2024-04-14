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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
    ScrollPane sp;

    boolean creat =false;

    Preferences preferences;

    Socket mSocket;

    Window popupWindow;
    TextButton closeButton; // Declare the close button
    ArrayList<Drawable> llistaImatges = new ArrayList<>();
    ArrayList<Image> imgList = new ArrayList<>();

    Table table = new Table();




    public PantallaPrincipal(Pixel_R6 game, boolean nou) {
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

        if(nou) {
            // Create a GET request to fetch the image
            Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
            httpRequest.setUrl("http://192.168.1.35:3168/logged/" + preferences.getString("username"));
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
                mSocket = IO.socket("http://192.168.1.35:3168");
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
        httpRequest2.setUrl("http://192.168.1.35:3168/getMonedes/" + preferences.getString("username"));
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
        bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT, false);

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
        //BitmapFont titleFont = skin_txt.getFont("title"); // Obtiene la fuente del título
        //titleFont.getData().setScale(2.0f); // Establece el tamaño de la fuente (cambia el valor como desees)


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
                //game.setScreen(new PantallaWIN(game));
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
        btn_tenda.setSize(100,70);
        btn_tenda.setPosition(0, Settings.GAME_HEIGHT - btn_tenda.getHeight());

        btn_tenda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ShopScreen(game));
            }
        });

        TextButton btn_news = new TextButton("NOTICIES", textButtonStyle);
        btn_news.setSize(150,70);
        btn_news.setPosition(20 + btn_tenda.getWidth() , Settings.GAME_HEIGHT - btn_news.getHeight());

        btn_news.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNewsWindows();
            }
        });

        TextButton btn_inventari = new TextButton("INVENTARI", textButtonStyle);
        btn_inventari.setSize(150,70);
        btn_inventari.setPosition(90 + btn_tenda.getWidth() * 2, Settings.GAME_HEIGHT - btn_news.getHeight());

        btn_inventari.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new InventariScreen(game));
            }
        });


        stage.addActor(btn_inventari);
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


        Label.LabelStyle labelDescripcioStyle = skin.get(Label.LabelStyle.class);
        Label.LabelStyle labelTitleStyle = skin.get("title-plain",Label.LabelStyle.class);
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://r6pixel.duckdns.org:3168/getBroadcastNews");
        httpRequest.setHeader("Content-Type", "application/json");

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();
                    noticies = new JSONArray(responseData);
                    llistaImatges = new ArrayList<>(Collections.nCopies(noticies.length(), null));
                    imgList = new ArrayList<>();

                    creat = true;
                }
            }

            @Override
            public void failed(Throwable t) {
                // Handle failure
            }

            @Override
            public void cancelled() {
                // Handle cancellation
            }
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(llistaImatges != null){
            for (int i = 0; i < imgList.size();i++) {
                Cell<Image> imageCell = table.getCell(imgList.get(i));
                Image novaImatge = new Image(llistaImatges.get(i));
                if(novaImatge.getDrawable() != null) {
                    imageCell.setActor(novaImatge);
                    imgList.set(i, novaImatge);
                }
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


    private void showNewsWindows() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Label.LabelStyle labelDescripcioStyle = skin.get(Label.LabelStyle.class);
        Label.LabelStyle labelTitleStyle = skin.get("title-plain",Label.LabelStyle.class);
        TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);

        closeButton = new TextButton("VOLVER", textButtonStyle);
        closeButton.setSize(Settings.GAME_WIDTH * 0.2f, Settings.GAME_HEIGHT * 0.1f);

        // Add listener to close button
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hide the Noticies window
                popupWindow.remove();
                // Remove the close button
                closeButton.remove();
                sp.remove();
                noticies = null;
                llistaImatges = null;
                table = null;

            }
        });
        closeButton.setPosition(0,0);
        stage.addActor(closeButton);

        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://192.168.1.35:3168/getBroadcastNews");
        httpRequest.setHeader("Content-Type", "application/json");

        Label.LabelStyle titleLabelStyle = skin_txt.get("title", Label.LabelStyle.class);
        popupWindow = new Window("NOTICIES", skin);
        popupWindow.getTitleLabel().setAlignment(Align.center);

        popupWindow.setSize(Settings.GAME_WIDTH * 0.75f, Settings.GAME_HEIGHT * 0.75f);
        popupWindow.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()) / 2);
        popupWindow.setMovable(false); // Make the window non-movable



        for (int i = 0; i < noticies.length(); i++) {
            JSONObject json = noticies.getJSONObject(i);

            //Load image asynchronously
            TextureRegionDrawable imageDrawable = new TextureRegionDrawable(new TextureRegion(AssetManager.persona));
            Image image = new Image(imageDrawable);
            table.add(image).colspan(1).size(Settings.GAME_WIDTH * 0.15f, Settings.GAME_HEIGHT * 0.15f); // Add image to the table and make it span two rows
            imgList.add(image);


            // Create title label
            Label title = new Label(json.getString("title"), labelTitleStyle);
            title.setWrap(true);
            table.add(title).expandX().fillX().pad(10).row(); // Add title to the table and start a new row

            Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);
            httpRequest2.setUrl("http://r6pixel.duckdns.org:3168/getImgBroadcast/" + json.getString("image"));
            httpRequest2.setHeader("Content-Type", "application/json");
            // Send the request
            int finalI = i;
            Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    byte[] imageData = httpResponse.getResult();
                    // Load the image data into a Pixmap
                    Pixmap pixmap = new Pixmap(imageData, 0, imageData.length);

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run () {
                            // Create a Texture from the Pixmap
                            Texture texture = new Texture(pixmap);

                            // Dispose the Pixmap to release its resources
                            pixmap.dispose();

                            // Create a TextureRegion from the Texture
                            TextureRegion textureRegion = new TextureRegion(texture);

                            // Create a TextureRegionDrawable from the TextureRegion
                            TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);

                            llistaImatges.set(finalI, drawable);

                        }
                    });
                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void cancelled() {

                }
            });

            // Create description label
            Label description = new Label(json.getString("description"), labelDescripcioStyle);
            description.setWrap(true);
            description.setAlignment(Align.center);
            table.add(description).left().expandX().fillX().pad(10).colspan(2).row(); // Add description to the table and start a new row

        }






        closeButton = new TextButton("VOLVER", textButtonStyle);
        closeButton.setSize(Settings.GAME_WIDTH * 0.2f, Settings.GAME_HEIGHT * 0.1f);

        // Add listener to close button
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Hide the Noticies window
                popupWindow.remove();
                // Remove the close button
                closeButton.remove();
                sp.remove();
                noticies = null;
                llistaImatges = null;
                table = null;

            }
        });
        closeButton.setPosition(0,0);
        stage.addActor(closeButton);



        Label.LabelStyle titleLabelStyle = skin_txt.get("title", Label.LabelStyle.class);
        popupWindow = new Window("NOTICIES", skin);
        popupWindow.getTitleLabel().setAlignment(Align.center);

        popupWindow.setSize(Settings.GAME_WIDTH * 0.75f, Settings.GAME_HEIGHT * 0.75f);
        popupWindow.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()) / 2);
        popupWindow.setMovable(false); // Make the window non-movable



        // Create a table to hold the titles, images, and descriptions
        table = new Table();
        table.setSize(Settings.GAME_WIDTH * 0.75f, Settings.GAME_HEIGHT * 0.75f);
        table.defaults().pad(5);


        // Add the table to the window
        stage.addActor(popupWindow);

        // Add the scroll pane with the table to the stage
        sp = new ScrollPane(table);
        sp.setSize(Settings.GAME_WIDTH * 0.65f, Settings.GAME_HEIGHT * 0.65f);
        sp.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()* 0.9f) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()* 0.9f) / 2);
        sp.setScrollingDisabled(true, false); // Disable horizontal scrolling
        stage.addActor(sp);
    }

}
