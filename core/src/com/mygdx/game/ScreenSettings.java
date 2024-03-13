package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import Utils.Settings;
import objects.Background;

public class ScreenSettings implements Screen {

    Pixel_R6 game;

    Background bg;

    Stage stage;

    OrthographicCamera camera;

    Skin skin, skin_txt;

    private float valorSliderVolumen, valorSliderMusic;


    public ScreenSettings(Pixel_R6 game) {
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


        //VOLUMEN
        // Obtener el estilo del Slider del Skin
        Slider.SliderStyle sliderStyle = skin.get("default-horizontal", Slider.SliderStyle.class);

        // Crear un nuevo estilo para el Slider en dirección horizontal
        Slider.SliderStyle horizontalSliderStyle = new Slider.SliderStyle(sliderStyle);

        // Asignar el nuevo background y knob antes al estilo horizontal del Slider
        horizontalSliderStyle.background = skin.getDrawable("slider-bar");
        horizontalSliderStyle.knob = skin.getDrawable("slider-bar-knob");
        horizontalSliderStyle.knobBefore = skin.getDrawable("slider-bar-fill");

        // Crear una instancia del Slider con el estilo horizontal
        Slider slider_volumen = new Slider(0, 100, 1, false, horizontalSliderStyle);

        Slider slider_musica = new Slider(0,100,1,false, horizontalSliderStyle);


        //BOTON DE MUTE
        Button.ButtonStyle buttonStyle = skin.get("sound", Button.ButtonStyle.class);

        Button.ButtonStyle buttonStylemusic = skin.get("music", Button.ButtonStyle.class);


        Button btn_volumen = new Button(buttonStyle);

        //ENCENDER BOTON
        btn_volumen.setChecked(true);

        btn_volumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               if(btn_volumen.isChecked()){
                   System.out.println("TRUE");
                   slider_volumen.setValue(valorSliderVolumen);
               }else{
                   System.out.println("FALSE");
                   // Guardar el valor actual del Slider
                   valorSliderVolumen = slider_volumen.getValue();
                   // Establecer el valor del Slider a 0
                   slider_volumen.setValue(0);
               }
            }
        });

        Button btn_musica = new Button(buttonStylemusic);

        btn_musica.setChecked(true);

        btn_musica.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(btn_musica.isChecked()){
                    System.out.println("TRUE");
                    slider_musica.setValue(valorSliderMusic);
                }else{
                    System.out.println("FALSE");
                    // Guardar el valor actual del Slider
                    valorSliderMusic = slider_musica.getValue();
                    // Establecer el valor del Slider a 0
                    slider_musica.setValue(0);
                }
            }
        });


        //VENTANA
        Window.WindowStyle windowStyle = skin.get(Window.WindowStyle.class);

        // Crea una instancia de Window con el estilo obtenido
        Window window = new Window("SETTINGS", windowStyle);
        window.getTitleLabel().setAlignment(Align.center);

        // Obtén las dimensiones de la ventana del juego desde la clase Settings
        int gameWidth = Settings.GAME_WIDTH;
        int gameHeight = Settings.GAME_HEIGHT;

        // Calcula las coordenadas X e Y para colocar la ventana en el centro de la pantalla
        float windowX = ((gameWidth / 2) - window.getWidth() * 1.5f);
        float windowY = ((gameHeight / 2) - window.getHeight() * 1.5f);

        // Establece la posición de la ventana en el centro de la pantalla
        window.setPosition(windowX, windowY);

        window.setSize(400, 400); // Establece el tamaño como desees

        window.add(btn_volumen);

        window.add(slider_volumen).prefSize(200, 80).row();

        window.add(btn_musica);

        window.add(slider_musica).prefSize(200, 80).row();

        stage.addActor(window);

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

    }
}
