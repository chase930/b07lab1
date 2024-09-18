public class Polynomial {
    double[] coefficients;
    
    public Polynomial() {
        this.coefficients = new double[]{0};
    }

    public Polynomial(double[] coefficients_to_insert) {
        int len = coefficients_to_insert.length;
        this.coefficients = new double[coefficients_to_insert.length];
        for (int i = 0; i < len; i++) {
            coefficients[i] = coefficients_to_insert[i];
        }
    }

    public Polynomial add(Polynomial p) {
        int len1 = this.coefficients.length;
        int len2 = p.coefficients.length;
        int maxLen = Math.max(len1, len2); // The new polynomial's length should be the max of both lengths
    
        Polynomial newPolynomial = new Polynomial(new double[maxLen]);
    
        for (int i = 0; i < Math.min(len1, len2); i++) {
            newPolynomial.coefficients[i] = this.coefficients[i] + p.coefficients[i];
        }
    
        if (len1 > len2) {
            for (int i = len2; i < len1; i++) {
                newPolynomial.coefficients[i] = this.coefficients[i];
            }
        }
        else if (len2 > len1) {
            for (int i = len1; i < len2; i++) {
                newPolynomial.coefficients[i] = p.coefficients[i];
            }
        }
    
        return newPolynomial;
    }

    public double evaluate(double x) {
        int len = this.coefficients.length;
        double result = 0;

        for (int i = 0; i < len; i++) {
            result += this.coefficients[i] * Math.pow(x, i);
        }

        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}