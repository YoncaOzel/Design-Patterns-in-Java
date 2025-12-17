package IteratorPattern;

import java.util.Iterator;

// 6. İSTEMCİ (Waitress)
class Waitress {
    Menu pancakeHouseMenu;
    Menu dinerMenu;

    public Waitress(Menu pancakeHouseMenu, Menu dinerMenu) {
        this.pancakeHouseMenu = pancakeHouseMenu;
        this.dinerMenu = dinerMenu;
    }

    public void printMenu() {
        Iterator<MenuItem> pancakeIterator = pancakeHouseMenu.createIterator();
        Iterator<MenuItem> dinerIterator = dinerMenu.createIterator();

        System.out.println("MENU\n----\nKAHVALTI");
        printMenu(pancakeIterator);
        System.out.println("\nÖĞLE YEMEĞİ");
        printMenu(dinerIterator);
    }

    // POLİMORFİZM: Bu metod parametre olarak ne tür bir iterator geldiğini bilmez (Array mi List mi?)
    // Sadece hasNext() ve next() çağırır.
    private void printMenu(Iterator<MenuItem> iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = iterator.next();
            System.out.println(menuItem.getName() + ", " + menuItem.getPrice() + " -- " + menuItem.getDescription());
        }
    }
}