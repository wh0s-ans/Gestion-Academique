package controllers;

import interfaces.IRecherchable;
import models.Etudiant;
import models.Module;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe GestionEtudiants
 * Controleur principal - gere la liste des etudiants et la persistance CSV.
 */
public class GestionEtudiants implements IRecherchable<Etudiant> {

    private List<Etudiant> etudiants;
    private static final String CSV_FILE = "data/etudiants.csv";

    public GestionEtudiants() {
        this.etudiants = new ArrayList<>();
        new File("data").mkdirs();
        chargerCSV();
    }

    // CRUD Etudiants
    public void ajouterEtudiant(Etudiant e) {
        etudiants.add(e);
        sauvegarderCSV();
    }

    public void supprimerEtudiant(String numeroEtudiant) {
        etudiants.removeIf(e -> e.getNumeroEtudiant().equals(numeroEtudiant));
        sauvegarderCSV();
    }

    public void modifierEtudiant(Etudiant modifie) {
        for (int i = 0; i < etudiants.size(); i++) {
            if (etudiants.get(i).getNumeroEtudiant().equals(modifie.getNumeroEtudiant())) {
                etudiants.set(i, modifie);
                break;
            }
        }
        sauvegarderCSV();
    }

    public List<Etudiant> getTousEtudiants() { return etudiants; }

    // IRecherchable
    @Override
    public List<Etudiant> rechercherParNom(String nom) {
        String q = nom.toLowerCase();
        return etudiants.stream()
                .filter(e -> e.getNom().toLowerCase().contains(q)
                        || e.getPrenom().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    @Override
    public Etudiant rechercherParId(String id) {
        return etudiants.stream()
                .filter(e -> e.getNumeroEtudiant().equals(id))
                .findFirst().orElse(null);
    }

    // Statistiques promotion
    public double getMoyennePromotion() {
        if (etudiants.isEmpty()) return 0;
        return etudiants.stream()
                .mapToDouble(Etudiant::calculerMoyenne)
                .average().orElse(0);
    }

    public long getNombreAdmis() {
        return etudiants.stream().filter(Etudiant::estAdmis).count();
    }

    public long getNombreAjournes() {
        return etudiants.size() - getNombreAdmis();
    }

    public Etudiant getMeilleurEtudiant() {
        return etudiants.stream()
                .filter(e -> !e.getModules().isEmpty())
                .max((a, b) -> Double.compare(a.calculerMoyenne(), b.calculerMoyenne()))
                .orElse(null);
    }

    // Persistance CSV
    public void sauvegarderCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
            for (Etudiant e : etudiants) {
                pw.print(e.toCSV());
            }
        } catch (IOException ex) {
            System.err.println("Erreur sauvegarde CSV: " + ex.getMessage());
        }
    }

    public void chargerCSV() {
        File f = new File(CSV_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            Etudiant current = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("MODULE") && current != null) {
                    // MODULE,numeroEtudiant,nom,note,coeff
                    if (parts.length >= 5) {
                        current.ajouterModule(new Module(
                                parts[2],
                                Double.parseDouble(parts[3]),
                                Integer.parseInt(parts[4])
                        ));
                    }
                } else if (parts.length >= 6) {
                    // nom,prenom,numero,filiere,semestre,moyenne,statut
                    current = new Etudiant(
                            parts[0], parts[1], parts[2],
                            parts[3], Integer.parseInt(parts[4])
                    );
                    etudiants.add(current);
                }
            }
        } catch (IOException ex) {
            System.err.println("Erreur chargement CSV: " + ex.getMessage());
        }
    }

    // Generer numero etudiant auto
    public String genererNumero() {
        return String.format("ET-%04d", etudiants.size() + 1);
    }
}
