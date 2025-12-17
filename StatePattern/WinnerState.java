package StatePattern;

public class WinnerState implements State {
    GumballMachine gumballMachine;

    public WinnerState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Hata");
    }

    public void ejectQuarter() {
        System.out.println("Hata");
    }

    public void turnCrank() {
        System.out.println("Hata");
    }

    public void dispense() {
        System.out.println("TEBRİKLER! İki sakız kazandınız!");
        gumballMachine.releaseBall();
        if (gumballMachine.getCount() == 0) {
            gumballMachine.setState(gumballMachine.getSoldOutState());
        } else {
            gumballMachine.releaseBall(); // İkinci sakız
            if (gumballMachine.getCount() > 0) {
                gumballMachine.setState(gumballMachine.getNoQuarterState());
            } else {
                System.out.println("Oops, sakız bitti!");
                gumballMachine.setState(gumballMachine.getSoldOutState());
            }
        }
    }
}
