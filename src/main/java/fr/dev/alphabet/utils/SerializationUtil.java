package fr.dev.alphabet.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.dev.alphabet.model.ReseauNeuronal;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class SerializationUtil {

    private SerializationUtil() {
    }

    public static void saveReseau(ReseauNeuronal r) {
        String pathString = "reseaux/" + r.getNom() + "_" + r.getGeneration() + ".json";
        try (Writer writer = new FileWriter(pathString)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(r, writer);
            LogUtil.out("Le réseau " + r.getNom() + " de génération " + r.getGeneration() + " a été créé");
        } catch (Exception e) {
            LogUtil.err("Erreur lors de la sauvegarde du réseau neuronal : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ReseauNeuronal readReseau(String nom) {
        try (Reader reader = new FileReader("reseaux/" + nom + ".json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, ReseauNeuronal.class);
        } catch (Exception e) {
            LogUtil.err("Erreur lors de la sauvegarde du réseau neuronal : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
