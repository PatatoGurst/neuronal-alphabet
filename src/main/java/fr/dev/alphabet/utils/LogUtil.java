package fr.dev.alphabet.utils;

public class LogUtil {

    private LogUtil() {

    }

    public static void out(String log) {
        System.out.println(log);
    }

    public static void err(String log) {
        System.err.println(log);
    }
}
