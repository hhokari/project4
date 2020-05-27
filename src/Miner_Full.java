import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Miner_Full extends Miner {

    public Miner_Full(
            final String ID,
            final Point position,
            final List<PImage> IMAGES,
            final int RESOURCELIMIT,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, ACTIONPERIOD, ANIMATIONPERIOD);
    }

    protected Point _nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = position;
            }
        }

        return newPos;
    }

    protected void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Miner_Not_Full miner = Factory.createMinerNotFull(ID, RESOURCELIMIT,
                position, ACTIONPERIOD,
                ANIMATIONPERIOD,
                IMAGES);

        world.removeEntity(miner);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(position, Blacksmith.class);

        if (fullTarget.isPresent() && move(world, fullTarget.get(),
                scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

}
