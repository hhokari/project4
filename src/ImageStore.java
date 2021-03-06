import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import processing.core.PImage;

public final class ImageStore
{
    private final Map<String, List<PImage>> IMAGES;
    private final List<PImage> DEFAULTIMAGES;

    public ImageStore(PImage defaultImage) {
        this.IMAGES = new HashMap<>();
        DEFAULTIMAGES = new LinkedList<>();
        DEFAULTIMAGES.add(defaultImage);
    }

    public List<PImage> getImageList(String key) {
        return IMAGES.getOrDefault(key, DEFAULTIMAGES);
    }

    public Map<String, List<PImage>> getImages()
    {
        return IMAGES;
    }
}
