package fr.dev.alphabet.utils;

import fr.dev.alphabet.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

public class ImageUtil {

    public static void writeImage(String fileName, BufferedImage bi, String resourceFolder)
            throws URISyntaxException, IOException {
        File file = new File(resourceFolder + fileName);
        if (! file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(bi, "png", file);
    }

    public static List<Image> getAllDatasetImages() {
        File datasetDir = new File("dataset");
        File[] imageList = datasetDir.listFiles();
        if (imageList != null) {
            return Stream.of(imageList).map(Image::new).toList();
        } else {
            return List.of();
        }
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

}
