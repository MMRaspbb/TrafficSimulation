package simulation.traffic.elements.crossings;

import simulation.traffic.elements.Car;

public interface Crossing {
    void addCar(Car car);

    void calcNewLightStatus();

    void moveFirstCars();

    void incrementWaitingTimes();
}
