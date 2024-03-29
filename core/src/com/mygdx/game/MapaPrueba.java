package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import objects.Jugador;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
public class MapaPrueba implements Screen {

    Pixel_R6 game;

    Background bg;
    int numJugador;

    Jugador jugador;

    public Jugador getJugador() {
        return jugador;
    }

    Stage stage;

    Batch batch;

    OrthographicCamera camera;
    private Timer timer;

    OrthogonalTiledMapRenderer renderer;

    Socket mSocket;

    Preferences preferences;

    Skin skin;

    Touchpad touchpad;

    Button disparo;
    ArrayList<Jugador> jugadors = new ArrayList<>();

    float knobXAnterior = 0;
    float knobYAnterior = 0;
    Sala sala;

    public MapaPrueba(Pixel_R6 game, Sala sala) {
        preferences = Gdx.app.getPreferences("Pref");
        this.sala= sala;
        this.game = game;

        AssetManager.load();
        try {
            mSocket = IO.socket("http://r6pixel.dam.inspedralbes.cat:3169");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();

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
        //bg = new Background(0, 0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

        JsonLoader jsonLoader = new JsonLoader();
        JSONObject jsonPosicions = jsonLoader.loadJson("posicions.json");
        jsonPosicions = jsonPosicions.getJSONObject("Mazmorra");
        int contador = 0;
        for (String usuari : sala.getUsers()) {
            if (!usuari.equals("NO PLAYER")) {
                JSONObject posicions = jsonPosicions.getJSONObject("pos" + (contador + 1));
                Jugador player;
                if(sala.getNombreMapa().equals("castillo")){
                     player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledCastillo);
                }else{
                     player = new Jugador(Float.valueOf((String) posicions.get("x")), Float.valueOf((String) posicions.get("y")), Settings.JUGADOR_WIDTH, Settings.JUGADOR_HEIGHT, usuari, AssetManager.tiledMazmorra);
                }
                jugadors.add(player);
                stage.addActor(player);
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

        if(sala.getNombreMapa().equals("castillo")){
            renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledCastillo);
        }else{
            renderer = new OrthogonalTiledMapRenderer(AssetManager.tiledMazmorra);
        }


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

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

        Button.ButtonStyle buttonStyle = skin.get("default",Button.ButtonStyle.class);

        // Crea una instancia de ImageButton con el estilo obtenido
        disparo = new Button(buttonStyle);

        // Asigna un listener para manejar eventos de clic en el botón
        disparo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DISPARO");
            }
        });

        disparo.setBounds(40, 225, 50, 50);

        // Agrega el ImageButton al Stage para que se pueda dibujar y recibir eventos de entrada
        stage.addActor(disparo);

        //Gdx.input.setInputProcessor(new InputHandlerGameScreen(this, touchpad));

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
                float knobX = touchpad.getKnobPercentX();
                float knobY = touchpad.getKnobPercentY();


                //if(knobX - knobXAnterior >= 0.1f || knobY - knobYAnterior >= 0.1f ||knobX - knobXAnterior <= -0.1f ||  knobY - knobYAnterior <= -0.1){
                    knobXAnterior = knobX;
                    knobYAnterior = knobY;
                    JSONObject movement = new JSONObject();
                    movement.put("knobX", knobX);
                    movement.put("knobY", knobY);
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
                    if(salaMovement.equals(sala.getId())) {
                        if(!data.getString("user").equals(jugadors.get(numJugador).getNomUsuari())){
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
                            float knobX = data.getFloat("knobX");
                            float knobY = data.getFloat("knobY");
                            jugadors.get(index).move(knobX, knobY);
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
                    if(salaMovement.equals(sala.getId())) {
                        if(!data.getString("user").equals(jugadors.get(numJugador).getNomUsuari())){
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
                            float x = data.getFloat("x");
                            float y = data.getFloat("y");
                            System.out.println("CORRECIO DE " + jugadors.get(index).getNomUsuari() + "X / Y: " + x + "/" + y);
                            jugadors.get(index).setPosition(x, y);                 }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
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
                jsonEnviar.put("x", jugadors.get(numJugador).getPosition().x);
                jsonEnviar.put("y", jugadors.get(numJugador).getPosition().y);
                mSocket.emit("posicioCorrecio", jsonEnviar);
                System.out.println("CORRECIO ENVIADA");
            }
        }, 0, 2000);
    }

    @Override
    public void render(float delta) {
        // Actualizar la posición de la cámara para que siga al jugador
        camera.position.set(jugadors.get(numJugador).getPosition().x, jugadors.get(numJugador).getPosition().y, 0);

        // Limitar la posición de la cámara para que no se salga del mapa
        float halfWidth = camera.viewportWidth * camera.zoom / 2;
        float halfHeight = camera.viewportHeight * camera.zoom / 2;
        float minX = halfWidth;
        float minY = halfHeight;
        float maxX = AssetManager.tiledMazmorra.getProperties().get("width", Integer.class) * AssetManager.tiledMazmorra.getProperties().get("tilewidth", Integer.class) - halfWidth;
        float maxY = AssetManager.tiledMazmorra.getProperties().get("height", Integer.class) * AssetManager.tiledMazmorra.getProperties().get("tileheight", Integer.class) - halfHeight;

        camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);

        // Actualizar la cámara
        camera.update();

        // Actualizar la posición del Touchpad en relación con la cámara
        float touchpadX = camera.position.x - camera.viewportWidth / 2 + 10; // Ajustar la posición del Touchpad en X
        float touchpadY = camera.position.y - camera.viewportHeight / 2 + 10; // Ajustar la posición del Touchpad en Y

        // Establecer límites para el Touchpad
        touchpadX = MathUtils.clamp(touchpadX, 0, Gdx.graphics.getWidth() - touchpad.getWidth());
        touchpadY = MathUtils.clamp(touchpadY, 0, Gdx.graphics.getHeight() - touchpad.getHeight());

        // Establecer límites para el Touchpad
        touchpadX = MathUtils.clamp(touchpadX, camera.position.x - minX, Gdx.graphics.getWidth() - touchpad.getWidth());
        touchpadY = MathUtils.clamp(touchpadY, camera.position.y - 200, Gdx.graphics.getHeight() - touchpad.getHeight());

        touchpad.setPosition(touchpadX, touchpadY);


        // Definir la posición del botón de disparo
        float buttonX = camera.position.x + camera.viewportWidth / 2; // Ajustar la posición del botón en X
        float buttonY = camera.position.y - camera.viewportHeight / 2 + 10; // Ajustar la posición del botón en Y

        // Establecer límites para el botón de disparo
        buttonX = MathUtils.clamp(buttonX, camera.position.x - minX, Gdx.graphics.getWidth() - touchpad.getWidth());
        buttonY = MathUtils.clamp(buttonY, 0, Gdx.graphics.getHeight() - disparo.getHeight());

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
