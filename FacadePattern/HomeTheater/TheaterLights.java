package FacadePattern.HomeTheater;

public class TheaterLights {
    public void dim(int level) {
        System.out.println("Theater Ceiling Lights dimming to " + level + "%");
    }

    public void on() {
        System.out.println("Theater Ceiling Lights on");
    }
}