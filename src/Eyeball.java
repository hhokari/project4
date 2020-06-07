import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class Eyeball extends Miner {

    private int destroyedBlackSmithCount;

    public Eyeball(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int RESOURCELIMIT,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, ACTIONPERIOD, ANIMATIONPERIOD);
        this.destroyedBlackSmithCount = 0;
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

    protected void _moveHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        destroyedBlackSmithCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }


    protected boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (destroyedBlackSmithCount > 0) {
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

    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(position, Blacksmith.class);

        if (!notFullTarget.isPresent() || !move(world, notFullTarget.get(),
                scheduler)
                || !transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }
}
