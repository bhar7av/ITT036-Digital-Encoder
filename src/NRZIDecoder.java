
public class NRZIDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be empty."
            );
        }

        StringBuilder bits = new StringBuilder(signal.length);

        // Must match the initial level used by the NRZI encoder.
        double previousLevel = 1.0;

        for (double currentLevel : signal) {

            if (currentLevel != 1.0 && currentLevel != -1.0) {
                throw new IllegalArgumentException(
                        "Invalid NRZ-I voltage level: " + currentLevel
                );
            }

            if (currentLevel != previousLevel) {
                bits.append('1');
            } else {
                bits.append('0');
            }

            previousLevel = currentLevel;
        }

        return bits.toString();
    }

    @Override
    public String getName() {
        return "NRZ-I Decoder";
    }
}
