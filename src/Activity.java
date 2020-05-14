public class  Activity implements Action {
    private final Execute ENTITY;
    private final WorldModel WORLD;
    private final ImageStore IMAGESTORE;

    public Activity(
            Execute ENTITY,
            WorldModel WORLD,
            ImageStore IMAGESTORE)
    {
        this.ENTITY = ENTITY;
        this.WORLD = WORLD;
        this.IMAGESTORE = IMAGESTORE;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
                ENTITY.executeActivity(WORLD,
                        IMAGESTORE, scheduler);
    }

}
