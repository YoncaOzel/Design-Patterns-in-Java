package TemplateMethodPattern;

abstract class CaffeineBeverageWithHook {
    final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();

        // HOOK (Kanca): Kullanıcı isterse çeşni ekleriz.
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    // Alt sınıfların uygulaması ZORUNLU olan metotlar (abstract)
    abstract void brew();

    abstract void addCondiments();

    // Ortak metotlar (Concrete)
    void boilWater() {
        System.out.println("Boiling water");
    }

    void pourInCup() {
        System.out.println("Pouring into cup");
    }

    // HOOK METODU
    // Varsayılan olarak true döner (her zaman ekle).Alt sınıflar isterse override
    // edebilir.
    boolean customerWantsCondiments() {
        return true;
    }

}
