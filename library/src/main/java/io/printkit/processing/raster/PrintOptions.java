package io.printkit.processing.raster;

public class PrintOptions {

    public enum Fit {
        CONTAIN, FILL, REAL_SIZE
    }

    public enum Orientation {
        PORTRAIT, LANDSCAPE
    }

    public static final class Paper {
        public static final Paper A4 = new Paper(210.0, 297.0, "A4");
        public static final Paper A3 = new Paper(297.0, 420.0, "A3");
        public static final Paper LETTER = new Paper(215.9, 279.4, "LETTER");
        public static final Paper LEGAL = new Paper(215.9, 355.6, "LEGAL");

        private final double wMm;
        private final double hMm;
        private final String name;

        public Paper(double wMm, double hMm, String name) {
            this.wMm = wMm;
            this.hMm = hMm;
            this.name = name;
        }

        public double widthMm(Orientation o) {
            return o == Orientation.PORTRAIT ? wMm : hMm;
        }

        public double heightMm(Orientation o) {
            return o == Orientation.PORTRAIT ? hMm : wMm;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public final Paper paper;
    public final Orientation orientation;
    public final double marginTopMm, marginRightMm, marginBottomMm, marginLeftMm;
    public final Fit fit;
    public final int dpi;
    public final boolean showDialog;
    public final String printerName;

    public final String jobName;
    public final String destinationPath;

    private PrintOptions(Builder b) {
        this.paper = b.paper;
        this.orientation = b.orientation;
        this.marginTopMm = b.marginTopMm;
        this.marginRightMm = b.marginRightMm;
        this.marginBottomMm = b.marginBottomMm;
        this.marginLeftMm = b.marginLeftMm;
        this.fit = b.fit;
        this.dpi = b.dpi;
        this.showDialog = b.showDialog;
        this.printerName = b.printerName;
        this.jobName = b.jobName;
        this.destinationPath = b.destinationPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Paper paper = Paper.A4;
        private Orientation orientation = Orientation.PORTRAIT;
        private double marginTopMm = 10, marginRightMm = 10, marginBottomMm = 10, marginLeftMm = 10;
        private Fit fit = Fit.CONTAIN;
        private int dpi = 300;
        private boolean showDialog = true;
        private String printerName = null;

        private String jobName = "PrintKit Job";
        private String destinationPath = null;

        public Builder paper(Paper v) {
            this.paper = v;
            return this;
        }

        public Builder orientation(Orientation v) {
            this.orientation = v;
            return this;
        }

        public Builder marginsMm(double t, double r, double b, double l) {
            this.marginTopMm = t;
            this.marginRightMm = r;
            this.marginBottomMm = b;
            this.marginLeftMm = l;
            return this;
        }

        public Builder fit(Fit v) {
            this.fit = v;
            return this;
        }

        public Builder dpi(int v) {
            this.dpi = v;
            return this;
        }

        public Builder showDialog(boolean v) {
            this.showDialog = v;
            return this;
        }

        public Builder printerName(String v) {
            this.printerName = v;
            return this;
        }

        public Builder jobName(String v) {
            this.jobName = v;
            return this;
        }

        public Builder destinationPath(String v) {
            this.destinationPath = v;
            return this;
        }

        public PrintOptions build() {
            return new PrintOptions(this);
        }
    }

    public static PrintOptions defaults() {
        return builder().build();
    }
}
