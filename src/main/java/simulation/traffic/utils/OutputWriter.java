package simulation.traffic.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {
    private List<String> carsInDestination = new ArrayList<>();
    private final List<StepStatus> stepStatuses = new ArrayList<>();

    private class StepStatus {
        public List<String> leftVehicles;

        public StepStatus(List<String> leftVehicles) {
            this.leftVehicles = leftVehicles;
        }
    }

    private class SimulationResult {
        public List<StepStatus> stepStatuses;

        public SimulationResult(List<StepStatus> stepStatuses) {
            this.stepStatuses = stepStatuses;
        }
    }

    public void updateCarsInDestination(String vehicleId) {
        carsInDestination.add(vehicleId);
    }

    public void update() {
        stepStatuses.add(new StepStatus(carsInDestination));
        carsInDestination = new ArrayList<>();
    }

    public void saveJson(String path) throws IOException {
        SimulationResult simulationResult = new SimulationResult(stepStatuses);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        File file = new File(path);
        writer.writeValue(file, simulationResult);
    }

}
