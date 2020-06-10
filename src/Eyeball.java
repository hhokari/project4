import processing.core.PImage;
import java.util.List;
import java.util.Optional;
public class Eyeball extends MoveEntity {
    public Eyeball(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD, ANIMATIONPERIOD);
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
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }
    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(position, Blacksmith.class);
        world.findNearest(position, Blacksmith.class);
        long nextPeriod = ACTIONPERIOD;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();
            if (move(world, blobTarget.get(), scheduler)) {
                Quake quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(Functions.QUAKE_KEY));
                world.addEntity(quake);
                nextPeriod += ACTIONPERIOD;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
/*
import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Eyeball extends ResourceCountEntity {


    public Eyeball(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int RESOURCELIMIT,
            int resourceCount,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, resourceCount, ACTIONPERIOD, ANIMATIONPERIOD);
    }

    protected Point _nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == Ore.class)))
        {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == Ore.class)))
            {
                newPos = position;
            }
        }

        return newPos;
    }


    protected boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount > 0) {
            RedEyeball redEyeball = Factory.createRedEyeball("redeyeball",
                    position,
                    imageStore.getImageList("redeyeball"));

            world.removeEntity(redEyeball);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(redEyeball);

            return true;
        }

        return false;
    }

    protected boolean _executeActivityHelper(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        return transform(world, scheduler, imageStore);
    }

    protected Optional<Entity> _executeActivityHelper2(WorldModel world) {
        return world.findNearest(position, Blacksmith.class);
    }
}
*/
