package models;

import interfaces.ICalculable;
import interfaces.IAffichable;
import interfaces.IExportable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Etudiant
 * Represente un etudiant avec ses modules.
 */
public class Etudiant implements ICalculable, IAffichable, IExportable {

    private String nom;
    private String prenom;
    private String numeroEtudiant;
    private String filiere;
    private int semestre;
    private List<Module> modules;

    public Etudiant(String nom, String prenom, String numeroEtudiant, String filiere, int semestre) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroEtudiant = numeroEtudiant;
        this.filiere = filiere;
        this.semestre = semestre;
        this.modules = new ArrayList<>();
    }

    // Gestion modules
    public void ajouterModule(Module m) { modules.add(m); }
    public void supprimerModule(Module m) { modules.remove(m); }
    public void supprimerTousModules() { modules.clear(); }
    public List<Module> getModules() { return modules; }

    // ICalculable
    @Override
    public double calculerMoyenne() {
        if (modules.isEmpty()) return 0.0;
        double totalPoints = 0;
        int totalCoeff = 0;
        for (Module m : modules) {
            totalPoints += m.getNote() * m.getCoefficient();
            totalCoeff += m.getCoefficient();
        }
        return totalCoeff == 0 ? 0 : totalPoints / totalCoeff;
    }

    @Override
    public String getMeilleurModule() {
        if (modules.isEmpty()) return "Aucun";
        Module best = modules.get(0);
        for (Module m : modules) if (m.getNote() > best.getNote()) best = m;
        return best.getNom();
    }

    @Override
    public String getPireModule() {
        if (modules.isEmpty()) return "Aucun";
        Module worst = modules.get(0);
        for (Module m : modules) if (m.getNote() < worst.getNote()) worst = m;
        return worst.getNom();
    }

    @Override
    public boolean estAdmis() { return calculerMoyenne() >= 10.0; }

    // IAffichable
    @Override
    public void afficherInfos() { System.out.println(genererReleve()); }

    @Override
    public String genererReleve() {
        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("              RELEVE DE NOTES OFFICIEL\n");
        sb.append("============================================================\n");
        sb.append(String.format("Etudiant  : %s %s\n", prenom, nom));
        sb.append(String.format("Numero    : %s\n", numeroEtudiant));
        sb.append(String.format("Filiere   : %s\n", filiere));
        sb.append(String.format("Semestre  : S%d\n", semestre));
        sb.append("------------------------------------------------------------\n");
        if (modules.isEmpty()) {
            sb.append("Aucun module enregistre.\n");
        } else {
            for (Module m : modules) sb.append(m.genererReleve()).append("\n");
        }
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("Moyenne Generale : %.2f/20\n", calculerMoyenne()));
        sb.append(String.format("Mention          : %s\n", getMention()));
        sb.append(String.format("Statut           : %s\n", estAdmis() ? "ADMIS" : "AJOURNE"));
        sb.append("============================================================\n");
        return sb.toString();
    }

    // IExportable
    @Override
    public boolean exporterTxt(String cheminFichier) {
        try (FileWriter fw = new FileWriter(cheminFichier)) {
            fw.write(genererReleve());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s,%s,%s,%s,%d,%.2f,%s\n",
                nom, prenom, numeroEtudiant, filiere, semestre,
                calculerMoyenne(), estAdmis() ? "Admis" : "Ajourne"));
        for (Module m : modules) {
            sb.append(String.format("MODULE,%s,%s,%.2f,%d\n",
                    numeroEtudiant, m.getNom(), m.getNote(), m.getCoefficient()));
        }
        return sb.toString();
    }

    // Getters & Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNumeroEtudiant() { return numeroEtudiant; }
    public void setNumeroEtudiant(String n) { this.numeroEtudiant = n; }
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public String getMention() {
        double moy = calculerMoyenne();
        if (moy >= 16) return "Tres Bien";
        if (moy >= 14) return "Bien";
        if (moy >= 12) return "Assez Bien";
        if (moy >= 10) return "Passable";
        return "Insuffisant";
    }

    @Override
    public String toString() { return prenom + " " + nom + " (" + numeroEtudiant + ")"; }
}
