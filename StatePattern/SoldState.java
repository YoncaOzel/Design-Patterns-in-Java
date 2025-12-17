package StatePattern;

public class SoldState implements State {
    GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Lütfen bekleyin, sakız veriliyor.");
    }

    public void ejectQuarter() {
        System.out.println("Üzgünüm, kolu zaten çevirdiniz.");
    }

    public void turnCrank() {
        System.out.println("İki kere çevirmek size bir şey kazandırmaz!");
    }

    public void dispense() {
        gumballMachine.releaseBall();
        if (gumballMachine.getCount() > 0) {
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        } else {
            System.out.println("Oops, sakız bitti!");
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}
