
import javax.swing.*;
import java.awt.*;

public class PCMPlotter extends JPanel {

    private final double[] original;
    private final double[] reconstructed;

    public PCMPlotter(double[] original, double[] reconstructed) {

        if (original == null || reconstructed == null
                || original.length == 0
                || original.length != reconstructed.length) {
            throw new IllegalArgumentException(
                    "Both arrays must have the same nonzero length."
            );
        }

        this.original = original.clone();
        this.reconstructed = reconstructed.clone();

        setPreferredSize(new Dimension(900, 520));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int left = 70;
            int right = getWidth() - 30;
            int top = 50;
            int bottom = getHeight() - 60;

            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < original.length; i++) {
                min = Math.min(min,
                        Math.min(original[i], reconstructed[i]));
                max = Math.max(max,
                        Math.max(original[i], reconstructed[i]));
            }

            double padding = Math.max((max - min) * 0.15, 0.1);
            min -= padding;
            max += padding;

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 17));
            g2.drawString("PCM: Original vs Reconstructed Signal",
                    left, 25);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

            // Draw horizontal grid lines.
            for (int i = 0; i <= 5; i++) {
                int y = top + i * (bottom - top) / 5;

                g2.setColor(new Color(220, 220, 220));
                g2.drawLine(left, y, right, y);

                double value = max - i * (max - min) / 5.0;

                g2.setColor(Color.DARK_GRAY);
                g2.drawString(String.format("%.2f", value),
                        20, y + 4);
            }

            g2.setColor(Color.BLACK);
            g2.drawLine(left, top, left, bottom);
            g2.drawLine(left, bottom, right, bottom);

            g2.drawString("Sample index", (left + right) / 2,
                    getHeight() - 15);

            int n = original.length;

            // Original signal: blue line with circular sample markers.
            g2.setColor(new Color(35, 100, 210));
            g2.setStroke(new BasicStroke(2.0f));

            for (int i = 0; i < n - 1; i++) {
                g2.drawLine(
                        x(i, n, left, right),
                        y(original[i], min, max, top, bottom),
                        x(i + 1, n, left, right),
                        y(original[i + 1], min, max, top, bottom)
                );
            }

            for (int i = 0; i < n; i++) {
                int px = x(i, n, left, right);
                int py = y(original[i], min, max, top, bottom);
                g2.fillOval(px - 3, py - 3, 6, 6);
            }

            // Reconstructed signal: red step-like line.
            g2.setColor(new Color(210, 55, 55));
            g2.setStroke(new BasicStroke(2.0f));

            for (int i = 0; i < n - 1; i++) {
                int x1 = x(i, n, left, right);
                int x2 = x(i + 1, n, left, right);
                int y1 = y(reconstructed[i], min, max, top, bottom);
                int y2 = y(reconstructed[i + 1], min, max, top, bottom);

                g2.drawLine(x1, y1, x2, y1);
                g2.drawLine(x2, y1, x2, y2);
            }

            // Legend.
            int legendY = getHeight() - 38;

            g2.setColor(new Color(35, 100, 210));
            g2.drawLine(left + 10, legendY, left + 35, legendY);
            g2.drawString("Original samples", left + 42, legendY + 4);

            g2.setColor(new Color(210, 55, 55));
            g2.drawLine(left + 190, legendY, left + 215, legendY);
            g2.drawString("PCM reconstruction",
                    left + 222, legendY + 4);

        } finally {
            g2.dispose();
        }
    }

    private int x(int index, int n, int left, int right) {
        if (n == 1) {
            return (left + right) / 2;
        }

        return left + (int) (
                (double) index * (right - left) / (n - 1)
        );
    }

    private int y(double value, double min, double max,
                  int top, int bottom) {

        return bottom - (int) (
                (value - min) * (bottom - top) / (max - min)
        );
    }

    public static void plot(double[] original, double[] reconstructed) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PCM Signal Visualization");

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(
                    new PCMPlotter(original, reconstructed)
            );

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
