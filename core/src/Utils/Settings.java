package Utils;

import com.badlogic.gdx.Gdx;

public class Settings {
    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = Gdx.graphics.getWidth();
    public static final int GAME_HEIGHT = Gdx.graphics.getHeight();

    public static final float BG_SPEED = -200; // Por ejemplo, la velocidad del fondo es 2.0

    public static float JUGADOR_VELOCITY = (float) 0.0010 * Gdx.graphics.getWidth();
    public static final int JUGADOR_WIDTH = 50;
    public static final int JUGADOR_HEIGHT = 25;
    public static final float JUGADOR_STARTX = 20;
    public static final float JUGADOR_STARTY = GAME_HEIGHT/2 - JUGADOR_HEIGHT/2;
}
