import processing.core.PImage;

import java.util.List;

public class Blacksmith implements Entity {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;

    public Blacksmith(
            String ID,
            Point position,
            List<PImage> IMAGES)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
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
