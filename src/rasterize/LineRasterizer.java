package rasterize;

import model.Line;
import raster.RasterBufferedImage;

import java.awt.*;

public abstract class LineRasterizer {
    protected RasterBufferedImage raster;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;
    }

    public void rasterize(Line line) {

    }

    public void rasterize(int x1, int y1, int x2, int y2) {

    }
}
