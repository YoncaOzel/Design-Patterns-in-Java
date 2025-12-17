# Facade Pattern - Kapsamlı Rehber

## 📋 Genel Bakış

Facade Pattern, karmaşık bir alt sistem için basit bir arayüz sağlayan yapı tasarım desenidir. Çok sayıda sınıfın karmaşık ilişkilerini gizleyip, tek bir basit arayüz sunarak istemci kodunu simplify eder.

Bu uygulamada **ev tiyatrosu sistemi** örneği kullanarak 6 farklı cihazın (amplifier, DVD, projektor, ekran, ışıklar, patlamış mısır makinesi) koordinasyonunu gössteriyoruz.

---

## 🎯 Amaç ve Faydaları

| Amaç | Açıklama |
|------|----------|
| **Karmaşıklık Azaltma** | Alt sistem kompleksitesini gizler |
| **Basitleştirme** | İstemci sadece Facade ile iletişim kurar |
| **Bağımlılık Azaltma** | İstemci alt sistemden bağımsız hale gelir |
| **Kontrol Merkezi** | Tüm işlemler merkezi bir noktadan yönetilir |
| **Kod Yeniden Kullanımı** | Facade'ın kullanılması kolay ve anlaşılır |

---

## 🏗️ Mimari Yapı

### **UML Class Diyagramı (Sade Versiyon)**

```
                    ┌───────────────────┐
                    │  HomeTheaterFacade│
                    ├───────────────────┤
                    │ - amp             │
                    │ - dvd             │
                    │ - projector       │
                    │ - screen          │
                    │ - lights          │
                    │ - popper          │
                    ├───────────────────┤
                    │ + watchMovie()    │
                    │ + endMovie()      │
                    └────────┬──────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
   ┌────▼─────┐        ┌─────▼──────┐     ┌──────▼───┐
   │Amplifier │        │ DvdPlayer  │     │Projector │
   │          │        │            │     │          │
   │+ setVol()│        │+ play()    │     │+ on()    │
   │+ on/off()│        │+ on/off()  │     │+ off()   │
   └──────────┘        └────────────┘     └──────────┘


   ┌──────────┐        ┌──────────────┐   ┌──────────────┐
   │ Screen   │        │TheaterLights │   │PopcornPopper │
   │          │        │              │   │              │
   │+ up()    │        │+ dim()       │   │+ pop()       │
   │+ down()  │        │+ on()        │   │+ on/off()    │
   └──────────┘        └──────────────┘   └──────────────┘
```

### **Kullanım Akışı**

```
┌──────────────────────────────────────┐
│      İstemci (Test Drive)             │
├──────────────────────────────────────┤
│  homeTheater.watchMovie("Film Adı")   │
└────────────────┬─────────────────────┘
                 │
     ┌───────────▼─────────────┐
     │  HomeTheaterFacade      │
     │  watchMovie() metodu    │
     └───────────┬─────────────┘
                 │
        ┌────────┴────────────────────┬────────────────┐
        │                             │                │
  ┌─────▼─────┐              ┌────────▼────────┐  ┌───▼─────┐
  │Popcorn ON │              │ All Devices     │  │ Devices │
  │Popcorn POP│              │ Working         │  │ Setup   │
  └───────────┘              │ Together        │  └─────────┘
                             └────────────────┘
```

---

## 💡 Kod Açıklaması

### 1️⃣ **HomeTheaterFacade - Facade (ÖNEMLİ)**

```java
public class HomeTheaterFacade {
    // Alt sistem nesneleri
    private Amplifier amp;
    private DvdPlayer dvd;
    private Projector projector;
    private TheaterLights lights;
    private Screen screen;
    private PopcornPopper popper;

    // Yapıcı: Tüm cihazları alır
    public HomeTheaterFacade(Amplifier amp, DvdPlayer dvd, Projector projector,
            Screen screen, TheaterLights lights, PopcornPopper popper) {
        this.amp = amp;
        this.dvd = dvd;
        this.projector = projector;
        this.screen = screen;
        this.lights = lights;
        this.popper = popper;
    }

    // FACADE METODU 1: Filmi izleme
    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        popper.on();           // Patlamış mısır makinesi aç
        popper.pop();          // Mısır patlat
        lights.dim(10);        // Işıkları %10'a kısımla
        screen.down();         // Ekranı indir
        projector.on();        // Projektor aç
        projector.wideScreenMode();  // Geniş ekran modu
        amp.on();              // Amplifier aç
        amp.setVolume(5);      // Ses seviyesi 5
        dvd.on();              // DVD aç
        dvd.play(movie);       // Filmi oynat
    }

    // FACADE METODU 2: Filmi bitirme
    public void endMovie() {
        System.out.println("Shutting movie theater down...");
        popper.off();          // Patlamış mısır makinesi kapat
        lights.on();           // Işıkları aç
        screen.up();           // Ekranı kaldır
        projector.off();       // Projektor kapat
        amp.off();             // Amplifier kapat
        dvd.stop();            // DVD'yi durdur
        dvd.eject();           // DVD'yi çıkar
        dvd.off();             // DVD'yi kapat
    }
}
```

**Rol:** Facade - Karmaşık alt sistemi basit arayüzü altında saklar.

**Önemli Noktalar:**
- ✅ Tüm cihazları içinde tutar
- ✅ İstemci sadece 2 metodu (watchMovie, endMovie) çağırır
- ✅ Cihaz konfigürasyonu gizlenir
- ✅ İstemci kompleksiteden haberdar değildir

---

### 2️⃣ **Amplifier - Güç Amplifikatörü**

```java
public class Amplifier {
    public void on() {
        System.out.println("Top-O-Line Amplifier on");
    }

    public void off() {
        System.out.println("Top-O-Line Amplifier off");
    }

    public void setVolume(int level) {
        System.out.println("Top-O-Line Amplifier setting volume to " + level);
    }
}
```

**Rol:** Ses sistemi - Ses gücünü yönetir.

---

### 3️⃣ **DvdPlayer - DVD Oynatıcı**

```java
public class DvdPlayer {
    public void on() {
        System.out.println("DVD Player on");
    }

    public void play(String movie) {
        System.out.println("DVD Player playing \"" + movie + "\"");
    }

    public void stop() {
        System.out.println("DVD Player stopped");
    }

    public void eject() {
        System.out.println("DVD Player eject");
    }

    public void off() {
        System.out.println("DVD Player off");
    }
}
```

**Rol:** Film kaynak - Filmleri oynatır.

---

### 4️⃣ **Projector - Projektor**

```java
public class Projector {
    public void on() {
        System.out.println("Projector on");
    }

    public void wideScreenMode() {
        System.out.println("Projector in widescreen mode");
    }

    public void off() {
        System.out.println("Projector off");
    }
}
```

**Rol:** Görüntü yönetim - Filmi ekrana yansıtır.

---

### 5️⃣ **Screen - Ekran**

```java
public class Screen {
    public void down() {
        System.out.println("Theater Screen going down");
    }

    public void up() {
        System.out.println("Theater Screen going up");
    }
}
```

**Rol:** Görüntü gösterim - Ekranı kaldırır/indirir.

---

### 6️⃣ **TheaterLights - Tiyatro Işıkları**

```java
public class TheaterLights {
    public void dim(int level) {
        System.out.println("Theater Ceiling Lights dimming to " + level + "%");
    }

    public void on() {
        System.out.println("Theater Ceiling Lights on");
    }
}
```

**Rol:** Aydınlatma - Işık seviyesini kontrol eder.

---

### 7️⃣ **PopcornPopper - Mısır Patlatıcı**

```java
public class PopcornPopper {
    public void on() {
        System.out.println("Popcorn Popper on");
    }

    public void pop() {
        System.out.println("Popcorn Popper popping popcorn!");
    }

    public void off() {
        System.out.println("Popcorn Popper off");
    }
}
```

**Rol:** Rahat hale - Patlamış mısır hazırlar.

---

### 8️⃣ **HomeTheaterTestDrive - Test Sınıfı**

```java
public class HomeTheaterTestDrive {
    public static void main(String[] args) {
        // 6 cihazı oluştur
        Amplifier amp = new Amplifier();
        DvdPlayer dvd = new DvdPlayer();
        Projector projector = new Projector();
        Screen screen = new Screen();
        TheaterLights lights = new TheaterLights();
        PopcornPopper popper = new PopcornPopper();

        // FACADE OLUŞTUR
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
            amp, dvd, projector, screen, lights, popper
        );

        // FACADE'ı KULLAN - Sadece 2 metod!
        homeTheater.watchMovie("Winter Sleep");
        System.out.println("\n--- Film finished ---\n");
        homeTheater.endMovie();
    }
}
```

**Davranış:**
- ✅ Tüm cihazları oluşturur
- ✅ Facade'ı oluşturur
- ✅ Sadece 2 metodu çağırır (watchMovie, endMovie)
- ✅ Tüm komplexlik gizlenir

---

## 🔑 Temel Kavramlar

| Kavram | Açıklama | Örnek |
|--------|----------|-------|
| **Facade** | Basit arayüzü sunan sınıf | HomeTheaterFacade |
| **Subsystem** | Karmaşık alt sistem | Amplifier, DvdPlayer... |
| **Client** | Facade'ı kullanan kod | HomeTheaterTestDrive |
| **Wrapping** | Kompleks işlemleri paketleme | watchMovie() metodu |
| **Encapsulation** | Detayları gizleme | Cihaz konfigürasyonu |

---

## 📊 Akış Diyagramı

```
homeTheater.watchMovie("Film")
    │
    ├─► System.out.println("Get ready...")
    ├─► popper.on()
    ├─► popper.pop()
    ├─► lights.dim(10)
    ├─► screen.down()
    ├─► projector.on()
    ├─► projector.wideScreenMode()
    ├─► amp.on()
    ├─► amp.setVolume(5)
    ├─► dvd.on()
    └─► dvd.play("Film")

homeTheater.endMovie()
    │
    ├─► System.out.println("Shutting down...")
    ├─► popper.off()
    ├─► lights.on()
    ├─► screen.up()
    ├─► projector.off()
    ├─► amp.off()
    ├─► dvd.stop()
    ├─► dvd.eject()
    └─► dvd.off()
```

---

## 🎓 Öğrenme Noktaları

### **Sorun: Karmaşık Konfigürasyon**

```java
// WITHOUT PATTERN (Kötü Yöntem)
public static void main(String[] args) {
    // Tüm cihazları açmak için:
    amp.on();
    amp.setVolume(5);
    dvd.on();
    dvd.play("Film");
    projector.on();
    projector.wideScreenMode();
    screen.down();
    lights.dim(10);
    popper.on();
    popper.pop();
    
    // Kapamak için:
    amp.off();
    dvd.stop();
    dvd.eject();
    dvd.off();
    projector.off();
    screen.up();
    lights.on();
    popper.off();
    
    // Çok karmaşık ve hata yapmaya açık!
}
```

### **Çözüm: Facade Pattern**

```java
// WITH PATTERN (İyi Yöntem)
HomeTheaterFacade homeTheater = new HomeTheaterFacade(
    amp, dvd, projector, screen, lights, popper
);

homeTheater.watchMovie("Film");    // Basit!
homeTheater.endMovie();             // Çok basit!
```

---

## ✅ Avantajları

- 🎯 **Karmaşıklık Azaltması**: Alt sistem kompleksitesi gizlenir
- 📦 **Kod Temizliği**: İstemci kodu çok daha basit ve okunur
- 🔄 **Bağımsızlık**: İstemci alt sistemin detaylarından haberdar değildir
- 🚀 **Esneklik**: Facade, alt sistemi yeniden yapılandırmadan değişebilir
- 🧹 **Bakım Kolaylığı**: Kompleks işlemler merkezi bir yerde
- 🔒 **Encapsulation**: Alt sistem nesnelerine doğrudan erişim engellenir

---

## ❌ Dezavantajları

- 📚 **Tanrı Nesnesi**: Facade'ın çok sorumluluğu olabilir
- 🔗 **Bağımlılık Artışı**: Facade'ın tüm alt sistemi bilmesi gerekir
- 📈 **Başlangıç Kompleksitesi**: Başlangıçta daha fazla kod yazılır
- 🎯 **Sınırlı Erişim**: Alt sisteme doğrudan erişim istenirse zor
- 🔄 **Değişiklik Maliyeti**: Yeni feature eklemek Facade'ı değiştirmek anlamına gelebilir

---

## 📈 Gerçek Dünya Kullanım Örnekleri

### 1️⃣ **Araç Kontrolü**
```
Sistem: Motor, Şanzıman, Frenler, Direksyon
Facade: Araba sınıfı
start() → Tüm sistemleri başlat
stop() → Tüm sistemleri durdur
```

### 2️⃣ **Web Framework'ler**
```
Sistem: Routing, Controller, View, Model, Database
Facade: Framework sınıfı
request() → Request işleme
render() → Response oluşturma
```

### 3️⃣ **E-Ticaret Sistemi**
```
Sistem: Ödeme, Kargo, Depo, Notification
Facade: Order Processing
placeOrder() → Siparişi işle
cancelOrder() → Siparişi iptal et
```

### 4️⃣ **Bilgisayar Başlatması**
```
Sistem: BIOS, Çekirdek, Sürücüler, Hizmetler
Facade: OS Loader
startup() → Bilgisayarı başlat
shutdown() → Bilgisayarı kapat
```

### 5️⃣ **Hastane Yönetim Sistemi**
```
Sistem: Hasta, Doktor, Eczane, Lab, Fatura
Facade: Appointment Manager
bookAppointment() → Randevu al
completeVisit() → Viziti tamamla
```

---

## 🚀 Çalıştırma

### **Derleme:**
```bash
javac .\FacadePattern\HomeTheater\*.java
```

### **Çalıştırma:**
```bash
java -cp . FacadePattern.HomeTheater.HomeTheaterTestDrive
```

### **Beklenen Çıktı:**
```
Get ready to watch a movie...
Popcorn Popper on
Popcorn Popper popping popcorn!
Theater Ceiling Lights dimming to 10%
Theater Screen going down
Projector on
Projector in widescreen mode
Top-O-Line Amplifier on
Top-O-Line Amplifier setting volume to 5
DVD Player on
DVD Player playing "Winter Sleep"

--- Film finished ---

Shutting movie theater down...
Popcorn Popper off
Theater Ceiling Lights on
Theater Screen going up
Projector off
Top-O-Line Amplifier off
DVD Player stopped
DVD Player eject
DVD Player off
```

---

## 🎯 Sonuç

Facade Pattern, karmaşık sistemleri basit hale getirir.

**Bu örnekten öğrendiklerimiz:**
- ✅ Facade, 6 farklı cihazı 2 metotta paketler
- ✅ İstemci kodu çok daha basit ve anlaşılır
- ✅ Cihaz konfigürasyonu gizlenir
- ✅ Yeni cihaz eklemek kolay (Facade'da değişim)
- ✅ Alt sistemi yeniden yapılandırmadan değişebilir

**Özet:** Facade Pattern, karmaşıklığı gizleyip basitliği sunar! 🎬
