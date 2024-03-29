package topchat.server.protocol.xmpp.stream.parser;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.stream.element.Features;
import topchat.server.protocol.xmpp.stream.element.XMPPStream;

/**
 * Contains methods used for preparing messages to be sent to the client
 * 
 * Uses StaX API included in JAVA SE 6 in javax.xml.stream
 * 
 */
public class Preparer
{

	private static Logger logger = Logger.getLogger(Preparer.class);

	/**
	 * Create the corresponding xml message from the Features object
	 * 
	 * @param features
	 *            the Features used for generating the xml
	 * @return a String containing the xml feature message
	 */
	public static String prepareFeatures(Features features)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, "stream", null, "features");

		// add TLS
		if (features.usesTLS())
		{
			Utils.addStartElement(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-tls", "starttls");
			Utils.addNamespace(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-tls");

			// add required
			Utils.addStartElement(eventFactory, writer, "", null, "required");
			Utils.addEndElement(eventFactory, writer, "", null, "required");

			Utils.addEndElement(eventFactory, writer, "", null, "starttls");
		}

		if (features.usesSASL())
		{
			Utils.addStartElement(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-sasl", "mechanisms");
			Utils.addNamespace(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-sasl");

			if (features.usesMD5())
			{
				Utils.addStartElement(eventFactory, writer, "", null,
						"mechanism");
				Utils.addContent(eventFactory, writer, "DIGEST-MD5");
				Utils
						.addEndElement(eventFactory, writer, "", null,
								"mechanism");
			}

			if (features.usesPlain())
			{
				Utils.addStartElement(eventFactory, writer, "", null,
						"mechanism");
				Utils.addContent(eventFactory, writer, "PLAIN");
				Utils
						.addEndElement(eventFactory, writer, "", null,
								"mechanism");
			}

			Utils.addEndElement(eventFactory, writer, "", null, "mechanisms");
		}

		if (features.offersBinding())
		{
			Utils.addStartElement(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-bind", "bind");
			Utils.addNamespace(eventFactory, writer, "",
					"urn:ietf:params:xml:ns:xmpp-bind");
			Utils.addEndElement(eventFactory, writer, "", null, "bind");

			if (features.sessionSupported())
			{
				Utils.addStartElement(eventFactory, writer, "",
						"urn:ietf:params:xml:ns:xmpp-session", "session");
				Utils.addNamespace(eventFactory, writer, "",
						"urn:ietf:params:xml:ns:xmpp-session");
				Utils.addEndElement(eventFactory, writer, "", null, "session");
			}
		}

		Utils.addEndElement(eventFactory, writer, "stream", null, "features");

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		logger.debug(result);

		return result;
	}

	/**
	 * Create the corresponding xml message for 'proceed'
	 * 
	 * @return a String containing the xml proceed message
	 */
	public static String prepareProceed()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, "",
				"urn:ietf:params:xml:ns:xmpp-tls", "proceed");
		Utils.addNamespace(eventFactory, writer, "",
				"urn:ietf:params:xml:ns:xmpp-tls");

		Utils.addEndElement(eventFactory, writer, "", null, "proceed");

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		logger.debug(result);

		return result;
	}

	/**
	 * Create the corresponding xml message for tls failure
	 * 
	 * @return a String containing the xml tls failure message
	 */
	public static String prepareTLSFailure()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, "",
				"urn:ietf:params:xml:ns:xmpp-tls", "failure");
		Utils.addNamespace(eventFactory, writer, "",
				"urn:ietf:params:xml:ns:xmpp-tls");

		Utils.addEndElement(eventFactory, writer, "", null, "failure");

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		// add end of stream
		result = result.concat("</stream:stream>");

		logger.debug(result);

		return result;
	}

	/**
	 * Create the corresponding xml message from the XMPPStream object
	 * 
	 * @param stream
	 *            the stream used for generating the xml
	 * @return a String containing the xml stream start message
	 */
	public static String prepareStreamStart(XMPPStream stream)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, "stream",
				"http://etherx.jabber.org/streams", "stream");

		Utils.addNamespace(eventFactory, writer, "jabber:client");

		Utils.addNamespace(eventFactory, writer, "stream",
				"http://etherx.jabber.org/streams");

		Utils.addAttribute(eventFactory, writer, "from", stream.getFrom());
		Utils.addAttribute(eventFactory, writer, "id", stream.getId());
		Utils
				.addAttribute(eventFactory, writer, "version", stream
						.getVersion());

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		// hack for ending tag without adding end tag (why does stax make me do
		// this?)
		result = result.concat(">");

		logger.debug(result);

		return result;
	}

}
