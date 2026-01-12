import java.util.Hashtable;

public class KWTable {
    private Hashtable<String, Integer> mTable;

    public KWTable() {
        mTable = new Hashtable<String, Integer>();
        
        // Ključne reči usklađene sa tvojim sym.java
        mTable.put("main", sym.MAIN);
        mTable.put("exit", sym.EXIT);
        mTable.put("int", sym.INT);
        mTable.put("float", sym.FLOAT);
        mTable.put("bool", sym.BOOL);
        mTable.put("for", sym.FOR);
        mTable.put("in", sym.IN);
        mTable.put("apply", sym.APPLY);
        
        // true i false se tretiraju kao konstante (CONST)
        mTable.put("true", sym.CONST);
        mTable.put("false", sym.CONST);
    }

    public int find(String keyword) {
        Object symbol = mTable.get(keyword);
        if (symbol != null)
            return ((Integer) symbol).intValue();

        // Ako nije u tabeli, onda je običan identifikator
        return sym.ID;
    }
}