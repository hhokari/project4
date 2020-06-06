public final class Point
{
    public int X;
    public int Y;

    public Point(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public String toString() {
        return "(" + X + "," + Y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).X == this.X
                && ((Point)other).Y == this.Y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + X;
        result = result * 31 + Y;
        return result;
    }
}