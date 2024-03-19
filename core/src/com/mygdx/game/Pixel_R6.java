package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pixel_R6 extends Game {

	private SpriteBatch spriteBatch;
	private BitmapFont bitmapFont;

    Preferences preferences;


	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();

        // Cargamos las preferencias
        preferences = Gdx.app.getPreferences("Pref");

        // Comprobamos si el usuario está logueado
        if (preferences.getBoolean("logged")) {
            setScreen(new PantallaPrincipal(this)); // Si está logueado, va a la pantalla principal
        } else {
            setScreen(new Login(this)); // Si no está logueado, va a la pantalla de login
        }


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
