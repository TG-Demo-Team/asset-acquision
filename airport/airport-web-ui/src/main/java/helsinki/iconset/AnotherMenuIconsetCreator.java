package helsinki.iconset;

import java.io.IOException;

import ua.com.fielden.platform.svg.combining.IronIconsetUtility;

/**
 * Generates an iconset resource with images for the main application menu.
 */
public class AnotherMenuIconsetCreator {

    public static void main(final String[] args) throws IOException {
        final String srcFolder= "src/main/resources/another_images";
        final String iconsetId = "anotherMainMenu";
        final int svgWidth = 1000;
        final String outputFile = "src/main/resources/another-main-menu.js";
        final IronIconsetUtility iconsetUtility = new IronIconsetUtility(iconsetId, svgWidth, srcFolder);
        iconsetUtility.createSvgIconset(outputFile);
        System.out.println("Finished");
    }
}
