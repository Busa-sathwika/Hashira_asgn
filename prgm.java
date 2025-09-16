import java.math.BigInteger;
import java.util.*;
import org.json.JSONObject;

public class HashiraPolynomial {
    // Converts a string in given base to BigInteger
    static BigInteger parseInBase(String value, int base) {
        return new BigInteger(value, base);
    }

    // Multiplies current coeffs by (x - root)
    static List<BigInteger> multiplyWithRoot(List<BigInteger> coeffs, BigInteger root) {
        List<BigInteger> result = new ArrayList<>(coeffs.size() + 1);
        // Initialize the result with zeros
        for (int i = 0; i <= coeffs.size(); ++i)
            result.add(BigInteger.ZERO);
        for (int i = 0; i < coeffs.size(); ++i) {
            // result[i + 1] += coeffs[i];
            result.set(i + 1, result.get(i + 1).add(coeffs.get(i)));
            // result[i] += coeffs[i] * (-root)
            result.set(i, result.get(i).subtract(coeffs.get(i).multiply(root)));
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder jsonInput = new StringBuilder();

        while (scanner.hasNextLine()) {
            jsonInput.append(scanner.nextLine());
        }
        scanner.close();

        String input = jsonInput.toString();

        // Parse JSON
        JSONObject obj = new JSONObject(input);
        int k = obj.getJSONObject("keys").getInt("k");
        List<BigInteger> roots = new ArrayList<>();

        for (int i = 1; i <= k; i++) {
            JSONObject rootInfo = obj.getJSONObject(String.valueOf(i));
            int base = Integer.parseInt(rootInfo.getString("base"));
            String value = rootInfo.getString("value");
            roots.add(parseInBase(value, base));
        }

        // Construct polynomial from roots: (x - r1)(x - r2)...
        List<BigInteger> coeffs = new ArrayList<>();
        coeffs.add(BigInteger.ONE); // start as "1"

        for (BigInteger root : roots) {
            coeffs = multiplyWithRoot(coeffs, root);
        }

        // Output: degree and all coefficients (highest to constant)
        int degree = coeffs.size() - 1;
        System.out.println("Degree: " + degree);
        System.out.println("Coefficients (highest power to constant):");
        for (BigInteger coeff : coeffs) {
            System.out.print(coeff + " ");
        }
        System.out.println();
    }
}
