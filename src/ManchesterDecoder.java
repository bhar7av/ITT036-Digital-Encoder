
public class ManchesterDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be empty."
            );
        }

        if (signal.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Manchester signal must contain pairs of samples."
            );
        }

        StringBuilder bits = new StringBuilder();

        for (int i = 0; i < signal.length; i += 2) {

            double firstHalf = signal[i];
            double secondHalf = signal[i + 1];

            if (!isValidLevel(firstHalf)
                    || !isValidLevel(secondHalf)) {
                throw new IllegalArgumentException(
                        "Invalid Manchester voltage level."
                );
            }

            if (firstHalf == 1.0 && secondHalf == -1.0) {
                bits.append('0');

            } else if (firstHalf == -1.0
                    && secondHalf == 1.0) {
                bits.append('1');

            } else {
                throw new IllegalArgumentException(
                        "Invalid Manchester transition at sample "
                                + i
                );
            }
        }

        return bits.toString();
    }

    private boolean isValidLevel(double level) {
        return level == 1.0 || level == -1.0;
    }

    @Override
    public String getName() {
        return "Manchester Decoder";
    }
}
