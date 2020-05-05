import java.util.*;

public final class EventScheduler
{
    private final PriorityQueue<Event> EVENTQUEUE;
    private final Map<Entity, List<Event>> PENDINGEVENTS;
    private final double TIMESCALE;

    public EventScheduler(double TIMESCALE) {
        this.EVENTQUEUE = new PriorityQueue<>(new EventComparator());
        this.PENDINGEVENTS = new HashMap<>();
        this.TIMESCALE = TIMESCALE;
    }

    public void updateOnTime(long time) {
        while (!EVENTQUEUE.isEmpty()
                && EVENTQUEUE.peek().getTime() < time) {
            Event next = EVENTQUEUE.poll();

            next.removePendingEvent(this);

            next.getAction().executeAction(this);
        }
    }

    public void unscheduleAllEvents(
            Entity entity)
    {
        List<Event> pending = PENDINGEVENTS.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                EVENTQUEUE.remove(event);
            }
        }
    }

    public void scheduleEvent(
            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * TIMESCALE);
        Event event = new Event(action, time, entity);

        EVENTQUEUE.add(event);

        // update list of pending events for the given entity
        List<Event> pending = PENDINGEVENTS.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        PENDINGEVENTS.put(entity, pending);
    }

    public Map<Entity, List<Event>> getPendingEvents()
    {
        return PENDINGEVENTS;
    }

}