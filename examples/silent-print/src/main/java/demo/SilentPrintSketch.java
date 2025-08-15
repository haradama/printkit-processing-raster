package demo;

import processing.core.PApplet;
import io.printkit.processing.raster.PrintKit;
import io.printkit.processing.raster.PrintOptions;

import javax.print.PrintService;
import java.util.Arrays;

public class SilentPrintSketch extends PApplet {

    private static final String PRINTER_NAME = "Your-Printer-Name";

    public static void main(String[] args) {
        listPrinters();
        PApplet.main(SilentPrintSketch.class);
    }

    public void settings() {
        size(640, 480);
        smooth();
    }

    public void draw() {
        background(250);
        rectMode(CENTER);
        ellipseMode(CENTER);
        textAlign(CENTER, TOP);
        textSize(16);

        float margin = 80f;
        float spacing = (width - margin * 2f) / 2f;
        float y = height / 2f - 10f;
        float s = 100f;

        // ---- Circle ----
        float x0 = margin + 0f * spacing;
        stroke(30);
        strokeWeight(3);
        fill(66, 135, 245);
        ellipse(x0, y, s, s);
        noStroke();
        fill(30);
        text("Circle", x0, y + s / 2f + 8f);

        // ---- Triangle ----
        float x1 = margin + 1f * spacing;
        stroke(30);
        strokeWeight(3);
        fill(255, 166, 0);
        drawIsoscelesTriangle(x1, y, s);
        noStroke();
        fill(30);
        text("Triangle", x1, y + s / 2f + 8f);

        // ---- Rectangle ----
        float x2 = margin + 2f * spacing;
        stroke(30);
        strokeWeight(3);
        fill(0, 200, 140);
        rect(x2, y, s, s);
        noStroke();
        fill(30);
        text("Rectangle", x2, y + s / 2f + 8f);

        textAlign(CENTER);
        text("Press 'p' to SILENT-PRINT", width / 2f, height - 20f);
    }

    void drawIsoscelesTriangle(float cx, float cy, float size) {
        float half = size / 2f;
        triangle(cx, cy - half,
                cx - half, cy + half,
                cx + half, cy + half);
    }

    public void keyPressed() {
        if (key == 'p') {
            PrintKit.with(this)
                    .paper(PrintOptions.Paper.A4)
                    .orientation(PrintOptions.Orientation.PORTRAIT)
                    .marginsMm(10, 10, 10, 10)
                    .fit(PrintOptions.Fit.CONTAIN)
                    .dpi(300)
                    .showDialog(false)
                    .printerName(PRINTER_NAME)
                    .jobName("SilentPrintSketch")
                    .print();
        }
    }

    private static void listPrinters() {
        PrintService[] services = java.awt.print.PrinterJob.lookupPrintServices();
        System.out.println("== Available Printers ==");
        Arrays.stream(services).forEach(s -> System.out.println(" - " + s.getName()));
        System.out.println("========================");
    }
}
