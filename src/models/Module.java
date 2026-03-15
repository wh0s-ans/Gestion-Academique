package models;

import interfaces.IEvaluable;
import interfaces.IAffichable;

/**
 * Classe Module
 * Represente un module universitaire avec sa note et son coefficient.
 */
public class Module implements IEvaluable, IAffichable {

    private String nom;
    private double note;
    private int coefficient;

    public Module(String nom, double note, int coefficient) {
        this.nom = nom;
        this.note = note;
        this.coefficient = coefficient;
    }

    // IEvaluable
    @Override
    public double getNote() { return note; }

    @Override
    public void setNote(double note) {
        if (note < 0 || note > 20)
            throw new IllegalArgumentException("Note entre 0 et 20.");
        this.note = note;
    }

    @Override
    public String getMention() {
        if (note >= 16) return "Tres Bien";
        if (note >= 14) return "Bien";
        if (note >= 12) return "Assez Bien";
        if (note >= 10) return "Passable";
        return "Insuffisant";
    }

    // IAffichable
    @Override
    public void afficherInfos() { System.out.println(genererReleve()); }

    @Override
    public String genererReleve() {
        return String.format("%-28s | %5.2f/20 | Coeff: %d | %s",
                nom, note, coefficient, getMention());
    }

    // Getters & Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getCoefficient() { return coefficient; }
    public void setCoefficient(int c) { this.coefficient = c; }

    @Override
    public String toString() { return nom; }
}
