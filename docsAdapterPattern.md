# Adapter Pattern - Kapsamlı Rehber

## 📋 Genel Bakış

Adapter Pattern, uyumsuz arayüzlere sahip nesneleri birlikte çalışabilir hale getiren yapı tasarım desenidir. İki farklı arayüz arasında köprü görevi görerek, var olan kodları değiştirmeden entegrasyon sağlar.

Bu uygulamada **ördek ve hindi** örneği kullanarak Hindi nesnesini Ördek arayüzü altında çalıştırmayı gösteriyoruz.

---

## 🎯 Amaç ve Faydaları

| Amaç | Açıklama |
|------|----------|
| **Arayüz Uyumlulaştırması** | İki farklı arayüzü birleştirir |
| **Var Olan Kodu Koruma** | Mevcut sınıfları değiştirmez |
| **Yeniden Kullanılabilirlik** | Uyumsuz sınıfları yeniden kullanır |
| **Bağımsızlık** | İstemci kodu adaptöre bağlı değildir |
| **Esneklik** | Uyumsuz bileşenleri kolayca entegre eder |

---

## 🏗️ Mimari Yapı

### **UML Class Diyagramı (Sade Versiyon)**

```
    ┌──────────────────┐
    │ <<interface>>    │
    │      Duck        │
    ├──────────────────┤
    │ + quack()        │
    │ + fly()          │
    └─────────┬────────┘
              △
    ┌─────────┴────────────┐
    │                      │
┌───▼──────────────┐  ┌────▼─────────────┐
│  MallardDuck     │  │  TurkeyAdapter   │
├──────────────────┤  ├──────────────────┤
│ + quack()        │  │ - turkey: Turkey │
│ + fly()          │  ├──────────────────┤
└──────────────────┘  │ + quack()        │
                      │ + fly()          │
                      └────────┬─────────┘
                               │ uses/adapts
                               │
                      ┌────────▼──────────┐
                      │ <<interface>>     │
                      │    Turkey         │
                      ├───────────────────┤
                      │ + gobble()        │
                      │ + fly()           │
                      └────────┬──────────┘
                               △
                      ┌────────┴──────────┐
                      │                   │
                 ┌────▼────────────┐
                 │  WildTurkey     │
                 ├─────────────────┤
                 │ + gobble()      │
                 │ + fly()         │
                 └─────────────────┘
```

### **Dönüşüm Akışı**

```
┌─────────────────────────────────────┐
│      Turkey (Uyumsuz)               │
├─────────────────────────────────────┤
│ + gobble()                          │
│ + fly()                             │
│   (Sadece kısa mesafe uçar)         │
└────────────────┬────────────────────┘
                 │
        Adaptör (TurkeyAdapter)
                 │
    ┌────────────┴────────────┐
    │ turkey.gobble() → quack │
    │ turkey.fly() × 5 → fly  │
    └────────────┬────────────┘
                 │
    ┌────────────▼──────────────────┐
    │   Duck (Uyumlu)               │
    ├───────────────────────────────┤
    │ + quack()                     │
    │ + fly()                       │
    └───────────────────────────────┘
```

---

## 💡 Kod Açıklaması

### 1️⃣ **Duck Interface - Ördek Arayüzü**

```java
public interface Duck {
    public void quack();    // Ses çıkarma
    public void fly();      // Uçma
}
```

**Rol:** Ördeklerin uygulaması gereken standart arayüz.

---

### 2️⃣ **Turkey Interface - Hindi Arayüzü**

```java
public interface Turkey {
    public void gobble();   // Hindi sesi
    public void fly();      // Uçma
}
```

**Rol:** Hindilerin uygulaması gereken standart arayüz (Duck'tan farklı).

---

### 3️⃣ **MallardDuck - Çelik Ördek**

```java
public class MallardDuck implements Duck {
    public void quack() {
        System.out.println("Quack");
    }

    public void fly() {
        System.out.println("I'm flying");
    }
}
```

**Davranış:**
- ✅ "Quack" sesi çıkarır
- ✅ Uzun mesafeler uçabilir

---

### 4️⃣ **WildTurkey - Yabani Hindi**

```java
public class WildTurkey implements Turkey {
    public void gobble() {
        System.out.println("Gobble gobble");
    }

    public void fly() {
        System.out.println("I'm flying a short distance");
    }
}
```

**Davranış:**
- ✅ "Gobble gobble" sesi çıkarır
- ✅ Sadece kısa mesafeler uçabilir

---

### 5️⃣ **TurkeyAdapter - Adaptör (ÖNEMLİ)**

```java
public class TurkeyAdapter implements Duck {
    Turkey turkey;  // Uyumsuz nesne

    // Yapıcı metot: Hindi nesnesini alır
    public TurkeyAdapter(Turkey turkey) {
        this.turkey = turkey;
    }

    // Duck arayüzünün quack() metodunu implement et
    // Turkey'nin gobble() metoduyla dönüştür
    public void quack() {
        turkey.gobble();  // "Gobble gobble" → "Quack" yerine
    }

    // Duck arayüzünün fly() metodunu implement et
    // Turkey'nin fly() metodunu 5 kere çağır (uzun uçuş sağla)
    public void fly() {
        for (int i = 0; i < 5; i++) {
            turkey.fly();  // Kısa uçuşları birleştir
        }
    }
}
```

**Rol:** Adaptör - Turkey nesnesini Duck arayüzü altında çalıştıran köprü.

**Önemli Noktalar:**
- ✅ `implements Duck` → Duck arayüzünü implement eder
- ✅ `Turkey turkey` → Uyumsuz nesneyi içinde tutar
- ✅ `quack()` → Turkey'nin `gobble()` metoduyla dönüştürür
- ✅ `fly()` → Turkey'nin `fly()` metodunu 5 kere çağırarak uzun uçuş sağlar

---

### 6️⃣ **DuckTestDrive - Test Sınıfı**

```java
public class DuckTestDrive {
    public static void main(String[] args) {
        MallardDuck duck = new MallardDuck();
        WildTurkey turkey = new WildTurkey();

        // ADAPTÖR OLUŞTUR: Turkey nesnesini Duck'a dönüştür
        Duck turkeyAdapter = new TurkeyAdapter(turkey);

        System.out.println("The Turkey says...");
        turkey.gobble();       // Gobble gobble
        turkey.fly();          // I'm flying a short distance

        System.out.println("\nThe Duck says...");
        testDuck(duck);        // Quack, I'm flying

        System.out.println("\nThe TurkeyAdapter says...");
        testDuck(turkeyAdapter);  // Gobble gobble, I'm flying a short distance × 5
    }

    static void testDuck(Duck duck) {
        duck.quack();  // Duck arayüzü kullanır
        duck.fly();    // Duck arayüzü kullanır
    }
}
```

**Davranış:**
- ✅ `testDuck()` sadece Duck arayüzünü bilir
- ✅ Adaptör sayesinde Turkey nesnesi Duck gibi davranır
- ✅ Original Turkey ve WildTurkey değişmez

---

## 🔑 Temel Kavramlar

| Kavram | Açıklama | Örnek |
|--------|----------|-------|
| **Target Interface** | İstenilen arayüz | Duck |
| **Adaptee** | Uyumsuz nesne | WildTurkey |
| **Adapter** | Dönüştürücü sınıf | TurkeyAdapter |
| **Delegation** | Adapte edilen nesneyi çağırma | `turkey.gobble()` |
| **Wrapping** | Uyumsuz nesneyi sarmalama | `Turkey turkey` |

---

## 📊 Akış Diyagramı

```
İstemci Kodu
    │
    ├─► testDuck(duck)
    │     │
    │     └─► duck.quack()
    │         duck.fly()
    │
    ├─► testDuck(turkeyAdapter)
    │     │
    │     ├─► turkeyAdapter.quack()
    │     │     │
    │     │     └─► turkey.gobble()  ← Dönüştürüldü
    │     │
    │     └─► turkeyAdapter.fly()
    │           │
    │           ├─► turkey.fly()  × 5  ← Birleştirildi
    │           └─► turkey.fly()
    │           ... (5 kere)
    │
    └─► Sonuç: WildTurkey, Duck gibi davranır!
```

---

## 🎓 Öğrenme Noktaları

### **Sorun: Uyumsuz Arayüzler**

```java
// WITHOUT PATTERN (Kötü Yöntem)
public void testDuck(Duck duck) {
    duck.quack();
    duck.fly();
}

// Bunu çalıştırmak istiyoruz:
WildTurkey turkey = new WildTurkey();
testDuck(turkey);  // HATA! Turkey, Duck değildir
                   // Interface uyuşmuyor
```

### **Çözüm: Adapter Pattern**

```java
// WITH PATTERN (İyi Yöntem)
Duck adapter = new TurkeyAdapter(turkey);
testDuck(adapter);  // BAŞARILI! Adaptör Duck gibi davranır

// Önemli: Original Turkey hiç değişmedi!
```

---

## ✅ Avantajları

- 🔌 **Uyumsuz Bileşenlerin Entegrasyonu**: Farklı arayüzleri birleştirir
- 📦 **Var Olan Kodu Koruma**: Mevcut sınıfları değiştirmez
- 🔄 **Yeniden Kullanılabilirlik**: Uyumsuz sınıfları yeniden kullanır
- 🧹 **Kod Temizliği**: İstemci kodu uyumsuzluktan haberdar değildir
- 🚀 **Esneklik**: Adaptörleri kolayca ekler/kaldırabilirsiniz

---

## ❌ Dezavantajları

- 📚 **Komplekslik**: Çok sayıda adaptör karmaşıklaştırabilir
- 🔗 **Ekstra Nesneler**: Her uyumsuzluk için yeni sınıf gerekli
- 📈 **Performans**: Adaptör, delegasyon nedeniyle hafif bir ek yapar
- 🎯 **Maintainability**: Çok sayıda adaptörü yönetmek zor olabilir

---

## 📈 Gerçek Dünya Kullanım Örnekleri

### 1️⃣ **Plug Adaptörleri (Gerçek Dünya)**
```
┌─────────────────┐     ┌──────────────┐     ┌──────────────┐
│ USB-C Cihaz     │────►│ USB Adaptör  │────►│ USB Port     │
└─────────────────┘     └──────────────┘     └──────────────┘
(Uyumsuz)              (Adapter)              (Hedef)
```

### 2️⃣ **Veri Depoları**
```
- MySQL Veritabanı
- Adaptör: MySQLAdapter implements DatabaseInterface
- Uygulamadan bağımsız veri erişimi

- PostgreSQL Veritabanı
- Adaptör: PostgreSQLAdapter implements DatabaseInterface
- Her ikisi de aynı arayüzü sağlar
```

### 3️⃣ **Ödeme Sistemleri**
```
Uygulamada: PaymentProcessor interface
Kredi Kartı → CreditCardAdapter
PayPal → PayPalAdapter
Stripe → StripeAdapter

Tüm ödemeler aynı arayüzle işlenir
```

### 4️⃣ **Media Players**
```
Uygulamada: MediaPlayer interface
MP3 Player → MP3Adapter
VideoPlayer → VideoAdapter
AudioPlayer → AudioAdapter

Tüm oynatıcılar aynı kontrolleri sağlar
```

### 5️⃣ **Yazı Tipi Dönüşümü**
```
Uygulamada: FontFormat interface
TTF dosyası → TTFAdapter
OTF dosyası → OTFAdapter
WOFF dosyası → WOFFAdapter

Her format aynı arayüzle kullanılır
```

---

## 🚀 Çalıştırma

### **Derleme:**
```bash
javac .\AdapterPattern\*.java
```

### **Çalıştırma:**
```bash
java -cp . AdapterPattern.DuckTestDrive
```

### **Beklenen Çıktı:**
```
The Turkey says...
Gobble gobble
I'm flying a short distance

The Duck says...
Quack
I'm flying

The TurkeyAdapter says...
Gobble gobble
I'm flying a short distance
I'm flying a short distance
I'm flying a short distance
I'm flying a short distance
I'm flying a short distance
```

---

## 🎯 Sonuç

Adapter Pattern, uyumsuz arayüzleri uyumlu hale getirir.

**Bu örnekten öğrendiklerimiz:**
- ✅ Adaptör, uyumsuz nesneyi sarmalanarak (wrapping) hedef arayüzü implement eder
- ✅ Original sınıflar değiştirilmez
- ✅ İstemci kodu adaptöre bağlı değildir
- ✅ Yeni arayüzler kolayca eklenebilir
- ✅ Delegasyon aracılığıyla dönüştürme yapılır

**Özet:** Adapter Pattern, uyumsuzluğu gizleyerek sistemleri entegre eder! 🔌
