package simulation.traffic.elements.crossings;

import simulation.traffic.elements.cars.Car;
import simulation.traffic.model.utils.Command;

public interface Crossing {
    public void addCar(Car car);
    public void calcNewLightStatus();
    public void moveFirstCars();
    public void incrementWaitingTimes();
}
