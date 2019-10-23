package helsinki.iconset;

import java.io.IOException;

import ua.com.fielden.platform.svg.combining.IronIconsetUtility;

/**
 * Generates an iconset resource with images for the main application menu.
 */
public class MenuIconsetCreator {

    public static void main(final String[] args) throws IOException {
        final String srcFolder= "src/main/resources/images_menu";
        final String iconsetId = "mainMenu";
        final int svgWidth = 1000;
        final String outputFile = "src/main/resources/main-menu.js";
        final IronIconsetUtility iconsetUtility = new IronIconsetUtility(iconsetId, svgWidth, srcFolder);
        iconsetUtility.createSvgIconset(outputFile);
    }
}
