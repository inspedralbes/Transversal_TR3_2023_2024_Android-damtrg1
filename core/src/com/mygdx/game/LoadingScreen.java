package com.mygdx.game;

import static com.mygdx.game.AssetManager.imatges_mapes;
import static com.mygdx.game.AssetManager.jugador_amunt;
import static com.mygdx.game.AssetManager.jugador_avall;
import static com.mygdx.game.AssetManager.jugador_dreta;
import static com.mygdx.game.AssetManager.jugador_esquerra;
import static com.mygdx.game.AssetManager.noms_mapes;
import static com.mygdx.game.AssetManager.spritesheet_joc_amunt;
import static com.mygdx.game.AssetManager.spritesheet_joc_avall;
import static com.mygdx.game.AssetManager.spritesheet_joc_dreta;
import static com.mygdx.game.AssetManager.spritesheet_joc_esquerra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Utils.Settings;
import objects.Background;

public class LoadingScreen implements Screen {

    Pixel_R6 game;

    Background bg;

    Stage stage;

    OrthographicCamera camera;

    // Añade un Skin
    Skin skin, skin_inputs;


    TextField Username, Password;

    Preferences preferences;

    static boolean peticion_getMapas = false;
    static boolean peticion_getSkins = false;

    static boolean peticion_getSkin_guardada = false;

    static boolean peticion_getInventari = false;

    public static String idUser;
    public static String idEquipat;
    public static ArrayList<String> inventariJugador;

    public static JSONArray skins;

    public static ArrayList<String> noms_skins;

    public static ArrayList<Drawable> imatgesBaixades = new ArrayList<>(10);

    private boolean inventari_canviat;

    public static String nom_skin = "";

    public static ArrayList<TiledMap> array_mapes = new ArrayList<>();


    public LoadingScreen(Pixel_R6 game, boolean inventari_canviat) {
        this.game = game;

        //AssetManager.load();

        this.inventari_canviat = inventari_canviat;

        preferences = Gdx.app.getPreferences("Pref");
        System.out.println("USUARI: " + preferences.getString("username"));

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
        Label titleLabel1 = new Label("R6 PIXEL", titleLabelStyle);
        Label titleLabel2 = new Label("LOADING...", titleLabelStyle);

        // Calcula la posición X centrada en la pantalla
        float posX = (Gdx.graphics.getWidth() - titleLabel1.getWidth()) / 2;

        // Calcula la posición Y en la parte superior de la pantalla
        float posY = Settings.GAME_HEIGHT - titleLabel1.getHeight(); // Margen de 20 píxeles

        // Establece la posición del título
        titleLabel1.setPosition(posX, posY); // Alinea el título en la parte superior y centrado
        titleLabel2.setPosition((float) 0.40 * Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2); // Alinea el título en la parte superior y centrado

        // Añade el título al stage
        stage.addActor(titleLabel1);
        stage.addActor(titleLabel2);


        //BOTONES
        // Cargar el Skin
        skin_inputs = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton.TextButtonStyle textButtonStyle = skin_inputs.get("round", TextButton.TextButtonStyle.class);

        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_inicio = new TextButton("Inicio Sesion", textButtonStyle);

        btn_inicio.setSize(200, 70);




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
        //stage.addActor(window);


        //POSICION BOTONES BAJO WINDOWS
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones



        // Agregar los botones al Stage
        //stage.addActor(btn_inicio);
        //stage.addActor(btn_registrar);

        //PARA INTRODUCIR DATOS
        Gdx.input.setInputProcessor(stage);

        noms_mapes = new ArrayList<String>();
        noms_skins = new ArrayList<String>();

        Net.HttpRequest httpRequest_assets = new Net.HttpRequest(Net.HttpMethods.GET);
        String url_assets = "http://r6pixel.duckdns.org:3168/getAssets";
        httpRequest_assets.setUrl(url_assets);
        httpRequest_assets.setHeader("Content-Type", "application/json");

        Gdx.net.sendHttpRequest(httpRequest_assets, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    JSONObject body = new JSONObject();
                    body.put("directory","mapas");
                    fetchAndSetAssets(body.toString());
                } else {
                    System.out.println("HTTP request failed with status code: " + status.getStatusCode());
                }
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void cancelled() {
            }
        });









    }

    public static void fetchAndSetAssets(String bodyData) {



        // Create a POST request to fetch the image
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://r6pixel.duckdns.org:3168/getAssets_post/");
        httpRequest.setHeader("Content-Type", "application/json");

        // Set the body data if needed
        if (bodyData != null && !bodyData.isEmpty()) {
            httpRequest.setContent(bodyData);
        }

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                byte[] zipData = httpResponse.getResult();

                // Descomprimir el archivo zip
                try {
                    // Crear un InputStream desde los datos del archivo zip
                    InputStream inputStream = new ByteArrayInputStream(zipData);
                    ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                    ZipEntry zipEntry = zipInputStream.getNextEntry();

                    while (zipEntry != null) {
                        String entryName = zipEntry.getName();
                        FileHandle newFile = Gdx.files.local(entryName); // Usar Gdx.files.local para obtener una ruta al directorio de almacenamiento interno

                        if (zipEntry.isDirectory()) {
                            // Si la entrada es un directorio, crea el directorio en la ruta correspondiente
                            newFile.mkdirs();
                        } else {
                            // Si la entrada es un archivo, escribe los datos en el archivo correspondiente
                            newFile.parent().mkdirs(); // Asegúrate de que los directorios padres existan
                            FileOutputStream fileOutputStream = new FileOutputStream(newFile.file());
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zipInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, length);
                            }
                            fileOutputStream.close();
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    zipInputStream.close();

                    // Cargar la textura mapaCastillo después de que la descompresión haya terminado
                    Gdx.app.postRunnable(() -> {
                        JSONObject jsonObject = new JSONObject(bodyData);
                        if (jsonObject.get("directory").equals("mapas")) {
                            System.out.println("DENTRO DE GETASSETS1");
                            FileHandle mapasDir = Gdx.files.local("mapas");
                            if (mapasDir.exists() && mapasDir.isDirectory()) {
                                FileHandle[] subdirs = mapasDir.list();
                                for (FileHandle subdir : subdirs) {
                                    if (subdir.isDirectory()) {
                                        noms_mapes.add(subdir.name());

                                        // Obtener el archivo "mapa" + nombre_mapa + ".jpg"
                                        FileHandle mapaFile = subdir.child("mapa" + subdir.name() + ".jpg");
                                        if (mapaFile.exists()) {
                                            // Hacer algo con el archivo mapaFile, como cargarlo o almacenarlo
                                            System.out.println("Archivo encontrado: " + mapaFile.path());
                                            Texture imatge_mapa = new Texture(Gdx.files.local(mapaFile.path()));
                                            imatges_mapes.add(imatge_mapa);
                                            //TiledMap mapa = new TmxMapLoader().load(String.valueOf(Gdx.files.local("mapas/"+subdir.name()+"/mapa_"+subdir.name()+".tmx")));
                                            //array_mapes.add(mapa);
                                        } else {
                                            System.out.println("Archivo no encontrado para el directorio: " + subdir.name());
                                        }
                                    }
                                }
                                peticion_getMapas = true;
                            }
                            System.out.println("Archivos descomprimidos correctamente en la carpeta 'assets'");
                            for (int i = 0;i<noms_mapes.size();i++) {
                                System.out.println("mapa: " + noms_mapes.get(i));
                            }


                        }




                        //imgFondo = new Texture(Gdx.files.internal("fondo2.jpg"));
                        //imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

                        //background = new TextureRegion(imgFondo);

                        else {
                            System.out.println("buscando las skins");
                            FileHandle mapasDir = Gdx.files.local("skinsMod");
                            if (mapasDir.exists() && mapasDir.isDirectory()) {
                                FileHandle[] subdirs = mapasDir.list();
                                for (FileHandle subdir : subdirs) {
                                    System.out.println(subdir.name());
                                        noms_skins.add(subdir.name());

                                        System.out.println(subdir.name());




                                        peticion_getSkin_guardada = true;
                                        System.out.println("peticion_getAssets true");
                                    }




                            }



                        }


                    });

                } catch (IOException e) {
                    System.out.println("Error al descomprimir el archivo zip: " + e.getMessage());
                }

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


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);



        if (peticion_getMapas) {
            peticion_getMapas = false;
            // Create an HTTP request
            Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

            // Construct the URL with query parameters
            String url = "http://r6pixel.duckdns.org:3168/getAssets";
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
                        System.out.println("skins: " + skins);
                        peticion_getSkins = true;
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

        if (peticion_getSkins) {
            peticion_getSkins = false;
            // Create an HTTP request
            Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);

            // Construct the URL with query parameters
            String url2 = "http://r6pixel.duckdns.org:3168/getInventari/" + preferences.getString("username");
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
                        idUser = json.getString("_id");
                        idEquipat = json.getString("activo");
                        JSONArray arrayResultat = (JSONArray) json.get("skins");
                        // Convert JSONArray to String[]
                        inventariJugador = new ArrayList<>();
                        for (int i = 0; i < arrayResultat.length(); i++) {
                            inventariJugador.add(String.valueOf(arrayResultat.get(i)));
                        }
                        System.out.println("inventariJugador:" + inventariJugador);
                        peticion_getInventari = true;
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
            /*
            if (!peticion_getInventari) {
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new PantallaPrincipal(game, true));
                });
            }

             */

        }

        if (peticion_getInventari) {
            peticion_getInventari = false;
            JSONObject resultat = new JSONObject();
            int index = 0;
            for (int i = 0; i < skins.length(); i++) {
                JSONObject a = skins.getJSONObject(i);
                if (idEquipat.equals(a.getString("_id"))) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("directory","skinsMod");
                    nom_skin = a.getString("path_directori_skin");
                    fetchAndSetAssets(jsonObject.toString());
                    break;
                }

                if (i==skins.length()-1) {
                    peticion_getSkin_guardada = true;
                }



            }
        }



        if (peticion_getSkin_guardada) {
            peticion_getSkin_guardada = false;

            if (this.inventari_canviat) {
                Gdx.app.postRunnable(() -> {
                    game.setScreen(new InventariScreen(game));
                });
            }

            else {
                if (preferences.getBoolean("logged")) {
                    Gdx.app.postRunnable(() -> {
                        game.setScreen(new PantallaPrincipal(game, true));
                    });
                } else {
                    Gdx.app.postRunnable(() -> {
                        game.setScreen(new Login(game));
                    });
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
        stage.dispose();
    }
}
