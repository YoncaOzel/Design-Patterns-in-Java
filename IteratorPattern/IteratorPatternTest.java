package IteratorPattern;

public class IteratorPatternTest {
    public static void main(String[] args) {
        PancakeHouseMenu pancakeMenu = new PancakeHouseMenu();
        DinerMenu dinerMenu = new DinerMenu();

        Waitress waitress = new Waitress(pancakeMenu, dinerMenu);

        // Garson, arkada Array mi ArrayList mi döndüğünü bilmeden her ikisini de yazar.
        waitress.printMenu();
    }
}
