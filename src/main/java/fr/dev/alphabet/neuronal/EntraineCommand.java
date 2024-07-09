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
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "entraine", description = "Entrainement du réseau à partir des dataset existants")
public class EntraineCommand implements Runnable {

    @CommandLine.Option(names = { "-r" }, description = "Nom du réseau", required = true)
    String reseau;

    @CommandLine.Option(names = { "-g" }, description = "Nombre de générations", defaultValue = "1")
    Integer generations;

    @SneakyThrows
    @Override
    public void run() {
        ReseauNeuronal reseauNeuronal = SerializationUtil.readReseau(reseau);
        LogUtil.out(
                "Le réseau " + reseauNeuronal.getNom() + " de génération " + reseauNeuronal.getGeneration() + " va s'entrainer");
        for (int i = 0; i < generations; i++) {
            List<Image> datasetImages = ImageUtil.getAllDatasetImages();
            List<ReseauNeuronal> diffReseaux = new ArrayList<>();
            for (Image image : datasetImages) {
                BufferedImage img = ImageIO.read(image.getImageFile());
                ReseauNeuronal diffR = reseauNeuronal.calculDiffReseau(img, image.getResultatAttendu());
                diffReseaux.add(diffR);
                reseauNeuronal = new ReseauNeuronal(reseauNeuronal, diffR, datasetImages.size());
            }
            reseauNeuronal = new ReseauNeuronal(reseauNeuronal.getNom(), reseauNeuronal.getGeneration() + 1,
                                                reseauNeuronal.getCouches());
            // double cout = 0d;
            double taux = 0d;
            for (Image image : datasetImages) {
                BufferedImage img = ImageIO.read(image.getImageFile());
                taux += reseauNeuronal.calculTauxImage(img, image.getResultatAttendu());
                // cout += reseauNeuronal.calculCoutImage(img, image.getResultatAttendu());
            }
            LogUtil.out("Le taux de réussite du réseau est " + String.format("%,.5f",
                                                                             taux * 100d / (1d * datasetImages.size())) + "%");
            // LogUtil.out("Le cout du réseau est " + cout);
            SerializationUtil.saveReseau(reseauNeuronal);
        }

    }
}
