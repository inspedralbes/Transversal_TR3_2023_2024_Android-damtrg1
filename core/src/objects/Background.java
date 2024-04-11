package objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.AssetManager;

public class Background extends Actor {

    protected Vector2 position;
    protected float width;
    protected float height;
    protected boolean leftOfScreen;
    protected boolean win;

    public Background(float x, float y, float width, float height, boolean win) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.win = win;
    }

    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!win) {
            batch.draw(AssetManager.background, position.x, position.y, width, height);
        } else {
            batch.draw(AssetManager.backWin, position.x, position.y, width, height);
        }

    }

    public float getTailX() {
        return position.x + width;
    }


}
