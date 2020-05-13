import processing.core.PImage;

public interface Entity
{
    public Point getPosition();
    public void setPosition(Point position);
    public void nextImage();
    public PImage getCurrentImage();
}