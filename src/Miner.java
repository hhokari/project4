import processing.core.PImage;

import java.util.List;

public abstract class Miner extends MoveEntity {

    protected final int RESOURCELIMIT;

    public Miner(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int RESOURCELIMIT,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)

    {
        super(ID, position, IMAGES, ACTIONPERIOD, ANIMATIONPERIOD);
        this.RESOURCELIMIT = RESOURCELIMIT;
    }

    protected void moveHelper(WorldModel world, Entity target, EventScheduler scheduler) {
    }


}
