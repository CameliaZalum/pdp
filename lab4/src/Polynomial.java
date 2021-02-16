import java.util.ArrayList;
import java.util.List;

public class Polynomial {
    List<Integer> coefficients;
    public Polynomial(List<Integer> c) {
        this.coefficients = c;
    }
    public Polynomial(int degree) {
        this.coefficients = new ArrayList<>();
        for( int i=0; i < degree; i ++){
            coefficients.add(0);
        }
    }
    public int getDegree(){
        return this.coefficients.size() - 1;
    }
    public List<Integer> getCoefficients(){
        return coefficients;
    }
    public void setCoefficient(int index, int value){
        this.coefficients.set(index, value);
    }
    @Override
    public String toString() {
        return "Polynomial{" +
                "coefficients=" + coefficients +
                '}';
    }

    public Polynomial minus(Polynomial a, Polynomial b) {
        int actualsize = a.getDegree();
        int maxlen = Math.max(b.getDegree(), a.getDegree());
        List<Integer> newCoefficients = new ArrayList<>();
        for ( int i =0; i < maxlen; i++){
            if(i < a.getDegree() && i < a.getDegree()){
                newCoefficients.add(a.coefficients.get(i) - b.getCoefficients().get(i));
            } else if(i > a.getDegree() && i < a.getDegree()){
                newCoefficients.add(-b.getCoefficients().get(i));
            } else if (i < actualsize && i > a.getDegree()){
                newCoefficients.add(a.coefficients.get(i));
            }
        }
        Polynomial result = new Polynomial(newCoefficients);
        return result;
    }
    public Polynomial addZeros(Polynomial a, int n){
        List<Integer> newCoefficients = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            newCoefficients.add(0);
        }
        for (int i = 0; i < a.getDegree() + 1; i++) {
            newCoefficients.add(a.coefficients.get(i));
        }
//        this.coefficients = newCoefficients;
        return new Polynomial(newCoefficients);
    }
}
