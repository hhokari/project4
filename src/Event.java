import java.util.List;

public final class Event
{
    private final Action ACTION;
    private final long TIME;
    private final Entity ENTITY;

    public Event(Action ACTION, long TIME, Entity ENTITY) {
        this.ACTION = ACTION;
        this.TIME = TIME;
        this.ENTITY = ENTITY;
    }

    public void removePendingEvent(
            EventScheduler scheduler)
    {
        List<Event> pending = scheduler.getPendingEvents().get(ENTITY);

        if (pending != null) {
            pending.remove(this);
        }
    }

    public Action getAction()
    {
        return ACTION;
    }

    public long getTime()
    {
        return TIME;
    }
}