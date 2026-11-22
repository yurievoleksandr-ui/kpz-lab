package components;

import annotations.Component;

@Component(name = "Thermostat", version = "2.0")
public class Thermostat {
    private String label;

    public Thermostat() {}

    public void execute() {
        System.out.println("[Thermostat] Температуру встановлено: " + label);
    }

    @Override
    public String toString() {
        return "Thermostat{label='" + label + "'}";
    }
}