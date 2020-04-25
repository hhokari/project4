import java.util.List;

public final class Event
{
    public Action action;
    public long time;
    public Entity entity;

    public Event(Action action, long time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }

    public void removePendingEvent(
            EventScheduler scheduler)
    {
        List<Event> pending = scheduler.pendingEvents.get(entity);

        if (pending != null) {
            pending.remove(this);
        }
    }

}