package TemplateMethodPattern;

public class BeverageTestDrive {
    public static void main(String[] args) {

        // Çay Testi (Hook kullanılmadı, varsayılan akış)
        TeaWithHook teaHook = new TeaWithHook();
        System.out.println("\nMaking tea...");
        teaHook.prepareRecipe();

        // Kahve Testi (Hook kullanıldı, kullanıcıya sorulacak)
        CoffeeWithHook coffeeHook = new CoffeeWithHook();
        System.out.println("\nMaking coffee...");
        coffeeHook.prepareRecipe();
    }
}
