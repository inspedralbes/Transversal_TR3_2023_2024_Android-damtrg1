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
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

import java.util.ArrayList;
import java.util.Collections;

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

    ArrayList<Drawable> imatgesBaixades = new ArrayList<>(10);

    Label labelNom, descripcioLabel, labelBtn, labelNumMonedas;
    ImageButton.ImageButtonStyle imgStyle;
    Button btnEsquerra, btnDreta;
    TextButton btnComprar;

    ArrayList<String> inventariJugador;
    Window popupWindow;

    private int currentIndex = 0;
    public ShopScreen(Pixel_R6 game) {
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

        Label.LabelStyle labelStyleNumMonedas = skin.get("title", Label.LabelStyle.class);
        labelNumMonedas = new Label(preferences.getString("monedas"), labelStyleNumMonedas);
        float posx = Settings.GAME_WIDTH - labelNumMonedas.getWidth();
        float posy = Settings.GAME_HEIGHT - labelNumMonedas.getHeight();
        labelNumMonedas.setPosition(posx, posy);
        stage.addActor(labelNumMonedas);

        //VENTANA
        Window.WindowStyle windowStyle = skin.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("TENDA", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Settings.GAME_WIDTH;
        int gameHeight = Settings.GAME_HEIGHT;
        window.setSize((gameWidth * 0.5f), gameHeight * 0.75f); // Establece el tamaño como desees

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = (gameWidth - window.getWidth()) /2;
        float windowY = (gameHeight - window.getHeight()) /2;

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
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new PantallaPrincipal(game, false));
                });
            }
        });

        btn_volver.setPosition(windowX + 100, windowY - 100);

        stage.addActor(btn_volver);


        Gdx.input.setInputProcessor(stage);


        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        // Construct the URL with query parameters
        String url = "http://192.168.0.14:3168/getAssets";
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
//                    System.out.println(skins.length());
                    imatgesBaixades = new ArrayList<>(Collections.nCopies(skins.length(), null));
//                    System.out.println(imatgesBaixades.size());
                    for (int i = 0; i < skins.length(); i++) {
                        JSONObject item = skins.getJSONObject(i);
                        System.out.println(item);
                        fetchAndSetImage(item.getString("pngSkin"), i);
                        System.out.println("------" + item.getString("pngSkin"));
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

        // Create an HTTP request
        Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);

        // Construct the URL with query parameters
        String url2 = "http://192.168.0.14:3168/getInventari/" + preferences.getString("username");
        httpRequest2.setUrl(url2);
        httpRequest2.setHeader("Content-Type", "application/json");

        // Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();
                    String jsonArrayString = responseData.substring(1, responseData.length() - 1);
                    // Handle the response data here
                    System.out.println(jsonArrayString);
                    JSONObject json = new JSONObject(jsonArrayString);
//                    System.out.println(json);
                    JSONArray arrayResultat = (JSONArray) json.get("skins");
                    // Convert JSONArray to String[]
                    inventariJugador = new ArrayList<>();
                    for (int i = 0; i < arrayResultat.length(); i++) {
                        inventariJugador.add(arrayResultat.getString(i));
                    }
//                    System.out.println(json.get("skins"));
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
        labelNom = new Label("ITEM ", labelStyle);
        labelNom.setPosition(100, 100);

        //IMatge de item
        imgStyle = new ImageButton.ImageButtonStyle();
        TextureRegion textureRegion = new TextureRegion(AssetManager.persona, 0, 0, 1600, 1600);
        TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
        imgStyle.imageUp = drawable;
        ImageButton imatge = new ImageButton(imgStyle);
        imatge.setSize(250, 200);
        imatge.getImageCell().size(imatge.getWidth(), imatge.getHeight()).expand().fill();

        //Boto de esquerra
        Button.ButtonStyle btnStyleEsquerra = skin.get("left", Button.ButtonStyle.class);
        btnEsquerra = new Button(btnStyleEsquerra);
        btnEsquerra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentIndex == 0){
                    currentIndex = skins.length()-1;
                }else{
                    currentIndex--;
                }
            }
        });

        //Boto a la dreta
        Button.ButtonStyle btnStyleDreta = skin.get("right", Button.ButtonStyle.class);
        btnDreta = new Button(btnStyleDreta);
        btnDreta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentIndex == skins.length()-1){
                    currentIndex = 0;
                }else{
                    currentIndex++;
                }
            }
        });







// Create a pop-up window
        popupWindow = new Window("Confirmacio", skin);
        popupWindow.getTitleLabel().setAlignment(Align.center);

        popupWindow.setSize(250, 250);
        popupWindow.setPosition((Gdx.graphics.getWidth() - popupWindow.getWidth()) / 2, (Gdx.graphics.getHeight() - popupWindow.getHeight()) / 2);
        popupWindow.setMovable(false); // Make the window non-movable
        popupWindow.setModal(true); // Make the window modal (blocks input to other actors)

        //DEscripcio del producte
        Label.LabelStyle labelDescripcioStyle = skin.get(Label.LabelStyle.class);
        descripcioLabel = new Label("", labelDescripcioStyle);
        descripcioLabel.setWrap(true); // Enable text wrapping for the label
        descripcioLabel.setAlignment(Align.center);



        Label missatge = new Label("", labelDescripcioStyle);
        missatge.setWrap(true);
        missatge.setAlignment(Align.center);
        // Add content to the pop-up window
        TextButton closeButton = new TextButton("Cancelar", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popupWindow.remove(); // Remove the window when the close button is clicked}
            }});
        TextButton acceptButton = new TextButton("Acceptar", skin);
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JSONObject item = (JSONObject) skins.get(currentIndex);
                int valorProducte = item.getInt("valorMonedas");
                int totalActualMonedes = Integer.parseInt(preferences.getString("monedas")) - valorProducte;

                if (totalActualMonedes < 0) {
                    showInsufficientFundsPopup();
                } else {
                    comprarItem(item);
                    popupWindow.remove(); // Remove the window when the close button is clicked
                }
            }});
        popupWindow.add(missatge).expandX().fillX().pad(10).colspan(2).row();
        popupWindow.add(acceptButton).pad(10);
        popupWindow.add(closeButton).pad(10).row();



        //Boto de compra
        TextButton.TextButtonStyle btnCompraStyle = skin.get("round", TextButton.TextButtonStyle.class);
        btnComprar = new TextButton("-", textButtonStyle);
        btnComprar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(popupWindow);
                JSONObject item = (JSONObject) skins.get(currentIndex);
                missatge.setText("Confimes la compra de " + item.getString("nombre") + " pel valor de " + item.get("valorMonedas") + " monedes?");
            }
        });



        //AFEGIR COSES A WINDOW
        window.row();
        window.add();
        window.add(labelNom);
        window.row();
        window.add(btnEsquerra);
        window.add(imatge).expandX().fillX().pad(10);
        window.add(btnDreta);
        window.row();
        window.add();
        window.add(descripcioLabel).expandX().fillX().pad(10).row();
        window.add();
        window.add(btnComprar).center();
        // Crear una instancia de Label con el texto "Username" y el estilo definido


    }

    private void showInsufficientFundsPopup() {
        // Cerrar el pop-up de confirmación si está abierto
        if (popupWindow != null && popupWindow.hasParent()) {
            popupWindow.remove();
        }

        // Crear un nuevo pop-up de fondos insuficientes
        popupWindow = new Window("Insufficient Funds", skin);
        popupWindow.setMovable(false);
        popupWindow.setModal(true);
        popupWindow.setSize(300, 200);
        popupWindow.setPosition((Settings.GAME_WIDTH - popupWindow.getWidth()) / 2, (Settings.GAME_HEIGHT - popupWindow.getHeight()) / 2);

        Label.LabelStyle labelStyle = skin.get("title", Label.LabelStyle.class);
        Label label = new Label("No tens monedes suficients", labelStyle);
        label.setAlignment(Align.center);
        label.setWrap(true);
        label.setSize(popupWindow.getWidth() - 20, popupWindow.getHeight() - 20);
        label.setPosition(10, (popupWindow.getHeight() - label.getHeight()) / 2);

        TextButton.TextButtonStyle textButtonStyle = skin.get("round", TextButton.TextButtonStyle.class);
        TextButton closeButton = new TextButton("Close", textButtonStyle);
        closeButton.setSize(100, 50);
        closeButton.setPosition((popupWindow.getWidth() - closeButton.getWidth()) / 2, 20);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popupWindow.remove();
            }
        });

        popupWindow.addActor(label);
        popupWindow.addActor(closeButton);

        stage.addActor(popupWindow);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);


        if(skins != null){
            JSONObject item = (JSONObject) skins.get(currentIndex);
            labelNom.setText(item.getString("nombre"));
            descripcioLabel.setText(item.getString("descripcion"));
            if(imatgesBaixades.size() > 0) {
                imgStyle.imageUp = imatgesBaixades.get(currentIndex);
            }
            btnComprar.setText(String.valueOf(item.getInt("valorMonedas")));
            btnComprar.setTouchable(Touchable.enabled);
            if(inventariJugador != null) {
                for (String id : inventariJugador) {
                    if (id.equals(item.getString("_id"))) {
                        btnComprar.setText("OBTINGUT");
                        btnComprar.setTouchable(Touchable.disabled);

                    }
                }
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
    public void fetchAndSetImage(String imageUrl, int num) {
        // Create a GET request to fetch the image
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://192.168.0.14:3168/getImg/");
        httpRequest.setHeader("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        json.put("path", imageUrl);
        httpRequest.setContent(json.toString());

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


                        imatgesBaixades.set(num, drawable);

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

    public void comprarItem(JSONObject item){
        //Canviar monedes
        String monedes = preferences.getString("monedas");
        System.out.println("MONEDES: " + monedes);
        int valorProducte = item.getInt("valorMonedas");

        int totalActualMonedes = Integer.parseInt(monedes) - item.getInt("valorMonedas");

        labelNumMonedas.setText(totalActualMonedes);
        //Gaurdar en prefs
        preferences.putString("monedas", String.valueOf(totalActualMonedes));
        //Editar sql
        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);

        // Construct the URL with query parameters
        String url = "http://192.168.0.14:3168/comprarProducte";
        httpRequest.setUrl(url);
        httpRequest.setHeader("Content-Type", "application/json");
        JSONObject jsonEnviar = new JSONObject();
        jsonEnviar.put("user", preferences.getString("username"));
        jsonEnviar.put("idProducto", item.getString("_id"));
        jsonEnviar.put("monedes", valorProducte);
        String jsonString = jsonEnviar.toString();
        httpRequest.setContent(jsonString);

        // Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    System.out.println("OK EDITAR MONEDES");

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


        //Editar mongo inventari



        //canviar boto a OBTINGUT

        inventariJugador.add(item.getString("_id"));
    }

}
