package fill;

import raster.Raster;

public class SeedFill implements Filler {
    private Raster raster;
    private int backgroundColor;
    private int x, y;

    public SeedFill(Raster raster, int backgroundColor, int x, int y) {
        this.raster = raster;
        this.backgroundColor = backgroundColor;
        this.x = x;
        this.y = y;
    }

    @Override
    public void fill() {
        seedFill(x, y);
    }

    private void seedFill(int x, int y) {
        int pixelColor = raster.getPixel(x, y);
        if(pixelColor != backgroundColor) {
            return;
        }

        if ((x >= 0) && (y >= 0) && (x < raster.getWidth()) && (y < raster.getHeight())){
            return;
        }

        raster.setPixel(x, y, 0x0000ff);
        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }
}
