import processing.core.PImage;
import java.util.List;

public abstract class ActiveEntity extends Entity {

    protected final int ACTIONPERIOD;

    public ActiveEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            int imageIndex,
            final int ACTIONPERIOD
    )

    {
        super(ID, position, IMAGES, imageIndex);
        this.ACTIONPERIOD = ACTIONPERIOD;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
