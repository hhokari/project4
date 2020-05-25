import processing.core.PImage;
import java.util.List;
//
//public interface Entity
//{
//    Point getPosition();
//    void setPosition(Point position);
//    PImage getCurrentImage();
//}

public abstract class Entity {
    protected final String ID;
    protected Point position;
    protected final List<PImage> IMAGES;
    protected int imageIndex;

    public Entity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            int imageIndex)
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