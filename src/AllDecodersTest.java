
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AllDecodersTest {

    public static void main(String[] args) {

        Map<String, Supplier<LineEncoder>> encoders =
                new LinkedHashMap<>();

        Map<String, Supplier<LineDecoder>> decoders =
                new LinkedHashMap<>();

        encoders.put("NRZ-L", NRZL::new);
        decoders.put("NRZ-L", NRZLDecoder::new);

        encoders.put("NRZ-I", NRZI::new);
        decoders.put("NRZ-I", NRZIDecoder::new);

        encoders.put("Manchester", Manchester::new);
        decoders.put("Manchester", ManchesterDecoder::new);

        encoders.put("Differential Manchester",
                DifferentialManchester::new);
        decoders.put("Differential Manchester",
                DifferentialManchesterDecoder::new);

        encoders.put("AMI", AMI::new);
        decoders.put("AMI", AMIDecoder::new);

        encoders.put("B8ZS", B8ZS::new);
        decoders.put("B8ZS", B8ZSDecoder::new);

        encoders.put("HDB3", HDB3::new);
        decoders.put("HDB3", HDB3Decoder::new);

        String[] testCases = {
                "10101010",
                "11001100",
                "10000",
                "110000",
                "101100001000",
                "111100001111",
                "1010101010101010"
        };

        int passed = 0;
        int failed = 0;

        System.out.println("==================================");
        System.out.println(" LINE ENCODER/DECODER TEST SUITE ");
        System.out.println("==================================");

        for (String scheme : encoders.keySet()) {

            System.out.println("\nTesting " + scheme);

            for (String original : testCases) {

                try {
                    LineEncoder encoder = encoders.get(scheme).get();
                    LineDecoder decoder = decoders.get(scheme).get();

                    double[] signal = encoder.encode(original);
                    String decoded = decoder.decode(signal);

                    if (original.equals(decoded)) {
                        System.out.println(
                                "PASS | " + original
                        );
                        passed++;
                    } else {
                        System.out.println(
                                "FAIL | " + original
                                        + " | Decoded: " + decoded
                        );
                        failed++;
                    }

                } catch (Exception e) {
                    System.out.println(
                            "ERROR | " + original
                                    + " | " + e.getMessage()
                    );
                    failed++;
                }
            }
        }

        System.out.println("\n==================================");
        System.out.println("TEST SUMMARY");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println("==================================");
    }
}
