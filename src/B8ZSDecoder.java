
public class B8ZSDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be null or empty."
            );
        }

        StringBuilder bits = new StringBuilder();

        // Zero means that no nonzero pulse has been processed yet.
        double lastMark = 0.0;

        int i = 0;

        while (i < signal.length) {

            if (i + 8 <= signal.length) {

                // If the preceding mark was positive, use the
                // positive-preceding-mark B8ZS pattern.
                if (lastMark > 0.0
                        && matches(signal, i,
                        new double[]{0, 0, 0, 1, -1, 0, -1, 1})) {

                    bits.append("00000000");
                    lastMark = 1.0;
                    i += 8;
                    continue;
                }

                // If the preceding mark was negative, use the
                // negative-preceding-mark B8ZS pattern.
                if (lastMark < 0.0
                        && matches(signal, i,
                        new double[]{0, 0, 0, -1, 1, 0, 1, -1})) {

                    bits.append("00000000");
                    lastMark = -1.0;
                    i += 8;
                    continue;
                }

                // At the beginning of a signal, the previous
                // polarity is unknown. Accept either valid
                // substitution pattern to match the encoder's
                // initial convention.
                if (lastMark == 0.0) {

                    if (matches(signal, i,
                            new double[]{0, 0, 0, 1, -1, 0, -1, 1})) {

                        bits.append("00000000");
                        lastMark = 1.0;
                        i += 8;
                        continue;
                    }

                    if (matches(signal, i,
                            new double[]{0, 0, 0, -1, 1, 0, 1, -1})) {

                        bits.append("00000000");
                        lastMark = -1.0;
                        i += 8;
                        continue;
                    }
                }
            }

            // Decode an ordinary AMI sample.
            double level = signal[i];

            if (level == 0.0) {

                bits.append('0');

            } else if (level == 1.0 || level == -1.0) {

                if (lastMark != 0.0 && level == lastMark) {
                    throw new IllegalArgumentException(
                            "Unexpected bipolar violation at index " + i
                    );
                }

                bits.append('1');
                lastMark = level;

            } else {

                throw new IllegalArgumentException(
                        "Invalid signal level at index "
                                + i + ": " + level
                );
            }

            i++;
        }

        return bits.toString();
    }

    private boolean matches(
            double[] signal,
            int start,
            double[] pattern) {

        if (start + pattern.length > signal.length) {
            return false;
        }

        for (int j = 0; j < pattern.length; j++) {
            if (signal[start + j] != pattern[j]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getName() {
        return "B8ZS Decoder";
    }
}
