package com.bz.fichepresence.entities;

/**
 * Created by Rog on 10/04/2018.
 */

public class Personne {
    private int id;
    private String nom;
    private String matricule;
    private String arrive;
    private int present;
    private String img;
    public Personne() {
    }

    public Personne(String nom, String matricule) {
        this.nom =nom;
        this.matricule =matricule;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getArrive() {
        return arrive;
    }

    public int getPresent() {
        return present;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", matricule='" + matricule + '\'' +
                ", arrive='" + arrive + '\'' +
                ", present=" + present +
                ", img='" + img + '\'' +
                '}';
    }
}
