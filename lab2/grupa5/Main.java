import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        try {
            FileReader f = new FileReader("src/testinput.txt");
            MPLexer lexer = new MPLexer(f);
            SyntaxAnalyzer sa = new SyntaxAnalyzer(lexer);
            
            if (sa.analiza()) {
                System.out.println("Tekst je ispravan");
            } else {
                System.out.println("Tekst neispravan");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}