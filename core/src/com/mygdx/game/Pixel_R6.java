package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pixel_R6 extends Game {

	private SpriteBatch spriteBatch;
	private BitmapFont bitmapFont;


	@Override
	public void create() {

		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();

		// I definim la pantalla principal com a la pantalla
		setScreen(new Login(this));

	}

	// Cridem per descartar els recursos carregats.
	@Override
	public void dispose() {
		super.dispose();
		spriteBatch.dispose();
		bitmapFont.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}
}
