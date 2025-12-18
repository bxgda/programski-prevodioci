import java.io.*;

public class Main {
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Draga\\OneDrive\\Documents\\ProgramskiPrevodioci\\PrevodiociLab1\\src\\testinput.txt";

        try {
            FileReader fr = new FileReader(inputFile);
            MPLexer lexer = new MPLexer(fr);

            Yytoken token;
            while ((token = lexer.next_token()).type != sym.EOF) {
                System.out.println(token);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fajl nije pronađen: " + inputFile);
        } catch (IOException e) {
            System.out.println("Greška pri čitanju fajla: " + e.getMessage());
        }
    }
}
