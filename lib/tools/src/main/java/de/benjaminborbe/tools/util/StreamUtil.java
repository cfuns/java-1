package de.benjaminborbe.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import javax.inject.Inject;

public class StreamUtil {

	@Inject
	public StreamUtil() {
	}

	public void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		// get an channel from the stream
		final ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
		final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
		// copy the channels
		ChannelTools.fastChannelCopy(inputChannel, outputChannel);
		// closing the channels
		inputChannel.close();
		outputChannel.close();
	}

}

class ChannelTools {

	public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}
}
