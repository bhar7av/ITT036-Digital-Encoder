
public class NRZI implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length()];

        double level = 1.0;

        for (int i = 0; i < data.length(); i++) {

            char bit = data.charAt(i);

            if (bit == '1') {
                level = -level;
            }

            signal[i] = level;
        }

        return signal;
    }

    @Override
    public String getName() {
        return "NRZ-I";
    }
}
