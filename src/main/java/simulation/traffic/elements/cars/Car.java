package simulation.traffic.elements.cars;

import simulation.traffic.model.utils.CarDirection;
import simulation.traffic.model.utils.MapDirection;
import simulation.traffic.model.utils.OutputWriter;

public class Car {
    private MapDirection startRoad;
    private MapDirection endRoad;
    private String vehicleId;
    private OutputWriter outputWriter = new OutputWriter();
    private CarDirection direction;
    private int awaitingTime = 0;
    public Car(String vehicleId, MapDirection startRoad, MapDirection endRoad, OutputWriter outputWriter) {
        this.vehicleId = vehicleId;
        this.startRoad = startRoad;
        this.endRoad = endRoad;
        this.outputWriter = outputWriter;
        if(startRoad.spinClockwise(1) == endRoad){
            direction = CarDirection.LEFT;
        }
        else if(startRoad.spinClockwise(2) == endRoad){
            direction = CarDirection.FORWARD;
        }
        else{
            direction = CarDirection.RIGHT;
        }
    }

    public MapDirection getStartRoad() {
        return startRoad;
    }

    public CarDirection getDirection() {
        return direction;
    }
    public MapDirection getEndRoad() {
        return endRoad;
    }

    public String getVehicleId() {
        return vehicleId;
    }
    public void move(){
        outputWriter.updateCarsInDestination(this.toString());
    }
    public void incrementAwaitingTime(){
        awaitingTime++;
    }
    public int getAwaitingTime(){
        return awaitingTime;
    }
    @Override
    public String toString(){
        return vehicleId;
    }
}
