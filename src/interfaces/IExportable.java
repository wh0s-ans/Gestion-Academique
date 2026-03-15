package interfaces;

/**
 * Interface IExportable
 * Contrat pour les classes qui peuvent exporter leurs donnees.
 */
public interface IExportable {
    /**
     * Exporte les donnees vers un fichier .txt
     * @param cheminFichier le chemin du fichier de destination
     * @return true si l'export a reussi, false sinon
     */
    boolean exporterTxt(String cheminFichier);

    /**
     * Convertit les donnees en format CSV
     * @return la representation CSV
     */
    String toCSV();
}
