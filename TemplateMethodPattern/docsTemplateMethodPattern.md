# Template Method Pattern

## 📋 Genel Bakış

Template Method Pattern, bir algoritmanın iskeletini tanımlayan ve adımlarının bazılarını alt sınıflara bırakıp, genel yapısını koruyyan bir davranış tasarım desenidir.

Bu uygulamada **kahve ve çay hazırlama** örneği kullanarak paternin temel konseptlerini gösteriyoruz.

---

## 🎯 Amaç ve Faydaları

| Amaç | Açıklama |
|------|----------|
| **Kod Yeniden Kullanımı** | Ortak algoritma adımları ana sınıfta tanımlanır |
| **Değişkenlik Kontrolü** | Alt sınıflar sadece gerekli kısımları özelleştirebilir |
| **Hook Mekanizması** | İsteğe bağlı adımları kontrol etmek için hook metodları sunulur |
| **Tutarlılık** | Tüm alt sınıflar aynı yapıyı takip eder |

---

## 🏗️ Mimari Yapı

### **ASCII Mimarisi**

```
CaffeineBeverageWithHook (Soyut Ana Sınıf)
    ├── prepareRecipe() [final]           ← Template Method (İskelet)
    ├── abstract brew()                   ← Alt sınıflar uygulamalı
    ├── abstract addCondiments()          ← Alt sınıflar uygulamalı
    ├── boilWater() [concrete]            ← Ortak işlem
    ├── pourInCup() [concrete]            ← Ortak işlem
    └── customerWantsCondiments() [hook]  ← İsteğe bağlı override
            │
            ├── TeaWithHook
            │   ├── brew()           → "Steeping the tea"
            │   └── addCondiments()  → "Adding Lemon"
            │
            └── CoffeeWithHook
                ├── brew()           → "Dripping Coffee through filter"
                ├── addCondiments()  → "Adding Sugar and Milk"
                └── customerWantsCondiments() [override]  → Kullanıcıdan sorar
```

### **UML Class Diyagramı**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │         <<abstract>>                                             │  │
│  │   CaffeineBeverageWithHook                                       │  │
│  │  ┌────────────────────────────────────────────────────────────┐ │  │
│  │  │ # TEMPLATE METHOD (Final)                                 │ │  │
│  │  │ + prepareRecipe(): void [final]                           │ │  │
│  │  │   ├─► boilWater()                                         │ │  │
│  │  │   ├─► brew() [ABSTRACT]                                  │ │  │
│  │  │   ├─► pourInCup()                                        │ │  │
│  │  │   └─► if(customerWantsCondiments()) addCondiments()     │ │  │
│  │  │                                                           │ │  │
│  │  │ # ABSTRACT METHODS (Alt sınıflar ZORUNLU implement)      │ │  │
│  │  │ + abstract brew(): void                                  │ │  │
│  │  │ + abstract addCondiments(): void                         │ │  │
│  │  │                                                           │ │  │
│  │  │ # CONCRETE METHODS (Ortak işlemler)                      │ │  │
│  │  │ + boilWater(): void                                      │ │  │
│  │  │ + pourInCup(): void                                      │ │  │
│  │  │                                                           │ │  │
│  │  │ # HOOK METHOD (İsteğe bağlı override)                    │ │  │
│  │  │ + customerWantsCondiments(): boolean                     │ │  │
│  │  │   (Varsayılan: return true;)                             │ │  │
│  │  └────────────────────────────────────────────────────────────┘ │  │
│  │                        △ extends                                  │  │
│  │          ┌─────────────┴────────────────┐                        │  │
│  │          │                              │                        │  │
│  │   ┌──────▼──────────────┐       ┌───────▼───────────────┐       │  │
│  │   │  TeaWithHook       │       │  CoffeeWithHook       │       │  │
│  │   ├───────────────────┤       ├──────────────────────┤       │  │
│  │   │ + brew(): void    │       │ + brew(): void       │       │  │
│  │   │   → Steeping tea  │       │   → Dripping Coffee  │       │  │
│  │   │                   │       │                      │       │  │
│  │   │ + addCondiments() │       │ + addCondiments()    │       │  │
│  │   │   → Adding Lemon  │       │   → Sugar & Milk     │       │  │
│  │   │                   │       │                      │       │  │
│  │   │ # HOOK (Not Impl.)│       │ + customerWants...() │       │  │
│  │   │ → Default: true   │       │   [OVERRIDE]         │       │  │
│  │   │ → Lemon Always    │       │   → getUserInput()   │       │  │
│  │   │   Added           │       │   → Dynamic Choice   │       │  │
│  │   └───────────────────┘       └──────────────────────┘       │  │
│  │                                                                  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                                                        │
│ **AÇIKLAMA:**                                                          │
│                                                                        │
│ • prepareRecipe() = Template Method (algoritmanın iskeletini tanımlar)│
│ • final → Bu metodu override edemezsiniz (yapı sabit kalır)           │
│ • abstract brew(), addCondiments() → Alt sınıflar ZORUNLU implement   │
│ • boilWater(), pourInCup() → Tüm alt sınıflar için aynı (Concrete)   │
│ • customerWantsCondiments() → Hook (TeaWithHook kullanmaz, Coffee     │
│   kullanır ve davranışı değiştirir)                                   │
│                                                                        │
└────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                                                                         │
│                    ALGORITHM FLOW (Kontrol Akışı)                      │
│                                                                         │
│  TeaWithHook.prepareRecipe()          CoffeeWithHook.prepareRecipe()  │
│  ─────────────────────────────        ──────────────────────────────  │
│      1. boilWater()                        1. boilWater()             │
│         ↓                                     ↓                        │
│      2. brew()                             2. brew()                  │
│         ↓                                     ↓                        │
│      3. pourInCup()                        3. pourInCup()             │
│         ↓                                     ↓                        │
│      4. if(true) ✓                        4. if(getUserInput())       │
│         addCondiments()                      ├─ "y" → true            │
│         → "Adding Lemon"                     ├─ "n" → false           │
│                                              └─ Dinamik!              │
│                                                                         │
│  Çıktı:                                  Çıktı (Evet):               │
│  - Boiling water                        - Boiling water             │
│  - Steeping the tea                     - Dripping Coffee through... │
│  - Pouring into cup                     - Pouring into cup          │
│  - Adding Lemon ✓ (Her Zaman)          - Would you like milk...?   │
│                                         - Adding Sugar and Milk ✓   │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 💡 Kod Açıklaması

### 1️⃣ **Ana Sınıf: `CaffeineBeverageWithHook`**

```java
abstract class CaffeineBeverageWithHook {
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }
    
    // Alt sınıflar ZORUNLU olarak uygulamalı
    abstract void brew();
    abstract void addCondiments();
    
    // Ortak işlemler (Concrete)
    void boilWater() {
        System.out.println("Boiling water");
    }
    
    void pourInCup() {
        System.out.println("Pouring into cup");
    }
    
    // HOOK: Varsayılan davranış = true (her zaman çeşni ekle)
    boolean customerWantsCondiments() {
        return true;
    }
}
```

**Önemli Noktalar:**
- `prepareRecipe()` → **template method** (algoritmanın iskeletini tanımlar)
- `final` anahtar kelimesi → bu metodu override edemezsiniz
- `hook` metodu → isteğe bağlı olarak davranışı değiştirir

---

### 2️⃣ **Çay Sınıfı: `TeaWithHook`**

```java
class TeaWithHook extends CaffeineBeverageWithHook {
    
    public void brew() {
        System.out.println("Steeping the tea");
    }
    
    public void addCondiments() {
        System.out.println("Adding Lemon");
    }
    
    // HOOK override edilmez → varsayılan true kullanılır
}
```

**Davranış:**
- ✅ Hook metodunu override etmez
- ✅ Varsayılan davranış (true) çalışır
- ✅ Limon her zaman eklenir

**Çıktı:**
```
Making tea...
Boiling water
Steeping the tea
Pouring into cup
Adding Lemon
```

---

### 3️⃣ **Kahve Sınıfı: `CoffeeWithHook`**

```java
class CoffeeWithHook extends CaffeineBeverageWithHook {
    
    public void brew() {
        System.out.println("Dripping Coffee through filter");
    }
    
    public void addCondiments() {
        System.out.println("Adding Sugar and Milk");
    }
    
    // HOOK override edilir → kullanıcıya sorar
    public boolean customerWantsCondiments() {
        String answer = getUserInput();
        return answer.toLowerCase().startsWith("y");
    }
    
    private String getUserInput() {
        String answer = null;
        System.out.print("Would you like milk and sugar with your coffee (y/n)? ");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            answer = in.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read your answer");
        }
        
        if (answer == null) {
            return "no";
        }
        return answer;
    }
}
```

**Davranış:**
- ✅ Hook metodunu override eder
- ✅ Kullanıcıdan giriş bekler
- ✅ Cevaba göre çeşni ekler veya eklemez

**Çıktı (Evet durumunda):**
```
Making coffee...
Boiling water
Dripping Coffee through filter
Pouring into cup
Would you like milk and sugar with your coffee (y/n)? y
Adding Sugar and Milk
```

**Çıktı (Hayır durumunda):**
```
Making coffee...
Boiling water
Dripping Coffee through filter
Pouring into cup
Would you like milk and sugar with your coffee (y/n)? n
```

---

## 🔑 Temel Kavramlar

### **Template Method**
- Algoritmanın iskeletini tanımlar
- `final` olarak tanımlandığı için override edilemez
- Alt sınıflar adımları özelleştirebilir

### **Abstract Methods**
- Alt sınıflar ZORUNLU olarak uygulamalı
- Örnek: `brew()`, `addCondiments()`

### **Concrete Methods**
- Tüm alt sınıflar için ortak işlemler
- Örnek: `boilWater()`, `pourInCup()`

### **Hook Methods**
- İsteğe bağlı davranışı kontrol eder
- Varsayılan bir implementasyon sunar
- Alt sınıflar isterse override edebilir
- Örnek: `customerWantsCondiments()`

---

## 📊 Akış Diyagramı

```
┌─────────────────────────────────────┐
│   prepareRecipe() [Template]        │
│  (Algoritmanın iskeletini tutar)    │
└──────────────┬──────────────────────┘
               │
        ┌──────┴───────┐
        │              │
    ┌───▼────┐     ┌──▼───┐
    │  TEA   │     │COFFEE │
    └───┬────┘     └───┬───┘
        │              │
   ┌────┴──────┐  ┌────┴──────────────┐
   │ Hook      │  │ Hook + getUserInput│
   │ → true    │  │ → Kullanıcıya sorar│
   │ (her      │  │ (dinamik)          │
   │  zaman)   │  │                    │
   └───────────┘  └────────────────────┘
```

---

## 🎓 Öğrenme Noktaları

| Konsept | Açıklama | Örnek |
|---------|----------|-------|
| **Template Method** | Algoritmanın yapısını sabit tutar | `prepareRecipe()` |
| **Abstraction** | Ortak arayüzü tanımlar | `brew()`, `addCondiments()` |
| **Inheritance** | Alt sınıflar özelleştirir | `TeaWithHook`, `CoffeeWithHook` |
| **Hook Pattern** | Kişiselleştirmeyi sağlar | `customerWantsCondiments()` |
| **DRY Prensibi** | Kodun tekrarını engeller | Ortak işlemler ana sınıfta |

---

## ✅ Avantajları

- 📝 **Kod Yeniden Kullanımı**: Ortak adımları ana sınıfta tanımlayıp tekrar yazılmasını engeller
- 🔒 **Yapı Tutarlılığı**: Alt sınıflar zorunlu adımları uygulamalı
- 🎛️ **Esneklik**: Hook metodları isteğe bağlı özelleştirmeyi sağlar
- 🚀 **Kolay Genişletme**: Yeni içecek türleri kolaylıkla eklenebilir

---

## ❌ Dezavantajları

- 📚 **Karmaşıklık**: Fazla sayıda soyut metot kafa karıştırabilir
- 🔗 **Bağımlılık**: Alt sınıflar ana sınıfın yapısına bağlı
- 🎯 **Esneklik Sınırı**: Algoritmanın temel yapısı değiştirilemez

---

## 🚀 Çalıştırma

```bash
# Derleme
javac .\TemplateMethodPattern\*.java

# Çalıştırma
java -cp . TemplateMethodPattern.BeverageTestDrive
```

---

## 🎯 Sonuç

Template Method Pattern, algoritmanın yapısını koruyarak alt sınıflara esneklik tanır. Hook mekanizması sayesinde isteğe bağlı adımlar kontrol edilebilir ve kod tekrarı azaltılır.

**İdeal Kullanım Alanları:**
- 🏪 E-ticaret (Ödeme işlemleri)
- 🎮 Oyun Geliştirme (Oyun döngüsü)
- 🔐 Kimlik Doğrulama (Login akışı)
- 📱 Mobil Uygulamalar (Senkronizasyon)
