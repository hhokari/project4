import processing.core.PImage;
import java.util.List;
public class Factory {
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    public static Action createAnimationAction(AnimatedEntity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }
    public static Activity createActivityAction(
            ActiveEntity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }
    public static Blacksmith createBlacksmith(
            String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }
    public static Miner_Full createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Miner_Full(id, position, images,
                resourceLimit, actionPeriod,
                animationPeriod);
    }
    public static Miner_Not_Full createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Miner_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }
    public static Obstacle createObstacle(
            String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }
    public static Ore createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, position, images, actionPeriod);
    }
    public static Ore_Blob createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Ore_Blob(id, position, images, actionPeriod, animationPeriod);
    }
    public static Quake createQuake(
            Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    public static Vein createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static Eyeball createEyeball(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Eyeball(id, position, images, actionPeriod, animationPeriod);
    }
}
/*
import processing.core.PImage;

import java.util.List;

public class Factory {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public static Action createAnimationAction(AnimatedEntity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public static Activity createActivityAction(
            ActiveEntity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public static Blacksmith createBlacksmith(
            String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static Miner_Full createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Miner_Full(id, position, images,
                resourceLimit, actionPeriod,
                animationPeriod);
    }

    public static Miner_Not_Full createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Miner_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(
            String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Ore createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, position, images, actionPeriod);
    }

    public static Ore_Blob createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Ore_Blob(id, position, images, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(
            Point position, List<PImage> images)
    {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Vein createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static Eyeball createEyeball(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Eyeball(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static RedEyeball createRedEyeball(
            String id,
            Point position,
            List<PImage> images)
    {
        return new RedEyeball(id, position, images);
    }
}
*/
