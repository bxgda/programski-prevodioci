
import java.util.Hashtable;
public class KWTable {

	private Hashtable mTable;
	public KWTable()
	{
		// Inicijalizcaija hash tabele koja pamti kljucne reci
		mTable = new Hashtable();
		 mTable.put("main",  sym.MAIN);

	     mTable.put("int",   sym.INT);
	     mTable.put("char",  sym.CHAR);
	     mTable.put("float", sym.FLOAT);
	     mTable.put("bool",  sym.BOOL);

	     mTable.put("loop",  sym.LOOP);
	     mTable.put("redo",  sym.REDO);
	}
	
	/**
	 * Vraca ID kljucne reci 
	 */
	public int find(String keyword)
	{
		Object symbol = mTable.get(keyword);
		if (symbol != null)
			return ((Integer)symbol).intValue();
		
		// Ako rec nije pronadjena u tabeli kljucnih reci radi se o identifikatoru
		return sym.ID;
	}
	

}
