package com.partagames.imageresizetool;

import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.partagames.imageresizetool.Constants.*;

/**
 * Simple tool that takes a list of image files as arguments and saves new resized image files to the given folder.
 * Created by Antti on 18.9.2015.
 */
public class SimpleImageResizeTool {

    private static Options options;
    private static String[] imageFileStrings;
    private static Dimensions dimensions;
    private static String format;
    private static String scalinghint;

    private static final Map<String, BufferedImage> imageFiles = new HashMap<>();

    public static void main(String[] args) throws Exception {
        options = new Options();

        // required options
        options.addOption(Option.builder(ARG_DIMENSIONS_SHORT).longOpt(ARG_DIMENSIONS).hasArg(true).optionalArg(false)
                .desc("Target image dimensions in pixels (e.g 1280x720)").required(true).build());

        // optional options
        options.addOption(Option.builder(ARG_FORMAT_SHORT).longOpt(ARG_FORMAT).hasArg(true).optionalArg(false)
                .desc("Image output format (png,jpg,gif)").required(false).build());
        options.addOption(Option.builder(ARG_OUTPUT_SHORT).longOpt(ARG_OUTPUT).hasArg(true).optionalArg(false)
                .desc("Image output folder").required(false).build());
        options.addOption(Option.builder(ARG_HINT_SHORT).longOpt(ARG_HINT).hasArg(true).optionalArg(false)
                .desc("Scaling hint (bicubic, bilinear)").required(false).build());
        options.addOption(Option.builder(ARG_HELP_SHORT).longOpt(ARG_HELP).hasArg(false)
                .desc("Shows this help message.").required(false).build());

        if (parseAndPrepareArguments(args, options)) {
            createBufferedImages();
            resizeAndWriteImages();
        }
    }

    private static boolean parseAndPrepareArguments(String[] args, Options options) {
        // parse through arguments and prepare them appropriately

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (MissingOptionException | MissingArgumentException e) {
            System.out.println(e.getMessage() + "\n");
            printHelpAndUsage();
            return false;
        } catch (ParseException e2) {
            System.out.println("There was a problem parsing the command line arguments, please check your command.\n");
            printHelpAndUsage();
            throw new RuntimeException(e2);
        }

        // show help
        if (cmd.hasOption(ARG_HELP)) {
            printHelpAndUsage();
            return false;
        }
        
        if (cmd.getArgList().isEmpty()) {
            System.out.println("Missing argument: comma-separated list of images!\n");
            printHelpAndUsage();
        } else {
            imageFileStrings = cmd.getArgList().get(0).split(",");
        }
        
        
        // prepare mandatory arguments
        if (cmd.hasOption(ARG_DIMENSIONS)) {
            final String[] dimensionStrings = cmd.getOptionValue(ARG_DIMENSIONS).split("x");
            try {
                dimensions = new Dimensions(Integer.parseInt(dimensionStrings[0]), Integer.parseInt(dimensionStrings[1]));
            } catch (Exception e) {
                System.out.println("Dimension argument was not correct!\n");
                printHelpAndUsage();
                return false;
            }
        }

        // prepare optional arguments
        if (cmd.hasOption(ARG_OUTPUT)) {
            System.out.println("Output folder not implemented!");
        }
        if (cmd.hasOption(ARG_FORMAT)) {
            final String outputFormat = cmd.getOptionValue("format").toLowerCase();
            if (Constants.OUTPUT_IMAGE_FORMATS.contains(outputFormat)) {

            } else {
                System.out.println("Error: Wrong output image format!\n");
                printHelpAndUsage();
                return false;
            }
        }
        if (cmd.hasOption(ARG_HINT)) {
            System.out.println("Scaling hint not implemented!"); 
        }

        return true;
    }

    private static void printHelpAndUsage() {
        // automatically generate the help statement
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("resizer [options ...] [/folder/image1,/folder/image2 ...]", options);
    }

    private static void createBufferedImages() {
        for (int i = 0; i < imageFileStrings.length; i++) {
            try {
                imageFiles.put(imageFileStrings[i], ImageIO.read(new File(imageFileStrings[i])));
            } catch (IOException e) {
                System.out.println("File " + imageFileStrings[i] + " missing, corrupted or not supported, ignoring...");
            }
        }
    }

    private static void resizeAndWriteImages() {

        // create output folder if it does not exist
        final File outputFolder = new File("output/");
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // resize and write images
        int i = 0;
        for (String key : imageFiles.keySet()) {
            i++;

            final String fileName = extractFileNameFromPath(key);

            final BufferedImage image = imageFiles.get(key);
            final BufferedImage scaledImage = scale(image, dimensions.width, dimensions.height);
            try {
                ImageIO.write(scaledImage, "png",
                        new File("output/" + dimensions.width + "_x_" + dimensions.height + " " + fileName + ".png"));
            } catch (IOException e) {
                System.out.println("Cannot write " + key + " to output folder. Ignoring...");
            }
        }
    }

    /**
     * Extracts file name from full file path.
     *
     * @param filePath File path
     * @return File name
     */
    private static String extractFileNameFromPath(String filePath) {
        final Path p = Paths.get(filePath);
        return p.getFileName().toString();
    }

    /**
     * Scales an image to the desired dimensions.
     *
     * @param img  Original image
     * @param newW Target width
     * @param newH Target height
     * @return Scaled image
     */
    public static BufferedImage scale(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        final BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        final Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

}
