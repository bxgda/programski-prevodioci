import java.io.FileReader;
import java.io.Reader;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Upotreba: java Main <ulazni_fajl>");
			System.exit(1);
		}

		Reader r = new FileReader(args[0]);
		MPLexer lexer = new MPLexer(r);
		MPParser parser = new MPParser(lexer);

		parser.parse();
		System.out.println("=== Parsiranje uspesno zavrseno ===");
	}
}
