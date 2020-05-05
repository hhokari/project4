import java.util.List;

import processing.core.PImage;

public final class Background
{
    private String id;
    private final List<PImage> IMAGES;
    private int imageIndex;

    public Background(String id, List<PImage> IMAGES) {
        this.id = id;
        this.IMAGES = IMAGES;
    }

    public PImage getCurrentImage() {
        return IMAGES.get(
                (imageIndex));
    }

}
