import java.io.*;
import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) {
        try {
            // Provera da li je korisnik prosledio ime fajla kao argument
            if (args.length == 0) {
                System.out.println("GRESKA: Morate proslediti ime ulaznog fajla kao argument.");
                System.out.println("Primer: java -cp \".;jcup_runtime.jar\" Main test.txt");
                return;
            }

            // Uzimamo prvi argument iz komandne linije
            String fileName = args[0];
            FileReader fr = new FileReader(fileName);
            
            // Inicijalizacija leksera
            MPLexer lexer = new MPLexer(fr);
            
            // Inicijalizacija parsera
            // Proveri da li se tvoja klasa zove 'parser' ili 'MPParser'
            // menja se u zavisnosti sta treba da pokrenemo
            parser_err p = new parser_err(lexer);
            
            System.out.println("Zapocinjem sintaksnu analizu fajla: " + fileName);
            
            // Pokretanje analize
            p.parse();
            
            System.out.println("Sintaksna analiza zavrsena uspesno!");
            
        } catch (FileNotFoundException e) {
            System.err.println("GRESKA: Fajl nije pronadjen.");
        } catch (Exception e) {
            System.err.println("Sintaksna analiza NIJE uspela.");
            System.err.println("Opis greske: " + e.getMessage());
            // e.printStackTrace(); // Ostavljeno u komentaru, odkomentarisi ako ti zatreba debug
        }
    }
}