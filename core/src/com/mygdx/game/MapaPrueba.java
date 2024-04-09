package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Utils.Sala;
import Utils.Settings;
import helpers.JsonLoader;
import objects.Background;
import objects.Disparo;
import objects.Jugador;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MapaPrueba implements Screen {

    Pixel_R6 game;

    Background bg;
    int numJugador;


    public static Stage stage;

    Batch batch;

    OrthographicCamera camera;
    private Timer timer;

    OrthogonalTiledMapRenderer renderer;

    public static Socket mSocket;

    Preferences preferences;

    Skin skin, skin_vida;

    ProgressBar progressBar;

    Touchpad touchpad;

    Label LabelNomJugador;

    Button disparo;
    public static ArrayList<Jugador> jugadors = new ArrayList<>();

    public static ArrayList<ProgressBar> progressBars = new ArrayList<>();

    public static ArrayList<Label> labelsNoms = new ArrayList<>();

    float knobXAnterior = 0;
    float knobYAnterior = 0;
    Sala sala;

    float knobX;
    float knobY;

    public static ArrayList<Jugador> array_jugadors_equip1;
    public static ArrayList<Jugador> array_jugadors_equip2;

    TextButton textButtonEquip1;
    TextButton textButtonEquip2;

    public MapaPrueba(Pixel_R6 game, Sala sala) {
        preferences = Gdx.app.getPreferences("Pref");
        this.sala = sala;
        this.game = game;


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin_vida = new Skin(Gdx.files.internal("skin_vida2/tubular-ui.json"));

        AssetManager.load();

        //AssetManager.music.stop();

        try {
            mSocket = IO.socket("http://r6pixel.duckdns.org:3169");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();

        // Creem la càmera de les dimensions del joc
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Posant el paràmetre a true configurem la càmera perquè
        // faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(false);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);

        //CONFIGURACION DEL FONDO
        //bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT);

        array_jugadors_equip1 = new ArrayList<Jugador>();
        array_jugadors_equip2 = new ArrayList<Jugador>();

        JsonLoader jsonLoader = new JsonLoader();
        JSONObject jsonPosicions = jsonLoader.loadJson("posicions.json");
        jsonPosicions = jsonPosicions.getJSONObject("Mazmorra");
        int contador = 0;
        for (String usuari : sala.getUsuarisAtacantes()) {
            if (!usuari.equals("NO PLAYER")) {
                JSONObject posicions = jsonPosicions.getJSONObject("pos" + (contador + 1));
                System.out.println("ATA: "+ posicions);
                Jugador player;
                if (sala.getNombreMapa().equals("castillo")) {
                    player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledCastillo);
                } else {
                    player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledMazmorra);
                }
                jugadors.add(player);
                stage.addActor(player);

                //PROVA, AFEGIR UN JUGADOR A EQUIP1 Y L'ALTRE A EQUIP2
                array_jugadors_equip1.add(player);

                //BARRA DE VIDA
                ProgressBar.ProgressBarStyle progressBarStyle = skin_vida.get("default-horizontal", ProgressBar.ProgressBarStyle.class);

                // Crear e inicializar el ProgressBar con el estilo obtenido
                progressBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
                progressBar.setHeight(10);
                progressBar.setWidth(50);
                progressBar.setValue(100); // Establecer el valor inicial del ProgressBar
                progressBars.add(progressBar); // Agregar la ProgressBar a la lista
                stage.addActor(progressBar); // Agregar el ProgressBar al Stage
            }
            if (usuari.equals(preferences.getString("username"))) {
                numJugador = contador;
            }

            contador++;
        }

        for (String usuari : sala.getUsuarisDefensores()) {
            if (!usuari.equals("NO PLAYER")) {
                JSONObject posicions = jsonPosicions.getJSONObject("pos" + (contador + 6));
                System.out.println("DEF: "+ posicions);
                Jugador player;
                if (sala.getNombreMapa().equals("castillo")) {
                    player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledCastillo);
                    System.out.println("-------------------------------------------");
                } else {
                    player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledMazmorra);
                }
                jugadors.add(player);
                stage.addActor(player);

                //PROVA, AFEGIR UN JUGADOR A EQUIP1 Y L'ALTRE A EQUIP2
                array_jugadors_equip2.add(player);

                //BARRA DE VIDA
                ProgressBar.ProgressBarStyle progressBarStyle = skin_vida.get("default-horizontal", ProgressBar.ProgressBarStyle.class);

                // Crear e inicializar el ProgressBar con el estilo obtenido
                progressBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
                progressBar.setHeight(10);
                progressBar.setWidth(50);
                progressBar.setValue(100); // Establecer el valor inicial del ProgressBar
                progressBars.add(progressBar); // Agregar la ProgressBar a la lista
                stage.addActor(progressBar); // Agregar el ProgressBar al Stage
            }
            if (usuari.equals(preferences.getString("username"))) {
                numJugador = contador;
            }

            contador++;
        }
        camera.update();

        //AÑADIMOS EL FONDO AL STAGE
        //stage.addActor(bg);

        batch = stage.getBatch();

        camera.zoom = 0.5f;

        if (sala.getNombreMapa().equals("castillo")) {
            renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledCastillo);
        } else {
            renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledMazmorra);
        }


        Touchpad.TouchpadStyle touchpadStyle = skin.get("default", Touchpad.TouchpadStyle.class);
        /*
        // Obtener el Drawable del fondo del Touchpad
        Drawable touchpadBackground = touchpadStyle.background;

        Color color = touchpadBackground.getColor();

        // Establecer la opacidad del color (transparencia)
        color.a = 0.2f; // Establece la opacidad a 0.2 (20%)

        // Establecer el color modificado en el Drawable
        touchpadBackground.setTint(color);*/

        touchpad = new Touchpad(0, touchpadStyle);
        touchpad.setBounds(40, 225, 100, 100); // Establecer posición y tamaño del Touchpad
        stage.addActor(touchpad); // Agregar el Touchpad al Stage

        Gdx.input.setInputProcessor(stage);

        Button.ButtonStyle buttonStyle = skin.get("default", Button.ButtonStyle.class);

        // Crea una instancia de ImageButton con el estilo obtenido
        disparo = new Button(buttonStyle);

        // Asigna un listener para manejar eventos de clic en el botón
        disparo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DISPARO");
                //progressBars.get(numJugador).setValue(progressBar.getValue()-10);
//                progressBar.setValue(progressBar.getValue()-10);
                System.out.println("BARRA: " + progressBar.getValue());
                if (touchpad.isTouched()) {
                    float x1 = jugadors.get(numJugador).getPosition().x;
                    float y1 = jugadors.get(numJugador).getPosition().y;
                    float x2 = jugadors.get(numJugador).getPosition().x + knobX * 100;
                    float y2 = jugadors.get(numJugador).getPosition().y + knobY * 100;
                    float x_vector_direccio = knobX * 100;
                    float y_vector_direccio = knobY * 100;
                    Disparo disparo_visual = new Disparo(x1, y1, x2, y2, x_vector_direccio, y_vector_direccio, jugadors.get(numJugador));
                    stage.addActor(disparo_visual);

                    JSONObject disparo = new JSONObject();
                    disparo.put("x1", String.valueOf(x1));
                    disparo.put("y1", String.valueOf(y1));
                    disparo.put("x2", String.valueOf(x2));
                    disparo.put("y2", String.valueOf(y2));
                    disparo.put("x_vector_direccio", String.valueOf(x_vector_direccio));
                    disparo.put("y_vector_direccio", String.valueOf(y_vector_direccio));
                    disparo.put("sala", sala.getId());
                    disparo.put("user", jugadors.get(numJugador).getNomUsuari());
                    mSocket.emit("disparo", disparo);
                } else {
                    float x1 = jugadors.get(numJugador).getPosition().x;
                    float y1 = jugadors.get(numJugador).getPosition().y;
                    float x2 = 0;
                    float y2 = 0;
                    float x_vector_direccio = 0;
                    float y_vector_direccio = 0;

                    Disparo disparo_visual = new Disparo(x1, y1, 0, 0, 0, 0, jugadors.get(numJugador));
                    stage.addActor(disparo_visual);

                    JSONObject disparo = new JSONObject();
                    disparo.put("x1", String.valueOf(x1));
                    disparo.put("y1", String.valueOf(y1));
                    disparo.put("x2", String.valueOf(x2));
                    disparo.put("y2", String.valueOf(y2));
                    disparo.put("x_vector_direccio", String.valueOf(x_vector_direccio));
                    disparo.put("y_vector_direccio", String.valueOf(y_vector_direccio));
                    disparo.put("sala", sala.getId());
                    disparo.put("user", jugadors.get(numJugador).getNomUsuari());
                    mSocket.emit("disparo", disparo);
                }



            }
        });

        disparo.setBounds(40, 225, 50, 50);

        // Agrega el ImageButton al Stage para que se pueda dibujar y recibir eventos de entrada
        stage.addActor(disparo);

        mSocket.on("disparo", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //JSONObject data = new JSONObject(jsonString);
                    String salaDisparo = data.getString("sala");
                    if (salaDisparo.equals(sala.getId())) {
                        if (!data.getString("user").equals(jugadors.get(numJugador).getNomUsuari())) {
                            String usernameToFind = data.getString("user");
                            // Loop through the list of Jugadors
                            int index = -1; // Initialize index to -1 (not found)
                            for (int i = 0; i < jugadors.size(); i++) {
                                Jugador jugador = jugadors.get(i);
                                if (jugador.getNomUsuari().equals(usernameToFind)) {
                                    // Found the Jugador with the specified username
                                    index = i;
                                    break; // No need to continue searching
                                }
                            }
                            float x1 = Float.valueOf(data.getString("x1"));
                            float y1 = Float.valueOf(data.getString("y1"));
                            float x2 = Float.valueOf(data.getString("x2"));
                            float y2 = Float.valueOf(data.getString("y2"));
                            float x_vector_direccio = Float.valueOf(data.getString("x_vector_direccio"));
                            float y_vector_direccio = Float.valueOf(data.getString("y_vector_direccio"));
                            Disparo disparo = new Disparo(x1, y1, x2, y2, x_vector_direccio, y_vector_direccio, jugadors.get(index));
                            stage.addActor(disparo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        // Obtener los estilos de botón de texto
        TextButton.TextButtonStyle textButtonStyle1 = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();

        // Asignar el estilo del botón de texto 1
        textButtonStyle1.font = skin.getFont("font-title");
        textButtonStyle1.fontColor = Color.BLUE;
        textButtonStyle1.up = skin.getDrawable("round-button"); // Asignar el drawable deseado

        // Asignar el estilo del botón de texto 2
        textButtonStyle2.font = skin.getFont("font-title");
        textButtonStyle2.fontColor = Color.YELLOW;
        textButtonStyle2.up = skin.getDrawable("round-button"); // Asignar el drawable deseado

        // Crear los botones de texto con los estilos respectivos
        textButtonEquip1 = new TextButton("EQUIP 1: " + array_jugadors_equip1.size() + " JUGADOR(S) VIUS", textButtonStyle1);
        textButtonEquip2 = new TextButton("EQUIP 2: " + array_jugadors_equip2.size() + " JUGADOR(S) VIUS", textButtonStyle2);

        // Agregar los botones al stage
        stage.addActor(textButtonEquip1);
        stage.addActor(textButtonEquip2);




        //Label.LabelStyle labelStyle = skin_vida.get("default", Label.LabelStyle.class);
        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        Label.LabelStyle labelStyle2 = new Label.LabelStyle();

        labelStyle1.font = skin.getFont("font-title");
        labelStyle1.fontColor = Color.BLUE;

        labelStyle2.font = skin.getFont("font-title");
        labelStyle2.fontColor = Color.YELLOW;

        //NOMBRES DE JUGADORES EN PANTALLA
        for (int i = 0; i < jugadors.size(); i++) {

            for (int j = 0; j < array_jugadors_equip1.size(); j++) {
                if (array_jugadors_equip1.get(j) == jugadors.get(i)) {
                    // Cambia el color del estilo
                    Label LabelNomJugador = new Label(jugadors.get(i).getNomUsuari(), labelStyle1);
                    labelsNoms.add(LabelNomJugador);

                    labelsNoms.get(i).setPosition(jugadors.get(i).getPosition().x, jugadors.get(i).getPosition().y + jugadors.get(i).getHeight() + 30);
                    stage.addActor(labelsNoms.get(i));
                }
            }
            for (int j = 0; j < array_jugadors_equip2.size(); j++) {
                if (array_jugadors_equip2.get(j) == jugadors.get(i)) {
                    // Cambia el color del estilo
                    Label LabelNomJugador = new Label(jugadors.get(i).getNomUsuari(), labelStyle2);
                    labelsNoms.add(LabelNomJugador);

                    labelsNoms.get(i).setPosition(jugadors.get(i).getPosition().x, jugadors.get(i).getPosition().y + jugadors.get(i).getHeight() + 30);
                    stage.addActor(labelsNoms.get(i));
                }
            }




        }


        // En el constructor de MapaPrueba
        touchpad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Este método se llama cuando el touchpad es tocado
                //System.out.println("Touchpad tocado");
                // Aquí puedes mostrar cualquier mensaje que desees
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Al tocar el touchpad, establecer la velocidad del jugador en 0 para evitar movimientos inesperados
                //System.out.println("ABAJO TOUCHPAD");
                //jugador.move(0, -1);
                //jugador.setVelocity(0, 0);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                knobX = touchpad.getKnobPercentX();
                knobY = touchpad.getKnobPercentY();


                //if(knobX - knobXAnterior >= 0.1f || knobY - knobYAnterior >= 0.1f ||knobX - knobXAnterior <= -0.1f ||  knobY - knobYAnterior <= -0.1){
                knobXAnterior = knobX;
                knobYAnterior = knobY;
                JSONObject movement = new JSONObject();
                movement.put("knobX", String.valueOf(knobX));
                movement.put("knobY", String.valueOf(knobY));
                movement.put("sala", sala.getId());
                movement.put("user", jugadors.get(numJugador).getNomUsuari());
                mSocket.emit("touchDragged", movement);
                //}

                // Llama al método move del jugador con los valores de deltaX y deltaY adecuados
                jugadors.get(numJugador).move(knobX, knobY);

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Al soltar el touchpad, establecer la velocidad del jugador en 0 para detener el movimiento
                //jugador.setVelocity(0, 0);
                //System.out.println("ARRIBA TOUCHPAD");
                //jugador.move(0, 1); // Cambia los valores según la velocidad de movimiento deseada
            }
        });

        mSocket.on("touchDragged", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //JSONObject data = new JSONObject(jsonString);
                    String salaMovement = data.getString("sala");
                    if (salaMovement.equals(sala.getId())) {
                        if (!data.getString("user").equals(jugadors.get(numJugador).getNomUsuari())) {
                            String usernameToFind = data.getString("user");
                            System.out.println("usernametoFind:" + usernameToFind);
                            // Loop through the list of Jugadors
                            int index = -1; // Initialize index to -1 (not found)
                            for (int i = 0; i < jugadors.size(); i++) {
                                Jugador jugador = jugadors.get(i);
                                System.out.println(jugador.getNomUsuari());
                                if (jugador.getNomUsuari().equals(usernameToFind)) {
                                    // Found the Jugador with the specified username
                                    index = i;
                                    break; // No need to continue searching
                                }
                            }
                            float knobX = Float.valueOf(data.getString("knobX"));
                            float knobY = Float.valueOf(data.getString("knobY"));
                            jugadors.get(index).moveRival(knobX, knobY);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("posicioCorrecio", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    //JSONObject data = new JSONObject(jsonString);
                    String salaMovement = data.getString("sala");
                    if (salaMovement.equals(sala.getId())) {
                        if (!data.getString("user").equals(jugadors.get(numJugador).getNomUsuari())) {
                            String usernameToFind = data.getString("user");
                            // Loop through the list of Jugadors
                            int index = -1; // Initialize index to -1 (not found)
                            for (int i = 0; i < jugadors.size(); i++) {
                                Jugador jugador = jugadors.get(i);
                                System.out.println("hola");
                                if (jugador.getNomUsuari().equals(usernameToFind)) {
                                    // Found the Jugador with the specified username
                                    index = i;
                                    break; // No need to continue searching
                                }
                            }
                            float x = Float.valueOf(data.getString("x"));
                            float y = Float.valueOf(data.getString("y"));
                            System.out.println("CORRECIO DE " + jugadors.get(index).getNomUsuari() + "X / Y: " + x + "/" + y);
                            jugadors.get(index).setPosition(x, y);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




    }

    @Override
    public void show() {
// Create a timer task to emit socket events every 2 seconds
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Emit socket event here
                JSONObject jsonEnviar = new JSONObject();
                jsonEnviar.put("user", jugadors.get(numJugador).getNomUsuari());
                jsonEnviar.put("sala", sala.getId());
                jsonEnviar.put("x", String.valueOf(jugadors.get(numJugador).getPosition().x));
                jsonEnviar.put("y", String.valueOf(jugadors.get(numJugador).getPosition().y));
                mSocket.emit("posicioCorrecio", jsonEnviar);
                System.out.println("CORRECIO ENVIADA");
            }
        }, 0, 10);
    }

    @Override
    public void render(float delta) {

        // Actualizar la posición de la cámara para que siga al jugador
        camera.position.set(jugadors.get(numJugador).getPosition().x, jugadors.get(numJugador).getPosition().y, 0);

        /*
        // Limitar la posición de la cámara para que no se salga del mapa
        float halfWidth = camera.viewportWidth * camera.zoom / 2;
        float halfHeight = camera.viewportHeight * camera.zoom / 2;
        float minX = halfWidth;
        float minY = halfHeight;
        float maxX = AssetManager.tiledMazmorra.getProperties().get("width", Integer.class) * AssetManager.tiledMazmorra.getProperties().get("tilewidth", Integer.class) - halfWidth;
        float maxY = AssetManager.tiledMazmorra.getProperties().get("height", Integer.class) * AssetManager.tiledMazmorra.getProperties().get("tileheight", Integer.class) - halfHeight;
        */

        camera.position.x = MathUtils.clamp(camera.position.x, (float) 0.25 * Gdx.graphics.getWidth(), (float) 0.65 * Gdx.graphics.getWidth());
        camera.position.y = MathUtils.clamp(camera.position.y, (float) 0.27 * Gdx.graphics.getHeight(), (float) 1.70 * Gdx.graphics.getHeight());


        // Actualizar la cámara
        camera.update();

        // Actualizar la posición del Touchpad en relación con la cámara
        float touchpadX = 30; // Ajustar la posición del Touchpad en X
        float touchpadY = 225; // Ajustar la posición del Touchpad en Y

        touchpadX = camera.position.x - (float) 0.25 * Gdx.graphics.getWidth(); // Ajustar la posición del Touchpad en X
        touchpadY = camera.position.y - (float) 0.20 * Gdx.graphics.getHeight(); // Ajustar la posición del Touchpad en Y


        // Establecer límites para el Touchpad

        touchpadX = MathUtils.clamp(touchpadX, camera.position.x - (float) 0.50 * Gdx.graphics.getWidth(), Gdx.graphics.getWidth() - touchpad.getWidth());
        touchpadY = MathUtils.clamp(touchpadY, camera.position.y - (float) 0.50 * Gdx.graphics.getHeight(), (float) 2 * Gdx.graphics.getHeight() - touchpad.getHeight());

        touchpad.setPosition(touchpadX, touchpadY);

        textButtonEquip1.setPosition(camera.position.x - (float) 0.25*Gdx.graphics.getWidth(), camera.position.y + (float) 0.20*Gdx.graphics.getHeight());
        textButtonEquip1.setText("EQUIP 1: " + array_jugadors_equip1.size() + " JUGADOR(S) VIUS");
        //System.out.println("equip 1:" + array_jugadors_equip1.size());

        textButtonEquip2.setPosition(camera.position.x + (float) 0.05*Gdx.graphics.getWidth(), camera.position.y + (float) 0.20*Gdx.graphics.getHeight());
        textButtonEquip2.setText("EQUIP 2: " + array_jugadors_equip2.size() + " JUGADOR(S) VIUS");
        //System.out.println("equip 2:" + array_jugadors_equip2.size());


        // Actualizar la posición del ProgressBar para que esté encima del jugador
        //progressBar.setPosition(jugadors.get(numJugador).getPosition().x,  jugadors.get(numJugador).getPosition().y +  jugadors.get(numJugador).getHeight() + 20);

        // Actualizar la posición de las ProgressBar para que estén encima de cada jugador
        for (int i = 0; i < jugadors.size(); i++) {
            progressBars.get(i).setPosition(jugadors.get(i).getPosition().x, jugadors.get(i).getPosition().y + jugadors.get(i).getHeight() + 25);
        }

        for (int i = 0; i < jugadors.size(); i++) {
            labelsNoms.get(i).setPosition(jugadors.get(i).getPosition().x, jugadors.get(i).getPosition().y + jugadors.get(i).getHeight() + 30);
        }

        // Definir la posición del botón de disparo

        float buttonX = 150; // Ajustar la posición del Touchpad en X
        float buttonY = 225; // Ajustar la posición del Touchpad en Y

        buttonX = camera.position.x + (float) 0.15 * Gdx.graphics.getWidth(); // Ajustar la posición del Touchpad en X
        buttonY = camera.position.y - (float) 0.20 * Gdx.graphics.getHeight(); // Ajustar la posición del Touchpad en Y


        // Establecer límites para el botón de disparo


        buttonX = MathUtils.clamp(buttonX, camera.position.x - (float) 0.50 * Gdx.graphics.getWidth(), Gdx.graphics.getWidth() - touchpad.getWidth());
        buttonY = MathUtils.clamp(buttonY, camera.position.y - (float) 0.50 * Gdx.graphics.getHeight(), (float) 2 * Gdx.graphics.getHeight() - touchpad.getHeight());


        // Actualizar la posición del botón de disparo
        disparo.setPosition(buttonX, buttonY);


        // Dibujar el mapa
        renderer.setView(camera);
        renderer.render();

        // Dibujar y actualizar el stage
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

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
