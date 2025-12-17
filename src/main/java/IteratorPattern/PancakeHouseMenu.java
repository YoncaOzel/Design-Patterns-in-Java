package IteratorPattern;

import java.util.ArrayList;
import java.util.Iterator;

// 4. PANCAKE HOUSE MENU (ArrayList Kullanır)
class PancakeHouseMenu implements Menu {
    ArrayList<MenuItem> menuItems;

    public PancakeHouseMenu() {
        menuItems = new ArrayList<MenuItem>();
        addItem("K&B's Pancake Breakfast", "Yumurtalı ve tostlu pancake", true, 2.99);
        addItem("Regular Pancake Breakfast", "Sosisli ve yumurtalı pancake", false, 2.99);
    }

    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        menuItems.add(menuItem);
    }

    // ArrayList zaten Java'nın Iterator'ına sahip olduğu için işimiz çok kolay.
    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }
}
