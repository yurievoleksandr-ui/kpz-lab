package components;

public class SmartCurtain {
    private String label;

    public SmartCurtain() {}

    public void execute() {
        System.out.println("[SmartCurtain] Штори відкрито: " + label);
    }

    @Override
    public String toString() {
        return "SmartCurtain{label='" + label + "'}";
    }
}