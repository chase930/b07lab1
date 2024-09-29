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
        this.coefficients = new double[len];
        this.exponents = new int[len];
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
            double coefficient = 1.0;
            int exponent = 0;

            if (term.contains("x")) {
                String[] parts = term.split("x");
                coefficient = parts[0].isEmpty() || parts[0].equals("+") ? 1.0 : Double.parseDouble(parts[0].replace("+", ""));
                exponent = (parts.length > 1 && !parts[1].isEmpty()) ? Integer.parseInt(parts[1]) : 1;
            } else {
                coefficient = Double.parseDouble(term);  // Constant term case
            }

            coefficientsList.add(coefficient);
            exponentsList.add(exponent);
        }

        this.coefficients = coefficientsList.stream().mapToDouble(Double::doubleValue).toArray();
        this.exponents = exponentsList.stream().mapToInt(Integer::intValue).toArray();

        reader.close();
    }

    public Polynomial add(Polynomial p) {
        ArrayList<Double> newCoefficients = new ArrayList<>();
        ArrayList<Integer> newExponents = new ArrayList<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            newCoefficients.add(this.coefficients[i]);
            newExponents.add(this.exponents[i]);
        }

        for (int i = 0; i < p.coefficients.length; i++) {
            boolean found = false;
            for (int j = 0; j < newExponents.size(); j++) {
                if (newExponents.get(j) == p.exponents[i]) {
                    newCoefficients.set(j, newCoefficients.get(j) + p.coefficients[i]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                newCoefficients.add(p.coefficients[i]);
                newExponents.add(p.exponents[i]);
            }
        }

        double[] finalCoefficients = newCoefficients.stream().mapToDouble(Double::doubleValue).toArray();
        int[] finalExponents = newExponents.stream().mapToInt(Integer::intValue).toArray();

        return new Polynomial(finalCoefficients, finalExponents);
    }

    public Polynomial multiply(Polynomial p) {
        int maxExponent = 0;
        for (int exp1 : this.exponents) {
            for (int exp2 : p.exponents) {
                int newExponent = exp1 + exp2;
                maxExponent = Math.max(maxExponent, newExponent);
            }
        }

        double[] resultCoefficients = new double[maxExponent + 1];

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < p.coefficients.length; j++) {
                int newExponent = this.exponents[i] + p.exponents[j];
                double newCoefficient = this.coefficients[i] * p.coefficients[j];
                resultCoefficients[newExponent] += newCoefficient;
            }
        }

        int nonZeroCount = 0;
        for (double coefficient : resultCoefficients) {
            if (coefficient != 0.0) {
                nonZeroCount++;
            }
        }

        double[] finalCoefficients = new double[nonZeroCount];
        int[] finalExponents = new int[nonZeroCount];

        int index = 0;
        for (int exponent = 0; exponent < resultCoefficients.length; exponent++) {
            if (resultCoefficients[exponent] != 0.0) {
                finalCoefficients[index] = resultCoefficients[exponent];
                finalExponents[index] = exponent;
                index++;
            }
        }

        return new Polynomial(finalCoefficients, finalExponents);
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
        return Math.abs(evaluate(x)) < 1e-9;
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
