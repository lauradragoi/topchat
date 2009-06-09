package topchat.server.protocol.xmpp.stream.parser;

import java.io.ByteArrayOutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;

import org.apache.log4j.Logger;

import topchat.server.protocol.xmpp.stream.Features;
import topchat.server.protocol.xmpp.stream.XMPPStream;

public class Preparer {

	private static Logger logger = Logger.getLogger(Preparer.class);
	
	/**
	 * Create the corresponding xml message from the Features object
	 * @param features
	 * @return
	 */
	public static String prepareFeatures(Features features)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, 
				"stream", null, "features");		

		// add TLS
		if (features.usesTLS())
		{
			Utils.addStartElement(eventFactory, writer, 
					"", "urn:ietf:params:xml:ns:xmpp-tls", "starttls");
			Utils.addNamespace(eventFactory, writer, "", "urn:ietf:params:xml:ns:xmpp-tls");

			// add required
			Utils.addStartElement(eventFactory, writer, 
					"", null, "required");
			Utils.addEndElement(eventFactory, writer, 
					"", null, "required");


			Utils.addEndElement(eventFactory, writer, 
					"", null, "starttls");				
		}

		Utils.addEndElement(eventFactory, writer, 
				"stream", null, "features");

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		logger.debug(result);

		return result; 
	}
	
	/**
	 * Create the corresponding xml message for 'proceed'
	 * @param features
	 * @return
	 */
	public static String prepareProceed()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, 
					"", "urn:ietf:params:xml:ns:xmpp-tls", "proceed");
		Utils.addNamespace(eventFactory, writer, "", "urn:ietf:params:xml:ns:xmpp-tls");

		Utils.addEndElement(eventFactory, writer, 
					"", null, "proceed");				

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());

		logger.debug(result);

		return result; 
	}	

	/**
	 * Create the corresponding xml message for tls failure
	 * @param features
	 * @return
	 */
	public static String prepareTLSFailure()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		XMLEventWriter writer = Utils.createWriter(baos);

		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();

		Utils.addStartElement(eventFactory, writer, 
					"", "urn:ietf:params:xml:ns:xmpp-tls", "failure");
		Utils.addNamespace(eventFactory, writer, "", "urn:ietf:params:xml:ns:xmpp-tls");

		Utils.addEndElement(eventFactory, writer, 
					"", null, "failure");		

		Utils.endWrite(writer);

		String result = new String(baos.toByteArray());
		
		// add end of stream
		result = result.concat("</stream:stream>");

		logger.debug(result);

		return result; 
	}	
	
	/**
	 * Create the corresponding xml message from the XMPPStream object
	 * @param stream
	 * @return
	 */
	public static String prepareStreamStart(XMPPStream stream)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
		XMLEventWriter writer = Utils.createWriter(baos);
		
		XMLEventFactory  eventFactory = XMLEventFactory.newInstance();
		
		Utils.addStartElement(eventFactory, writer, 
						"stream", "http://etherx.jabber.org/streams", "stream");		
	
		Utils.addNamespace(eventFactory, writer, "stream", "http://etherx.jabber.org/streams");
				
		Utils.addAttribute(eventFactory, writer, "from", stream.getFrom());
		Utils.addAttribute(eventFactory, writer, "id", stream.getId());
		Utils.addAttribute(eventFactory, writer, "version", stream.getVersion());
		
		Utils.endWrite(writer);
		
		String result = new String(baos.toByteArray());
		
		// hack for ending tag without adding end tag (why does stax make me do this?)
		result = result.concat(">");
		
		logger.debug(result);
		
		return result; 
	}

}
