public class Factory {
    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, null, null,
                repeatCount);
    }

    public static Action createActivityAction(
            Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }
}
