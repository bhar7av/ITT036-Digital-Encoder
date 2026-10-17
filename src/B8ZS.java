
public class B8ZS implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length()];

        // The first ordinary AMI mark will be +1.
        double lastMark = -1.0;

        int i = 0;

        while (i < data.length()) {

            // Check whether eight consecutive zeros start here.
            if (i + 8 <= data.length()
                    && data.startsWith("00000000", i)) {

                double[] pattern;

                if (lastMark > 0) {
                    pattern = new double[] {
                            0, 0, 0, 1, -1, 0, -1, 1
                    };
                } else {
                    pattern = new double[] {
                            0, 0, 0, -1, 1, 0, 1, -1
                    };
                }

                for (int j = 0; j < 8; j++) {
                    signal[i + j] = pattern[j];
                }

                // The final pulse is the final B pulse.
                lastMark = pattern[7];

                i += 8;

            } else {

                char bit = data.charAt(i);

                if (bit == '0') {
                    signal[i] = 0.0;
                } else {
                    lastMark = -lastMark;
                    signal[i] = lastMark;
                }

                i++;
            }
        }

        return signal;
    }

    @Override
    public String getName() {
        return "B8ZS";
    }
}
