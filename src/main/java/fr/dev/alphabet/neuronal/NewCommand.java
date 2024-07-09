package fr.dev.alphabet.neuronal;

import fr.dev.alphabet.model.ReseauNeuronal;
import fr.dev.alphabet.utils.SerializationUtil;
import picocli.CommandLine;

@CommandLine.Command(name = "new", description = "Crée un nouveau réseau neuronal")
public class NewCommand implements Runnable {

    @CommandLine.Option(names = { "-r" }, description = "Nom du réseau", required = true)
    String name;

    @Override
    public void run() {
        ReseauNeuronal reseauNeuronal = new ReseauNeuronal(name, 1);
        SerializationUtil.saveReseau(reseauNeuronal);
    }
}
