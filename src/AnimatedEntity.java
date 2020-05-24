import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity implements Animate {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES;
    private int imageIndex;
    private final int ACTIONPERIOD;
    private final int ANIMATIONPERIOD;


    public AnimatedEntity(
            final String ID,
            Point position,
            final List<PImage> IMAGES,
            final int ACTIONPERIOD,
            final int ANIMATIONPERIOD)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
        this.ACTIONPERIOD = ACTIONPERIOD;
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
    }

    public int getAnimationPeriod() {
        return ANIMATIONPERIOD;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }
}
