package org.ganges.expressionengine.extensions;

import java.io.File;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ganges.exceptions.XException;
import org.ganges.expressionengine.ExpressionContext;
import org.ganges.expressionengine.ExpressionEngineConstants;
import org.ganges.expressionengine.exceptions.ExpressionEngineException;
import org.ganges.types.Type;
import org.ganges.types.ValueObject;
import org.ganges.utils.StringUtils;
import org.ganges.utils.xml.XMLUtils;
import org.ganges.utils.xml.XPathUtils;
import org.w3c.dom.Document;


/**
 * The function format is as xml('<any xpath expression>').
 * 
 * Parser will consider the parameter of this function as string 
 * and will not parse it further. The whole string parameter will pass to the 
 * functiona as single parameter.
 * 
 * 
 * @author Mohit Gupta
 *
 */
public class XMLFunctionProvider implements FunctionProvider {

	private static Log		LOGGER = LogFactory.getLog( XMLFunctionProvider.class );

	/**
	 * Context for the expression engine evaluation
	 */
	private ExpressionContext expressionContext;

	/**
	 * URL for the XML document
	 */
	private Object			xmlPathURL;

	/**
	 * Parsed XML document
	 */
	private Document		  xmlDocument;


	public Type getFunctionType( String functionName, Type[] parameterTypes ) throws ExpressionEngineException {
		// context should fire event on property set, and this event should listen to that
		ensureInitialized();
		checkFunctionValid( functionName );
		// TODO: should be changed to Any type being a dynamic type, but currently we are getting only string from XML
		return Type.STRING;
	}

	public ValueObject getFunctionValue( String functionName, ValueObject[] parameters )
			throws ExpressionEngineException {
		ensureInitialized();
		checkFunctionValid( functionName );
		if( parameters.length != 1 ) {
			throw new ExpressionEngineException( "Only one parameter is expected however got " + parameters.length );
		}
		if( parameters[0].getValueType() != Type.STRING ) {
			throw new ExpressionEngineException(
					"Only string type parameter is expected however got parameter of type["
							+ parameters[0].getValueType() + "]" );
		}
		try {
			ValueObject value = XPathUtils.evaluateExpressionToValueObject( xmlDocument, (String) parameters[0]
					.getValue() );
			if( value.getValueType() == Type.XML ) {
				value = new ValueObject( XPathUtils.getXMLString( value.getValue() ), Type.STRING );
			}
			else if( value.getValueType() == Type.ANY_TYPE ) {
				LOGGER
						.error( "Result value should be of String/Number/Boolean type, not XML nodes, null or others. result-value["
								+ value + "]" );
				throw new ExpressionEngineException(
						"Result value should be of String type, not XML nodes or others. result-value[" + value + "]" );
			}
			return value;
		}
		catch( XException e ) {
			LOGGER.error( "Problem while evaluating the XPath Expression. expression[" + parameters[0].getValue()
					+ "] rootCause[" + e.getMessage() + "]", e );
			throw new ExpressionEngineException( "Problem while evaluating the XPath Expression. expression["
					+ parameters[0].getValue() + "] rootCause[" + e.getMessage() + "]", e );
		}
	}

	public void initialize( ExpressionContext expressionContext ) throws ExpressionEngineException {
		this.expressionContext = expressionContext;
		xmlPathURL = expressionContext.getContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_XML_PATH_URL );
		if( xmlPathURL != null ) {
			ensureInitialized();
		}
	}

	private void ensureInitialized() throws ExpressionEngineException {
		if( xmlDocument != null ) {
			return;
		}
		xmlPathURL = expressionContext.getContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_XML_PATH_URL );
		if( xmlPathURL == null ) {
			throw new ExpressionEngineException( "No XML Path URL is specified" );
		}
		if( !( xmlPathURL instanceof URL ) ) {
			throw new ExpressionEngineException(
					"Specified XML Path URL is not a instance of URL class. specified-url[" + xmlPathURL + "]" );
		}
		if( !StringUtils.isQualifiedString( ( (URL) xmlPathURL ).getPath() ) ) {
			throw new ExpressionEngineException( "Specified XML Path URL is not a well formed path. specified-url["
					+ xmlPathURL + "]" );
		}

		try {
			xmlDocument = XMLUtils.parseXML( (URL) xmlPathURL, null );
			xmlDocument.getDocumentElement().normalize();
		}
		catch( XException e ) {
			LOGGER.error( "Problem while parsing the XML from URL. rootCause[" + e.getMessage() + "]" );
			throw new ExpressionEngineException( "Problem while parsing the XML from URL. rootCause[" + e.getMessage()
					+ "]", e );
		}
	}

	public boolean supportsFunction( String functionName, Type[] parameterTypes ) throws ExpressionEngineException {
		return "xml".equalsIgnoreCase( functionName ) && parameterTypes != null && parameterTypes.length == 1
				&& parameterTypes[0] == Type.STRING;
	}

	private void checkFunctionValid( String functionName ) throws ExpressionEngineException {
		if( !"xml".equalsIgnoreCase( functionName ) ) {
			throw new ExpressionEngineException( "Only XML function is supported. functionName[" + functionName + "]" );
		}
	}

	public static void main( String[] args ) throws Exception {
		URL url = new URL( "file:\\F:\\temp\\test.xml" );
		System.out.println( url.getFile() );
		File file = new File( url.getFile() );
		System.out.println( file.getPath() );

		ExpressionContext expressionContext = new ExpressionContext();
		expressionContext.setContextProperty( ExpressionEngineConstants.EXPRESSION_CONTENXT_XML_PATH_URL, url );
		XMLFunctionProvider xmlFunctionProvider = new XMLFunctionProvider();
		xmlFunctionProvider.initialize( expressionContext );
		ValueObject value = xmlFunctionProvider.getFunctionValue( "xml", new ValueObject[] { new ValueObject(
				"/new1/book/@name", Type.STRING ) } );
		System.out.println( "value[" + value.getValue() + "]" );
	}
}
