/* abstract class made for:
    Eyeball & Miner_Not_Full
 */
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class EyeballEntity extends MoveEntity{

    public EyeballEntity(
            final String ID,
            final Point position,
            final List<PImage> IMAGES,
            int ActionPeriod,
            int AnimationPeriod)
    {
        super(ID, position, IMAGES, ActionPeriod, AnimationPeriod);
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
                _executeActivityHelper(world);
        _executeActivityHelper(world);
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

    protected abstract Optional<Entity> _executeActivityHelper(WorldModel world);

}
