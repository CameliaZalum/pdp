import java.util.ArrayList;
import java.util.List;

public class Polynomial {
    List<Integer> coefficients;
    public Polynomial(List<Integer> c) {
        this.coefficients = c;
    }
    public Polynomial(int degree) {
        this.coefficients = new ArrayList<>(degree);
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
    public Polynomial plus(Polynomial a) {
        int actualsize = this.coefficients.size() -1;
        int maxlen = Math.max(this.coefficients.size() -1, a.getDegree());
        List<Integer> newCoefficients = new ArrayList<>();
        for ( int i =0; i < maxlen; i++){
            if(i < actualsize && i < a.getDegree()){
                newCoefficients.add(this.coefficients.get(i) + a.getCoefficients().get(i));
            } else if(i > actualsize && i < a.getDegree()){
                newCoefficients.add(a.getCoefficients().get(i));
            } else if (i < actualsize && i > a.getDegree()){
                newCoefficients.add(this.coefficients.get(i));
            }
        }
         Polynomial result = new Polynomial(newCoefficients);
        return result;
    }
    public Polynomial minus(Polynomial a) {
        int actualsize = this.coefficients.size() -1;
        int maxlen = Math.max(this.coefficients.size() -1, a.getDegree());
        List<Integer> newCoefficients = new ArrayList<>();
        for ( int i =0; i < maxlen; i++){
            if(i < actualsize && i < a.getDegree()){
                newCoefficients.add(this.coefficients.get(i) - a.getCoefficients().get(i));
            } else if(i > actualsize && i < a.getDegree()){
                newCoefficients.add(-a.getCoefficients().get(i));
            } else if (i < actualsize && i > a.getDegree()){
                newCoefficients.add(this.coefficients.get(i));
            }
        }
        Polynomial result = new Polynomial(newCoefficients);
        return result;
    }
    public void addZeros(int n){
        for ( int i = 0; i < n; i ++){
            this.coefficients.add(0);
        }
    }
}
