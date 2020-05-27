import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {
    public Obstacle(
            final String ID,
            Point position,
            final List<PImage> IMAGES)
    {
        super(ID, position, IMAGES);
    }

}
