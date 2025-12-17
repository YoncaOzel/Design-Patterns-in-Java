package IteratorPattern;

import java.util.Iterator;

class DinerMenu implements Menu {
    static final int MAX_ITEMS = 6;
    int numberOfItems = 0;
    MenuItem[] menuItems;

    public DinerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];
        addItem("Vegetarian BLT", "Vejetaryen pastırmalı sandviç", true, 2.99);
        addItem("BLT", "Klasik pastırmalı sandviç", false, 2.99);
        addItem("Soup of the day", "Günün çorbası ve patates salatası", false, 3.29);
    }

    public void addItem(String name, String description, boolean vegetarian, double price) {
        if (numberOfItems >= MAX_ITEMS) {
            System.err.println("Üzgünüm, menü dolu!");
        } else {
            menuItems[numberOfItems] = new MenuItem(name, description, vegetarian, price);
            numberOfItems = numberOfItems + 1;
        }
    }

    // Dizi için yazdığımız özel iterator'ı döndürür.
    public Iterator<MenuItem> createIterator() {
        return new DinerMenuIterator(menuItems);
    }
}
