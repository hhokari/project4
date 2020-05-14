public class Animation implements Action {
    private final Animate ENTITY;
    private final int REPEATCOUNT;

    public Animation(
            Animate ENTITY,
            int REPEATCOUNT)
    {
        this.ENTITY = ENTITY;
        this.REPEATCOUNT = REPEATCOUNT;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
        ENTITY.nextImage();

        if (REPEATCOUNT != 1) {
            scheduler.scheduleEvent(ENTITY,
                    Factory.createAnimationAction(ENTITY, Math.max(REPEATCOUNT - 1, 0)),
                    ENTITY.getAnimationPeriod());
        }
    }

}
