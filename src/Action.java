//public interface Action
//{
//    void executeAction(EventScheduler scheduler);
//}

public abstract class Action {

    protected abstract void executeAction(EventScheduler scheduler);
}
