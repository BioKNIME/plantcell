
package uk.ac.ebi.jdispatcher.soap.interpro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "JDispatcherService", targetNamespace = "http://soap.jdispatcher.ebi.ac.uk", wsdlLocation = "http://www.ebi.ac.uk/Tools/services/soap/iprscan?wsdl")
public class JDispatcherService_Service
    extends Service
{

    private final static URL JDISPATCHERSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(uk.ac.ebi.jdispatcher.soap.interpro.JDispatcherService_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = uk.ac.ebi.jdispatcher.soap.interpro.JDispatcherService_Service.class.getResource(".");
            url = new URL(baseUrl, "http://www.ebi.ac.uk/Tools/services/soap/iprscan?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://www.ebi.ac.uk/Tools/services/soap/iprscan?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        JDISPATCHERSERVICE_WSDL_LOCATION = url;
    }

    public JDispatcherService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public JDispatcherService_Service() {
        super(JDISPATCHERSERVICE_WSDL_LOCATION, new QName("http://soap.jdispatcher.ebi.ac.uk", "JDispatcherService"));
    }

    /**
     * 
     * @return
     *     returns JDispatcherService
     */
    @WebEndpoint(name = "JDispatcherServiceHttpPort")
    public JDispatcherService getJDispatcherServiceHttpPort() {
        return super.getPort(new QName("http://soap.jdispatcher.ebi.ac.uk", "JDispatcherServiceHttpPort"), JDispatcherService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns JDispatcherService
     */
    @WebEndpoint(name = "JDispatcherServiceHttpPort")
    public JDispatcherService getJDispatcherServiceHttpPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://soap.jdispatcher.ebi.ac.uk", "JDispatcherServiceHttpPort"), JDispatcherService.class, features);
    }

}
