package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Disparo extends Actor {
    private ShapeRenderer shapeRenderer;
    private float x1, y1, x2, y2, x_vector_direccio, y_vector_direccio;
    private Jugador jugador_associat;

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






        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        drawThickLine(shapeRenderer, x1, y1, x2, y2, 50); // Dibuja una línea roja gruesa con grosor de 5 píxeles

        shapeRenderer.end();

        this.x1 = this.x1 + this.x_vector_direccio;
        this.y1 = this.y1 + this.y_vector_direccio;
        this.x2 = this.x2 + this.x_vector_direccio;
        this.y2 = this.y2 + this.y_vector_direccio;
    }

    private static void drawThickLine(ShapeRenderer shapeRenderer, float x1, float y1, float x2, float y2, float lineWidth) {
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
    }



    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
