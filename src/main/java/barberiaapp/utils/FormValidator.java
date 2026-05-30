package barberiaapp.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

public class FormValidator {

    private static final Scanner sc = new Scanner(System.in);

    public static int validarEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                int v = sc.nextInt(); sc.nextLine(); return v;
            } catch (InputMismatchException e) {
                System.out.println("  [!] Ingresa un numero entero valido.");
                sc.nextLine();
            }
        }
    }

    public static double validarDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                double v = sc.nextDouble(); sc.nextLine(); return v;
            } catch (InputMismatchException e) {
                System.out.println("  [!] Ingresa un numero decimal valido.");
                sc.nextLine();
            }
        }
    }

    public static String validarTexto(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String v = sc.nextLine().trim();
            if (!v.isEmpty()) return v;
            System.out.println("  [!] El campo no puede estar vacio.");
        }
    }

    public static boolean validarBooleano(String prompt) {
        while (true) {
            System.out.print(prompt + " (1=Si / 2=No): ");
            String v = sc.nextLine().trim();
            if (v.equals("1")) return true;
            if (v.equals("2")) return false;
            System.out.println("  [!] Ingresa 1 o 2.");
        }
    }

    public static String validarTextoConRegla(String prompt, Predicate<String> regla, String mensajeError) {
        while (true) {
            String v = validarTexto(prompt);
            if (regla.test(v)) return v;
            System.out.println("  [!] " + mensajeError);
        }
    }

    public static int validarEnteroConRegla(String prompt, Predicate<Integer> regla, String mensajeError) {
        while (true) {
            int v = validarEntero(prompt);
            if (regla.test(v)) return v;
            System.out.println("  [!] " + mensajeError);
        }
    }
}
