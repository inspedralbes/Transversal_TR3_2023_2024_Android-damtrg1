package objects;

import static objects.Jugador.tiledMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.MapaPrueba;

import java.util.ArrayList;

public class Disparo extends Actor {
    private ShapeRenderer shapeRenderer;
    private float x1, y1, x2, y2, x_vector_direccio, y_vector_direccio;
    private Jugador jugador_associat;
    private Polygon bounds; // Área de colisión del disparo
    private ArrayList<Jugador> array_jugadors_rivals;
    private Jugador jugador_colisionat;

    public Disparo(float x1, float y1, float x2, float y2, float x_vector_direccio, float y_vector_direccio, Jugador jugador_associat) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.x_vector_direccio = x_vector_direccio;
        this.y_vector_direccio = y_vector_direccio;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);


        this.jugador_associat = jugador_associat;



        boolean pertany_equip1 = false;

        for (Jugador jugador : MapaPrueba.array_jugadors_equip1) {



            if (jugador == this.jugador_associat) {
                pertany_equip1 = true;
                this.array_jugadors_rivals = MapaPrueba.array_jugadors_equip2;
            }
        }

        if (!pertany_equip1) {
            this.array_jugadors_rivals = MapaPrueba.array_jugadors_equip1;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        if (this.x2 == 0 && this.y2 == 0) {
            if (this.jugador_associat.getOrientacio_jugador().equals("dreta")) {
                this.x2 = this.x1 + 1*100;
                this.y2 = this.y1;
                this.x_vector_direccio = 1*100;
                this.y_vector_direccio = 0;
            }
            else if (this.jugador_associat.getOrientacio_jugador().equals("esquerra")) {
                this.x2 = this.x1 - 1*100;
                this.y2 = this.y1;
                this.x_vector_direccio = -1*100;
                this.y_vector_direccio = 0;
            }
            else if (this.jugador_associat.getOrientacio_jugador().equals("amunt")) {
                this.x2 = this.x1;
                this.y2 = this.y1 + 1*100;
                this.x_vector_direccio = 0;
                this.y_vector_direccio = 1*100;
            }
            else {
                this.x2 = this.x1;
                this.y2 = this.y1 - 1*100;
                this.x_vector_direccio = 0;
                this.y_vector_direccio = -1*100;
            }
        }
        //System.out.println(tiledMap.toString());








        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        drawThickLine(shapeRenderer, x1, y1, x2, y2, 50); // Dibuja una línea roja gruesa con grosor de 5 píxeles



        this.x1 = this.x1 + this.x_vector_direccio;
        this.y1 = this.y1 + this.y_vector_direccio;
        this.x2 = this.x2 + this.x_vector_direccio;
        this.y2 = this.y2 + this.y_vector_direccio;

        bounds.setPosition(this.x1,this.y1);

        //System.out.println(bounds.x);
        //System.out.println(bounds.y);

        shapeRenderer.end();
    }

    private void drawThickLine(ShapeRenderer shapeRenderer, float x1, float y1, float x2, float y2, float lineWidth) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        float normalLength = (float) Math.sqrt(dx * dx + dy * dy);
        float halfLineWidth = lineWidth / 2;

        float nx = dy / normalLength;
        float ny = -dx / normalLength;

        float x3 = x1 + halfLineWidth * nx;
        float y3 = y1 + halfLineWidth * ny;
        float x4 = x2 + halfLineWidth * nx;
        float y4 = y2 + halfLineWidth * ny;
        float x5 = x2 - halfLineWidth * nx;
        float y5 = y2 - halfLineWidth * ny;
        float x6 = x1 - halfLineWidth * nx;
        float y6 = y1 - halfLineWidth * ny;

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.triangle(x3, y3, x4, y4, x5, y5);
        shapeRenderer.triangle(x5, y5, x6, y6, x3, y3);

        this.bounds = new Polygon(new float[]{x3, y3, x4, y4, x5, y5, x6, y6});

        // Dibujar líneas con Vector2
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(x3, y3);
        vertices[1] = new Vector2(x4, y4);
        vertices[2] = new Vector2(x5, y5);
        vertices[3] = new Vector2(x6, y6);
        for (int i = 0; i < vertices.length; i++) {
            int nextIndex = (i + 1) % vertices.length;
            shapeRenderer.line(vertices[i], vertices[nextIndex]);
        }
        shapeRenderer.end();



        boolean colision_player = collidesWithPlayer(this.array_jugadors_rivals);
        if (colision_player) {
            this.remove();
            for (Jugador jugador : MapaPrueba.jugadors) {
                if (jugador == this.jugador_colisionat) {
                    MapaPrueba.progressBars.get(jugador.getZIndex()).setValue(MapaPrueba.progressBars.get(jugador.getZIndex()).getValue()-10);
                }
            }
        }

        boolean colision_walls = collidesWithWalls(tiledMap, this.bounds);
        if (colision_walls) {
            System.out.println("aaaaaaaaaaaaa");
            this.remove();

        }
    }

    public boolean collidesWithWalls(TiledMap map, Polygon polygon) {
        // Obtener la capa de objetos del mapa
        MapLayer objectLayer = map.getLayers().get("colision");
        // Iterar sobre los objetos de la capa de objetos
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                // Verificar si hay una colisión entre el polígono y el rectángulo
                if (polygon.getBoundingRectangle().overlaps(rect)) {
                    // Convertir el rectángulo del mapa en un polígono
                    Polygon rectPolygon = rectToPolygon(rect);
                    if (Intersector.overlapConvexPolygons(polygon, rectPolygon)) {
                        return true; // Hay colisión
                    }
                }
            }
        }
        return false; // No hay colisión
    }

    public boolean collidesWithPlayer(ArrayList <Jugador> array_equip_rival) {


        for (Jugador jugador : array_equip_rival) {
            // Verificar si los límites se intersectan
            if (jugador.getBounds().overlaps(this.bounds.getBoundingRectangle())) {
                this.jugador_colisionat = jugador;
                return true;
            }
        }

        return true;
    }

    // Función para convertir un rectángulo a un polígono
    private Polygon rectToPolygon(Rectangle rect) {
        float[] vertices = new float[8];
        vertices[0] = rect.x;
        vertices[1] = rect.y;
        vertices[2] = rect.x + rect.width;
        vertices[3] = rect.y;
        vertices[4] = rect.x + rect.width;
        vertices[5] = rect.y + rect.height;
        vertices[6] = rect.x;
        vertices[7] = rect.y + rect.height;
        return new Polygon(vertices);
    }


    @Override
    public void act(float delta) {
        super.act(delta);


    }

}
