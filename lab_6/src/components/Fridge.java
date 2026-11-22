package components;

public class Fridge {
    private String label;

    public Fridge() {}

    public void execute() {
        System.out.println("[Fridge] Охолодження: " + label);
    }

    @Override
    public String toString() {
        return "Fridge{label='" + label + "'}";
    }
}