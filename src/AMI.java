
public class AMI implements LineEncoder {

    @Override
    public double[] encode(String data) {

        if (data == null || !data.matches("[01]+")) {
            throw new IllegalArgumentException(
                    "Input must contain only 0s and 1s."
            );
        }

        double[] signal = new double[data.length()];

        // The first 1 must be +1.
        double lastMark = -1.0;

        for (int i = 0; i < data.length(); i++) {

            char bit = data.charAt(i);

            if (bit == '0') {
                signal[i] = 0.0;
            } else {
                lastMark = -lastMark;
                signal[i] = lastMark;
            }
        }

        return signal;
    }

    @Override
    public String getName() {
        return "AMI";
    }
}
