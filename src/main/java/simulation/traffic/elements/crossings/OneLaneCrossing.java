package simulation.traffic.elements.crossings;

import simulation.traffic.elements.Lane;
import simulation.traffic.elements.Light;
import simulation.traffic.elements.cars.Car;
import simulation.traffic.model.utils.CarDirection;
import simulation.traffic.model.utils.MapDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OneLaneCrossing implements Crossing {
    private static final int defaultCycle = 5;
    private static final List<Boolean> initialGreenLights = List.of(true, false, true, false);
    private final List<Lane> lanes = new ArrayList<>(); //0: north, 1: east, 2: south, 3: west
    private int stepsToCyclesEnd = defaultCycle;

    public OneLaneCrossing() {
        for (int i = 0; i < 4; i++) {
            lanes.add(makeLane(MapDirection.intToDirection(i)));
        }
    }

    private Lane makeLane(MapDirection beginning) {
        List<MapDirection> destinations = new ArrayList<>();
        for(MapDirection direction: MapDirection.values()){
            if(direction != beginning){
                destinations.add(direction);
            }
        }
        return new Lane(beginning, destinations, initialGreenLights.get(MapDirection.parseDirection(beginning)));
    }

    @Override
    public void addCar(Car car) {
        lanes.get(MapDirection.parseDirection(car.getStartRoad())).addCar(car);
    }

    @Override
    public void calcNewLightStatus() {
        int carsOnActiveLanes = 0;
        int carsOnInactiveLanes = 0;
        for(Lane lane: lanes){
            if(lane.getLightState()) carsOnActiveLanes += lane.getCarCount();
            else carsOnInactiveLanes += lane.getCarCount();
        }
        if(carsOnInactiveLanes == 0 && stepsToCyclesEnd == 1) {
            return;
        }
        else if(carsOnActiveLanes == 0){
            stepsToCyclesEnd = 1;
        }
        stepsToCyclesEnd--;
        int averageCarsPerLane = (carsOnActiveLanes + carsOnInactiveLanes) / 4;
        switchLightsIfNecessary(averageCarsPerLane);
    }
    private void switchLightsIfNecessary(int averageCarsPerLane){
        if (stepsToCyclesEnd == 0) {
            int newCycleTime = 0;
            for (Lane lane : lanes) {
                if(!lane.getLightState()){
                    newCycleTime = Math.max(newCycleTime, lane.getLongestAwaitingTimeAmountToPush(defaultCycle)[1]);
                }
                lane.changeLightState();
            }
            stepsToCyclesEnd = averageCarsPerLane;
            if(newCycleTime > averageCarsPerLane){
                stepsToCyclesEnd = Math.min(newCycleTime, averageCarsPerLane * 2);
            }
            System.out.println(stepsToCyclesEnd);
        }
    }
    public void incrementWaitingTimes(){
        for(Lane lane: lanes){
            lane.incrementCarsWaitingTime();
        }
    }

    @Override
    public void moveFirstCars() {
        ArrayList<Lane> lanesToMoveFirstCars = new ArrayList<>();
        for (Lane lane : lanes) {
            Car car = lane.getFirstCarInLane();
            if (carCanMove(car, lane)) {
                lanesToMoveFirstCars.add(lane);
            }
        }
        for (Lane lane : lanesToMoveFirstCars) {
            lane.pollFirstCarInLane().move();
        }
    }

    private boolean carCanMove(Car car, Lane lane) {
        if (car == null) {
            return false;
        }
        if (car.getDirection() == CarDirection.FORWARD && lane.getLightState()) {
            return true;
        }
        if (car.getDirection() == CarDirection.RIGHT) {
            if (lane.getLightState()) {
                return true;
            }
            Lane laneOnTheLeft = lanes.get(MapDirection.parseDirection(lane.getBeginning().spinClockwise(1)));
            int laneOnTheLeftCarCount = laneOnTheLeft.getCarCount();
            if (laneOnTheLeftCarCount == 0) {
                return true;
            }
            Car firstCarOnLeftLane = laneOnTheLeft.getFirstCarInLane();
            if(firstCarOnLeftLane.getDirection() != CarDirection.FORWARD){
                return true;
            }
            return false;
        }
        if (!lane.getLightState()) {
            return false;
        }
        int laneOnTheOppositeIndex = MapDirection.parseDirection(lane.getBeginning().spinClockwise(2));
        if(lanes.get(laneOnTheOppositeIndex).getCarCount() == 0){
            return true;
        }
        CarDirection firstCarOnTheOppositeLaneDirection = lanes.get(laneOnTheOppositeIndex).getFirstCarInLane().getDirection();
        if (firstCarOnTheOppositeLaneDirection == CarDirection.LEFT || firstCarOnTheOppositeLaneDirection == CarDirection.RIGHT) {
            return true;
        }
        return false;
    }

}
