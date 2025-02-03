package simulation.traffic.elements;

import org.junit.jupiter.api.Test;
import simulation.traffic.utils.MapDirection;
import simulation.traffic.utils.OutputWriter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LaneTest {
    private final OutputWriter outputWriter = new OutputWriter();

    @Test
    void laneQueueTest() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        Car car = new Car("carId", MapDirection.S, MapDirection.N, outputWriter);
        lane.addCar(car);
        assertEquals(1, lane.getCarCount());
        assertEquals(car, lane.getFirstCarInLane());
        assertEquals(1, lane.getCarCount());
        Car polledCar = lane.pollFirstCarInLane();
        assertEquals(car, polledCar);
        assertEquals(0, lane.getCarCount());
    }

    @Test
    void getBeginningTest() {
        Lane lane = new Lane(MapDirection.N, List.of(MapDirection.N, MapDirection.E), true);
        assertEquals(MapDirection.N, lane.getBeginning());
        lane = new Lane(MapDirection.E, List.of(MapDirection.N, MapDirection.E), true);
        assertEquals(MapDirection.E, lane.getBeginning());
        lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        assertEquals(MapDirection.S, lane.getBeginning());
        lane = new Lane(MapDirection.W, List.of(MapDirection.N, MapDirection.E), true);
        assertEquals(MapDirection.W, lane.getBeginning());
    }

    @Test
    void getDesitnationsTest() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        assertEquals(List.of(MapDirection.N, MapDirection.E), lane.getDesitnations());
        lane = new Lane(MapDirection.S, List.of(MapDirection.W, MapDirection.N), true);
        assertEquals(List.of(MapDirection.W, MapDirection.N), lane.getDesitnations());
    }

    @Test
    void getLightStateTest() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        assertTrue(lane.getLightState());
    }

    @Test
    void changeLightStateTest() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        lane.changeLightState();
        assertFalse(lane.getLightState());
    }

    @Test
    void incrementCarsWaitingTimeTest() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        Car car1 = new Car("1", MapDirection.S, MapDirection.N, outputWriter);
        Car car2 = new Car("1", MapDirection.S, MapDirection.E, outputWriter);
        lane.addCar(car1);
        lane.addCar(car2);
        lane.incrementCarsWaitingTime();
        assertEquals(1, car1.getAwaitingTime());
        assertEquals(1, car2.getAwaitingTime());
        lane.incrementCarsWaitingTime();
        assertEquals(2, car1.getAwaitingTime());
        assertEquals(2, car2.getAwaitingTime());
    }

    @Test
    void getLongestAwaitingTimeAmountToPush() {
        Lane lane = new Lane(MapDirection.S, List.of(MapDirection.N, MapDirection.E), true);
        for (int i = 0; i < 3; i++) {
            lane.addCar(new Car("vehicle", MapDirection.S, MapDirection.N, outputWriter));
        }
        for (int i = 0; i < 2; i++) {
            lane.incrementCarsWaitingTime();
        }
        for (int i = 0; i < 3; i++) {
            lane.addCar(new Car("vehicle", MapDirection.S, MapDirection.N, outputWriter));
        }
        lane.incrementCarsWaitingTime();
        for (int i = 0; i < 3; i++) {
            lane.addCar(new Car("vehicle", MapDirection.S, MapDirection.N, outputWriter));
        }
        for (int i = 0; i < 2; i++) {
            lane.incrementCarsWaitingTime();
        }
        int[] output = lane.getLongestAwaitingTimeAmountToPush(3);
        assertEquals(5, output[0]);
        assertEquals(6, output[1]);
    }
}