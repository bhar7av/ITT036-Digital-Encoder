
public class DifferentialManchesterDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be empty."
            );
        }

        if (signal.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Differential Manchester requires pairs of samples."
            );
        }

        StringBuilder bits = new StringBuilder();

        double previousLevel = 1.0;

        for (int i = 0; i < signal.length; i += 2) {

            double firstHalf = signal[i];
            double secondHalf = signal[i + 1];

            if (!isValidLevel(firstHalf)
                    || !isValidLevel(secondHalf)) {
                throw new IllegalArgumentException(
                        "Invalid voltage level at sample " + i
                );
            }

            // Every bit must have a midpoint transition.
            if (firstHalf == secondHalf) {
                throw new IllegalArgumentException(
                        "Missing midpoint transition at bit "
                                + (i / 2)
                );
            }

            // Starting transition means bit 0.
            // No starting transition means bit 1.
            if (firstHalf != previousLevel) {
                bits.append('0');
            } else {
                bits.append('1');
            }

            previousLevel = secondHalf;
        }

        return bits.toString();
    }

    private boolean isValidLevel(double level) {
        return level == 1.0 || level == -1.0;
    }

    @Override
    public String getName() {
        return "Differential Manchester Decoder";
    }
}
