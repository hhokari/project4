import processing.core.PImage;

public interface Entity
{
    Point getPosition();
    void setPosition(Point position);
//    void nextImage();
    PImage getCurrentImage();
}