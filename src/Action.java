import processing.core.PImage;

import java.util.List;

public final class Action
{
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

//    public void executeAction(EventScheduler scheduler) {
//        switch (kind) {
//            case ACTIVITY:
//                executeActivityAction(scheduler);
//                break;
//            case ANIMATION:
//                executeAnimationAction(scheduler);
//                break;
//        }
//    }
//
//    public void executeAnimationAction(EventScheduler scheduler)
//    {
//        entity.nextImage();
//
//        if (repeatCount != 1) {
//            scheduleEvent(scheduler, entity,
//                    createAnimationAction(entity,
//                            Math.max(repeatCount - 1, 0)),
//                    entity.getAnimationPeriod());
//        }
//    }
//
//    public Action createActivityAction(
//            Entity entity, WorldModel world, ImageStore imageStore)
//    {
//        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
//    }
//
//    public
    public void executeAnimationAction(
         Action action, EventScheduler scheduler) {
        this.entity.nextImage();

        if (action.repeatCount != 1) {
            scheduler.scheduleEvent(scheduler, action.entity,
                    entity.createAnimationAction(action.entity,
                            Math.max(action.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }

    public void executeActivityAction(
            Action action, EventScheduler scheduler)
    {
        switch (this.entity.kind) {
            case MINER_FULL:
                scheduler.executeMinerFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                scheduler.executeMinerNotFullActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case ORE:
                scheduler.executeOreActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case ORE_BLOB:
                scheduler.executeOreBlobActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                scheduler.executeQuakeActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            case VEIN:
                scheduler.executeVeinActivity(this.entity, this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        action.entity.kind));
        }
    }
}
