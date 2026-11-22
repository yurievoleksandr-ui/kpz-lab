package components;

import annotations.Component;

@Component(name = "VoiceSpeaker", version = "1.5")
public class VoiceSpeaker {
    private String label;

    public VoiceSpeaker() {}

    public void execute() {
        System.out.println("[VoiceSpeaker] Колонка слухає команди: " + label);
    }

    @Override
    public String toString() {
        return "VoiceSpeaker{label='" + label + "'}";
    }
}