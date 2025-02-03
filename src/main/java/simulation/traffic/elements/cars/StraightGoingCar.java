package simulation.traffic.elements.cars;

import simulation.traffic.model.utils.MapDirection;
import simulation.traffic.model.utils.OutputWriter;

public class StraightGoingCar extends Car{
    public StraightGoingCar(String vehicleId, MapDirection startingRoad, MapDirection endRoad, OutputWriter outputWriter){
        super(vehicleId, startingRoad, endRoad, outputWriter);
    }

    @Override
    public void move() {
        System.out.println("Car with Id: " + getVehicleId() + " is moving forward.");
    }
}
