# State Pattern

## 📋 Genel Bakış

State Pattern, bir nesnenin iç durumuna bağlı olarak davranışını değiştiren davranış tasarım desenidir. Duruma özgü mantığı ayrı sınıflarda kapsüller, böylece durum geçişleri temiz ve anlaşılır hale gelir.

Bu uygulamada **sakız dağıtıcı makinesi** örneği kullanarak farklı durumlar (para yok, para var, satıldı vb.) arasında geçişleri gösteriyoruz.

---

## 🎯 Amaç ve Faydaları

| Amaç | Açıklama |
|------|----------|
| **Durum Kapsülleme** | Her durum kendi davranışını kontrol eder |
| **If-Else Eliminasyonu** | Karmaşık koşullu mantığı sınıflara böler |
| **Açık/Kapalı Prensibi** | Yeni durum eklemek kolay, var olanı değiştiremezsiniz |
| **Tutarlılık** | Her durum aynı interface'i implement eder |
| **Kolay Bakım** | Durum mantığı izole edilmiş ve kolay anlaşılır |

---

## 🏗️ Mimari Yapı

### **UML Class Diyagramı (Sade Versiyon)**

```
              ┌──────────────────────┐
              │ <<interface>>        │
              │      State           │
              ├──────────────────────┤
              │ + insertQuarter()    │
              │ + ejectQuarter()     │
              │ + turnCrank()        │
              │ + dispense()         │
              └─────────────┬────────┘
                            △
        ┌───────────────────┼───────────────────┐
        │                   │                   │
  ┌─────▼──────────┐ ┌─────▼──────────┐ ┌─────▼──────────┐
  │ NoQuarterState │ │ HasQuarterState│ │ SoldState      │
  └────────────────┘ └────────────────┘ └────────────────┘
        │                   │                   │
        └───────────────────┼───────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
  ┌─────▼──────────┐ ┌─────▼──────────┐ ┌─────▼──────────┐
  │ SoldOutState   │ │ WinnerState    │ │ GumballMachine │
  └────────────────┘ └────────────────┘ ├────────────────┤
                                        │ - state: State │
                                        │ - count: int   │
                                        ├────────────────┤
                                        │ + insertQuarter│
                                        │ + ejectQuarter │
                                        │ + turnCrank()  │
                                        │ + setState()   │
                                        └────────────────┘
```

### **Durum Geçiş Diyagramı**

```
                ┌─────────────────┐
                │  NoQuarterState │
                │  (Para Yok)     │
                └────────┬────────┘
                         │
         para atıldı     │
         (insertQuarter) │
                         ▼
                ┌─────────────────┐
         ┌─────►│ HasQuarterState │
         │      │  (Para Var)     │
    iade│      └────────┬────────┘
   (eject)            │
         │     kol çevrildi (turnCrank)
         │     10% şans kazanma
         │            │
         │     ┌──────┴──────┐
         │     │             │
         │     ▼             ▼
    ┌────┴──────────┐  ┌────────────┐
    │  SoldState    │  │ WinnerState│
    │  (Satıldı)    │  │ (Kazandı)  │
    │      │        │  │      │     │
    │      └───┬────┘  └────┬─┘     │
    │          │             │      │
    └──────────┼─────────────┴──────┘
               │
              dispense
               │
               ▼
        ┌─────────────────┐
        │  SoldOutState   │
        │  (Tükenmiş)     │ (stok = 0 ise)
        └─────────────────┘
```

---

## 💡 Kod Açıklaması

### 1️⃣ **State Interface - Ortak Arayüz**

```java
public interface State {
    void insertQuarter();      // Para atma
    void ejectQuarter();       // Para iade
    void turnCrank();          // Kolu çevirme
    void dispense();           // Dağıtma
}
```

**Rol:** Tüm durum sınıflarının implement etmesi gereken kontrat.

---

### 2️⃣ **NoQuarterState - Para Yok Durumu**

```java
public class NoQuarterState implements State {
    GumballMachine gumballMachine;

    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Para attınız.");
        gumballMachine.setState(gumballMachine.getHasQuarterState());
    }

    public void ejectQuarter() {
        System.out.println("Para atmadınız.");
    }

    public void turnCrank() {
        System.out.println("Para yoksa sakız da yok.");
    }

    public void dispense() {
        System.out.println("Önce ödeme yapın.");
    }
}
```

**Davranış:**
- ✅ Para atıldığında → HasQuarterState'e geçer
- ✅ Para iade edilemez → Uyarı mesajı
- ✅ Kolu çevirme işe yaramaz → Uyarı mesajı

---

### 3️⃣ **HasQuarterState - Para Var Durumu**

```java
public class HasQuarterState implements State {
    GumballMachine gumballMachine;
    Random randomWinner = new Random(System.currentTimeMillis());

    public HasQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Zaten para attınız.");
    }

    public void ejectQuarter() {
        System.out.println("Para iade edildi.");
        gumballMachine.setState(gumballMachine.getNoQuarterState());
    }

    public void turnCrank() {
        System.out.println("Kolu çevirdiniz...");
        int winner = randomWinner.nextInt(10); // %10 şans
        if ((winner == 0) && (gumballMachine.getCount() > 1)) {
            gumballMachine.setState(gumballMachine.getWinnerState());
        } else {
            gumballMachine.setState(gumballMachine.getSoldState());
        }
    }

    public void dispense() {
        System.out.println("Sakız verilmedi.");
    }
}
```

**Davranış:**
- ✅ Kolu çevirince → 10% şansla WinnerState, 90% şansla SoldState
- ✅ Para iade edilebilir → NoQuarterState'e geri dönüş
- ✅ Para tekrar atılamaz → Uyarı mesajı

---

### 4️⃣ **SoldState - Satıldı Durumu**

```java
public class SoldState implements State {
    GumballMachine gumballMachine;

    public SoldState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Lütfen bekleyin, sakız veriliyor.");
    }

    public void ejectQuarter() {
        System.out.println("Üzgünüm, kolu zaten çevirdiniz.");
    }

    public void turnCrank() {
        System.out.println("İki kere çevirmek size bir şey kazandırmaz!");
    }

    public void dispense() {
        gumballMachine.releaseBall();
        if (gumballMachine.getCount() > 0) {
            gumballMachine.setState(gumballMachine.getNoQuarterState());
        } else {
            System.out.println("Oops, sakız bitti!");
            gumballMachine.setState(gumballMachine.getSoldOutState());
        }
    }
}
```

**Davranış:**
- ✅ Sakız dağıtılır
- ✅ Stok varsa → NoQuarterState
- ✅ Stok biterse → SoldOutState

---

### 5️⃣ **WinnerState - Kazanan Durumu**

```java
public class WinnerState implements State {
    GumballMachine gumballMachine;

    public WinnerState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void dispense() {
        System.out.println("TEBRİKLER! İki sakız kazandınız!");
        gumballMachine.releaseBall();
        if (gumballMachine.getCount() == 0) {
            gumballMachine.setState(gumballMachine.getSoldOutState());
        } else {
            gumballMachine.releaseBall(); // İkinci sakız
            if (gumballMachine.getCount() > 0) {
                gumballMachine.setState(gumballMachine.getNoQuarterState());
            } else {
                System.out.println("Oops, sakız bitti!");
                gumballMachine.setState(gumballMachine.getSoldOutState());
            }
        }
    }
    
    // Diğer metotlar: Hata mesajı
}
```

**Davranış:**
- ✅ 2 sakız dağıtılır
- ✅ Kazanma animasyonu
- ✅ İki sakız biterse → SoldOutState

---

### 6️⃣ **SoldOutState - Tükenmiş Durumu**

```java
public class SoldOutState implements State {
    GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    public void insertQuarter() {
        System.out.println("Makine boş.");
    }

    public void ejectQuarter() {
        System.out.println("Para atamazsınız.");
    }

    public void turnCrank() {
        System.out.println("Sakız yok.");
    }

    public void dispense() {
        System.out.println("Sakız yok.");
    }
}
```

**Davranış:**
- ✅ Tüm işlem engellenir
- ✅ Uyarı mesajları verilir

---

### 7️⃣ **GumballMachine - Context Sınıfı**

```java
public class GumballMachine {
    State soldOutState;
    State noQuarterState;
    State hasQuarterState;
    State soldState;
    State winnerState;
    
    State state;
    int count = 0;

    public GumballMachine(int numberGumballs) {
        // Tüm durumlar oluşturulur
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

    // Eylemler delegasyonu
    public void insertQuarter() {
        state.insertQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }

    // Durum değiştirme
    void setState(State state) {
        this.state = state;
    }
}
```

**Rol:** Context sınıfı - Durum nesnesini tutar ve delegasyon yapar.

---

## 🔑 Temel Kavramlar

| Kavram | Açıklama | Örnek |
|--------|----------|-------|
| **State (Durum)** | Nesnenin davranışını belirleyen nitelikleri | NoQuarterState, HasQuarterState |
| **Context** | Durum nesnesi tutan ve delegasyon yapan nesne | GumballMachine |
| **Delegasyon** | Context, işlemleri state'e devreder | `state.insertQuarter()` |
| **Durum Geçişi** | Bir durumdan diğerine geçiş | `setState(newState)` |
| **Interface** | Tüm state'ler implement eden kontrat | State interface |

---

## 📊 Akış Diyagramı

```
GumballMachine
    │
    ├─► insertQuarter()
    │     │
    │     └─► currentState.insertQuarter()
    │           │
    │           ├─ NoQuarterState: Para alır → HasQuarterState
    │           ├─ HasQuarterState: "Zaten var" mesajı
    │           └─ SoldOutState: "Makine boş" mesajı
    │
    ├─► turnCrank()
    │     │
    │     ├─► currentState.turnCrank()
    │     └─► currentState.dispense()
    │           │
    │           ├─ HasQuarterState: 
    │           │     ├─ 10% → WinnerState
    │           │     └─ 90% → SoldState
    │           │
    │           ├─ SoldState:
    │           │     ├─ Sakız ver
    │           │     └─ Stok > 0 → NoQuarterState
    │           │        Stok = 0 → SoldOutState
    │           │
    │           └─ WinnerState:
    │                 ├─ 2 sakız ver
    │                 └─ Durum güncelle
    │
    └─► ejectQuarter()
          │
          └─► currentState.ejectQuarter()
                ├─ HasQuarterState: Para iade → NoQuarterState
                └─ Diğer: "Mümkün değil" mesajı
```

---

## 🎓 Öğrenme Noktaları

### **Sorun: If-Else Spaghetti Kodu**

```java
// WITHOUT PATTERN (Kötü Yöntem)
public void turnCrank() {
    if (state == NO_QUARTER) {
        // İşlem 1
    } else if (state == HAS_QUARTER) {
        // İşlem 2
    } else if (state == SOLD) {
        // İşlem 3
    } else if (state == SOLD_OUT) {
        // İşlem 4
    } else if (state == WINNER) {
        // İşlem 5
    }
    // Yeni durum eklemek → Tüm metodları değiştirir!
}
```

### **Çözüm: State Pattern**

```java
// WITH PATTERN (İyi Yöntem)
public void turnCrank() {
    state.turnCrank();
    state.dispense();
}

// Yeni durum ekleme → Sadece yeni durum sınıfı oluş!
```

---

## ✅ Avantajları

- 🎯 **Durum Kapsülleme**: Her durum kendi mantığına sahip
- 📦 **Kod Temizliği**: If-else yapısı ortadan kalkar
- 🔄 **Durum Geçiş Netliği**: `setState()` ile açık geçişler
- 🚀 **Kolay Genişletme**: Yeni durum = yeni sınıf
- 📝 **Bakım Kolaylığı**: Her durum izole ve test edilebilir
- ✏️ **Açık/Kapalı Prensibi**: Var olanı değiştirmez, yenisini ekler

---

## ❌ Dezavantajları

- 📚 **Komplekslik**: Basit durumlar için gereksiz olabilir
- 🔗 **Bağımlılık**: State sınıfları Context'i bilmeli
- 💾 **Bellek**: Her durum için ayrı nesne oluşturulur
- 🎯 **Boilerplate Kod**: Çok sayıda State sınıfı yazılması gerekir

---

## 📈 Gerçek Dünya Kullanım Örnekleri

### 1️⃣ **E-Ticaret Sipariş Sistemi**
```
Siparişler farklı durumları takip eder:
- Pending (Beklemeде)
- Processing (İşleniyor)
- Shipped (Gönderildi)
- Delivered (Teslim Edildi)
- Cancelled (İptal Edildi)
```

### 2️⃣ **Müzik Çalar**
```
Müzik çalar durumları:
- Playing (Çalıyor)
- Paused (Duraklatılmış)
- Stopped (Durdurulmuş)
- Buffering (Yükleniyor)
```

### 3️⃣ **Oyun Geliştirme**
```
Karakter durumları:
- Idle (Hareketsiz)
- Running (Koşuyor)
- Jumping (Zıplıyor)
- Falling (Düşüyor)
- Dead (Öldü)
```

### 4️⃣ **Trafik Işığı Sistemi**
```
Işık durumları:
- Red (Kırmızı - Dur)
- Yellow (Sarı - Hazır)
- Green (Yeşil - Geç)
```

### 5️⃣ **ATM Makinesi**
```
ATM durumları:
- Idle (Bekleyen)
- CardInserted (Kart Takılı)
- PinEntered (PIN Girildi)
- Authenticated (Kimlik Doğrulandı)
- TransactionComplete (İşlem Tamamlandı)
```

---

## 🚀 Çalıştırma

### **Derleme:**
```bash
javac .\StatePattern\*.java
```

### **Çalıştırma:**
```bash
java -cp . StatePattern.Main
```

### **Beklenen Çıktı Örneği:**
```
Para attınız.
Kolu çevirdiniz...
Bir sakız yuvaya yuvarlanıyor...

Makine boş.
```

---

## 🎯 Sonuç

State Pattern, nesnenin durumuna bağlı davranışı yönetmek için güçlü bir araçtır.

**Bu örnekten öğrendiklerimiz:**
- ✅ Her durum kendi davranışını kontrol eder
- ✅ Context sınıfı delegasyon yapar
- ✅ Yeni durum eklemek çok kolay
- ✅ Kod temiz, anlaşılır ve bakım yapılması kolay
- ✅ Durum geçişleri açık ve net

**Özet:** State Pattern, karmaşık durumlu sistemleri basit ve yönetilebilir hale dönüştürür! 🎉
