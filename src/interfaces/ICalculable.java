package interfaces;

/**
 * Interface ICalculable
 * Contrat pour les classes qui effectuent des calculs statistiques.
 */
public interface ICalculable {
    double calculerMoyenne();
    String getMeilleurModule();
    String getPireModule();
    boolean estAdmis();
}
