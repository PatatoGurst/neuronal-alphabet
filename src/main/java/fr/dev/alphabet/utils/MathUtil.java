package fr.dev.alphabet.utils;

public class MathUtil {

    private MathUtil() {

    }

    public static double fonctionActivation(double entree) {
        return 1d / (1d + Math.exp(- 1d * entree));
    }

    public static double deriveeActivation(double entree) {
        return fonctionActivation(entree) * (1d - fonctionActivation(entree));
    }

    public static double fonctionCout(double entree, double cible) {
        return Math.pow(cible - entree, 2);
    }

    public static double deriveeCout(double entree, double cible) {
        return 2d * (entree - cible);
    }

}
