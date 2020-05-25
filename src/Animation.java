public class Animation extends Action {
    private final AnimatedEntity ENTITY;
    private final int REPEATCOUNT;

    public Animation(
            AnimatedEntity ENTITY,
            int REPEATCOUNT)
    {
        this.ENTITY = ENTITY;
        this.REPEATCOUNT = REPEATCOUNT;
    }

    protected void executeAction(
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
