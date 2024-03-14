package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.AssetManager;

public class Mapa extends Actor {

    protected Vector2 position;
    protected float width;
    protected float height;
    protected boolean leftOfScreen;

    public Mapa(float x, float y, float width, float height) {
        position = new Vector2(x,y);
        this.width = width;
        this.height = height;
    }

    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //batch.draw(AssetManager.tiledMap, position.x, position.y, width, height);
    }

    public float getTailX() {
        return position.x + width;
    }
}
