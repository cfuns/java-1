package de.benjaminborbe.mail.imap;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class ImapClient {

	public static void main(final String[] args) {

		final Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			final Session session = Session.getDefaultInstance(props, null);
			final Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", "benjamin.borbe@gmail.com", "<PASSWORD>");

			System.err.println("store.isConnected() = " + store.isConnected());

			for (final Folder folder : store.getDefaultFolder().list()) {
				System.err.println("folder = " + folder.getName());
				System.err.println("exists = " + folder.exists());
				if (folder.exists()) {
					folder.open(Folder.READ_ONLY);
					System.err.println("getNewMessageCount = " + folder.getNewMessageCount());
					System.err.println("getUnreadMessageCount = " + folder.getUnreadMessageCount());
					folder.close(false);
				}
			}

			//
			// final Message[] msgs = folder.getMessages();
			// System.err.println("msgs = " + msgs.length);
			// for (final Message msg : msgs) {
			// final Flags flags = msg.getFlags();
			// System.err.println("flag = " + flags.toString());
			// }
		} catch (final NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (final MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
}
