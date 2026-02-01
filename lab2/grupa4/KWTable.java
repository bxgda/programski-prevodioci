import java.util.Hashtable;

public class KWTable {

    // Dodato <String, Integer> da uklonimo warnings
    private Hashtable<String, Integer> mTable;

    public KWTable() {
        // Inicijalizacija hash tabele
        mTable = new Hashtable<String, Integer>();
        mTable.put("program", sym.PROGRAM);
        mTable.put("end", sym.END);
        mTable.put("int", sym.INT);
        mTable.put("float", sym.FLOAT);
        mTable.put("char", sym.CHAR);
    }

    /**
     * Vraca ID kljucne reci ili ID identifikatora
     */
    public int find(String keyword) {
        Object symbol = mTable.get(keyword);
        if (symbol != null)
            return ((Integer) symbol).intValue();

        // Ako nije kljucna rec, onda je identifikator
        return sym.ID;
    }
}