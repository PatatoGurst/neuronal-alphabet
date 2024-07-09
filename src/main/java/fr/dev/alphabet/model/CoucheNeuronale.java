package fr.dev.alphabet.model;

import fr.dev.alphabet.utils.MathUtil;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CoucheNeuronale {
    private final int tailleEntree;
    private final int tailleSortie;
    private double[][] poids;
    private double[] seuils;

    public CoucheNeuronale(int tailleEntree, int tailleSortie) {
        this.tailleEntree = tailleEntree;
        this.tailleSortie = tailleSortie;
        this.poids = new double[tailleSortie][tailleEntree];
        this.seuils = new double[tailleSortie];
        for (int i = 0; i < tailleSortie; i++) {
            for (int j = 0; j < tailleEntree; j++) {
                poids[i][j] = 10 * Math.random() - 5;
            }
        }
        for (int i = 0; i < tailleSortie; i++) {
            seuils[i] = 10 * Math.random() - 5;
        }
    }

    public CoucheNeuronale(int tailleEntree, int tailleSortie, double[][] poids, double[] seuils) {
        this.tailleEntree = tailleEntree;
        this.tailleSortie = tailleSortie;
        this.poids = poids;
        this.seuils = seuils;
    }

    public double[] calculsCouche(double[] valeurs) {
        double[] values = new double[tailleSortie];
        for (int i = 0; i < tailleSortie; i++) {
            double sum = 0d;
            for (int j = 0; j < tailleEntree; j++) {
                sum += valeurs[j] * poids[i][j];
            }
            sum += seuils[i];
            values[i] = MathUtil.fonctionActivation(sum);
        }
        return values;
    }

    public void wipe() {
        for (int i = 0; i < tailleSortie; i++) {
            for (int j = 0; j < tailleEntree; j++) {
                poids[i][j] = 0;
            }
        }
        for (int i = 0; i < tailleSortie; i++) {
            seuils[i] = 0;
        }
    }

    public void addDiff(CoucheNeuronale diff, int ponderation) {
        for (int i = 0; i < tailleSortie; i++) {
            for (int j = 0; j < tailleEntree; j++) {
                poids[i][j] += diff.getPoids()[i][j] / (1d * ponderation);
            }
        }
        for (int i = 0; i < tailleSortie; i++) {
            seuils[i] += diff.getSeuils()[i] / (1d * ponderation);
        }
    }

    public double[] calculsBeforeActivationCouche(double[] valeurs) {
        double[] values = new double[tailleSortie];
        for (int i = 0; i < tailleSortie; i++) {
            double sum = 0d;
            for (int j = 0; j < tailleEntree; j++) {
                sum += valeurs[j] * poids[i][j];
            }
            sum += seuils[i];
            values[i] = sum;
        }
        return values;
    }
}
