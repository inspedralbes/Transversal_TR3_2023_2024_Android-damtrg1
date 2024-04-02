package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Disparo extends Actor {
    private ShapeRenderer shapeRenderer;
    private float x1, y1, x2, y2, x_vector_direccio, y_vector_direccio;

    public Disparo(float x1, float y1, float x2, float y2, float x_vector_direccio, float y_vector_direccio) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.x_vector_direccio = x_vector_direccio;
        this.y_vector_direccio = y_vector_direccio;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        this.x1 = this.x1 + this.x_vector_direccio;
        this.y1 = this.y1 + this.y_vector_direccio;
        this.x2 = this.x2 + this.x_vector_direccio;
        this.y2 = this.y2 + this.y_vector_direccio;

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(x1, y1, x2, y2);
        shapeRenderer.end();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

}
