package FacadePattern.HomeTheater;

public class HomeTheaterTestDrive {
    public static void main(String[] args) {
        Amplifier amp = new Amplifier();
        DvdPlayer dvd = new DvdPlayer();
        Projector projector = new Projector();
        Screen screen = new Screen();
        TheaterLights lights = new TheaterLights();
        PopcornPopper popper = new PopcornPopper();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(amp, dvd, projector, screen, lights, popper);

        homeTheater.watchMovie("Winter Sleep");

        System.out.println("\n--- Film finished ---\n");

        homeTheater.endMovie();
    }
}
