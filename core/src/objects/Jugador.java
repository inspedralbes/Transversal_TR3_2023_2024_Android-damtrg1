package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.AssetManager;
import com.mygdx.game.GameSceen;

import java.util.Iterator;
import java.util.Map;

import Utils.Settings;

public class Jugador extends Actor {

    private Vector2 position;
    private int width, height;
    private int direction;

    public static boolean COLISIO_DRETA = false;
    public static boolean COLISIO_ABAIX = false;
    public static boolean COLISIO_ESQUERRA = false;
    public static boolean COLISIO_ADALT = false;

    private Rectangle bounds; // Área de colisión del jugador

    public Vector2 getPosition() {
        return position;
    }

    //ShapeRenderer shapeRenderer = new ShapeRenderer();
    public Jugador(float x, float y, int width, int height) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.direction = 0;

        // Inicializar el área de colisión
        this.bounds = new Rectangle(x, y, width/2, height);
    }

    public void move(float deltaX, float deltaY) {
        // Guardar la posición anterior del jugador
        float previousX = position.x;
        float previousY = position.y;

        // Mover el jugador basado en la lógica del juego
        // Por ejemplo:
        // position.add(velocidadX * delta, velocidadY * delta);

        // Verificar colisiones con las paredes del mapa
        boolean collisionX = false;
        boolean collisionY = false;

        if (deltaX > 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(AssetManager.tiledMap)) {
                collisionX = true;
                position.x = previousX;
                bounds.setPosition(position);
                COLISIO_DRETA = true;
            }
        }

        else if (deltaX < 0) {
            position.x += deltaX * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(AssetManager.tiledMap)) {
                collisionX = true;
                position.x = previousX;
                bounds.setPosition(position);
                COLISIO_ESQUERRA = true;
            }
        }

        if (deltaY > 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(AssetManager.tiledMap)) {
                collisionY = true;
                position.y = previousY;
                bounds.setPosition(position);
                COLISIO_ADALT = true;
            }
        }

        else if (deltaY < 0) {
            position.y += deltaY * Settings.JUGADOR_VELOCITY;
            bounds.setPosition(position);
            if (collidesWithWalls(AssetManager.tiledMap)) {
                collisionY = true;
                position.y = previousY;
                bounds.setPosition(position);
                COLISIO_ABAIX = true;
            }
        }


    }


    @Override
    public void act(float delta) {
        // Aquí podrías manejar la lógica de movimiento del jugador


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(AssetManager.jugadorSprite, position.x, position.y, width, height);
        // Draw the player hitbox (for debugging purposes)
        batch.end();
        /*shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();*/
        batch.begin();

    }

    // Método para verificar colisiones con las paredes del mapa
    public boolean collidesWithWalls(TiledMap map) {
        // Obtener la capa de objetos del mapa
        MapLayer objectLayer = map.getLayers().get("Capa de Objetos 1");
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
}
