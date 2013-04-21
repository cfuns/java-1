package de.benjaminborbe.tools.stream;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class StreamUtil {

	private final ChannelTools channelTools;

	@Inject
	public StreamUtil(final ChannelTools channelTools) {
		this.channelTools = channelTools;
	}

	public void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		// get an channel from the stream
		final ReadableByteChannel inputChannel = Channels.newChannel(inputStream);
		final WritableByteChannel outputChannel = Channels.newChannel(outputStream);
		// copy the channels
		channelTools.fastChannelCopy(inputChannel, outputChannel);
		// closing the channels
		inputChannel.close();
		outputChannel.close();
	}

}

