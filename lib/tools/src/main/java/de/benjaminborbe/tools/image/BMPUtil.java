package de.benjaminborbe.tools.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import javax.inject.Inject;

public class BMPUtil {

	@Inject
	public BMPUtil() {
		super();
	}

	public void writeBMP(final OutputStream out, final int width, final int height, final int[] rgbArray, final int resolution) throws IOException {
		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final int startx = 0;
		final int starty = 0;
		final int offset = 0;
		final int scansize = 0;
		image.setRGB(startx, starty, width, height, rgbArray, offset, scansize);
		writeBMP(out, image, resolution);
	}

	public void writeBMP(final OutputStream out, final RenderedImage image, final int resolution) throws IOException {
		final ImageTypeSpecifier its = new ImageTypeSpecifier(image.getColorModel(), image.getSampleModel());
		@SuppressWarnings("rawtypes")
		final Iterator iterator = ImageIO.getImageWritersByFormatName("bmp");
		final ImageWriter imageWriter = (ImageWriter) iterator.next();
		final ImageWriteParam param = imageWriter.getDefaultWriteParam();
		final IIOMetadata iiomd = imageWriter.getDefaultImageMetadata(its, param);

		// im header einer bmp-datei steht die auflösung als 32bit-integers in dots per meter,
		// wobei die bytes rückwärts angeordnet sind
		final int dotsPerMeter = Integer.reverseBytes(Math.round((float) (resolution / 0.0254)));

		// outputstream, der die auflösung der bmp-datei korrigiert
		class CorrectingOutputStream extends DataOutputStream {

			private ByteArrayOutputStream baos = new ByteArrayOutputStream(50);

			public CorrectingOutputStream(final OutputStream out) {
				super(out);
			}

			@Override
			public void write(byte b[], final int off, final int len) throws IOException {
				if (baos != null) {
					baos.write(b, off, len);
					if (baos.size() >= 38) {
						b = baos.toByteArray();
						super.write(b, 0, 38);
						super.writeInt(dotsPerMeter);
						super.writeInt(dotsPerMeter);
						if (b.length > 46)
							super.write(b, 46, b.length - 46);
						baos = null;
					}
				}
				else { // if baos == null
					super.write(b, off, len);
				}
			}
		}

		// schreibe datei
		imageWriter.setOutput(new MemoryCacheImageOutputStream(new CorrectingOutputStream(out)));
		imageWriter.write(new IIOImage(image, null, iiomd));
	}
}
