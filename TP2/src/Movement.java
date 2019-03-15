import java.util.HashMap;
import java.util.Map;

public class Movement {

    private Coordinates velocity;
    private Map<Double, Coordinates> positionInTime;

    public Movement(Coordinates coordinates){
        this.positionInTime = new HashMap<>();
        this.velocity = coordinates;
    }

    public void Move (Double time, Double x, Double y){
        Coordinates newPosition = new Coordinates(x*velocity.getX(), y*velocity.getY());
        positionInTime.put(time,newPosition);
    }

    public Map<Double, Coordinates> getPositionInTime() {
        return positionInTime;
    }
}
