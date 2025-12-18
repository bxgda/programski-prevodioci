import java.util.Hashtable;

public class KWTable {

    private Hashtable<String, Integer> mTable;

    public KWTable() {
        // inicijalizacija hes tabele za kljucne reci
        mTable = new Hashtable<String, Integer>();

        // Kljucne reci jezika
        mTable.put("program", sym.PROGRAM);
        mTable.put("begin", sym.BEGIN);
        mTable.put("end", sym.END);

        mTable.put("integer", sym.INTEGER);
        mTable.put("char", sym.CHAR);
        mTable.put("real", sym.REAL);
        mTable.put("boolean", sym.BOOLEAN);

        mTable.put("while", sym.WHILE);
        mTable.put("else", sym.ELSE);

        mTable.put("or", sym.OR);
        mTable.put("and", sym.AND);

        mTable.put("true", sym.TRUE);
        mTable.put("false", sym.FALSE);
    }

    public int find(String keyword) 
    {
        Integer symbol = mTable.get(keyword);
        if (symbol != null) {
            return ((Integer)symbol).intValue();
        }

        return sym.ID;
    }
}
