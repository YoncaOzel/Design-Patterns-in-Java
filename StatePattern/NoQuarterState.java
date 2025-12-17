package StatePattern;

public class NoQuarterState implements State {
    GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Para attınız.");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }

    public void ejectQuarter() {
        System.out.println("Para atmadınız.");
    }

    public void turnCrank() {
        System.out.println("Para yoksa sakız da yok.");
    }

    public void dispense() {
        System.out.println("Önce ödeme yapın.");
    }
}
