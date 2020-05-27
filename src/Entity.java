import processing.core.PImage;
import java.util.List;

public abstract class Entity {
    protected final String ID;
    protected Point position;
    protected final List<PImage> IMAGES;

    public Entity(
            final String ID,
            Point position,
            final List<PImage> IMAGES)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(0));
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