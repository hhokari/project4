import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Vein extends ActiveEntity {

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final Random RAND = new Random();
    private static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";

    public Vein(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD);
    }


    protected void _executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(position);

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + ID, openPt.get(),
                    ORE_CORRUPT_MIN + RAND.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                ACTIONPERIOD);
    }
}
