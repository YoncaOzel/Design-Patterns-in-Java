package StatePattern;

public class SoldOutState implements State {
    GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Makine boş.");
    }

    public void ejectQuarter() {
        System.out.println("Para atamazsınız.");
    }

    public void turnCrank() {
        System.out.println("Sakız yok.");
    }

    public void dispense() {
        System.out.println("Sakız yok.");
    }
}
