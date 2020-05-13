public interface Move extends Entity {
    boolean move(WorldModel world, Entity target, EventScheduler scheduler);
}
