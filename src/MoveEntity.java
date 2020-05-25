import processing.core.PImage;

import java.util.List;

public abstract class MoveEntity extends AnimatedEntity {

    public MoveEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            int imageIndex,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)

    {
        super(ID, position, IMAGES, imageIndex, ACTIONPERIOD, ANIMATIONPERIOD);
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);

    protected abstract boolean move(WorldModel world, Entity target, EventScheduler scheduler);
}
