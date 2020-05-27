import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity {

    protected final int ANIMATIONPERIOD;
    protected int imageIndex;

    public AnimatedEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        super(ID, position, IMAGES, ACTIONPERIOD);
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
        this.imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }

    public int getAnimationPeriod() {
        return ANIMATIONPERIOD;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }
}
