package simulation.traffic.simulation;

import simulation.traffic.elements.Car;
import simulation.traffic.elements.crossings.Crossing;
import simulation.traffic.elements.crossings.OneLaneCrossing;
import simulation.traffic.elements.crossings.TwoLaneCrossing;
import simulation.traffic.utils.Command;
import simulation.traffic.utils.OutputWriter;

import java.io.IOException;

public class Simulation {
    public static void run(Command[] commands, int crossingType, String outputPath) throws IOException {
        Crossing crossing;
        OutputWriter outputWriter = new OutputWriter();
        if (crossingType == 1) {
            crossing = new OneLaneCrossing();
        } else {
            crossing = new TwoLaneCrossing();
        }
        for (Command command : commands) {
            switch (command.type()) {
                case STEP -> {
                    crossing.moveFirstCars();
                    crossing.incrementWaitingTimes();
                    outputWriter.update();
                    crossing.calcNewLightStatus();
                }
                case ADDVEHICLE -> {
                    Car car = new Car(command.vehicleId(), command.startRoad(), command.endRoad(), outputWriter);
                    crossing.addCar(car);
                }
            }
        }
        outputWriter.saveJson(outputPath);
    }
}
