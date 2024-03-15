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

    public static final int COTXE_STRAIGHT = 0;
    public static final int COTXE_UP = 1;
    public static final int COTXE_DOWN = 2;

    private Rectangle bounds; // Área de colisión del jugador

    public Vector2 getPosition() {
        return position;
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();
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
        if (collidesWithWalls(AssetManager.tiledMap)) {
            // Si hay colisión, deshacer el último movimiento
            position.set(previousX, previousY);
            bounds.setPosition(position);
        }

        else {
            // Mover el jugador y actualizar el área de colisión
            position.add(deltaX, deltaY);
            bounds.setPosition(position);
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
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();
        shapeRenderer.end();
        batch.begin();

    }

    // Método para verificar colisiones con las paredes del mapa
    public boolean collidesWithWalls(TiledMap map) {
        // Obtener la capa de objetos del mapa
        MapLayer objectLayer = map.getLayers().get("Capa de Objetos 1");

        // Obtener la capa de colisión del mapa
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("paredCasa");

        // Obtener las celdas que intersectan con el área de colisión del jugador
        int startX = (int) (position.x / collisionLayer.getTileWidth());
        int startY = (int) (position.y / collisionLayer.getTileHeight());
        int endX = (int) ((position.x + bounds.getWidth()) / collisionLayer.getTileWidth());
        int endY = (int) ((position.y + bounds.getHeight()) / collisionLayer.getTileHeight());

        System.out.println("StartX: " + startX + ", StartY: " + startY + ", EndX: " + endX + ", EndY: " + endY);

        // Iterar sobre las celdas del área de colisión del jugador
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null && cell.getTile() != null) {
                    System.out.println("CELDA");
                    // Get the tile from the cell
                    int tileId = cell.getTile().getId();

                    TiledMapTileSets ts = map.getTileSets();
                    TiledMapTileSet terrenosExteriores = ts.getTileSet("TerrenosExterirores");
                    MapProperties properties = terrenosExteriores.getTile(383).getProperties();
                  


                }
            }
        }

        // No hay colisión
        return false;
    }
}
