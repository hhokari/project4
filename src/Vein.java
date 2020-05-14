import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Vein implements Entity, Execute {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;
    private final int ACTIONPERIOD;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final Random RAND = new Random();
    private static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";

    public Vein(
            String ID,
            Point position,
            List<PImage> IMAGES,
            int ACTIONPERIOD)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
        this.ACTIONPERIOD = ACTIONPERIOD;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
    }

    public void executeActivity(
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


    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }


    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }
}
