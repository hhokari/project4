public class Activity extends Action {
    private final ActiveEntity ENTITY;
    private final WorldModel WORLD;
    private final ImageStore IMAGESTORE;

    public Activity(
            ActiveEntity ENTITY,
            WorldModel WORLD,
            ImageStore IMAGESTORE)
    {
        this.ENTITY = ENTITY;
        this.WORLD = WORLD;
        this.IMAGESTORE = IMAGESTORE;
    }

    protected void executeAction(
            EventScheduler scheduler)
    {
                ENTITY.executeActivity(WORLD,
                        IMAGESTORE, scheduler);
    }

}
