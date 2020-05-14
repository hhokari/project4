import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;
    public Obstacle(
            String ID,
            Point position,
            List<PImage> IMAGES)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }
}
