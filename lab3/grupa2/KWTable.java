import java.util.Hashtable;

public class KWTable {
    private Hashtable<String, Integer> mTable;

    public KWTable() {
        mTable = new Hashtable<String, Integer>();
        
        // Ključne reči koje tvoja gramatika STVARNO koristi
        mTable.put("main", sym.MAIN);
        mTable.put("int", sym.INT);
        mTable.put("real", sym.REAL);     // U tvom sym.java je REAL, a ne FLOAT
        mTable.put("boolean", sym.BOOLEAN); // U tvom sym.java je BOOLEAN, a ne BOOL
        mTable.put("if", sym.IF);
        mTable.put("else", sym.ELSE);
        mTable.put("elif", sym.ELIF);
        
        // true i false su konstante
        mTable.put("true", sym.CONST);
        mTable.put("false", sym.CONST);
    }

    public int find(String keyword) {
        Integer symbol = mTable.get(keyword);
        if (symbol != null)
            return symbol.intValue();

        // Ako nije u tabeli, onda je običan identifikator
        return sym.ID;
    }
}