package IteratorPattern;

import java.util.Iterator;

// Java dizilerinin (Array) standart bir iterator'ı olmadığı için bunu elle yazarız.
class DinerMenuIterator implements Iterator<MenuItem> {
    MenuItem[] items;
    int position = 0;

    public DinerMenuIterator(MenuItem[] items) {
        this.items = items;
    }

    // Sırada eleman var mı?
    public boolean hasNext() {
        if (position >= items.length || items[position] == null) {
            return false;
        } else {
            return true;
        }
    }

    // Sıradaki elemanı ver ve bir adım ilerle
    public MenuItem next() {
        MenuItem menuItem = items[position];
        position = position + 1;
        return menuItem;
    }

    public void remove() {
        throw new UnsupportedOperationException("Silme işlemi desteklenmiyor.");
    }
}
