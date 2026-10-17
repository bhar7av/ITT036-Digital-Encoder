
public class DifferentialManchester implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length() * 2];

        double level = 1.0;

        for (int i = 0; i < data.length(); i++) {

            char bit = data.charAt(i);

            // A zero causes a transition at the start.
            if (bit == '0') {
                level = -level;
            }

            // First half of the bit interval.
            signal[2 * i] = level;

            // Mandatory transition at the middle.
            level = -level;

            // Second half of the bit interval.
            signal[2 * i + 1] = level;
        }

        return signal;
    }

    @Override
    public String getName() {
        return "Differential Manchester";
    }
}
