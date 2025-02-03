package simulation.traffic.elements;

public class Light {
    private boolean greenLightOn;

    public Light(boolean initialLightState) {
        greenLightOn = initialLightState;
    }

    public void changeLightState() {
        greenLightOn = !greenLightOn;
    }

    public boolean isGreenLightOn() {
        return greenLightOn;
    }
}
