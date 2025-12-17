package StatePattern;

public class GumballMachine {
    State soldOutState;
    State noQuarterState;
    State hasQuarterState;
    State soldState;
    State winnerState; // Yeni eklenen durum

    State state;
    int count = 0;

    public GumballMachine(int numberGumballs) {
        soldOutState = new SoldOutState(this);
        noQuarterState = new NoQuarterState(this);
        hasQuarterState = new HasQuarterState(this);
        soldState = new SoldState(this);
        winnerState = new WinnerState(this);

        this.count = numberGumballs;
        if (numberGumballs > 0) {
            state = noQuarterState;
        } else {
            state = soldOutState;
        }
    }

    // Eylemler (Delegasyon)
    public void insertQuarter() {
        state.insertQuarter();
    }

    public void ejectQuarter() {
        state.ejectQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense(); // Çevirme işlemi dispense'i tetikler
    }

    // Durum Değiştirme ve Yardımcı Metotlar
    void setState(State state) {
        this.state = state;
    }

    void releaseBall() {
        System.out.println("Bir sakız yuvaya yuvarlanıyor...");
        if (count != 0) {
            count = count - 1;
        }
    }

    // Getter metodları (Durum sınıfları için gerekli)
    public State getSoldOutState() {
        return soldOutState;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public State getSoldState() {
        return soldState;
    }

    public State getWinnerState() {
        return winnerState;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return "\nMighty Gumball, Inc.\nStok: " + count + " sakız\nDurum: " + state + "\n";
    }
}