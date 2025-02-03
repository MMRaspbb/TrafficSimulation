package simulation.traffic.elements;

import simulation.traffic.elements.cars.Car;
import simulation.traffic.model.utils.MapDirection;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Lane {
    private MapDirection beginning;
    private List<MapDirection> desitnations;
    private Queue<Car> carQueue = new ArrayDeque<>();
    private boolean greenLightOn = false;
    private Light light;

    public Lane(MapDirection beginning, List<MapDirection> destinations, Boolean initLightState) {
        this.beginning = beginning;
        this.desitnations = destinations;
        this.light = new Light(initLightState);
    }

    public void addCar(Car car) {
        carQueue.add(car);
    }

    public int getCarCount() {
        return carQueue.size();
    }

    public MapDirection getBeginning() {
        return beginning;
    }

    public List<MapDirection> getDesitnations() {
        return desitnations;
    }

    public void changeLightState() {
        light.changeLightState();
    }

    public boolean getLightState() {
        return light.isGreenLightOn();
    }

    public Car getFirstCarInLane() {
        return carQueue.peek();
    }

    public Car pollFirstCarInLane() {
        return carQueue.poll();
    }

    public void incrementCarsWaitingTime() {
        for (Car car : carQueue) {
            car.incrementAwaitingTime();
        }
    }

    public int[] getLongestAwaitingTimeAmountToPush(int timeDelay){
        Car firstCar = getFirstCarInLane();
        if(firstCar == null) return new int[]{0, 0};
        int longestAwaitingTime = carQueue.peek().getAwaitingTime();
        int longestAwaitingCarsCount = 0;
        int secondLongestAwaitingCarsCount = 0;
        for(Car car: carQueue){
            if(car.getAwaitingTime() == longestAwaitingTime) longestAwaitingCarsCount++;
            else if(car.getAwaitingTime() > longestAwaitingCarsCount - timeDelay) secondLongestAwaitingCarsCount++;
            else break;
        }
        return new int[]{longestAwaitingTime, longestAwaitingCarsCount + (secondLongestAwaitingCarsCount / 2)};
    }
}
