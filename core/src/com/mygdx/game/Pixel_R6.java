package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.net.HttpStatus;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class Pixel_R6 extends Game {

	private SpriteBatch spriteBatch;
	private BitmapFont bitmapFont;

    Preferences preferences;

	public static ShapeRenderer shapeRenderer;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();

        // Cargamos las preferencias
        preferences = Gdx.app.getPreferences("Pref");

		shapeRenderer = new ShapeRenderer();

        // Comprobamos si el usuario está logueado
        if (preferences.getBoolean("logged")) {

				setScreen(new PantallaPrincipal(this, true));
        } else {
            setScreen(new Login(this)); // Si no está logueado, va a la pantalla de login
        }

		Gdx.graphics.setResizable(false);  // Deshabilitar la capacidad de redimensionar

	}

	// Cridem per descartar els recursos carregats.
	@Override
	public void dispose() {
		super.dispose();
		spriteBatch.dispose();
		bitmapFont.dispose();
	}

    // Método para actualizar el estado de logueo
    public void setLoggedIn(boolean loggedIn, String username) {
        preferences.putBoolean("logged", loggedIn);
        preferences.putString("username", username);
        preferences.flush(); // Guardamos los cambios en las preferencias
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}
}
