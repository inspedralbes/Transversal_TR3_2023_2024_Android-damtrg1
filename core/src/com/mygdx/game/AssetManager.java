package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.net.HttpStatus;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AssetManager {

    public static Texture imgFondo, imgCuadrado, mapaCastillo, mapaMazmorra, persona;

    public static TextureRegion background;

    public static Color grisTransparente;

    public static BitmapFont font;

    public static TiledMap tiledMazmorra, tiledCastillo;

    public static Texture spritesheet_joc_dreta;
    public static Texture spritesheet_joc_avall;
    public static Texture spritesheet_joc_esquerra;
    public static Texture spritesheet_joc_amunt;

    public static TextureRegion jugador_dreta;
    public static Sprite jugadorSprite_dreta;
    public static TextureRegion jugador_avall;
    public static Sprite jugadorSprite_avall;
    public static TextureRegion jugador_esquerra;
    public static Sprite jugadorSprite_esquerra;
    public static TextureRegion jugador_amunt;
    public static Sprite jugadorSprite_amunt;

    public static ArrayList<String> noms_mapes;
    public static ArrayList<Texture> imatges_mapes;


    public static Music music;

    public static float volumen = 1f;

    public static void load() {

        noms_mapes = new ArrayList<String>();
        imatges_mapes = new ArrayList<Texture>();

        //noms_mapes.add("castillo");
        //noms_mapes.add("mazmorra");

        // Crear un semáforo para sincronizar el proceso de descompresión
        final Semaphore semaphore = new Semaphore(0);

        // Create an HTTP request
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);

        // Construct the URL with query parameters
        String url = "http://r6pixel.duckdns.org:3169/getAssets";
        httpRequest.setUrl(url);
        httpRequest.setHeader("Content-Type", "application/json");

        // Send the HTTP request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode() == 200) {
                    JSONObject body = new JSONObject();
                    body.put("directory","mapas");
                    fetchAndSetAssets(body.toString());
                } else {
                    // If the request failed, handle the error
                    System.out.println("HTTP request failed with status code: " + status.getStatusCode());
                }
                semaphore.release(); // Liberar el semáforo después de que el proceso de descompresión haya terminado
            }

            @Override
            public void failed(Throwable t) {
                // Handle the case where the HTTP request failed
                t.printStackTrace();
                semaphore.release(); // Liberar el semáforo después de que el proceso de descompresión haya terminado
            }

            @Override
            public void cancelled() {
                // Handle the case where the HTTP request was cancelled
                semaphore.release(); // Liberar el semáforo después de que el proceso de descompresión haya terminado
            }
        });

        // Esperar hasta que la descompresión haya terminado
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        imgFondo = new Texture(Gdx.files.internal("fondo2.jpg"));
        imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

        background = new TextureRegion(imgFondo);
        */

        //MAPAS
        //mapaCastillo = new Texture(Gdx.files.internal("mapas/mapes/IMGmapas/mapacastillo.jpg"));
        //mapaMazmorra = new Texture(Gdx.files.internal("mapas/mapes/IMGmapas/mapamazmorra.jpg"));







        grisTransparente = new Color(115/255f,115/255f,115/255f, 150/255f);
        Pixmap pixmapgrisTransparente = new Pixmap(500, 400, Pixmap.Format.RGBA8888);
        pixmapgrisTransparente.setColor(grisTransparente);
        pixmapgrisTransparente.fill();
        imgCuadrado = new Texture(pixmapgrisTransparente);

        tiledMazmorra = new TmxMapLoader().load("mapas/mapes/mapaMasmorra/mapaMazmorra.tmx");
        tiledCastillo = new TmxMapLoader().load("mapas/mapes/mapaCastell/mapaCastell.tmx");

        spritesheet_joc_dreta = new Texture(Gdx.files.internal("swat_sprite_dreta.png"));
        spritesheet_joc_dreta.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_avall = new Texture(Gdx.files.internal("swat_sprite_avall.png"));
        spritesheet_joc_avall.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_esquerra = new Texture(Gdx.files.internal("swat_sprite_esquerra.png"));
        spritesheet_joc_esquerra.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        spritesheet_joc_amunt = new Texture(Gdx.files.internal("swat_sprite_amunt.png"));
        spritesheet_joc_amunt.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        jugador_dreta = new TextureRegion(spritesheet_joc_dreta, 47, 8, 31, 23);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_dreta = new Sprite(jugador_dreta);

        jugador_avall = new TextureRegion(spritesheet_joc_avall, 426, 42, 31, 38);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_avall = new Sprite(jugador_avall);
        jugador_esquerra = new TextureRegion(spritesheet_joc_esquerra, 485, 427, 36, 30);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_esquerra = new Sprite(jugador_esquerra);
        jugador_amunt = new TextureRegion(spritesheet_joc_amunt, 7, 484, 26, 38);
        // Crear una nueva Sprite con la región de textura
        jugadorSprite_amunt = new Sprite(jugador_amunt);

        FileHandle fontFile = Gdx.files.internal("default.fnt"); // Assuming your font is in the "fonts" folder
        font = new BitmapFont(fontFile);


        persona = new Texture(Gdx.files.internal("persona.jpg"));

        /******************************* MUSICA *************************************/

        music = Gdx.audio.newMusic(Gdx.files.internal("musica_ambiente1.mp3"));
        music.setVolume(volumen);
        music.setLooping(true);


    }

    public static void fetchAndSetAssets(String bodyData) {



        // Create a POST request to fetch the image
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
        httpRequest.setUrl("http://r6pixel.duckdns.org:3169/getAssets_post/");
        httpRequest.setHeader("Content-Type", "application/json");

        // Set the body data if needed
        if (bodyData != null && !bodyData.isEmpty()) {
            httpRequest.setContent(bodyData);
        }

        // Send the request
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                byte[] zipData = httpResponse.getResult();

                // Descomprimir el archivo zip
                try {
                    // Crear un InputStream desde los datos del archivo zip
                    InputStream inputStream = new ByteArrayInputStream(zipData);
                    ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                    ZipEntry zipEntry = zipInputStream.getNextEntry();

                    while (zipEntry != null) {
                        String entryName = zipEntry.getName();
                        FileHandle newFile = Gdx.files.local(entryName); // Usar Gdx.files.local para obtener una ruta al directorio de almacenamiento interno

                        if (zipEntry.isDirectory()) {
                            // Si la entrada es un directorio, crea el directorio en la ruta correspondiente
                            newFile.mkdirs();
                        } else {
                            // Si la entrada es un archivo, escribe los datos en el archivo correspondiente
                            newFile.parent().mkdirs(); // Asegúrate de que los directorios padres existan
                            FileOutputStream fileOutputStream = new FileOutputStream(newFile.file());
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zipInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, length);
                            }
                            fileOutputStream.close();
                        }
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    zipInputStream.close();
                    System.out.println("Archivos descomprimidos correctamente en la carpeta 'assets'");
                    // Cargar la textura mapaCastillo después de que la descompresión haya terminado
                    Gdx.app.postRunnable(() -> {

                            FileHandle mapasDir = Gdx.files.local("mapas");
                            if (mapasDir.exists() && mapasDir.isDirectory()) {
                                FileHandle[] subdirs = mapasDir.list();
                                for (FileHandle subdir : subdirs) {
                                    if (subdir.isDirectory()) {
                                        noms_mapes.add(subdir.name());

                                        // Obtener el archivo "mapa" + nombre_mapa + ".jpg"
                                        FileHandle mapaFile = subdir.child("mapa" + subdir.name() + ".jpg");
                                        if (mapaFile.exists()) {
                                            // Hacer algo con el archivo mapaFile, como cargarlo o almacenarlo
                                            System.out.println("Archivo encontrado: " + mapaFile.path());
                                            Texture imatge_mapa = new Texture(Gdx.files.local(mapaFile.path()));
                                            imatges_mapes.add(imatge_mapa);
                                        } else {
                                            System.out.println("Archivo no encontrado para el directorio: " + subdir.name());
                                        }
                                    }
                                }
                            }
                            for (int i = 0;i<noms_mapes.size();i++) {
                                System.out.println("mapa: " + noms_mapes.get(i));
                            }

                            imgFondo = new Texture(Gdx.files.internal("fondo2.jpg"));
                            imgFondo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);

                            background = new TextureRegion(imgFondo);


                    });

                } catch (IOException e) {
                    System.out.println("Error al descomprimir el archivo zip: " + e.getMessage());
                }

            }

            @Override
            public void failed(Throwable t) {
                // Handle the failure of the HTTP request
                Gdx.app.error("ImageFetch", "Failed to fetch image: " + t.getMessage());
            }

            @Override
            public void cancelled() {
                // Handle the cancellation of the HTTP request
                Gdx.app.log("ImageFetch", "Image fetch request cancelled");
            }
        });


    }




    public static void dispose() {
        imgFondo.dispose();
        music.dispose();
    }
}
