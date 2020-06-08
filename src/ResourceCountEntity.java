/* abstract class made for:
    Eyeball & Miner_Not_Full
 */
import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class ResourceCountEntity extends Miner{

    protected int resourceCount;

    public ResourceCountEntity(
            final String ID,
            final Point position,
            final List<PImage> IMAGES,
            int RESOURCELIMIT,
            int resourceCount,
            int ActionPeriod,
            int AnimationPeriod)
    {
        super(ID, position, IMAGES, RESOURCELIMIT, ActionPeriod, AnimationPeriod);
        this.resourceCount = resourceCount;
    }

    protected void _moveHelper(WorldModel world, Entity target, EventScheduler scheduler) {
        resourceCount += 1;
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = _executeActivityHelper2(world);

        if (!notFullTarget.isPresent() || !move(world, notFullTarget.get(),
                scheduler)
                || !_executeActivityHelper(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

    protected abstract boolean _executeActivityHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    protected abstract Optional<Entity> _executeActivityHelper2(WorldModel world);

}
