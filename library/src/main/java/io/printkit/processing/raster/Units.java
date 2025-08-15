package io.printkit.processing.raster;

final class Units {
    private Units() {
    }

    static double mmToPt(double mm) {
        return mm * 72.0 / 25.4;
    }

    static double ptToMm(double pt) {
        return pt * 25.4 / 72.0;
    }

    static double pxToPtAtDpi(double px, int dpi) {
        return (px / (double) dpi) * 72.0;
    }
}
