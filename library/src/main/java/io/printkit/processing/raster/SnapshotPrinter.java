package io.printkit.processing.raster;

import processing.core.PApplet;
import processing.core.PImage;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.net.URI;

import static io.printkit.processing.raster.Units.*;

final class SnapshotPrinter {

    private final PApplet app;
    private final PrintOptions opt;

    SnapshotPrinter(PApplet app, PrintOptions opt) {
        this.app = app;
        this.opt = opt;
    }

    void print() throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();

        if (opt.printerName != null) {
            for (PrintService s : PrinterJob.lookupPrintServices()) {
                if (s.getName().equalsIgnoreCase(opt.printerName)) {
                    try {
                        job.setPrintService(s);
                    } catch (PrinterException ignore) {
                    }
                    break;
                }
            }
        }

        PageFormat pf = job.defaultPage();
        Paper paper = new Paper();

        double pw = mmToPt(opt.paper.widthMm(opt.orientation));
        double ph = mmToPt(opt.paper.heightMm(opt.orientation));
        paper.setSize(pw, ph);

        double ix = mmToPt(opt.marginLeftMm);
        double iy = mmToPt(opt.marginTopMm);
        double iw = pw - mmToPt(opt.marginLeftMm + opt.marginRightMm);
        double ih = ph - mmToPt(opt.marginTopMm + opt.marginBottomMm);
        paper.setImageableArea(ix, iy, Math.max(0, iw), Math.max(0, ih));

        pf.setPaper(paper);
        pf.setOrientation(
                opt.orientation == PrintOptions.Orientation.PORTRAIT ? PageFormat.PORTRAIT : PageFormat.LANDSCAPE);

        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();

        attrs.add(opt.orientation == PrintOptions.Orientation.PORTRAIT ? OrientationRequested.PORTRAIT
                : OrientationRequested.LANDSCAPE);

        MediaSizeName media = mapPaperToMedia(opt);
        if (media != null)
            attrs.add(media);

        attrs.add(new MediaPrintableArea(
                (float) opt.marginLeftMm,
                (float) opt.marginTopMm,
                (float) (opt.paper.widthMm(opt.orientation) - (opt.marginLeftMm + opt.marginRightMm)),
                (float) (opt.paper.heightMm(opt.orientation) - (opt.marginTopMm + opt.marginBottomMm)),
                MediaPrintableArea.MM));

        if (opt.jobName != null) {
            attrs.add(new JobName(opt.jobName, null));
        }

        if (opt.destinationPath != null && job.getPrintService() != null &&
                job.getPrintService().isAttributeCategorySupported(Destination.class)) {
            try {
                URI uri = new File(opt.destinationPath).toURI();
                attrs.add(new Destination(uri));
            } catch (Exception ignored) {
            }
        }

        Printable printable = (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0)
                return Printable.NO_SUCH_PAGE;

            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            PImage frame = app.get();
            BufferedImage img = ImageUtil.toBufferedImage(frame);

            double iwPt = pageFormat.getImageableWidth();
            double ihPt = pageFormat.getImageableHeight();

            double s;
            if (opt.fit == PrintOptions.Fit.REAL_SIZE) {
                s = 72.0 / opt.dpi;
            } else {
                double sx = iwPt / img.getWidth();
                double sy = ihPt / img.getHeight();
                s = (opt.fit == PrintOptions.Fit.FILL) ? Math.max(sx, sy) : Math.min(sx, sy);
            }

            double drawW = img.getWidth() * s;
            double drawH = img.getHeight() * s;
            double ox = pageFormat.getImageableX() + (iwPt - drawW) / 2.0;
            double oy = pageFormat.getImageableY() + (ihPt - drawH) / 2.0;

            g2.drawImage(img, (int) Math.round(ox), (int) Math.round(oy),
                    (int) Math.round(drawW), (int) Math.round(drawH), null);

            return Printable.PAGE_EXISTS;
        };

        job.setPrintable(printable, pf);

        if (opt.showDialog) {
            if (job.printDialog(attrs)) {
                job.print(attrs);
            }
        } else {
            job.print(attrs);
        }
    }

    private static MediaSizeName mapPaperToMedia(PrintOptions opt) {
        if (opt.paper == PrintOptions.Paper.A4)
            return MediaSizeName.ISO_A4;
        if (opt.paper == PrintOptions.Paper.A3)
            return MediaSizeName.ISO_A3;
        if (opt.paper == PrintOptions.Paper.LETTER)
            return MediaSizeName.NA_LETTER;
        if (opt.paper == PrintOptions.Paper.LEGAL)
            return MediaSizeName.NA_LEGAL;
        return null;
    }
}
