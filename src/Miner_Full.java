import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Miner_Full implements Entity, Animate, Execute, NextPosition {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;
    private final int RESOURCELIMIT;
    private final int ACTIONPERIOD;
    private final int ANIMATIONPERIOD;

    public Miner_Full(
            String ID,
            Point position,
            List<PImage> IMAGES,
            int RESOURCELIMIT,
            int ACTIONPERIOD,
            int ANIMATIONPERIOD)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
        this.RESOURCELIMIT = RESOURCELIMIT;
        this.ACTIONPERIOD = ACTIONPERIOD;
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = position;
            }
        }

        return newPos;
    }

    public boolean move(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(position, target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Miner_Not_Full miner = Factory.createMinerNotFull(ID, RESOURCELIMIT,
                position, ACTIONPERIOD,
                ANIMATIONPERIOD,
                IMAGES);

        world.removeEntity(miner);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        Factory.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this,
                        Factory.createAnimationAction(this, 0),
                        getAnimationPeriod());
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(position, Blacksmith.class);

        if (fullTarget.isPresent() && move(world, fullTarget.get(),
                scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public int getAnimationPeriod() {
        return ANIMATIONPERIOD;
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
