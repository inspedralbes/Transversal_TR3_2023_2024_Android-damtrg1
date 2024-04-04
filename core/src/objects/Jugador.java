package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    private Rectangle bounds; // Área de colisión del jugador

    public Vector2 getPosition() {
        return position;
    }

    TiledMap tiledMap;

    //ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Jugador(float x, float y, int width, int height, String nomUsuari, TiledMap tiledMap) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.direction = 0;
        this.nomUsuari = nomUsuari;
        this.tiledMap = tiledMap;
        System.out.println("Contructur: "+tiledMap.toString());

        // Inicializar el área de colisión
        this.bounds = new Rectangle(x, y, width/2, height);

        this.spriteJugador = AssetManager.jugadorSprite_dreta;
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
            bounds.setPosition(position);
            if (collidesWithWalls(tiledMap)) {
                collisionX = true;
                position.x = previousX;
                bounds.setPosition(position);
                COLISIO_DRETA = true;
            }

        }

        else if (deltaX < 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(tiledMap)) {
                collisionX = true;
                position.x = previousX;
                bounds.setPosition(position);
                COLISIO_ESQUERRA = true;
            }

        }

        if (deltaY > 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(tiledMap)) {
                collisionY = true;
                position.y = previousY;
                bounds.setPosition(position);
                COLISIO_ADALT = true;
            }
        }

        else if (deltaY < 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(tiledMap)) {
                collisionY = true;
                position.y = previousY;
                bounds.setPosition(position);
                COLISIO_ABAIX = true;
            }
        }

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                this.spriteJugador = AssetManager.jugadorSprite_dreta;
                this.orientacio_jugador = "dreta";
            }
            else if (deltaX < 0){
                this.spriteJugador = AssetManager.jugadorSprite_esquerra;
                this.orientacio_jugador = "esquerra";
            }
        }
        else {
            if (deltaY > 0) {
                this.spriteJugador = AssetManager.jugadorSprite_amunt;
                this.orientacio_jugador = "amunt";
            }
            else if (deltaY < 0){
                this.spriteJugador = AssetManager.jugadorSprite_avall;
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

        batch.draw(spriteJugador, position.x, position.y, width, height);
        // Draw the player hitbox (for debugging purposes)
        batch.end();
        batch.begin();

    }

    // Método para verificar colisiones con las paredes del mapa
    public boolean collidesWithWalls(TiledMap map) {
        // Obtener la capa de objetos del mapa
        MapLayer objectLayer = map.getLayers().get("colision");
        // Iterar sobre los objetos de la capa de objetos
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();

                // Verificar si el área de colisión del jugador se superpone con este objeto de rectángulo
                if (bounds.overlaps(rect)) {
                    System.out.println("true");
                    return true; // Hay colisión
                }
            }
        }
        System.out.println("false");

        // No hay colisión
        return false;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

}
