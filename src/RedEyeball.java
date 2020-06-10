import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class RedEyeball extends EyeballEntity {
    public RedEyeball(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD, ANIMATIONPERIOD);
    }

    protected Optional<Entity> _executeActivityHelper(WorldModel world) {
        return world.findNearest(position, Eyeball.class);
    }
}
