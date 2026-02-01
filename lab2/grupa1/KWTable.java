import java.util.Hashtable;

public class KWTable {

    // Dodato <String, Integer> da uklonimo warnings
    private Hashtable<String, Integer> mTable;

    public KWTable() {
        // Inicijalizacija hash tabele
        mTable = new Hashtable<String, Integer>();
        mTable.put("for", sym.FOR);
        mTable.put("in", sym.IN);
        mTable.put("apply", sym.APPLY);
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