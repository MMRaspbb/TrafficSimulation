package simulation.traffic.elements.cars;

import simulation.traffic.model.utils.MapDirection;
import simulation.traffic.model.utils.OutputWriter;

public class LeftTurningCar extends Car{
    public LeftTurningCar(String vehicleId, MapDirection startingRoad, MapDirection endRoad, OutputWriter outputWriter){
        super(vehicleId, startingRoad, endRoad, outputWriter);
    }

    @Override
    public void move() {
        System.out.println("Car with Id: " + getVehicleId() + " is moving left.");
    }
}
