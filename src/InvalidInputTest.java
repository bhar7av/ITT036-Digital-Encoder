
public class InvalidInputTest {

    public static void main(String[] args) {

        test("NRZ-L", () ->
                new NRZLDecoder().decode(new double[]{0.5}));

        test("NRZ-I", () ->
                new NRZIDecoder().decode(new double[]{0.0}));

        test("Manchester", () ->
                new ManchesterDecoder().decode(new double[]{1.0}));

        test("Differential Manchester", () ->
                new DifferentialManchesterDecoder()
                        .decode(new double[]{1.0}));

        test("AMI", () ->
                new AMIDecoder().decode(new double[]{0.5}));

        test("B8ZS", () ->
                new B8ZSDecoder().decode(new double[]{0.5}));

        test("HDB3", () ->
                new HDB3Decoder().decode(new double[]{0.5}));
    }

    private static void test(String name, Runnable operation) {

        try {
            operation.run();
            System.out.println(
                    "FAIL | " + name + " accepted invalid input."
            );
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "PASS | " + name + " rejected invalid input."
            );
        } catch (Exception e) {
            System.out.println(
                    "FAIL | " + name + " threw "
                            + e.getClass().getSimpleName()
            );
        }
    }
}
