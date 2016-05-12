package wekey.ui.entities;

import junit.framework.Assert;

import org.junit.Test;
    
@SuppressWarnings("deprecation")
public class ConstantsTest {
    Constants constant = new Constants();
    @Test
    public void emptyStringTest() {
        Assert.assertEquals("", Constants.EMPTY_STRING);
    }

    @Test
    public void cellPosTest00() {
        Assert.assertEquals("cell 0 0", Constants.CELL_0_0_POSITION);
    }

    @Test
    public void cellPosTest01() {
        Assert.assertEquals("cell 0 1", Constants.CELL_0_1_POSITION);
    }

    @Test
    public void cellPosTest02() {
        Assert.assertEquals("cell 0 2", Constants.CELL_0_2_POSITION);
    }

    @Test
    public void commaTest() {
        Assert.assertEquals(",", Constants.COMMA);
    }

    @Test
    public void emptyConstrainTest() {
        Assert.assertEquals("[]", Constants.EMPTY_CONSTRAIN);
    }

    @Test
    public void extentionExpressionTest() {
        Assert.assertEquals("[.][^.]+$", Constants.EXTENSION_EXPRESSION);
    }

    @Test
    public void fileNameLabelTest() {
        Assert.assertEquals("File Name: ", Constants.FILE_NAME_LABEL);
    }

    @Test
    public void growConstainTest() {
        Assert.assertEquals("[grow]", Constants.GROW_CONSTRAIN);
    }

    @Test
    public void growConstainCellTest() {
        Assert.assertEquals("grow", Constants.GROW_CONSTRAIN_CELL);
    }

    @Test
    public void noConstainTest() {
        Assert.assertEquals("", Constants.NO_CONSTRAIN);
    }

    @Test
    public void paneDisplayFormatTest() {
        Assert.assertEquals("text/html", Constants.PANE_DISPLAY_FORMAT);
    }
}
