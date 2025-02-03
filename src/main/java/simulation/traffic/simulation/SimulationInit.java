package simulation.traffic.simulation;

import org.json.JSONObject;
import org.json.JSONTokener;
import simulation.traffic.exceptions.JsonVerificationException;
import simulation.traffic.utils.Command;
import simulation.traffic.utils.JsonVerifier;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class SimulationInit {
    public static void init(String[] args) throws JsonVerificationException {
        try (Reader reader = new FileReader(args[0])) {
            Command[] commands = JsonVerifier.verifyInputJson(new JSONObject(new JSONTokener(reader)));
            int crossingType = 1;
            if (args.length > 2 && args[2].equals("2")) {
                crossingType = 2;
            }
            Simulation.run(commands, crossingType, args[1]);
        } catch (IOException e) {
            throw new JsonVerificationException(e.getMessage());
        }
    }
}
