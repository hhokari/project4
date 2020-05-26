import processing.core.PImage;
import java.util.List;

public abstract class ActiveEntity extends Entity {

    protected final int ACTIONPERIOD;

    public ActiveEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD
    )

    {
        super(ID, position, IMAGES);
        this.ACTIONPERIOD = ACTIONPERIOD;
        this.imageIndex = 0;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                ACTIONPERIOD);
    }
}
