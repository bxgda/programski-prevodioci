
import java.util.Hashtable;
public class KWTable {

    private final Hashtable mTable;
    public KWTable()
    {
            mTable = new Hashtable();
            mTable.put("main", sym.MAIN);
            mTable.put("int", sym.INT);
            mTable.put("char", sym.CHAR);
            mTable.put("float", sym.FLOAT);
            mTable.put("case", sym.CASE);
            mTable.put("when", sym.WHEN);
    }

    public int find(String keyword)
    {
            Object symbol = mTable.get(keyword);
            if (symbol != null)
                    return ((Integer)symbol);

            return sym.ID;
    }
}
