package helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import objects.Jugador;
import com.mygdx.game.MapaPrueba;

public class InputHandlerGameScreen implements InputProcessor {

    // Objectes necessaris
    private Jugador jugador;
    private MapaPrueba screen;

    // Enter per a la gestió del moviment d'arrossegament
    int previousY = 0;

    public InputHandlerGameScreen(MapaPrueba screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        jugador = screen.getJugador();

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                // Mover hacia arriba
                jugador.move(0, 1); // Cambia los valores según la velocidad de movimiento deseada
                break;
            case Input.Keys.S:
                // Mover hacia abajo
                jugador.move(0, -1);
                break;
            case Input.Keys.A:
                // Mover hacia la izquierda
                jugador.move(-1, 0);
                break;
            case Input.Keys.D:
                // Mover hacia la derecha
                jugador.move(50, 0);
                break;
        }
        return true; // Devuelve true para indicar que la tecla ha sido procesada
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem el cotxe a l'estat normal
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
