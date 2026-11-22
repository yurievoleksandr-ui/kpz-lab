package components;

import annotations.Component;

@Component(name = "DoorLock", version = "1.0")
public class DoorLock {
    private String label;

    public DoorLock() {}

    public void execute() {
        System.out.println("[DoorLock] Двері замкнено: " + label);
    }

    @Override
    public String toString() {
        return "DoorLock{label='" + label + "'}";
    }
}