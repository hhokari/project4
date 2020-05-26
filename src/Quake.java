import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {
//    private final String ID;
//    private Point position;
//    private final List<PImage> IMAGES ;
//    private int imageIndex;
//    private final int ACTIONPERIOD;
//    private final int ANIMATIONPERIOD;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD, ANIMATIONPERIOD);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this , world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this, Factory.createAnimationAction(this,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        getAnimationPeriod());
    }

    protected void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

//    public void nextImage() {
//        imageIndex = (imageIndex + 1) % IMAGES.size();
//    }
//
//    public int getAnimationPeriod() {
//        return ANIMATIONPERIOD;
//    }
//
//    public PImage getCurrentImage() {
//        return (IMAGES.get(imageIndex));
//    }
//
//    public Point getPosition()
//    {
//        return position;
//    }
//
//    public void setPosition(Point position)
//    {
//        this.position = position;
//    }
}
