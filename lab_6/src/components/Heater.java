package components;

public class Heater {
    private String label;

    public Heater() {}

    public void execute() {
        System.out.println("[Heater] Обігрів: " + label);
    }

    @Override
    public String toString() {
        return "Heater{label='" + label + "'}";
    }
}