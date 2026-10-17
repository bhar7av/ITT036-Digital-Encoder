
public class NRZL implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length()];

        for (int i = 0; i < data.length(); i++) {

            char bit = data.charAt(i);

            if (bit == '0') {
                signal[i] = 1.0;
            } else {
                signal[i] = -1.0;
            }
        }

        return signal;
    }

    @Override
    public String getName() {
        return "NRZ-L";
    }
}
