package helsinki.common;

import static java.lang.String.format;
import static ua.com.fielden.platform.web.action.StandardMastersWebUiConfig.MASTER_ACTION_CUSTOM_SPECIFICATION;
import static ua.com.fielden.platform.web.action.StandardMastersWebUiConfig.MASTER_ACTION_DEFAULT_WIDTH;
import static ua.com.fielden.platform.web.layout.api.impl.LayoutCellBuilder.layout;

import ua.com.fielden.platform.web.layout.api.impl.FlexLayoutConfig;

/**
 * Provides an API to consistently compose Web UI layouts for both entity centres and masters.
 * 
 * @author Generated
 */
public class LayoutComposer {
    public static final int MARGIN = 20;
    public static final String MARGIN_PIX = MARGIN + "px";
    public static final String MARGIN_PIX_FOR_MASTER_ACTION = "10px";

    public static final String CENTRE_LAYOUT_SPECIFICATION = "'horizontal', 'center', 'start-justified',";
    public static final String COMPONENT = "['flex']";
    public static final String COMPONENT_WITH_PADDING = "['flex', 'margin-right: " + MARGIN_PIX + "']";

    public static final String MASTER_LAYOUT_SPECIFICATION = "'horizontal','justified',";
    public static final String MASTER_ACTION_LAYOUT_SPECIFICATION = "'horizontal', 'padding: " + MARGIN_PIX_FOR_MASTER_ACTION + "', 'wrap', 'justify-content: center',";
    public static final FlexLayoutConfig CELL_LAYOUT = layout().flex().end();
    public static final FlexLayoutConfig ROW_LAYOUT = layout().withStyle("padding-left", "32px").end();
    public static final FlexLayoutConfig FLEXIBLE_ROW = layout().flexAuto().end();
    public static final FlexLayoutConfig FLEXIBLE_ROW_WITH_PADDING = layout().withStyle("padding-left", "32px").flexAuto().end();
    public static final FlexLayoutConfig PADDING_LAYOUT = layout().withStyle("padding", MARGIN_PIX).end();
    public static final FlexLayoutConfig FLEXIBLE_LAYOUT_WITH_PADDING = layout()
            .withStyle("height", "100%")
            .withStyle("box-sizing", "border-box")
            .withStyle("min-height", "fit-content")
            .withStyle("padding", MARGIN_PIX).end();
    public static final FlexLayoutConfig SUBHEADER_LAYOUT = layout().withStyle("padding-top", "16px").end();
    public static final FlexLayoutConfig INVISIBLE_LAYOUT = layout().withStyle("display", "none").end();

    private static final int SIMPLE_ONE_COLUMN_MASTER_MIN_WIDTH = 480;
    private static final int SIMPLE_TWO_COLUMN_MASTER_MIN_WIDTH = 720;
    private static final int SIMPLE_THREE_COLUMN_MASTER_MIN_WIDTH = 960;

    // Should be used as the first parameter for functions PrefDim.mkDim()
    public static final int SIMPLE_ONE_COLUMN_MASTER_DIM_WIDTH = SIMPLE_ONE_COLUMN_MASTER_MIN_WIDTH + 2 * MARGIN;
    public static final int SIMPLE_TWO_COLUMN_MASTER_DIM_WIDTH = SIMPLE_TWO_COLUMN_MASTER_MIN_WIDTH + 2 * MARGIN;
    public static final int SIMPLE_THREE_COLUMN_MASTER_DIM_WIDTH = SIMPLE_THREE_COLUMN_MASTER_MIN_WIDTH + 2 * MARGIN;

    /**
     * @param layout
     *            provides layout which will be modified by adding row of additional columns.
     * @param specification
     *            provides layout specification.
     * @param colNumber
     *            number of columns that should be added.
     */
    private static void appendRow(final StringBuilder layout, final String specification, final int colNumber) {
        layout.append("[").append(specification);
        for (int col = 0; col < colNumber - 1; col++) {
            layout.append(COMPONENT_WITH_PADDING).append(",");
        }
        layout.append(COMPONENT).append("],");
    }

    /**
     * Produces consistent rectangular layout for centre with rowNumber by columnNumber dimension.
     *
     * @param rowNumber
     *            specifies number of rows in the layout that would be produced.
     * @param colNumber
     *            specifies number of columns in the layout that would be produced.
     * @return String representation of the layout.
     *         <p>
     *         For example, mkGridForCentre(3, 2) will produce [[[],[]], [[],[]], [[],[]],]
     */

    public static String mkGridForCentre(final int rowNumber, final int colNumber) {
        final StringBuilder layout = new StringBuilder();
        layout.append("['vertical',");
        for (int row = 0; row < rowNumber; row++) {
            appendRow(layout, CENTRE_LAYOUT_SPECIFICATION, colNumber);
        }
        layout.append("]");
        return layout.toString();
    }


    /**
     * Produces an inconsistent rectangular layout for centre with different number of columns in rows.
     *
     * @param numColsInFirstRow
     *            specifies number of columns in the first row. Prevents incorrect usage of API without specifying number of columns and providing empty array.
     * @param colsPerSecondRowOnwards
     *            array that specifies number of columns in each rows (starting from the second row) in the layout that would be produced.
     * @return String representation of the layout.
     *         <p>
     *         For example, mkVarGridForCentre(3, 2) will produce two rows with 3 components in the first row and 2 components in the second row [[[],[], []], [[],[]],]
     */

    public static String mkVarGridForCentre(final int numColsInFirstRow, final int... colsPerSecondRowOnwards) {
        final StringBuilder layout = new StringBuilder();
        layout.append("['vertical',");
        //processing the first row
        appendRow(layout, CENTRE_LAYOUT_SPECIFICATION, numColsInFirstRow);
        //processing the array
        for (final int colsInRow : colsPerSecondRowOnwards) {
            appendRow(layout, CENTRE_LAYOUT_SPECIFICATION, colsInRow);
        }
        layout.append("]");
        return layout.toString();
    }

    /**
     * Produces a consistent rectangular layout for simple master with rowNumber by columnNum dimension.
     *
     * @param rowNumber
     *            specifies number of rows in the layout that would be produced.
     * @param colNumber
     *            specifies number of columns in the layout that would be produced.
     * @return String representation of the layout.
     *         <p>
     *         For example, mkGridForMasterFitWidth(3, 2) will produce [[[],[]], [[],[]], [[],[]]]
     * @return
     */
    public static String mkGridForMasterFitWidth(final int rowNumber, final int colNumber) {
        final StringBuilder layout = new StringBuilder();
        layout.append("['padding:" + MARGIN_PIX + "',");
        for (int row = 0; row < rowNumber; row++) {
            appendRow(layout, MASTER_LAYOUT_SPECIFICATION, colNumber);
        }
        layout.deleteCharAt(layout.length() - 1);
        layout.append("]");
        return layout.toString();
    }

    /**
     * Produces an inconsistent rectangular layout for simple master with different number of columns in rows.
     *
     * @param numColsInFirstRow
     *            specifies number of columns in the first row. Prevents incorrect usage of API without specifying number of columns and providing empty array.
     * @param colsPerSecondRowOnwards
     *            array that specifies number of columns in each rows in the layout that would be produced.
     * @return String representation of the layout.
     *         <p>
     *         For example, mkVarGridForMasterFitWidth(3, 1, 2 ) will produce three rows with 3 components in the first row, 1 component in the second row and 2 components in the
     *         third row [[[],[],[]], [[]], [[],[]],]
     */
    public static String mkVarGridForMasterFitWidth(final int numColsInFirstRow, final int... colsPerSecondRowOnwards) {
        final StringBuilder layout = new StringBuilder();
        layout.append("['padding:" + MARGIN_PIX + "',");
        //processing the first row
        appendRow(layout, MASTER_LAYOUT_SPECIFICATION, numColsInFirstRow);
        //processing the array
        for (final int colsInRow : colsPerSecondRowOnwards) {
            appendRow(layout, MASTER_LAYOUT_SPECIFICATION, colsInRow);
        }
        layout.deleteCharAt(layout.length() - 1);
        layout.append("]");
        return layout.toString();
    }

    public static String mkActionLayoutForMaster() {
        return mkActionLayoutForMaster(2, MASTER_ACTION_DEFAULT_WIDTH);
    }

    public static String mkActionLayoutForMaster(final int buttonCount, final int buttonWidth) {
        final StringBuilder layout = new StringBuilder();
        layout.append("[").append(MASTER_ACTION_LAYOUT_SPECIFICATION);
        for (int buttonIndex = 0; buttonIndex < buttonCount; buttonIndex++) {
            layout.append(",[" + format(MASTER_ACTION_CUSTOM_SPECIFICATION, buttonWidth) + "]");
        }
        layout.append("]");
        return layout.toString();
    }
}

