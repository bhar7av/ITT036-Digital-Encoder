
public class AMIDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be empty."
            );
        }

        StringBuilder bits = new StringBuilder(signal.length);

        // First nonzero AMI pulse may be positive or negative.
        double lastMark = 0.0;

        for (int i = 0; i < signal.length; i++) {

            double level = signal[i];

            if (level == 0.0) {
                bits.append('0');

            } else if (level == 1.0 || level == -1.0) {

                if (lastMark != 0.0 && level == lastMark) {
                    throw new IllegalArgumentException(
                            "AMI bipolar violation at index " + i
                    );
                }

                bits.append('1');
                lastMark = level;

            } else {
                throw new IllegalArgumentException(
                        "Invalid AMI voltage level at index " + i
                );
            }
        }

        return bits.toString();
    }

    @Override
    public String getName() {
        return "AMI Decoder";
    }
}
