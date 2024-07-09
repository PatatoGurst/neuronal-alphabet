package fr.dev.alphabet.model;

import fr.dev.alphabet.utils.LogUtil;
import fr.dev.alphabet.utils.MathUtil;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReseauNeuronal {
    private static final String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                                               "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    private static final double FACTEUR_APPRENDISSAGE = 0.25d;
    private final String nom;
    private final int generation;
    final List<CoucheNeuronale> couches;

    public ReseauNeuronal(String nom, int generation) {
        this.nom = nom;
        this.generation = generation;
        this.couches = new ArrayList<>();
        this.couches.add(new CoucheNeuronale(400, 20));
        this.couches.add(new CoucheNeuronale(20, 10));
        this.couches.add(new CoucheNeuronale(10, 26));
    }

    public ReseauNeuronal(String nom, int generation, List<CoucheNeuronale> couches) {
        this.nom = nom;
        this.generation = generation;
        this.couches = couches;
    }

    public ReseauNeuronal(ReseauNeuronal reference, List<ReseauNeuronal> diffs, int ponderation) {
        List<CoucheNeuronale> couchesEntrainees = reference.getCouches();
        for (ReseauNeuronal d : diffs) {
            for (int i = 0; i < 3; i++) {
                couchesEntrainees.get(i).addDiff(d.getCouches().get(i), ponderation);
            }
        }
        this.nom = reference.getNom();
        this.generation = reference.getGeneration() + 1;
        this.couches = couchesEntrainees;
    }

    public ReseauNeuronal(ReseauNeuronal reference, ReseauNeuronal d, int ponderation) {
        List<CoucheNeuronale> couchesEntrainees = reference.getCouches();
        for (int i = 0; i < 3; i++) {
            couchesEntrainees.get(i).addDiff(d.getCouches().get(i), ponderation);
        }
        this.nom = reference.getNom();
        this.generation = reference.getGeneration();
        this.couches = couchesEntrainees;
    }

    public double[] calculResultatImage(BufferedImage img, String result, boolean displayLog) {
        double[] values = getImageDoubleValues(img);
        for (CoucheNeuronale couche : couches) {
            values = couche.calculsCouche(values);
        }
        if (displayLog) {
            for (int i = 0; i < 26; i++) {
                LogUtil.out(alphabet[i] + " : " + String.format("%,.5f", values[i]) + (alphabet[i].equals(result)
                        ? " réponse"
                        : ""));
            }
        }
        return values;
    }

    private static double[] getImageDoubleValues(BufferedImage img) {
        double[] values = new double[400];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int grayScale = img.getRGB(j, i) & 0xFF;
                values[20 * i + j] = grayScale / 255.0;
            }
        }
        return values;
    }

    public double calculCoutImage(BufferedImage img, String result) {
        double[] values = calculResultatImage(img, result, false);
        double cout = 0;
        for (int i = 0; i < values.length; i++) {
            double valueToMatch = alphabet[i].equals(result) ? 1d : 0d;
            cout += MathUtil.fonctionCout(values[i], valueToMatch);
        }
        return cout;
    }

    public double calculTauxImage(BufferedImage img, String result) {
        double[] values = calculResultatImage(img, result, false);
        double cout = 0;
        double tmp = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] > tmp) {
                tmp = values[i];
                cout = alphabet[i].equals(result) ? 1d : 0d;
            }
        }
        return cout;
    }

    public ReseauNeuronal calculDiffReseau(BufferedImage img, String result) {
        double[] values = getImageDoubleValues(img);
        List<double[]> valuesCouches = new ArrayList<>();
        List<double[]> beforeActivationCouches = new ArrayList<>();
        List<double[]> deltaCouches = new ArrayList<>();
        valuesCouches.add(values);
        for (CoucheNeuronale couche : couches) {
            beforeActivationCouches.add(couche.calculsBeforeActivationCouche(values));
            values = couche.calculsCouche(values);
            valuesCouches.add(values);
            deltaCouches.add(new double[couche.getTailleSortie()]);
        }

        calculerDeltaCoucheFinale(deltaCouches.get(2), beforeActivationCouches.get(2), valuesCouches.get(3), result);
        calculerDeltaCoucheIntermediaire(deltaCouches, beforeActivationCouches, 1);
        calculerDeltaCoucheIntermediaire(deltaCouches, beforeActivationCouches, 0);

        List<CoucheNeuronale> diffCouches = new ArrayList<>();
        CoucheNeuronale diffCouche2 = getDiffCoucheNeuronale(valuesCouches, deltaCouches, 2);
        CoucheNeuronale diffCouche1 = getDiffCoucheNeuronale(valuesCouches, deltaCouches, 1);
        CoucheNeuronale diffCouche0 = getDiffCoucheNeuronale(valuesCouches, deltaCouches, 0);
        diffCouches.add(diffCouche0);
        diffCouches.add(diffCouche1);
        diffCouches.add(diffCouche2);
        return new ReseauNeuronal("tmp", 1, diffCouches);
    }

    private void calculerDeltaCoucheIntermediaire(List<double[]> deltaCouches, List<double[]> beforeActivation,
                                                  int index) {
        double[] delta = deltaCouches.get(index);
        double[] deltaP1 = deltaCouches.get(index + 1);
        double[][] poidsP1 = couches.get(index + 1).getPoids();

        for (int i = 0; i < delta.length; i++) {
            double sum = 0;
            for (int j = 0; j < deltaP1.length; j++) {
                sum += poidsP1[j][i] + deltaP1[j];
            }
            delta[i] = MathUtil.deriveeActivation(beforeActivation.get(index)[i]) * sum;
        }
    }

    private void calculerDeltaCoucheFinale(double[] deltas, double[] beforeActivation, double[] values, String result) {
        for (int i = 0; i < values.length; i++) {
            double cible = alphabet[i].equals(result) ? 1d : 0d;
            deltas[i] = MathUtil.deriveeActivation(beforeActivation[i]) * MathUtil.deriveeCout(values[i], cible);
        }
    }

    private CoucheNeuronale getDiffCoucheNeuronale(List<double[]> valuesCouches, List<double[]> deltas,
                                                   int indexCouche) {
        CoucheNeuronale couche = couches.get(indexCouche);
        double[] delta = deltas.get(indexCouche);
        double[] valuesCouche = valuesCouches.get(indexCouche);
        double[] diffSeuilsCouche = new double[couche.getTailleSortie()];
        double[][] diffPoidsCouche = new double[couche.getTailleSortie()][couche.getTailleEntree()];
        for (int i = 0; i < couche.getTailleSortie(); i++) {
            diffSeuilsCouche[i] = - delta[i] * FACTEUR_APPRENDISSAGE;
            for (int j = 0; j < couche.getTailleEntree(); j++) {
                diffPoidsCouche[i][j] = - delta[i] * valuesCouche[j] * FACTEUR_APPRENDISSAGE;
            }
        }
        return new CoucheNeuronale(couche.getTailleEntree(), couche.getTailleSortie(), diffPoidsCouche,
                                   diffSeuilsCouche);
    }

    public void wipe() {
        for (CoucheNeuronale couche : couches) {
            couche.wipe();
        }
    }

}
