
public class Production {
	int left;  // leva strana produkcije (neterminal)
    int[] right; // desna strana produkcije (niz terminala/neterminala)
    
    Production(int left, int[] right) {
        this.left = left;
        this.right = right;
    }
}
