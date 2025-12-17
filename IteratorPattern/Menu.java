package IteratorPattern;

import java.util.Iterator;

// ORTAK ARAYÜZ (Menu)
// Garsonun her iki menüyle de konuşabilmesini sağlar.
interface Menu {
    public Iterator<MenuItem> createIterator();
}