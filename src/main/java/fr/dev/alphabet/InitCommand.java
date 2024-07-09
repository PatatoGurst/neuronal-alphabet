package fr.dev.alphabet;

import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CommandLine.Command(name = "init", description = "Init de l'application")
public class InitCommand implements Runnable {

    @Override
    public void run() {
        try {
            List<String> dossiers = List.of("dataset", "reseaux");
            for (String dossier : dossiers) {
                Path path = Paths.get(dossier);
                if (Files.exists(path)) {
                    System.out.println("Le dossier " + dossier + " existe déjà");
                } else {
                    Files.createDirectories(path);
                    System.out.println("Le dossier " + dossier + " a été créé");
                }
            }

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }
}
