import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Polynomial {
    double[] coefficients;
    int[] exponents;
    
    public Polynomial() {
        this.coefficients = new double[]{0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] coefficients_to_insert, int[] exponents_to_insert) {
        int len = coefficients_to_insert.length;
        this.coefficients = new double[coefficients_to_insert.length];
        this.exponents = new int[exponents_to_insert.length];
        for (int i = 0; i < len; i++) {
            coefficients[i] = coefficients_to_insert[i];
            exponents[i] = exponents_to_insert[i];
        }
    }

    public Polynomial(File file) throws IOException {
        ArrayList<Double> coefficientsList = new ArrayList<>();
        ArrayList<Integer> exponentsList = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        String[] terms = line.split("(?=[+-])"); 

        for (String term : terms) {
            term = term.trim();
            String[] parts = term.split("x");
            double coefficient = parts[0].isEmpty() ? 1.0 : Double.parseDouble(parts[0]);
            int exponent = 0;

            if (parts.length > 1) {
                exponent = parts[1].isEmpty() ? 1 : Integer.parseInt(parts[1]);
            }

            coefficientsList.add(coefficient);
            exponentsList.add(exponent);
        }

        this.coefficients = coefficientsList.stream().mapToDouble(d -> d).toArray();
        this.exponents = exponentsList.stream().mapToInt(i -> i).toArray();

        reader.close();
    }

    public Polynomial add(Polynomial p) {
        int maxLength = this.coefficients.length + p.coefficients.length;
        double[] newCoefficients = new double[maxLength];
        int[] newExponents = new int[maxLength];

        HashMap<Integer, Double> combined = new HashMap<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            combined.put(this.exponents[i], combined.getOrDefault(this.exponents[i], 0.0) + this.coefficients[i]);
        }

        for (int i = 0; i < p.coefficients.length; i++) {
            combined.put(p.exponents[i], combined.getOrDefault(p.exponents[i], 0.0) + p.coefficients[i]);
        }

        int index = 0;
        for (Map.Entry<Integer, Double> entry : combined.entrySet()) {
            newExponents[index] = entry.getKey();
            newCoefficients[index] = entry.getValue();
            index++;
        }

        double[] finalCoefficients = new double[index];
        int[] finalExponents = new int[index];
        System.arraycopy(newCoefficients, 0, finalCoefficients, 0, index);
        System.arraycopy(newExponents, 0, finalExponents, 0, index);
    
        Polynomial newPolynomial = new Polynomial(finalCoefficients, finalExponents);
    
        return newPolynomial;
    }

    public Polynomial multiply(Polynomial p) {
        int maxLength = this.coefficients.length * p.coefficients.length;
        double[] newCoefficients = new double[maxLength];
        int[] newExponents = new int[maxLength];

        int index = 0;

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < p.coefficients.length; j++) {
                newCoefficients[index] = this.coefficients[i] * p.coefficients[j];
                newExponents[index] = this.exponents[i] + p.exponents[j];
                index++;
            }
        }

        HashMap<Integer, Double> combined = new HashMap<>();

        for (int i = 0; i < index; i++) {
            combined.put(newExponents[i], combined.getOrDefault(newExponents[i], 0.0) + newCoefficients[i]);
        }

        double[] finalCoefficients = new double[combined.size()];
        int[] finalExponents = new int[combined.size()];

        int index2 = 0;
        for (Map.Entry<Integer, Double> entry : combined.entrySet()) {
            finalExponents[index2] = entry.getKey();
            finalCoefficients[index2] = entry.getValue();
            index2++;
        }

        Polynomial newPolynomial = new Polynomial(finalCoefficients, finalExponents);
    
        return newPolynomial;
    }

    public double evaluate(double x) {
        int len = this.coefficients.length;
        double result = 0;

        for (int i = 0; i < len; i++) {
            result += this.coefficients[i] * Math.pow(x, this.exponents[i]);
        }

        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }

    public void saveToFile(String fileName) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            if (i > 0 && coefficients[i] >= 0) {
                sb.append("+");
            }
            sb.append(coefficients[i]);
            if (exponents[i] != 0) {
                sb.append("x");
                if (exponents[i] != 1) {
                    sb.append(exponents[i]);
                }
            }
        }
        writer.write(sb.toString());
    }
}
}