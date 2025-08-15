package io.printkit.processing.raster;

import processing.core.PApplet;

import java.awt.print.PrinterException;

public class PrintKit {

    private PrintKit() {
    }

    public static void print(PApplet app) {
        with(app).print();
    }

    public static Builder with(PApplet app) {
        return new Builder(app);
    }

    public static final class Builder {
        private final PApplet app;
        private final PrintOptions.Builder opt = PrintOptions.builder();

        private Builder(PApplet app) {
            this.app = app;
        }

        public Builder paper(PrintOptions.Paper p) {
            opt.paper(p);
            return this;
        }

        public Builder orientation(PrintOptions.Orientation o) {
            opt.orientation(o);
            return this;
        }

        public Builder marginsMm(double t, double r, double b, double l) {
            opt.marginsMm(t, r, b, l);
            return this;
        }

        public Builder fit(PrintOptions.Fit f) {
            opt.fit(f);
            return this;
        }

        public Builder dpi(int v) {
            opt.dpi(v);
            return this;
        }

        public Builder showDialog(boolean v) {
            opt.showDialog(v);
            return this;
        }

        public Builder printerName(String v) {
            opt.printerName(v);
            return this;
        }

        public Builder jobName(String v) {
            opt.jobName(v);
            return this;
        }

        public Builder destinationPath(String v) {
            opt.destinationPath(v);
            return this;
        }

        public void print() {
            try {
                new SnapshotPrinter(app, opt.build()).print();
            } catch (PrinterException e) {
                throw new RuntimeException("Printing failed: " + e.getMessage(), e);
            }
        }
    }
}
