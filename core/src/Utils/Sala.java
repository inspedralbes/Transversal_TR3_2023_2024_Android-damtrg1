package Utils;

import java.util.ArrayList;

public class Sala {
    public static String id;
    ArrayList<String> users;

    ArrayList<String> usuarisAtacantes;

    ArrayList<String> usuarisDefensores;
    ArrayList<String> skinsAtacantes;

    ArrayList<String> skinsDefensores;

    String nombreMapa;

    public Sala(String id, String nombreMapa, ArrayList<String> atacantes, ArrayList<String> defensores, ArrayList<String> skinsAtacantes, ArrayList<String> skinsDefensores ) {
        this.id = id;
        this.nombreMapa = nombreMapa;
        this.usuarisAtacantes = atacantes;
        this.usuarisDefensores = defensores;
        this.skinsAtacantes = skinsAtacantes;
        this.skinsDefensores = skinsDefensores;
    }

    public Sala(String id,  ArrayList<String> atacantes, ArrayList<String> defensores) {
        this.id = id;
        this.usuarisAtacantes = atacantes;
        this.usuarisDefensores = defensores;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getNombreMapa() {
        return nombreMapa;
    }

    public void setNombreMapa(String nombreMapa) {
        this.nombreMapa = nombreMapa;
    }

    public ArrayList<String> getSkinsAtacantes() {
        return skinsAtacantes;
    }

    public ArrayList<String> getSkinsDefensores() {
        return skinsDefensores;
    }

    public ArrayList<String> getUsuarisAtacantes() {
        return usuarisAtacantes;
    }

    public ArrayList<String> getUsuarisDefensores() {
        return usuarisDefensores;
    }
}
