package fill;

import raster.Raster;

import java.util.ArrayDeque;
import java.util.Deque;

public class SeedFill implements Filler {
    private final Raster raster;
    private final int backgroundColor;
    private final int x, y;

    public SeedFill(Raster raster, int backgroundColor, int x, int y) {
        this.raster = raster;
        this.backgroundColor = backgroundColor;
        this.x = x;
        this.y = y;
    }

    @Override
    public void fill() {
        seedFill();
    }

    private void seedFill() {

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            int x = p[0];
            int y = p[1];

            // bounds
            if (x < 0 || y < 0 || x >= raster.getWidth() || y >= raster.getHeight())
                continue;

            // color check
            int current = raster.getPixel(x, y);
            if (current != backgroundColor)
                continue;

            // fill
            int fillColor = 0x0000FF;
            raster.setPixel(x, y, fillColor);

            // neighbors
            stack.push(new int[]{x + 1, y});
            stack.push(new int[]{x - 1, y});
            stack.push(new int[]{x, y + 1});
            stack.push(new int[]{x, y - 1});
        }
    }
}
