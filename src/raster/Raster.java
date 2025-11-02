package raster;

public interface Raster {
    void setPixel(int x, int y, int color);
    void setBackgroundColor(int color);
    int getPixel(int x, int y);
    int getWidth();
    int getHeight();
    int getBackgroundColor();
    void clear();
}
