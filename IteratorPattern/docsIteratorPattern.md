# Iterator Pattern - Kapsamlı Rehber

## 📋 Genel Bakış

Iterator Pattern, koleksiyonun dahili yapısını gizleyerek (Array, ArrayList vb.) elemanlarına erişmek için ortak bir arayüz sağlayan davranış tasarım desenidir.

Bu uygulamada **restoran menüsü** örneği kullanarak farklı veri yapılarını (Array ve ArrayList) aynı şekilde dolaşmayı gösteriyoruz.

---

## 🎯 Amaç ve Faydaları

| Amaç | Açıklama |
|------|----------|
| **Soyutlama** | Koleksiyonun iç yapısını gizler (Array, List, Set...) |
| **Birleşik Arayüz** | Tüm koleksiyonlar aynı Iterator arayüzünü kullanır |
| **Bağımsızlık** | İstemci kodunun veri yapısından bağımsız olması |
| **Esneklik** | Veri yapısı değişse bile istemci kodu değişmez |
| **Standardizasyon** | Tüm programda tutarlı dolaşma şekli |

---

## 🏗️ Mimari Yapı

```
┌─────────────────────────────────────┐
│  Iterator<MenuItem> (Arayüz)        │
│  ├── hasNext()                      │
│  └── next()                         │
└──────────────┬──────────────────────┘
               │
        ┌──────┴───────────────┐
        │                      │
   ┌────▼──────────┐    ┌─────▼──────────────┐
   │ ArrayList      │    │ Array              │
   │ Iterator       │    │ DinerMenuIterator  │
   │ (Java Built-in)    │ (Özel yazılmış)    │
   └────┬──────────┘    └──────┬─────────────┘
        │                      │
        └──────────┬───────────┘
                   │
         ┌─────────▼─────────┐
         │  Menu (Arayüz)     │
         │  ├── createIterator│
         └────┬────────┬─────┘
              │        │
    ┌─────────▼─┐  ┌──▼──────────────┐
    │ Pancake    │  │ Diner Menu      │
    │ HouseMenu  │  │ (Array Kullanır)│
    │(ArrayList) │  │                 │
    └──────┬─────┘  └────────┬────────┘
           │                 │
           └─────────┬───────┘
                     │
              ┌──────▼────────┐
              │   Waitress    │
              │ (İstemci Kodu)│
              └───────────────┘
```

---

## 💡 Kod Açıklaması

### 1️⃣ **MenuItem - Menü Öğesi**

```java
class MenuItem {
    String name;
    String description;
    boolean vegetarian;
    double price;

    public MenuItem(String name, String description, boolean vegetarian, double price) {
        this.name = name;
        this.description = description;
        this.vegetarian = vegetarian;
        this.price = price;
    }
    // Getter metodları...
}
```

**Rol:** Menüdeki her bir öğeyi temsil eder.

---

### 2️⃣ **Menu Interface - Ortak Arayüz**

```java
interface Menu {
    public Iterator<MenuItem> createIterator();
}
```

**Rol:** Her menü (Pancake, Diner) bu arayüzü implement ederek Iterator'ünü döndürmelidir.

---

### 3️⃣ **PancakeHouseMenu - ArrayList Kullanan Menü**

```java
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

    // ArrayList zaten Java'nın Iterator'ına sahip
    public Iterator<MenuItem> createIterator() {
        return menuItems.iterator();
    }
}
```

**Özellikler:**
- ✅ ArrayList kullanır (dinamik boyut)
- ✅ Java'nın yerleşik Iterator'ü ile çalışır
- ✅ Yeni eleman eklemek kolay

---

### 4️⃣ **DinerMenu - Array Kullanan Menü**

```java
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

    // Dizi için özel Iterator döndürür
    public Iterator<MenuItem> createIterator() {
        return new DinerMenuIterator(menuItems);
    }
}
```

**Özellikler:**
- ✅ Array kullanır (sabit boyut)
- ✅ Özel DinerMenuIterator'ü döndürür
- ✅ Kapasiteyi aşarsa hata verir

---

### 5️⃣ **DinerMenuIterator - Özel Iterator Implementasyonu**

```java
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
```

**Metodlar:**
- `hasNext()` → Sırada eleman var mı kontrol eder
- `next()` → Mevcut elemanı döner ve ilerler
- `remove()` → İsteğe bağlı işlem (biz desteklemiyoruz)

---

### 6️⃣ **Waitress - İstemci Kodu**

```java
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

    // POLİMORFİZM: Iterator türü ne olursa olsun aynı şekilde çalışır
    private void printMenu(Iterator<MenuItem> iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = iterator.next();
            System.out.println(menuItem.getName() + ", " + menuItem.getPrice() 
                             + " -- " + menuItem.getDescription());
        }
    }
}
```

**Önemli Noktalar:**
- ✅ Her menüden Iterator alır
- ✅ Iterator türünü bilmez (ArrayList mı Array mı)
- ✅ Aynı döngü ile her ikisini de dolaşır
- ✅ **Polimorfizm**: Farklı Iterator'lar aynı arayüzü implement eder

---

## 🔑 Temel Kavramlar

| Kavram | Açıklama | Örnek |
|--------|----------|-------|
| **Iterator** | Koleksiyonu dolaşan arayüz | `Iterator<MenuItem>` |
| **Collection** | Dolaşılacak elemanları saklayan nesne | `ArrayList`, `Array` |
| **Aggregate** | Iterator üreten nesne | `Menu` interface |
| **Polymorphism** | Farklı Iterator'lar aynı arayüzü kullanır | `hasNext()`, `next()` |
| **Encapsulation** | İç yapı gizlenir | Garson veri yapısını bilmez |

---

## 📊 Veri Akış Diyagramı

```
┌──────────────┐                    ┌──────────────┐
│  Pancake     │                    │   Diner      │
│  House Menu  │                    │   Menu       │
│              │                    │              │
│ ArrayList:   │                    │ Array:       │
│ [Item1]      │                    │ [Item1]      │
│ [Item2]      │                    │ [Item2]      │
│ [Item3]      │                    │ [Item3]      │
└──────┬───────┘                    └──────┬───────┘
       │                                   │
       │ createIterator()                  │ createIterator()
       │                                   │
       ▼                                   ▼
┌──────────────────────┐         ┌──────────────────────┐
│ ArrayList.iterator() │         │DinerMenuIterator     │
│ (Java Built-in)      │         │ (Custom)             │
│                      │         │                      │
│ position = 0         │         │ position = 0         │
│ items = [...]        │         │ items = [...]        │
└──────┬───────────────┘         └──────┬───────────────┘
       │                                │
       └────────────┬────────────────────┘
                    │
                    ▼
          ┌─────────────────────┐
          │     Waitress        │
          │  printMenu(iterator)│
          │                     │
          │ while(hasNext()) {  │
          │   next()            │
          │ }                   │
          └─────────────────────┘
                    │
                    ▼
          ┌─────────────────────┐
          │    Konsol Çıktı     │
          │                     │
          │ Tüm menü öğeleri    │
          │ sıra ile yazdırılır │
          └─────────────────────┘
```

---

## 🎓 Öğrenme Noktaları

### **Neden Iterator Pattern Gerekli?**

**Sorun (Without Pattern):**
```java
// Her koleksiyon türü için ayrı kod yazmalıyız
if (collection instanceof ArrayList) {
    for (int i = 0; i < list.size(); i++) {
        MenuItem item = list.get(i);
        // İşlem...
    }
} else if (collection instanceof Array) {
    for (int i = 0; i < array.length; i++) {
        MenuItem item = array[i];
        // İşlem...
    }
}
```

**Çözüm (With Pattern):**
```java
// Ortak Iterator arayüzü ile
Iterator<MenuItem> iterator = menu.createIterator();
while (iterator.hasNext()) {
    MenuItem item = iterator.next();
    // İşlem...
}
```

---

## ✅ Avantajları

- 🎯 **Erişim Abstraksiyon**: Koleksiyonun iç yapısı gizlenir
- 🔄 **Tutarlı Arayüz**: Tüm koleksiyonlar aynı şekilde dolaşılır
- 🏗️ **Kolayca Genişletme**: Yeni koleksiyon türleri eklemek basit
- 📦 **Bağımsızlık**: İstemci kodu veri yapısından bağımsız
- 🔐 **Kapsülleme**: İç veriler korunur, sadece Iterator aracılığıyla erişilir

---

## ❌ Dezavantajları

- 📚 **Fazla Kompleksiklik**: Basit koleksiyonlar için gereksiz olabilir
- ⚡ **Performans**: Her element için metod çağrısı yapılır
- 🔒 **Sınırlı Erişim**: Iterator ile sadece ileri doğru dolaşabilirsiniz
- 💾 **Bellek**: Her Iterator için ayrı nesne oluşturulur

---

## 📈 Gerçek Dünya Kullanım Örnekleri

### 1️⃣ **Veritabanı Sorgular**
```java
// Sonuçları Iterator ile dolaş
ResultSet results = statement.executeQuery();
while (results.next()) {
    // Her satırı işle
}
```

### 2️⃣ **Dosya Sistemi**
```java
// Klasördeki dosyaları Iterator ile dolaş
File[] files = directory.listFiles();
for (File file : files) {
    // Her dosyayı işle
}
```

### 3️⃣ **Web Framework'ler**
```java
// Sayfa koleksiyonunda gezinme
Iterator<Page> pages = website.getPages();
while (pages.hasNext()) {
    Page page = pages.next();
    // Sayfayı işle
}
```

### 4️⃣ **Ağaç Yapıları**
```java
// DOM ağacında dolaşma (HTML/XML)
Iterator<Element> elements = document.getElements();
while (elements.hasNext()) {
    Element element = elements.next();
    // Element'i işle
}
```

### 5️⃣ **E-ticaret Sistemleri**
```java
// Alışveriş sepeti öğeleri
Iterator<CartItem> items = cart.getItems();
while (items.hasNext()) {
    CartItem item = items.next();
    totalPrice += item.getPrice();
}
```

---

## 🚀 Çalıştırma

### **Derleme:**
```bash
javac .\IteratorPattern\*.java
```

### **Çalıştırma:**
```bash
java -cp . IteratorPattern.IteratorPatternTest
```

### **Beklenen Çıktı:**
```
MENU
----
KAHVALTI
K&B's Pancake Breakfast, 2.99 -- Yumurtalı ve tostlu pancake
Regular Pancake Breakfast, 2.99 -- Sosisli ve yumurtalı pancake

ÖĞLE YEMEĞİ
Vegetarian BLT, 2.99 -- Vejetaryen pastırmalı sandviç
BLT, 2.99 -- Klasik pastırmalı sandviç
Soup of the day, 3.29 -- Günün çorbası ve patates salatası
```

---

## 🔍 Çalışma Akışı Özeti

```
1. Garson (Waitress) her menüden Iterator ister
   ↓
2. PancakeHouseMenu → ArrayList.iterator() döner
   DinerMenu → DinerMenuIterator döner
   ↓
3. Garson aynı printMenu() metodunu her Iterator ile çağırır
   ↓
4. while (iterator.hasNext()) ile sıra ile öğeler getirilir
   ↓
5. Konsola tüm öğeler yazdırılır
```

---

## 🎯 Sonuç

Iterator Pattern, koleksiyonların iç yapısını gizleyerek, tutarlı bir şekilde elemanlarına erişim sağlar.

**Bu örneğin gösterdiği:**
- ✅ ArrayList ve Array'i aynı şekilde dolaşıyoruz
- ✅ Garson kodu hiç değişmemiş
- ✅ Yeni menü türü eklemek kolay (sadece Iterator impl. gerekli)
- ✅ Polimorfizm sayesinde esneklik sağlanıyor

**Sonuç: Farklı veri yapılarında tutarlı dolaşma sağlanır!** 🎉
