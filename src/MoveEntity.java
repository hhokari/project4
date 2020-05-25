import processing.core.PImage;

import java.util.List;

public abstract class MoveEntity extends AnimatedEntity {

    public MoveEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)

    {
        super(ID, position, IMAGES, ACTIONPERIOD, ANIMATIONPERIOD);
        this.imageIndex = 0;
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);

    protected abstract boolean move(WorldModel world, Entity target, EventScheduler scheduler);
}
