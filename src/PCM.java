
public class PCM {

    private final int bitsPerSample;
    private final int levels;
    private final double minValue;
    private final double maxValue;
    private final double stepSize;

    public PCM(int bitsPerSample, double minValue, double maxValue) {

        if (bitsPerSample < 1 || bitsPerSample > 16) {
            throw new IllegalArgumentException(
                    "Bits per sample must be between 1 and 16."
            );
        }

        if (!Double.isFinite(minValue)
                || !Double.isFinite(maxValue)
                || minValue >= maxValue) {
            throw new IllegalArgumentException(
                    "Maximum value must be greater than minimum value."
            );
        }

        this.bitsPerSample = bitsPerSample;
        this.levels = 1 << bitsPerSample;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = (maxValue - minValue) / levels;
    }

    // Encode each sample into a fixed-length binary codeword.
    public String[] encode(double[] samples) {

        if (samples == null || samples.length == 0) {
            throw new IllegalArgumentException(
                    "Samples cannot be null or empty."
            );
        }

        String[] encoded = new String[samples.length];

        for (int i = 0; i < samples.length; i++) {

            double sample = samples[i];

            if (!Double.isFinite(sample)) {
                throw new IllegalArgumentException(
                        "Sample " + i + " is not a finite number."
                );
            }

            // Clamp samples to the supported input range.
            sample = Math.max(minValue, Math.min(maxValue, sample));

            int quantizationIndex =
                    (int) ((sample - minValue) / stepSize);

            // The maximum endpoint belongs to the final quantization level.
            quantizationIndex = Math.min(
                    quantizationIndex, levels - 1
            );

            encoded[i] = String.format(
                    "%"
                            + bitsPerSample
                            + "s",
                    Integer.toBinaryString(quantizationIndex)
            ).replace(' ', '0');
        }

        return encoded;
    }

    // Decode each binary codeword to the midpoint of its quantization interval.
    public double[] decode(String[] encoded) {

        if (encoded == null || encoded.length == 0) {
            throw new IllegalArgumentException(
                    "Encoded data cannot be null or empty."
            );
        }

        double[] reconstructed = new double[encoded.length];

        for (int i = 0; i < encoded.length; i++) {

            String codeword = encoded[i];

            if (codeword == null
                    || codeword.length() != bitsPerSample
                    || !codeword.matches("[01]+")) {
                throw new IllegalArgumentException(
                        "Invalid PCM codeword at index " + i
                );
            }

            int quantizationIndex =
                    Integer.parseInt(codeword, 2);

            if (quantizationIndex >= levels) {
                throw new IllegalArgumentException(
                        "PCM codeword exceeds the supported range."
                );
            }

            reconstructed[i] =
                    minValue
                            + (quantizationIndex + 0.5) * stepSize;
        }

        return reconstructed;
    }

    // Combine all sample codewords into one continuous bitstream.
    public String toBitstream(String[] encoded) {

        if (encoded == null || encoded.length == 0) {
            throw new IllegalArgumentException(
                    "Encoded data cannot be null or empty."
            );
        }

        StringBuilder bitstream = new StringBuilder();

        for (String codeword : encoded) {

            if (codeword == null
                    || codeword.length() != bitsPerSample
                    || !codeword.matches("[01]+")) {
                throw new IllegalArgumentException(
                        "Invalid PCM codeword."
                );
            }

            bitstream.append(codeword);
        }

        return bitstream.toString();
    }

    public int getBitsPerSample() {
        return bitsPerSample;
    }

    public int getLevels() {
        return levels;
    }

    public double getStepSize() {
        return stepSize;
    }
}
