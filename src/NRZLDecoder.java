
public class NRZLDecoder implements LineDecoder {

    @Override
    public String decode(double[] signal) {

        if (signal == null || signal.length == 0) {
            throw new IllegalArgumentException(
                    "Signal cannot be empty."
            );
        }

        StringBuilder bits = new StringBuilder(signal.length);

        for (double level : signal) {

            if (level == 1.0) {
                bits.append('0');
            } else if (level == -1.0) {
                bits.append('1');
            } else {
                throw new IllegalArgumentException(
                        "Invalid NRZ-L voltage level: " + level
                );
            }
        }

        return bits.toString();
    }

    @Override
    public String getName() {
        return "NRZ-L Decoder";
    }
}
