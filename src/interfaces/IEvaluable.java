package interfaces;

/**
 * Interface IEvaluable
 * Contrat pour les entites qui peuvent etre evaluees.
 */
public interface IEvaluable {
    double getNote();
    void setNote(double note);
    String getMention();
}
