import processing.core.PImage;

import java.util.List;
import java.util.Random;


public class Ore extends ActiveEntity {

    private static final Random RAND = new Random();
    public static final String ORE_KEY = "ore";
    private static final String BLOB_KEY = "blob";
    private static final String EYEBALL_KEY = "eyeball";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(
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
        Point pos = position;

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Ore_Blob blob = Factory.createOreBlob(ID + BLOB_ID_SUFFIX, pos,
                ACTIONPERIOD / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + RAND.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        //addded code
//        Eyeball eyeball = Factory.createEyeball(ID + BLOB_ID_SUFFIX, pos,
//                ACTIONPERIOD / BLOB_PERIOD_SCALE,
//                BLOB_ANIMATION_MIN + RAND.nextInt(
//                        BLOB_ANIMATION_MAX
//                                - BLOB_ANIMATION_MIN),
//                imageStore.getImageList(EYEBALL_KEY));

        world.addEntity(blob);
//        world.addEntity(eyeball);
//        eyeball.scheduleActions(scheduler, world, imageStore);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public int getACTIONPERIOD() {
        return ACTIONPERIOD;
    }

}
