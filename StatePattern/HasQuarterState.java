package StatePattern;

import java.util.Random;

public class HasQuarterState implements State {
    GumballMachine gumballMachine;
    Random randomWinner = new Random(System.currentTimeMillis());

    public HasQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Zaten para attınız.");
    }

    public void ejectQuarter() {
        System.out.println("Para iade edildi.");
        gumballMachine.setState(gumballMachine.getNoQuarterState());
    }

    public void turnCrank() {
        System.out.println("Kolu çevirdiniz...");
        int winner = randomWinner.nextInt(10); // %10 şans
        if ((winner == 0) && (gumballMachine.getCount() > 1)) {
            gumballMachine.setState(gumballMachine.getWinnerState());
        } else {
            gumballMachine.setState(gumballMachine.getSoldState());
        }
    }

    public void dispense() {
        System.out.println("Sakız verilmedi.");
    }
}
