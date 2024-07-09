package fr.dev.alphabet.neuronal;

import fr.dev.alphabet.model.Image;
import fr.dev.alphabet.model.ReseauNeuronal;
import fr.dev.alphabet.utils.SerializationUtil;
import lombok.SneakyThrows;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@CommandLine.Command(name = "calcul", description = "Calcule le résultat du réseau par rapport à l'image")
public class CalculCommand implements Runnable {

    @CommandLine.Option(names = { "-r" }, description = "Nom du réseau", required = true)
    String reseau;

    @CommandLine.Option(names = { "-i" }, description = "Nom de l'image", required = true)
    String image;

    @SneakyThrows
    @Override
    public void run() {
        ReseauNeuronal reseauNeuronal = SerializationUtil.readReseau(reseau);
        Image i = new Image(new File("dataset/" + image));
        BufferedImage img = ImageIO.read(i.getImageFile());
        reseauNeuronal.calculResultatImage(img, i.getResultatAttendu(), true);
    }
}
