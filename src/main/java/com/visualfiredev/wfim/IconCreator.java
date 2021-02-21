package com.visualfiredev.wfim;

import de.codecentric.centerdevice.javafxsvg.BufferedImageTranscoder;
import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class IconCreator {

    private int[] pixelSizes = new int[] { 20, 24, 32, 40, 48, 64, 256 };
    private int[] sourceOffsetsX = new int[] { 9, 11, 13, 18, 21, 29, 117 };
    private int[] sourceOffsetsY = new int[] { 8, 9, 13, 15, 15, 25, 105 };
    private int[] sourceSizes = new int[] { 10, 12, 16, 20, 25, 32, 120 };
    private BufferedImage[] templatesBack = new BufferedImage[7];
    private BufferedImage[] templatesFront = new BufferedImage[7];
    private BufferedImage source;

    // Load Templates
    public void loadTemplates() throws IOException {
        // Load Backs
        templatesBack[0] = ImageIO.read(this.getClass().getResourceAsStream("/template/20x_1.png"));
        templatesBack[1] = ImageIO.read(this.getClass().getResourceAsStream("/template/24x_1.png"));
        templatesBack[2] = ImageIO.read(this.getClass().getResourceAsStream("/template/32x_1.png"));
        templatesBack[3] = ImageIO.read(this.getClass().getResourceAsStream("/template/40x_1.png"));
        templatesBack[4] = ImageIO.read(this.getClass().getResourceAsStream("/template/48x_1.png"));
        templatesBack[5] = ImageIO.read(this.getClass().getResourceAsStream("/template/64x_1.png"));
        templatesBack[6] = ImageIO.read(this.getClass().getResourceAsStream("/template/256x_1.png"));

        // Load Fronts
        templatesFront[0] = ImageIO.read(this.getClass().getResourceAsStream("/template/20x_2.png"));
        templatesFront[1] = ImageIO.read(this.getClass().getResourceAsStream("/template/24x_2.png"));
        templatesFront[2] = ImageIO.read(this.getClass().getResourceAsStream("/template/32x_2.png"));
        templatesFront[3] = ImageIO.read(this.getClass().getResourceAsStream("/template/40x_2.png"));
        templatesFront[4] = ImageIO.read(this.getClass().getResourceAsStream("/template/48x_2.png"));
        templatesFront[5] = ImageIO.read(this.getClass().getResourceAsStream("/template/64x_2.png"));
        templatesFront[6] = ImageIO.read(this.getClass().getResourceAsStream("/template/256x_2.png"));
    }

    // Load Source
    public void loadSource(File file) throws IOException, TranscoderException {
        if (file.getName().contains(".png")) {
            source = ImageIO.read(file); // TODO: Take CLI arguments
        } else if (file.getName().contains(".ico")) {
            // Load Icons
            List<BufferedImage> icons = ICODecoder.read(file);

            // Find Biggest Size
            source = icons.stream().max(Comparator.comparingInt(BufferedImage::getWidth)).get();
        } else if (file.getName().contains(".svg")) {
            TranscoderInput svg = new TranscoderInput(Files.newBufferedReader(file.toPath()));
            BufferedImageTranscoder transcoder = new BufferedImageTranscoder(BufferedImage.TYPE_INT_ARGB);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 256.0f);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 256.0f);
            transcoder.transcode(svg, null);
            source = transcoder.getBufferedImage();
        } else {
            throw new IOException("Unsupported file type!");
        }
    }

    // Create Icons
    public void createIcon() throws IOException {
        BufferedImage[] sizes = new BufferedImage[8];

        // Create 16x layer
        sizes[0] = sandwich(16, 16, 0, 0, null, source, null);
        ImageIO.write(sizes[0], "png", new File("./" + 16 + "x.png"));

        // Create The Rest
        for (int i = 0; i < 7; i++) {
            sizes[i + 1] = sandwich(pixelSizes[i], sourceSizes[i], sourceOffsetsX[i], sourceOffsetsY[i], templatesBack[i], source, templatesFront[i]);
            ImageIO.write(sizes[i + 1], "png", new File("./" + pixelSizes[i] + "x.png"));
        }

        // Write
        ICOEncoder.write(Arrays.asList(sizes), new File("./icon.ico"));
    }

    // Sandwiches the middle image between the two source images
    private BufferedImage sandwich(int pixelSize, int size, int offsetX, int offsetY, BufferedImage back, BufferedImage middle, BufferedImage front) {
        BufferedImage combined = new BufferedImage(pixelSize, pixelSize, BufferedImage.TYPE_INT_ARGB);

        // Draw Images
        Graphics2D g = combined.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        if (back != null) {
            g.drawImage(back, 0, 0, 256, 256, null);
        }
        g.drawImage(Scalr.resize(middle, size, size), offsetX, offsetY, size, size, null);
        if (front != null) {
            g.drawImage(front, 0, 0, 256, 256, null);
        }
        g.dispose();

        return combined;
    }

}
