package fr.dev.alphabet.neuronal;

import fr.dev.alphabet.model.Image;
import fr.dev.alphabet.model.ReseauNeuronal;
import fr.dev.alphabet.utils.ImageUtil;
import fr.dev.alphabet.utils.LogUtil;
import fr.dev.alphabet.utils.SerializationUtil;
import lombok.SneakyThrows;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;

@CommandLine.Command(name = "taux", description = "Calcule le taux de réussite du réseau")
public class TauxCommand implements Runnable {

    @CommandLine.Option(names = { "-r" }, description = "Nom du réseau", required = true)
    String reseau;

    @SneakyThrows
    @Override
    public void run() {
        ReseauNeuronal reseauNeuronal = SerializationUtil.readReseau(reseau);
        LogUtil.out(
                "Calcul du taux pour le réseau " + reseauNeuronal.getNom() + " de génération " + reseauNeuronal.getGeneration());
        List<Image> datasetImages = ImageUtil.getAllDatasetImages();
        double cout = 0d;
        for (Image image : datasetImages) {
            BufferedImage img = ImageIO.read(image.getImageFile());
            cout += reseauNeuronal.calculTauxImage(img, image.getResultatAttendu());
        }
        LogUtil.out("Le taux de réussite du réseau est " + String.format("%,.5f",
                                                                         cout * 100d / (1d * datasetImages.size())) + "%");
    }
}
