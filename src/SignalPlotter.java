
import javax.swing.*;
import java.awt.*;

public class SignalPlotter {

    public static void plot(
            String scheme, String data, double[] signal) {

        if (data == null || data.isEmpty()
                || signal == null || signal.length == 0
                || signal.length % data.length() != 0) {
            throw new IllegalArgumentException(
                    "Invalid input data or encoded signal."
            );
        }

        int samplesPerBit = signal.length / data.length();

        JFrame frame = new JFrame(scheme + " Signal Waveform");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 650);
        frame.setLocationRelativeTo(null);

        frame.add(new WaveformPanel(
                scheme, data, signal, samplesPerBit
        ));

        frame.setVisible(true);
    }

    private static class WaveformPanel extends JPanel {

        private final String scheme;
        private final String data;
        private final double[] signal;
        private final int samplesPerBit;

        WaveformPanel(
                String scheme,
                String data,
                double[] signal,
                int samplesPerBit) {

            this.scheme = scheme;
            this.data = data;
            this.signal = signal;
            this.samplesPerBit = samplesPerBit;

            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int left = 75;
            int right = getWidth() - 35;
            int top = 160;
            int bottom = getHeight() - 85;

            int width = right - left;
            int height = bottom - top;
            int bitCount = data.length();

            double centerY = top + height / 2.0;
            double amplitude = height * 0.32;

            double intervalWidth = (double) width / bitCount;
            double sampleWidth = intervalWidth / samplesPerBit;

            // Title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString(scheme + " Signal Waveform", left, 35);

            // Original digital data
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString("Original bits:", left, 75);

            for (int i = 0; i < bitCount; i++) {
                int x = (int) Math.round(
                        left + (i + 0.5) * intervalWidth
                );

                String bit = String.valueOf(data.charAt(i));

                g2.setColor(
                        bit.equals("1")
                                ? new Color(30, 100, 210)
                                : new Color(190, 65, 65)
                );

                g2.drawString(bit, x - 5, 105);
            }

            // Encoded signal information
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(
                    "Encoded voltage levels (V):", left, 135
            );

            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            for (int i = 0; i < signal.length; i++) {
                int x = (int) Math.round(
                        left + (i + 0.5) * sampleWidth
                );

                g2.setColor(new Color(70, 70, 70));

                String value = formatVoltage(signal[i]);

                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                        value,
                        x - fm.stringWidth(value) / 2,
                        150
                );
            }

            // Voltage reference lines
            g2.setColor(new Color(215, 215, 215));

            g2.drawLine(
                    left, (int) (centerY - amplitude),
                    right, (int) (centerY - amplitude)
            );

            g2.drawLine(
                    left, (int) centerY,
                    right, (int) centerY
            );

            g2.drawLine(
                    left, (int) (centerY + amplitude),
                    right, (int) (centerY + amplitude)
            );

            // Voltage labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            g2.drawString(
                    "+1 V", 20, (int) (centerY - amplitude + 5)
            );

            g2.drawString(
                    "0 V", 30, (int) centerY + 5
            );

            g2.drawString(
                    "-1 V", 20, (int) (centerY + amplitude + 5)
            );

            // Bit boundaries
            for (int i = 0; i <= bitCount; i++) {

                int x = (int) Math.round(
                        left + i * intervalWidth
                );

                g2.setColor(new Color(235, 235, 235));
                g2.drawLine(x, top, x, bottom);

                g2.setColor(Color.BLACK);

                String label = Integer.toString(i);

                g2.drawString(
                        label, x - 3, bottom + 22
                );
            }

            // Draw the staircase waveform
            g2.setColor(new Color(30, 100, 210));
            g2.setStroke(new BasicStroke(2.0f));

            int previousY = voltageToY(
                    signal[0], centerY, amplitude
            );

            for (int i = 0; i < signal.length; i++) {

                int x1 = (int) Math.round(
                        left + i * sampleWidth
                );

                int x2 = (int) Math.round(
                        left + (i + 1) * sampleWidth
                );

                int y = voltageToY(
                        signal[i], centerY, amplitude
                );

                if (i > 0 && y != previousY) {
                    g2.drawLine(x1, previousY, x1, y);
                }

                g2.drawLine(x1, y, x2, y);

                previousY = y;
            }

            // Axis labels
            g2.setColor(Color.BLACK);

            g2.drawString(
                    "Time (bit intervals)",
                    right - 145, bottom + 55
            );

            g2.dispose();
        }

        private String formatVoltage(double voltage) {
            if (voltage == 0.0) {
                return "0";
            }

            return voltage > 0 ? "+1" : "-1";
        }

        private int voltageToY(
                double voltage,
                double centerY,
                double amplitude) {

            return (int) Math.round(
                    centerY - voltage * amplitude
            );
        }
    }
}
