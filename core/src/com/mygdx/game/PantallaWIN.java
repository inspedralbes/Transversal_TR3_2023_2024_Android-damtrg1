package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Settings;
import objects.Background;
import objects.Jugador;

public class PantallaWIN implements Screen {

    Pixel_R6 game;

    Background bg;

    static Stage stage;

    OrthographicCamera camera;

    Skin skin_inputs;

    Table table;

    ArrayList<Label> ganadores = new ArrayList<>();
    ArrayList<Label> perdedores = new ArrayList<>();

    ArrayList<Label> monedasGanadores = new ArrayList<>();

    ArrayList<Label> monedasPerdedores = new ArrayList<>();

    public PantallaWIN(Pixel_R6 game, ArrayList<Jugador> Userganadores, ArrayList<Jugador> Userperdedores) {
        this.game = game;

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
        bg = new Background(0, 0, Gdx.graphics.getWidth(), Settings.GAME_HEIGHT, true);

        //AÑADIMOS EL FONDO AL STAGE
        stage.addActor(bg);

        // Cargar el Skin
        skin_inputs = new Skin(Gdx.files.internal("skin/uiskin.json"));


        // Obtén el estilo de la etiqueta "title" del Skin
        Label.LabelStyle titleLabelStyle = skin_inputs.get("title", Label.LabelStyle.class);


        Label titulo = new Label("GANADORES", titleLabelStyle);

        // Calcula la posición X centrada en la pantalla
        float posX = (Gdx.graphics.getWidth() - titulo.getWidth()) / 2;

        // Calcula la posición Y en la parte superior de la pantalla
        float posY = Settings.GAME_HEIGHT - titulo.getHeight(); // Margen de 20 píxeles

        // Establece la posición del título
        titulo.setPosition(posX, posY); // Alinea el título en la parte superior y centrado

        // Añade el título al stage
        stage.addActor(titulo);


        Window.WindowStyle windowStyle = skin_inputs.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("", windowStyle);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Gdx.graphics.getWidth();
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 3.3f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 2.3f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(1000, 600); // Establece el tamaño como desees


        Table ganadors = new Table();
        Table perdedors = new Table();
        Table moneWINS = new Table();
        Table moneLose = new Table();


        for (int i = 0; i < Userganadores.size(); i++) {
            System.out.println("Ganar WIN: " + Userganadores.get(i));
        }

        for (int i = 0; i < Userperdedores.size(); i++) {
            System.out.println("Perder WIN: " + Userperdedores.get(i));
        }

        Label.LabelStyle labeljugadors = skin_inputs.get("default", Label.LabelStyle.class);
        labeljugadors.font.getData().setScale(2f); // Ajustar el tamaño de la fuente


        for (int i = 0; i < Userganadores.size(); i++) {
            Label wins = new Label(Userganadores.get(i).getNomUsuari(), labeljugadors);
            Label monWin = new Label("50 Coins", skin_inputs);
            monedasGanadores.add(monWin);
            ganadores.add(wins);
            moneWINS.add(monWin).pad(40);
            ganadors.add(wins).pad(20);
        }

        for (int i = 0; i < Userperdedores.size(); i++) {
            Label perduts = new Label(Userperdedores.get(i).getNomUsuari(), labeljugadors);
            Label monLose = new Label("10 Coins", skin_inputs);
            monedasPerdedores.add(monLose);
            perdedores.add(perduts);
            moneLose.add(monLose).pad(40);
            perdedors.add(perduts).pad(20);
        }

        Label tituloGanador = new Label("GANADORES", skin_inputs);
        Label tituloPerdedores = new Label("PERDEDORES", skin_inputs);


        //GANADORES
        window.add(tituloGanador).row();
        window.add(moneWINS).pad(20).row();
        window.add(ganadors).row();

        //PERDEDORES
        window.add(tituloPerdedores).pad(20).row();
        window.add(moneLose).row();
        window.add(perdedors).pad(20);

        stage.addActor(window);

        JSONObject jsonUserWinKills = new JSONObject();

        for (int i = 0; i < Userganadores.size(); i++) {
            String jugador = Userganadores.get(i).getNomUsuari();
            int kills = Userganadores.get(i).getKills();
            System.out.println("KILLS: " + jugador + " " + kills);
            jsonUserWinKills.put("UserWIN", jugador);
            jsonUserWinKills.put("KillWIN", kills);
        }

        JSONObject jsonUserLoseKills = new JSONObject();
        for (int i = 0; i < Userperdedores.size(); i++) {
            String perdedor = Userperdedores.get(i).getNomUsuari();
            int kill = Userperdedores.get(i).getKills();
            System.out.println("KILL PERDEDOR: " + perdedor + " " + kill);
            jsonUserLoseKills.put("UserLose", perdedor);
            jsonUserLoseKills.put("KillLOSE", kill);
        }

        System.out.println();

        // Crear un HashMap para almacenar los datos del usuario
        JSONObject json = new JSONObject();

        json.put("userWin", jsonUserWinKills);
        json.put("userLose", jsonUserLoseKills);
        json.put("monedasWin", 50);
        json.put("monedasLose", 10);


        // Crear una solicitud HTTP POST
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://r6pixel.duckdns.org:3168/finalGame"); // URL de tu servidor
        httpRequest.setHeader("Content-Type", "application/json");
        String data = json.toString();
        httpRequest.setContent(data);

        // Enviar la solicitud al servidor
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

            }

            @Override
            public void failed(Throwable t) {
                // Ocurrió un error al enviar la solicitud
                System.out.println("Error al enviar la solicitud: " + t.getMessage());
            }

            @Override
            public void cancelled() {
                // La solicitud fue cancelada
                System.out.println("Solicitud cancelada");
            }
        });

        TextButton.TextButtonStyle textButtonStyle = skin_inputs.get("round", TextButton.TextButtonStyle.class);

        TextButton btn_volver = new TextButton("VOLVER", textButtonStyle);

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
        stage.dispose();
    }
}
