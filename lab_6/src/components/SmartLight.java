package components;

import annotations.Component;

@Component(name = "SmartLight", version = "1.2")
public class SmartLight {
    private String label;

    public SmartLight() {}

    public void execute() {
        System.out.println("[SmartLight] Світло ввімкнено: " + label);
    }

    @Override
    public String toString() {
        return "SmartLight{label='" + label + "'}";
    }
}