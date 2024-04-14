package objects;

import static com.mygdx.game.AssetManager.jugador_amunt;
import static com.mygdx.game.AssetManager.jugador_avall;
import static com.mygdx.game.AssetManager.jugador_dreta;
import static com.mygdx.game.AssetManager.jugador_esquerra;
import static com.mygdx.game.AssetManager.spritesheet_joc_amunt;
import static com.mygdx.game.AssetManager.spritesheet_joc_avall;
import static com.mygdx.game.AssetManager.spritesheet_joc_dreta;
import static com.mygdx.game.AssetManager.spritesheet_joc_esquerra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.AssetManager;
import com.mygdx.game.MapaPrueba;

import Utils.Settings;

public class Jugador extends Actor {

    private Vector2 position;
    private int width, height;
    private int direction;
    private String nomUsuari;
    private Sprite spriteJugador;
    private String orientacio_jugador;
    private Rectangle bounds; // Área de colisión del jugador

    private Sprite jugadorSprite_dreta;
    private Sprite jugadorSprite_avall;
    private Sprite jugadorSprite_esquerra;
    private Sprite jugadorSprite_amunt;

    public static boolean COLISIO_DRETA = false;
    public static boolean COLISIO_ABAIX = false;
    public static boolean COLISIO_ESQUERRA = false;
    public static boolean COLISIO_ADALT = false;

    public void setSpriteJugador(Sprite spriteJugador) {
        this.spriteJugador = spriteJugador;
    }

    public Sprite getSpriteJugador() {
        return spriteJugador;
    }



    private int kills;



    public Vector2 getPosition() {
        return position;
    }

    public static TiledMap tiledMap;

    //ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Jugador(float x, float y, int width, int height, String nomUsuari, TiledMap tiledMap, String nom_skin) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.direction = 0;
        this.nomUsuari = nomUsuari;
        this.tiledMap = tiledMap;
        System.out.println("Contructur: "+tiledMap.toString());

        // Inicializar el área de colisión
        this.bounds = new Rectangle(x, y, width/2, height);

        this.kills = 0;

        System.out.println("buscando las skins");
        FileHandle mapasDir = Gdx.files.local("skinsMod");
        if (mapasDir.exists() && mapasDir.isDirectory()) {

            FileHandle[] subdirs = mapasDir.list();
            for (FileHandle subdir : subdirs) {
                System.out.println(subdir);
                System.out.println(nom_skin);
                if (String.valueOf(subdir).equals("skinsMod/"+nom_skin)) {

                    Texture spritesheet_joc_dreta = new Texture(Gdx.files.local(subdir+"/swat_sprite_dreta.png"));
                    spritesheet_joc_dreta.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    Texture spritesheet_joc_avall = new Texture(Gdx.files.local(subdir+"/swat_sprite_avall.png"));
                    spritesheet_joc_avall.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    Texture spritesheet_joc_esquerra = new Texture(Gdx.files.local(subdir+"/swat_sprite_esquerra.png"));
                    spritesheet_joc_esquerra.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    Texture spritesheet_joc_amunt = new Texture(Gdx.files.local(subdir+"/swat_sprite_amunt.png"));
                    spritesheet_joc_amunt.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

                    TextureRegion jugador_dreta = new TextureRegion(spritesheet_joc_dreta, 47, 8, 31, 23);
                    // Crear una nueva Sprite con la región de textura
                    this.jugadorSprite_dreta = new Sprite(jugador_dreta);

                    TextureRegion jugador_avall = new TextureRegion(spritesheet_joc_avall, 426, 42, 31, 38);
                    // Crear una nueva Sprite con la región de textura
                    this.jugadorSprite_avall = new Sprite(jugador_avall);
                    TextureRegion jugador_esquerra = new TextureRegion(spritesheet_joc_esquerra, 485, 427, 36, 30);
                    // Crear una nueva Sprite con la región de textura
                    this.jugadorSprite_esquerra = new Sprite(jugador_esquerra);
                    TextureRegion jugador_amunt = new TextureRegion(spritesheet_joc_amunt, 7, 484, 26, 38);
                    // Crear una nueva Sprite con la región de textura
                    this.jugadorSprite_amunt = new Sprite(jugador_amunt);

                }
            }

        }

        this.spriteJugador = this.jugadorSprite_dreta;

        this.orientacio_jugador = "dreta";
    }

    public String getOrientacio_jugador() {
        return orientacio_jugador;
    }

    public void move(float deltaX, float deltaY) {
        // Guardar la posición anterior del jugador
        float previousX = position.x;
        float previousY = position.y;

        System.out.println(deltaX);
        System.out.println(deltaY);

        // Mover el jugador basado en la lógica del juego
        // Por ejemplo:
        // position.add(velocidadX * delta, velocidadY * delta);

        // Verificar colisiones con las paredes del mapa
        boolean collisionX = false;
        boolean collisionY = false;

        if (deltaX > 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);
            boolean colision = collidesWithWalls(tiledMap, this.bounds);
            if (colision) {
                collisionX = true;
                position.x = previousX;
                this.bounds.setPosition(position);
                COLISIO_DRETA = true;
            }

        }

        else if (deltaX < 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);
            boolean colision = collidesWithWalls(tiledMap, this.bounds);
            if (colision) {
                collisionX = true;
                position.x = previousX;
                this.bounds.setPosition(position);
                COLISIO_ESQUERRA = true;
            }

        }

        if (deltaY > 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);
            boolean colision = collidesWithWalls(tiledMap, this.bounds);
            if (colision) {
                collisionY = true;
                position.y = previousY;
                this.bounds.setPosition(position);
                COLISIO_ADALT = true;
            }
        }

        else if (deltaY < 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);
            boolean colision = collidesWithWalls(tiledMap, this.bounds);
            if (colision) {
                collisionY = true;
                position.y = previousY;
                this.bounds.setPosition(position);
                COLISIO_ABAIX = true;
            }
        }

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                this.spriteJugador = this.jugadorSprite_dreta;
                this.orientacio_jugador = "dreta";
            }
            else if (deltaX < 0){
                this.spriteJugador = this.jugadorSprite_esquerra;
                this.orientacio_jugador = "esquerra";
            }
        }
        else {
            if (deltaY > 0) {
                this.spriteJugador = this.jugadorSprite_amunt;
                this.orientacio_jugador = "amunt";
            }
            else if (deltaY < 0){
                this.spriteJugador = this.jugadorSprite_avall;
                this.orientacio_jugador = "avall";
            }
        }




    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void aumentarKills(){
        this.kills++;
    }

    public int getKills(){
        return this.kills;
    }

    public void moveRival(float deltaX, float deltaY) {
        // Guardar la posición anterior del jugador
        float previousX = position.x;
        float previousY = position.y;

        System.out.println(deltaX);
        System.out.println(deltaY);

        // Mover el jugador basado en la lógica del juego
        // Por ejemplo:
        // position.add(velocidadX * delta, velocidadY * delta);

        // Verificar colisiones con las paredes del mapa
        boolean collisionX = false;
        boolean collisionY = false;
/*
        if (deltaX > 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);


        }

        else if (deltaX < 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);




        }

        if (deltaY > 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);

        }

        else if (deltaY < 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            this.bounds.setPosition(position);

        }

 */

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                this.spriteJugador = this.jugadorSprite_dreta;
                this.orientacio_jugador = "dreta";
            }
            else if (deltaX < 0){
                this.spriteJugador = this.jugadorSprite_esquerra;
                this.orientacio_jugador = "esquerra";
            }
        }
        else {
            if (deltaY > 0) {
                this.spriteJugador = this.jugadorSprite_amunt;
                this.orientacio_jugador = "amunt";
            }
            else if (deltaY < 0){
                this.spriteJugador = this.jugadorSprite_avall;
                this.orientacio_jugador = "avall";
            }
        }




    }

    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void act(float delta) {
        // Aquí podrías manejar la lógica de movimiento del jugador


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (MapaPrueba.permetre_render) {
            batch.draw(spriteJugador, position.x, position.y, width, height);
        }

        // Draw the player hitbox (for debugging purposes)

    }

    // Método para verificar colisiones con las paredes del mapa
    public boolean collidesWithWalls(TiledMap map, Rectangle bounds) {
        // Obtener la capa de objetos del mapa
        MapLayer objectLayer = map.getLayers().get("colision");
        // Iterar sobre los objetos de la capa de objetos
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                //System.out.println("hola");
                // Verificar si el área de colisión del jugador se superpone con este objeto de rectángulo
                if (bounds.overlaps(rect)) {
                    //System.out.println("true");
                    return true; // Hay colisión
                }
            }
        }
        //System.out.println("false");

        // No hay colisión
        return false;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
