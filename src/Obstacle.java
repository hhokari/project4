import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {
//    private final String ID;
//    private Point position;
//    private final List<PImage> IMAGES ;
//    private int imageIndex;
    public Obstacle(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            int imageIndex)
    {
        super(ID, position, IMAGES, imageIndex);
    }

//    public PImage getCurrentImage() {
//        return (IMAGES.get(imageIndex));
//    }
//
//    public Point getPosition()
//    {
//        return position;
//    }
//
//    public void setPosition(Point position)
//    {
//        this.position = position;
//    }
}
