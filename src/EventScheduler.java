import java.util.*;

public final class EventScheduler
{
    public PriorityQueue<Event> eventQueue;
    public Map<Entity, List<Event>> pendingEvents;
    public double timeScale;
    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;
    public static final String BLOB_KEY = "blob";

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public void removePendingEvent(
            EventScheduler scheduler, Event event)
    {
        List<Event> pending = this.pendingEvents.get(event.entity);

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void unscheduleAllEvents(
            EventScheduler scheduler, Entity entity)
    {
        List<Event> pending = this.pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                this.eventQueue.remove(event);
            }
        }
    }

    public void updateOnTime(EventScheduler scheduler, long time) {
        while (!this.eventQueue.isEmpty()
                && this.eventQueue.peek().time < time) {
            Event next = this.eventQueue.poll();

            this.removePendingEvent(scheduler, next);

            scheduler.executeAction(next.action, scheduler);
        }
    }

    public void scheduleEvent(
            EventScheduler scheduler,
            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * this.timeScale);
        Event event = new Event(action, time, entity);

        this.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = this.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        this.pendingEvents.put(entity, pending);
    }

    public void scheduleActions(
            Entity entity,
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (entity.kind) {
            case MINER_FULL:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                this.scheduleEvent(scheduler, entity,
                        entity.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                this.scheduleEvent(scheduler, entity,
                        entity.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case ORE:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                break;

            case ORE_BLOB:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                this.scheduleEvent(scheduler, entity,
                        entity.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case QUAKE:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                this.scheduleEvent(scheduler, entity, entity.createAnimationAction(entity,
                        Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                        entity.getAnimationPeriod());
                break;

            case VEIN:
                this.scheduleEvent(scheduler, entity,
                        entity.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod);
                break;

            default:
        }
    }

    public void executeVeinActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(world, entity.position);

        if (openPt.isPresent()) {
            Entity ore = world.createOre(ORE_ID_PREFIX + entity.id, openPt.get(),
                    ORE_CORRUPT_MIN + Functions.rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(imageStore, ORE_KEY));
            world.addEntity(world, ore);
            this.scheduleActions(ore, scheduler, world, imageStore);
        }

        this.scheduleEvent(scheduler, entity,
                entity.createActivityAction(entity, world, imageStore),
                entity.actionPeriod);
    }

    public void executeQuakeActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.unscheduleAllEvents(scheduler, entity);
        world.removeEntity(world, entity);
    }

    public void executeOreBlobActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                entity.findNearest(world, entity.position, EntityKind.VEIN);
        long nextPeriod = entity.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().position;

            if (entity.moveToOreBlob(entity, world, blobTarget.get(), scheduler)) {
                Entity quake = world.createQuake(tgtPos,
                        imageStore.getImageList(imageStore, Functions.QUAKE_KEY));

                world.addEntity(world, quake);
                nextPeriod += entity.actionPeriod;
                this.scheduleActions(quake, scheduler, world, imageStore);
            }
        }

        this.scheduleEvent(scheduler, entity,
                entity.createActivityAction(entity, world, imageStore),
                nextPeriod);
    }

    public void executeOreActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = entity.position;

        world.removeEntity(world, entity);
        this.unscheduleAllEvents(scheduler, entity);

        Entity blob = world.createOreBlob(entity.id + BLOB_ID_SUFFIX, pos,
                entity.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(imageStore, BLOB_KEY));

        world.addEntity(world, blob);
        scheduler.scheduleActions(blob, scheduler, world, imageStore);
    }

    public void executeMinerNotFullActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                entity.findNearest(world, entity.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !entity.moveToNotFull(entity, world,
                notFullTarget.get(),
                scheduler)
                || !entity.transformNotFull(entity, world, scheduler, imageStore))
        {
            this.scheduleEvent(scheduler, entity,
                    entity.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
        }
    }

    public void executeMinerFullActivity(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                entity.findNearest(world, entity.position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() && entity.moveToFull(entity, world,
                fullTarget.get(), scheduler))
        {
            entity.transformFull(entity, world, scheduler, imageStore);
        }
        else {
            this.scheduleEvent(scheduler, entity,
                    entity.createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
        }
    }

    public void executeAction(Action action, EventScheduler scheduler) {
        switch (action.kind) {
            case ACTIVITY:
                action.executeActivityAction(action, scheduler);
                break;

            case ANIMATION:
                action.executeAnimationAction(action, scheduler);
                break;
        }
    }
}
