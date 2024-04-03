package com.mygdx.game;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import Utils.Settings;
import objects.Background;

public class ShopScreen implements Screen {

    Pixel_R6 game;

    Background bg;

    Stage stage;

    OrthographicCamera camera;

    Skin skin, skin_txt;

    private float valorSliderVolumen, valorSliderMusic;

    Preferences preferences;

    JSONArray skins;
    boolean carregat, imageLoaded = false;

    Label[] labels = new Label[4];
    ImageButton[] imatges = new ImageButton[4];

    Drawable[] imatgesBaixades = new Drawable[4];

    ImageButton.ImageButtonStyle[] imageStyle = new ImageButton.ImageButtonStyle[4];
    public ShopScreen(Pixel_R6 game) {
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



        //VENTANA
        Window.WindowStyle windowStyle = skin.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("TENDA", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Settings.GAME_WIDTH;
        int gameHeight = Settings.GAME_HEIGHT;
        window.setSize(1200,600); // Establece el tamaño como desees

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = gameWidth - window.getWidth() * 1.1f;
        float windowY = gameHeight - window.getHeight() * 1.2f;

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);




        stage.addActor(window);


        TextButton.TextButtonStyle textButtonStyle = skin.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_volver = new TextButton("Volver", textButtonStyle);

        btn_volver.setSize(200, 70);

        btn_volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PantallaPrincipal(game));


            }
        });

        btn_volver.setPosition(windowX + 100, windowY - 100);

        stage.addActor(btn_volver);


        Gdx.input.setInputProcessor(stage);


        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        // Construct the URL with query parameters
        String url = "http://r6pixel.dam.inspedralbes.cat:3169/getAssets";
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
                    skins = json.getJSONArray("skins");

                    for(int i = 0; i < skins.length(); i++){
                        JSONObject item = skins.getJSONObject(i);
                        fetchAndSetImage(item.getString("pngSkin"),i);
                    }

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



        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin.get("title", Label.LabelStyle.class);

        for (int i = 0; i < labels.length; i++){
            labels[i] = new Label("ITEM " + i, labelStyle);
            labels[i].setPosition(i * 100, 100);
        }


        for(int i = 0; i < imatges.length; i++){
            imageStyle[i] = new ImageButton.ImageButtonStyle();

            TextureRegion textureRegion = new TextureRegion(AssetManager.persona, 0, 0, 1600, 1600);
//            textureRegion.setRegionWidth(1600);
//            textureRegion.setRegionHeight(1600);
            TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
            imageStyle[i].imageUp = drawable;
            imatges[i] = new ImageButton(imageStyle[i]);
            imatges[i].setSize(250,200);
            imatges[i].getImageCell().size(imatges[i].getWidth(), imatges[i].getHeight()).expand().fill();
        }




        //AFEGIR COSES A WINDOW
        window.row();
        window.add(labels[0]);
        window.add(labels[1]);
        window.row();
        window.add(imatges[0]);
        window.add(imatges[1]);
        window.row();
        window.add(labels[2]);
        window.add(labels[3]);
        window.row();
        window.add(imatges[2]);
        window.add(imatges[3]);
        // Crear una instancia de Label con el texto "Username" y el estilo definido




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);

        if(skins != null && !carregat){
            for(int i = 0; i < labels.length; i++){
                JSONObject item = skins.getJSONObject(i);
                labels[i].setText(item.get("nombre").toString());
            }
            carregat = true;
        }
            for (int i = 0; i < imatgesBaixades.length; i++) {
                if (imatgesBaixades[i] != null) {
                    System.out.println(i);
                    imageStyle[i].imageUp = imatgesBaixades[i];
                    //imatges[i].getImage().setDrawable(imatgesBaixades[i]);
                }

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

    }
    public void fetchAndSetImage(String imageUrl, int index) {
        // Create a GET request to fetch the image
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("http://r6pixel.dam.inspedralbes.cat:3169/getImg/" + imageUrl);
        httpRequest.setHeader("Content-Type", "application/json");

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
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
                        System.out.println(index);
                        imatgesBaixades[index] = drawable;

                    }
                });

            }

            @Override
            public void failed(Throwable t) {
                // Handle the failure of the HTTP request
                Gdx.app.error("ImageFetch", "Failed to fetch image: " + t.getMessage());
            }

            @Override
            public void cancelled() {
                // Handle the cancellation of the HTTP request
                Gdx.app.log("ImageFetch", "Image fetch request cancelled");
            }
        });
    }

}
