package fr.dev.alphabet.model;

import lombok.Getter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Image {
    private static final Pattern PATTERN = Pattern.compile("[0-9a-zA-Z]+_([a-z])\\.png");

    private final File imageFile;
    private final String resultatAttendu;

    public Image(String nom) {
        imageFile = new File("dataset/" + nom);
        resultatAttendu = calculeResultatAttendu(nom);
    }

    public Image(File f) {
        imageFile = f;
        resultatAttendu = calculeResultatAttendu(f.getName());
    }

    private String calculeResultatAttendu(String nom) {
        Matcher m = PATTERN.matcher(nom);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }
}
