import processing.core.PImage;

import java.util.List;

public class Blacksmith extends Entity {
//    private final String ID;
//    private Point position;
//    private final List<PImage> IMAGES ;
//    private int imageIndex;

    public Blacksmith(
            final String ID,
            Point position,
            final List<PImage> IMAGES)
    {
        super(ID, position, IMAGES);
        this.imageIndex = 0;
    }

//
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
