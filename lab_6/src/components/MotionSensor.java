package components;

import annotations.Component;

@Component(name = "MotionSensor", version = "0.9")
public class MotionSensor {
    private String label;

    public MotionSensor() {}

    public void execute() {
        System.out.println("[MotionSensor] Рух зафіксовано: " + label);
    }

    @Override
    public String toString() {
        return "MotionSensor{label='" + label + "'}";
    }
}