
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Not_Full extends Miner {
    private int resourceCount;

    public Miner_Not_Full(
            String ID,
            Point position,
            List<PImage> IMAGES,
            int RESOURCELIMIT,
            int resourceCount,
            int ACTIONPERIOD,
            int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, ACTIONPERIOD, ANIMATIONPERIOD);
        this.resourceCount = resourceCount;
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

    protected void _moveHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        resourceCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    protected boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount >= RESOURCELIMIT) {
            Miner_Full miner = Factory.createMinerFull(ID, RESOURCELIMIT,
                    position, ACTIONPERIOD,
                    ANIMATIONPERIOD,
                    IMAGES);

            world.removeEntity(miner);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(position, Ore.class);

        if (!notFullTarget.isPresent() || !move(world, notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

}
/*
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Not_Full extends ResourceCountEntity {

    public Miner_Not_Full(
            String ID,
            Point position,
            List<PImage> IMAGES,
            int RESOURCELIMIT,
            int resourceCount,
            int ACTIONPERIOD,
            int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, resourceCount, ACTIONPERIOD, ANIMATIONPERIOD);
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

    protected boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount >= RESOURCELIMIT) {
            Miner_Full miner = Factory.createMinerFull(ID, RESOURCELIMIT,
                    position, ACTIONPERIOD,
                    ANIMATIONPERIOD,
                    IMAGES);

            world.removeEntity(miner);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    protected boolean _executeActivityHelper(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        return transformNotFull(world, scheduler, imageStore);
    }

    protected Optional<Entity> _executeActivityHelper2(WorldModel world) {
        return world.findNearest(position, Ore.class);
    }
}
*/
