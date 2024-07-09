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

@CommandLine.Command(name = "cout", description = "Calcule le cout du réseau par rapport aux dataset")
public class CoutCommand implements Runnable {

    @CommandLine.Option(names = { "-r" }, description = "Nom du réseau", required = true)
    String reseau;

    @SneakyThrows
    @Override
    public void run() {
        ReseauNeuronal reseauNeuronal = SerializationUtil.readReseau(reseau);
        LogUtil.out(
                "Calcul du cout pour le réseau " + reseauNeuronal.getNom() + " de génération " + reseauNeuronal.getGeneration());
        List<Image> datasetImages = ImageUtil.getAllDatasetImages();
        double cout = 0d;
        for (Image image : datasetImages) {
            BufferedImage img = ImageIO.read(image.getImageFile());
            cout += reseauNeuronal.calculCoutImage(img, image.getResultatAttendu());
        }
        LogUtil.out("Le cout du réseau est " + cout);
    }
}
