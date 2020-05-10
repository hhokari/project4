public class  Activity implements Action {
    private final Entity ENTITY;
    private final WorldModel WORLD;
    private final ImageStore IMAGESTORE;
    private final int REPEATCOUNT;

    public Activity(
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
        switch (ENTITY.getKind()) {
            case MINER_FULL:
                ENTITY.executeMinerFullActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            case MINER_NOT_FULL:
                ENTITY.executeMinerNotFullActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            case ORE:
                ENTITY.executeOreActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            case ORE_BLOB:
                ENTITY.executeOreBlobActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            case QUAKE:
                ENTITY.executeQuakeActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            case VEIN:
                ENTITY.executeVeinActivity(WORLD,
                        IMAGESTORE, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        ENTITY.getKind()));
        }
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
