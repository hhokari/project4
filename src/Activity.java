public class  Activity implements Action {
    private final Execute ENTITY;
    private final WorldModel WORLD;
    private final ImageStore IMAGESTORE;
    private final int REPEATCOUNT;

    public Activity(
            Execute ENTITY,
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
                ENTITY.executeActivity(WORLD,
                        IMAGESTORE, scheduler);
    }

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
