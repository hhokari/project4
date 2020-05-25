import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity {

    protected final int ANIMATIONPERIOD;

    public AnimatedEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD);
        this.imageIndex = 0;
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
    }

    public int getAnimationPeriod() {
        return ANIMATIONPERIOD;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }
}
