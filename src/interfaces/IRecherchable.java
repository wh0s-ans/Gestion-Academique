package interfaces;

import java.util.List;

/**
 * Interface IRecherchable
 * Contrat pour les classes qui supportent la recherche.
 */
public interface IRecherchable<T> {
    /**
     * Recherche par nom (partiel ou complet)
     * @param nom le nom a rechercher
     * @return liste des resultats
     */
    List<T> rechercherParNom(String nom);

    /**
     * Recherche par identifiant unique
     * @param id l'identifiant a rechercher
     * @return l'element trouve ou null
     */
    T rechercherParId(String id);
}
