package com.palight.playerinfo.util;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ImageSelection implements Transferable {
    private Image image;

    public ImageSelection(Image image)
    {
        this.image = image;
    }

    // Returns supported flavors
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    // Returns true if flavor is supported
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    // Returns image
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException
    {
        if (!DataFlavor.imageFlavor.equals(flavor))
        {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }
}
