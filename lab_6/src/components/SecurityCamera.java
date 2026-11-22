package components;

import annotations.Component;

@Component(name = "SecurityCamera", version = "3.1")
public class SecurityCamera {
    private String label;

    public SecurityCamera() {}

    public void execute() {
        System.out.println("[SecurityCamera] Запис розпочато: " + label);
    }

    @Override
    public String toString() {
        return "SecurityCamera{label='" + label + "'}";
    }
}