package com.mygdx.game;

import static com.mygdx.game.AssetManager.noms_mapes;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import Utils.Sala;
import Utils.Settings;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import objects.Background;

public class CreacionPartidaScreen implements Screen {

    Game game;

    Background bg;

    Stage stage;

    Socket mSocket;
    OrthographicCamera camera;

    // Añade un Skin
    Skin skin, skin_inputs, skin_btns;
    ArrayList<String> usuarisSala = new ArrayList<>();
    ArrayList<Label> labelsUsuaris = new ArrayList<>();
    ArrayList<String> skinsSala = new ArrayList<>();


    ArrayList<String> usuarisAtacantes = new ArrayList<>();
    ArrayList<Label> labelsAtacantes = new ArrayList<>();
    ArrayList<String> skinsAtacantes = new ArrayList<>();

    ArrayList<String> usuarisDefensores = new ArrayList<>();
    ArrayList<Label> labelDefensores = new ArrayList<>();
    ArrayList<String> skinsDefensores = new ArrayList<>();
    Label salaLabel, seleccioMapa, aliadoLabel, jugadorLabel, enemigoLabel;

    int numMapa = 0;
    String salaId;
    JSONObject json;
    Preferences preferences;

    Image IMGMapas;

    TextureRegionDrawable castillo, mazmorra;

    Sala salaNova;

    String mapaSelecionado;

    Table table;

    ArrayList<TextureRegionDrawable> arrayTextureRegionDrawable;


    public CreacionPartidaScreen(Pixel_R6 game) {
        this.game = game;
        //AssetManager.load();
        preferences = Gdx.app.getPreferences("Pref");
        for (int i = 0; i < 10; i++) {
            usuarisSala.add("NO PLAYER");
        }
        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        // Construct the URL with query parameters
        String url = "http://r6pixel.duckdns.org:3168/crearSala";
        String username = preferences.getString("username");
        url += "?user=" + username;
        System.out.println(LoadingScreen.nom_skin);
        url += "&skin=" + String.valueOf(LoadingScreen.nom_skin);

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
                    JSONArray jsonArray = json.getJSONArray("users");

                    // Iterate through the JSON array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Get each element from the JSON array and add it to the list
                        String element = jsonArray.getString(i);
                        usuarisSala.set(i, element);
                        skinsSala.add(LoadingScreen.nom_skin);
                        System.out.println("skinsSala: " + skinsSala);
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

        Net.HttpRequest httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);

        httpRequest2.setUrl("http://r6pixel.duckdns.org:3168/getMapes");
        httpRequest2.setHeader("Content-Type", "application/json");

        // Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    // If the request was successful (status code 200)
                    String responseData = httpResponse.getResultAsString();

                    // Remove the square brackets, quotes, and commas from the string
                    String cleanInput = responseData.replaceAll("[\\[\\]\"]", "");
                    // Split the string into an array by empty space
                    //noms_mapes = cleanInput.split(",");
                    // Output the array elements
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
        System.out.println("numero de mapes: " + noms_mapes.size());

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
        skin_btns = new Skin(Gdx.files.internal("skin_vida2/tubular-ui.json"));

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

        TextButton botonListo = new TextButton("EMPEZAR PARTIDA", textButtonStyle);


        //LABELS
        // Obtener el estilo del Label del Skin
        Label.LabelStyle labelStyle = skin_inputs.get("title-plain", Label.LabelStyle.class);

        // Crear una instancia de Label con el texto "Username" y el estilo definido
        salaLabel = new Label("Sala: " + salaId, labelStyle);

        Table tbSalaId = new Table();
        tbSalaId.setFillParent(false);

        tbSalaId.add(salaLabel);

        //VENTANA
        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("SALA", windowStyle);
        window.setMovable(false);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Gdx.graphics.getWidth();
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 3.3f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 2.3f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(1000, 600); // Establece el tamaño como desees

        Label.LabelStyle labelStyle2 = skin_inputs.get("subtitle", Label.LabelStyle.class);

        seleccioMapa = new Label("Mapa Castillo", labelStyle2);

        // Establecer la alineación del texto en el centro
        seleccioMapa.setAlignment(Align.center);
        seleccioMapa.setFontScale(2);


        Button botoEsquerra = new Button(skin_inputs.get("left", Button.ButtonStyle.class));
        Button botoDreta = new Button(skin_inputs.get("right", Button.ButtonStyle.class));

        arrayTextureRegionDrawable = new ArrayList<TextureRegionDrawable>();
        for (int i=0;i<AssetManager.imatges_mapes.size();i++) {
            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(AssetManager.imatges_mapes.get(i));
            arrayTextureRegionDrawable.add(textureRegionDrawable);
        }
        IMGMapas = new Image();
        //castillo = new TextureRegionDrawable(AssetManager.imatges_mapes.get(0));
        //mazmorra = new TextureRegionDrawable(AssetManager.imatges_mapes.get(1));

        Table tableInfo = new Table();
        tableInfo.add(botoEsquerra).prefSize(40, 40).pad(20);
        tableInfo.add(seleccioMapa).prefSize(90, 30).pad(10);
        tableInfo.add(botoDreta).prefSize(40, 40).pad(20).row();

        Table tableImg = new Table();
        tableImg.add(IMGMapas).prefSize(200, 200);

        botoEsquerra.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarMapa(-1);
            }
        });
        botoDreta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarMapa(1);
            }

        });


        // Agregar la ventana al Stage
        stage.addActor(window);
        // Ajustar la posición de los botones debajo de la ventana
        float btnY = windowY - 100; // Espacio vertical entre la ventana y los botones

        // Establecer la posición de los botones
        botonListo.setPosition(windowX + 350, btnY + 30); // Posición del botón "Inicio Sesión"
        botonListo.setSize(300, 70);

        // Agregar los botones al Stage
        stage.addActor(botonListo);

        //PARA INTRODUCIR DATOS
        Gdx.input.setInputProcessor(stage);


        try {
            mSocket = IO.socket("http://r6pixel.duckdns.org:3168");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("user", preferences.getString("username"));

        mSocket.on("userNuevo", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonString = (String) args[0];
                try {
                    JSONObject data = new JSONObject(jsonString);
                    String user = data.getString("user");
                    String sala = data.getString("sala");
                    String equip = data.getString("equip");
                    String nom_skin = data.getString("nom_skin");
                    //System.out.println("hola");
                    if (sala.equals(salaId)) {

                        if (!user.equals(preferences.getString("username"))) {
                            int contador = 0;
                            for (String usuari : usuarisSala) {
                                if (!usuari.equals("NO PLAYER")) {
                                    contador++;
                                }
                            }

                            if (!usuarisSala.contains(user)) {
                                usuarisSala.set(contador, user);
                                skinsSala.add(nom_skin);
                                System.out.println("skinsSala: " + skinsSala);
                            }


                            if (equip.equals("EQUIP 1") && usuarisAtacantes.size()<5) {
                                usuarisAtacantes.add(user);
                                skinsAtacantes.add(nom_skin);
                                System.out.println("skinsAtacantes: " + skinsAtacantes);
                                if (usuarisSala.contains(user)) {
                                    int id = usuarisSala.indexOf(user);
                                    System.out.println("ANTES NOM: "+user);
                                    System.out.println("SALA: "+ id);
                                    //usuarisSala.remove(id);
                                    System.out.println("DESPUES NOM: "+user);
                                    //usuarisSala.add("NO PLAYER");
                                    //labelsUsuaris.get(id).setText("NO PLAYER");
                                    for (String la : usuarisSala) {
                                        System.out.println("MEDIO: " + la);
                                    }
                                    usuarisSala.remove(id);
                                    skinsSala.remove(id);
                                    System.out.println("skinsSala: " + skinsSala);
                                }
                                if (usuarisDefensores.contains(user)) {
                                    int id = usuarisDefensores.indexOf(user);
                                    usuarisDefensores.remove(id);
                                    skinsDefensores.remove(id);
                                    System.out.println("skinsDefensores: " + skinsDefensores);
                                    //labelDefensores.remove(user);
                                    System.out.println("N: " + labelDefensores.size());
                                    //labelDefensores.get(id).setText("Defensores");

                                }
                                /*
                                if (usuarisAtacantes.size() <= 5) {
                                    for (int i = 0; i < usuarisAtacantes.size(); i++) {
                                        System.out.println("USERS: " + usuarisAtacantes.get(i));
                                        labelsAtacantes.get(i).setText(usuarisAtacantes.get(i));
                                    }
                                }

                                 */
                            } else if (equip.equals("EQUIP 2") && usuarisDefensores.size()<5) {
                                usuarisDefensores.add(user);
                                skinsDefensores.add(nom_skin);
                                System.out.println("skinsDefensores: " + skinsDefensores);
                                if (usuarisSala.contains(user)) {
                                    int id = usuarisSala.indexOf(user);
                                    System.out.println("ANTES NOM: "+user);
                                    System.out.println("SALA: "+ id);
                                    usuarisSala.remove(id);
                                    skinsSala.remove(id);
                                    System.out.println("skinsSala: " + skinsSala);
                                    //usuarisSala.add("NO PLAYER");
                                    System.out.println("DESPUES NOM: "+user);
                                    //labelsUsuaris.get(id).setText("NO PLAYER");
                                    for (String la : usuarisSala) {
                                        System.out.println("MEDIO: " + la);
                                    }
                                }
                                if (usuarisAtacantes.contains(user)) {
                                    int id = usuarisAtacantes.indexOf(user);
                                    usuarisAtacantes.remove(id);
                                    skinsAtacantes.remove(id);
                                    System.out.println("skinsAtacantes: " + skinsAtacantes);
                                    //labelsAtacantes.remove(user);
                                    System.out.println("N: " + labelsAtacantes.size());
                                    //labelsAtacantes.get(id).setText("Atacantes");

                                }
                                /*
                                if (usuarisDefensores.size() <= 5) {
                                    for (int i = 0; i < usuarisDefensores.size(); i++) {
                                        System.out.println("USERS DEFENSORES: " + usuarisDefensores.get(i));
                                        labelDefensores.get(i).setText(usuarisDefensores.get(i));

                                    }
                                }

                                 */
                            }
                        }
                    }
                    System.out.println("Nuevo usuario: " + user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mSocket.on("startGame", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonString = (String) args[0];
                try {
                    JSONObject data = new JSONObject(jsonString);
                    String sala = data.getString("sala");
                    if (sala.equals(salaId)) {
                        salaNova = new Sala(sala, mapaSelecionado, usuarisAtacantes, usuarisDefensores, skinsAtacantes, skinsDefensores);
                        if (usuarisAtacantes.isEmpty() && usuarisDefensores.isEmpty()) {
                            System.out.println("No puedes jugar sin elegir equipo");
                        } else {
                            Gdx.app.postRunnable(() -> {
                                //AssetManager.music.stop();
                                game.setScreen(new MapaPrueba(game, salaNova));
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        botonListo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JSONObject jsonEnviar = new JSONObject();
                jsonEnviar.put("sala", salaId);
                mapaSelecionado = seleccioMapa.getText().toString();
                jsonEnviar.put("mapaSelecionado", mapaSelecionado);
                mSocket.emit("startGame", jsonEnviar.toString());
            }
        });

        Table tbBTN = new Table();
        tbBTN.setFillParent(false);

        TextButton btnAtacante = new TextButton("ATACANTES", skin_inputs.get("default", TextButton.TextButtonStyle.class));
        Label jugadores = new Label("JUGADORES", skin_inputs.get("subtitle", Label.LabelStyle.class));
        TextButton btnDefensor = new TextButton("DEFENSORES", skin_inputs.get("default", TextButton.TextButtonStyle.class));

        table = new Table();
        table.setFillParent(false); // Para que la tabla ocupe todo el espacio del padre, en este caso, el Stage

        // Columna de aliados
        Table tableAliados = new Table();
        tableAliados.top().left(); // Alinea la tabla en la parte superior izquierda

        // Agrega los labels de los aliados a la columna de aliados
        for (int i = 0; i < 5; i++) {
            aliadoLabel = new Label("Atacantes", skin_inputs);
            labelsAtacantes.add(aliadoLabel);
            tableAliados.add(aliadoLabel).pad(10).row(); // Agrega un padding y pasa a la siguiente fila

        }

        // Columna de jugadores conectados a la sala
        Table tableJugadores = new Table();
        tableJugadores.top().center(); // Alinea la tabla en la parte superior centrada

        // Agrega los labels de los jugadores conectados a la sala a la columna de jugadores
        for (int i = 0; i < usuarisSala.size(); i++) {
            jugadorLabel = new Label(usuarisSala.get(i), skin_inputs);
            labelsUsuaris.add(jugadorLabel);
            tableJugadores.add(jugadorLabel).pad(3).row(); // Agrega un padding y pasa a la siguiente fila
        }

        // Columna de equipo enemigo
        Table tableEnemigos = new Table();
        tableEnemigos.top().right(); // Alinea la tabla en la parte superior derecha

        // Agrega los labels de los enemigos a la columna de enemigos
        for (int i = 5; i < usuarisSala.size(); i++) {
            enemigoLabel = new Label("Defensores", skin_inputs);
            labelDefensores.add(enemigoLabel);
            tableEnemigos.add(enemigoLabel).pad(10).row(); // Agrega un padding y pasa a la siguiente fila
        }


        btnAtacante.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Actualiza los componentes de la interfaz de usuario aquí

                if (usuarisAtacantes.size()<5) {
                    System.out.println("ATACANTES");
                    // Obtener el nombre del usuario que hizo clic en el botón
                    String usuarioClic = preferences.getString("username");
                    // Imprimir el nombre del usuario
                    System.out.println("Usuario seleccionado como atacante: " + usuarioClic);


                    System.out.println("ATANCANTE: " + usuarioClic);




                    JSONObject json_canvi_equip = new JSONObject();
                    json_canvi_equip.put("user", usuarioClic);
                    json_canvi_equip.put("sala", salaId);
                    json_canvi_equip.put("equip", "EQUIP 1");
                    json_canvi_equip.put("nom_skin", LoadingScreen.nom_skin);

                    mSocket.emit("userNuevo", json_canvi_equip.toString());


                    usuarisAtacantes.add(usuarioClic);
                    skinsAtacantes.add(LoadingScreen.nom_skin);
                    System.out.println("skinsAtacantes: " + skinsAtacantes);

                    if (usuarisSala.contains(usuarioClic)) {
                        usuarisSala.remove(usuarioClic);
                        skinsSala.remove(LoadingScreen.nom_skin);
                        System.out.println("skinsSala: " + skinsSala);
                    }




                    else if (usuarisDefensores.contains(usuarioClic)) {

                        int id = usuarisDefensores.indexOf(usuarioClic);
                        usuarisDefensores.remove(id);
                        skinsDefensores.remove(id);
                        System.out.println("skinsDefensores: " + skinsDefensores);
                        //labelDefensores.remove(usuarioClic);
                        System.out.println("N: " + labelDefensores.size());
                        //enemigoLabel.setText("Defensor "+i);
                        //labelDefensores.get(id).setText("Defensores");


                    }






                /*
                for (int i = 0; i < usuarisAtacantes.size(); i++) {
                    System.out.println("USERS: " + usuarisAtacantes.get(i));
                    labelsAtacantes.get(i).setText(usuarisAtacantes.get(i));

                }
                */

                    System.out.println("AAA: " + labelsAtacantes.size());
                }


            }
        });


        btnDefensor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (usuarisDefensores.size()<5) {

                    // Actualiza los componentes de la interfaz de usuario aquí

                    // Actualiza los componentes de la interfaz de usuario aquí

                    System.out.println("DEFENSORES");

                    String usuarioClic = preferences.getString("username");






                    JSONObject json_canvi_equip = new JSONObject();
                    json_canvi_equip.put("user", usuarioClic);
                    json_canvi_equip.put("sala", salaId);
                    json_canvi_equip.put("equip", "EQUIP 2");
                    json_canvi_equip.put("nom_skin", LoadingScreen.nom_skin);

                    mSocket.emit("userNuevo", json_canvi_equip.toString());

                    usuarisDefensores.add(usuarioClic);
                    skinsDefensores.add(LoadingScreen.nom_skin);
                    System.out.println("skinsDefensores: " + skinsDefensores);

                    if (usuarisSala.contains(usuarioClic)) {
                        usuarisSala.remove(usuarioClic);
                        skinsSala.remove(LoadingScreen.nom_skin);
                        System.out.println("skinsSala: " + skinsSala);
                    }




                    else if (usuarisAtacantes.contains(usuarioClic)) {

                        int id = usuarisAtacantes.indexOf(usuarioClic);
                        usuarisAtacantes.remove(id);
                        skinsAtacantes.remove(id);
                        System.out.println("skinsAtacantes: " + skinsAtacantes);
                        //labelDefensores.remove(usuarioClic);
                        System.out.println("N: " + labelDefensores.size());
                        //enemigoLabel.setText("Defensor "+i);
                        //labelDefensores.get(id).setText("Defensores");


                    }




                /*
                for (int i = 0; i < usuarisDefensores.size(); i++) {
                    System.out.println("USERS DEFENSORES: " + usuarisDefensores.get(i));
                    labelDefensores.get(i).setText(usuarisDefensores.get(i));
                }

                 */
                }



            }
        });

        tbBTN.add(btnAtacante).padRight(90);
        tbBTN.add(jugadores).prefSize(50, 30);
        tbBTN.add(btnDefensor).padLeft(90);


// Agrega las columnas a la tabla principal
        table.add(tableAliados).expandY().padRight(100); // Expande la columna de aliados en el eje Y y agrega un espaciado a la derecha
        table.add(tableJugadores).expandY().padRight(100); // Expande la columna de jugadores en el eje Y y agrega un espaciado a la derecha
        table.add(tableEnemigos).expandY(); // Expande la columna de enemigos en el eje Y

        window.add(tbSalaId).pad(10).row();
        window.add(tbBTN).row();
        window.add(table).row(); // Agregar la tabla a la ventana
        //window.add(tableEnemigo);
        window.add(tableInfo).row();
        window.add(tableImg);



        // Crear instancia del TextButton con el estilo obtenido del Skin
        TextButton btn_volver = new TextButton("Volver", textButtonStyle);

        btn_volver.setSize(200, 70);

        btn_volver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectPlayScreen(game));

            }
        });

        btn_volver.setPosition(windowX + 100, windowY - 100);

        stage.addActor(btn_volver);
    }

    // Método para actualizar los labels de los atacantes
    private void updateAtacantesLabels() {
        // Limpiar la lista de labels de atacantes
        labelsAtacantes.clear();
        // Agregar los nombres de los atacantes a los labels
        for (String atacante : usuarisAtacantes) {
            Label atacanteLabel = new Label(atacante, skin_inputs);
            labelsAtacantes.add(atacanteLabel);
        }
        // Actualizar la interfaz gráfica
        stage.act();
    }

    // Método para actualizar los labels de los usuarios en la sala
    private void actualizarLabelsUsuarios() {
        // Limpiar la lista de labels de usuarios
        labelsUsuaris.clear();

        // Agregar los nombres de los usuarios a los labels
        for (String usuario : usuarisSala) {
            Label usuarioLabel = new Label(usuario, skin_inputs);
            labelsUsuaris.add(usuarioLabel);
        }

        // Actualizar la interfaz gráfica
        stage.act();
    }


    @Override
    public void show() {

    }

    public void cambiarMapa(int num) {
        switch (num) {
            case 1:
                if (numMapa == noms_mapes.size() - 1) {
                    numMapa = 0;
                } else {
                    numMapa++;
                }
                break;
            case -1:
                if (numMapa == 0) {
                    numMapa = noms_mapes.size() - 1;
                } else {
                    numMapa--;
                }
        }

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
        salaLabel.setText("Sala: " + salaId);
        for (int i = 0; i < usuarisSala.size(); i++) {
            labelsUsuaris.get(i).setText(usuarisSala.get(i));

        }

        for (int i = 0; i < 5; i++) {
            if (i < usuarisAtacantes.size()) {
                labelsAtacantes.get(i).setText(usuarisAtacantes.get(i));
            }
            else {
                labelsAtacantes.get(i).setText("Atacantes");
            }
            if (i < usuarisDefensores.size()) {
                labelDefensores.get(i).setText(usuarisDefensores.get(i));
            }
            else {
                labelDefensores.get(i).setText("Defensores");
            }
        }

        seleccioMapa.setText(noms_mapes.get(numMapa));

        /*
        if (seleccioMapa.getText().toString().equals("castillo")) {
            IMGMapas.setDrawable(castillo);
        } else {
            IMGMapas.setDrawable(mazmorra);
        }

         */

        IMGMapas.setDrawable(arrayTextureRegionDrawable.get(numMapa));

    }

    private void updateSala () {
        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);

        // Construct the URL with query parameters
        String url = "http://r6pixel.duckdns.org:3168/updateSala";
        JSONObject body = new JSONObject();
        body.put("directory","mapas");

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
                    JSONArray jsonArray = json.getJSONArray("users");

                    // Iterate through the JSON array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Get each element from the JSON array and add it to the list
                        String element = jsonArray.getString(i);
                        usuarisSala.set(i, element);
                        skinsSala.add(LoadingScreen.nom_skin);
                        System.out.println("skinsSala: " + skinsSala);
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
