package TemplateMethodPattern;

class TeaWithHook extends CaffeineBeverageWithHook {

    public void brew() {
        System.out.println("Steeping the tea");
    }

    public void addCondiments() {
        System.out.println("Adding Lemon");
    }
    
    // Tea sınıfı hook metodunu (customerWantsCondiments) ezmez.
    // Bu yüzden varsayılan olarak "true" döner ve limon her zaman eklenir.
}

