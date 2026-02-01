import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Molim vas navedite putanju do test fajla (npr. test.txt)");
            return;
        }

        try {
            // 1. Inicijalizacija leksickog analizatora
            System.out.println("Pokretanje leksicke analize...");
            FileReader fileReader = new FileReader(args[0]);
            MPLexer lexer = new MPLexer(fileReader);

            // 2. Inicijalizacija sintaksnog analizatora (LR Parser)
            System.out.println("Pokretanje sintaksne analize...");
            LRParser parser = new LRParser(lexer);

            // 3. Izvrsavanje analize
            if (parser.parse()) {
                System.out.println("Status: SUCCESS");
            } else {
                System.out.println("Status: FAILED");
            }

        } catch (Exception e) {
            System.err.println("Fatalna greska: " + e.getMessage());
            e.printStackTrace();
        }
    }
}