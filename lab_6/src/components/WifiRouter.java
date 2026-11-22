package components;

public class WifiRouter {
    private String label;

    public WifiRouter() {}

    public void execute() {
        System.out.println("[WifiRouter] Мережа активна: " + label);
    }

    @Override
    public String toString() {
        return "WifiRouter{label='" + label + "'}";
    }
}