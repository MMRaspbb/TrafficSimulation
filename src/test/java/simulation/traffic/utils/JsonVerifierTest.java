package simulation.traffic.utils;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import simulation.traffic.exceptions.JsonVerificationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class JsonVerifierTest {
    private static JSONObject testData;

    @BeforeAll
    static void loadTestData() throws IOException {
        Reader reader = new FileReader("src/test/resources/json_verifier_data.json");
        testData = new JSONObject(new JSONTokener(reader));
    }

    @Test
    void correctInputTest1() {
        JSONObject jsonObject = testData.getJSONObject("correct_input_1");
        try {
            Command[] commands = JsonVerifier.verifyInputJson(jsonObject);
            Command[] correctCommands = {new Command(Type.ADDVEHICLE, "vehicle1", MapDirection.W, MapDirection.E)};
            assertArrayEquals(correctCommands, commands);
        } catch (JsonVerificationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void correctInputTest2() {
        JSONObject jsonObject = testData.getJSONObject("correct_input_2");
        try {
            Command[] commands = JsonVerifier.verifyInputJson(jsonObject);
            Command[] correctCommands = {new Command(Type.ADDVEHICLE, "vehicle2", MapDirection.N, MapDirection.S)};
            assertArrayEquals(correctCommands, commands);
        } catch (JsonVerificationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void correctInputTest3() {
        JSONObject jsonObject = testData.getJSONObject("correct_input_3");
        try {
            Command[] commands = JsonVerifier.verifyInputJson(jsonObject);
            Command[] correctCommands = {new Command(Type.STEP)};
            assertArrayEquals(correctCommands, commands);
        } catch (JsonVerificationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void incorrectCommandTypeTest() {
        JSONObject jsonObject = testData.getJSONObject("incorrect_command_input");
        try {
            Command[] commands = JsonVerifier.verifyInputJson(jsonObject);
            fail();
        } catch (JsonVerificationException e) {
            assertEquals("Command has invalid type.", e.getMessage());
        }
    }

    @Test
    void incorrectRoadDirectionTest() {
        JSONObject jsonObject = testData.getJSONObject("incorrect_road_direction_input");
        try {
            Command[] commands = JsonVerifier.verifyInputJson(jsonObject);
            fail();
        } catch (JsonVerificationException e) {
            assertEquals("Command has incorrect road direction", e.getMessage());
        }
    }
}