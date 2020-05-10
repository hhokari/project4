public class Animation implements Action {
    private final Entity ENTITY;
    private final WorldModel WORLD;
    private final ImageStore IMAGESTORE;
    private final int REPEATCOUNT;

    public Animation(
            Entity ENTITY,
            WorldModel WORLD,
            ImageStore IMAGESTORE,
            int REPEATCOUNT)
    {
        this.ENTITY = ENTITY;
        this.WORLD = WORLD;
        this.IMAGESTORE = IMAGESTORE;
        this.REPEATCOUNT = REPEATCOUNT;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
        ENTITY.nextImage();

        if (REPEATCOUNT != 1) {
            scheduler.scheduleEvent(ENTITY,
                    Factory.createAnimationAction(ENTITY,
                            Math.max(REPEATCOUNT - 1,
                                    0)),
                    ENTITY.getAnimationPeriod());
        }
    }

//    public void executeAction(EventScheduler scheduler) {
//        switch (KIND) {
//            case ACTIVITY:
//                executeActivityAction(scheduler);
//                break;
//
//            case ANIMATION:
//                executeAnimationAction(scheduler);
//                break;
//        }
//    }
//
//    public static Action createAnimationAction(Entity entity, int repeatCount) {
//        return new Action(ActionKind.ANIMATION, entity, null, null,
//                repeatCount);
//    }
//
//    public static Action createActivityAction(
//            Entity entity, WorldModel world, ImageStore imageStore)
//    {
//        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
//    }

}
