
public class HDB3Decoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be null or empty."
            );
        }

        StringBuilder bits = new StringBuilder();

        // Must match the initial polarity used by your HDB3 encoder.
        double lastMark = -1.0;

        // Number of normal nonzero pulses since the last substitution.
        int pulseCount = 0;

        int i = 0;

        while (i < signal.length) {

            // Check whether four samples are available.
            if (i + 4 <= signal.length) {

                double a = signal[i];
                double b = signal[i + 1];
                double c = signal[i + 2];
                double d = signal[i + 3];

                // Case 1: 000V substitution.
                // Used when the pulse count is odd.
                if (pulseCount % 2 == 1
                        && a == 0.0
                        && b == 0.0
                        && c == 0.0
                        && d == lastMark) {

                    bits.append("0000");

                    lastMark = d;
                    pulseCount = 0;
                    i += 4;

                    continue;
                }

                // Case 2: B00V substitution.
                // B and V have the same polarity.
                // Used when the pulse count is even.
                if (pulseCount % 2 == 0
                        && a == -lastMark
                        && b == 0.0
                        && c == 0.0
                        && d == a) {

                    bits.append("0000");

                    lastMark = d;
                    pulseCount = 0;
                    i += 4;

                    continue;
                }
            }

            // Decode an ordinary AMI sample.
            double level = signal[i];

            if (level == 0.0) {

                bits.append('0');

            } else if (level == 1.0 || level == -1.0) {

                // Ordinary AMI pulses must alternate polarity.
                if (level == lastMark) {
                    throw new IllegalArgumentException(
                            "Invalid HDB3 signal at sample " + i
                                    + ": unexpected bipolar violation."
                    );
                }

                bits.append('1');
                lastMark = level;
                pulseCount++;

            } else {

                throw new IllegalArgumentException(
                        "Invalid signal level at sample " + i
                                + ": " + level
                );
            }

            i++;
        }

        return bits.toString();
    }

    @Override
    public String getName() {
        return "HDB3 Decoder";
    }
}
