
public class HDB3 implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length()];

        // The first normal AMI mark will be +1.
        double lastMark = -1.0;

        // Count normal nonzero AMI pulses since the last substitution.
        int pulseCount = 0;

        int i = 0;

        while (i < data.length()) {

            // Detect four consecutive zeros.
            if (i + 4 <= data.length()
                    && data.startsWith("0000", i)) {

                if (pulseCount % 2 == 0) {

                    // Even pulse count: B00V.
                    double b = -lastMark;
                    double v = b;

                    signal[i] = b;
                    signal[i + 1] = 0.0;
                    signal[i + 2] = 0.0;
                    signal[i + 3] = v;

                    lastMark = v;

                } else {

                    // Odd pulse count: 000V.
                    double v = lastMark;

                    signal[i] = 0.0;
                    signal[i + 1] = 0.0;
                    signal[i + 2] = 0.0;
                    signal[i + 3] = v;

                    lastMark = v;
                }

                // Start a new counting interval after substitution.
                pulseCount = 0;
                i += 4;

            } else {

                char bit = data.charAt(i);

                if (bit == '0') {
                    signal[i] = 0.0;
                } else {
                    lastMark = -lastMark;
                    signal[i] = lastMark;
                    pulseCount++;
                }

                i++;
            }
        }

        return signal;
    }

    @Override
    public String getName() {
        return "HDB3";
    }
}
