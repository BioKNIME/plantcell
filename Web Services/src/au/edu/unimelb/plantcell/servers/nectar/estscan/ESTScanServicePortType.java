
package au.edu.unimelb.plantcell.servers.nectar.estscan;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ESTScanServicePortType", targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ESTScanServicePortType {


    /**
     * 
     * @param nucleotideSequenceAsFasta
     * @param scoringModel
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:submit")
    @WebResult(targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
    @RequestWrapper(localName = "submit", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.Submit")
    @ResponseWrapper(localName = "submitResponse", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.SubmitResponse")
    public String submit(
        @WebParam(name = "nucleotide_sequence_as_fasta", targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
        String nucleotideSequenceAsFasta,
        @WebParam(name = "scoring_model", targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
        String scoringModel);

    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod(action = "urn:getAvailableScoreModels")
    @WebResult(targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
    @RequestWrapper(localName = "getAvailableScoreModels", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetAvailableScoreModels")
    @ResponseWrapper(localName = "getAvailableScoreModelsResponse", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetAvailableScoreModelsResponse")
    public List<String> getAvailableScoreModels();

    /**
     * 
     * @param jobID
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:getStatus")
    @WebResult(targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
    @RequestWrapper(localName = "getStatus", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetStatus")
    @ResponseWrapper(localName = "getStatusResponse", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetStatusResponse")
    public String getStatus(
        @WebParam(name = "jobID", targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
        String jobID);

    /**
     * 
     * @param jobId
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:getResult")
    @WebResult(targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
    @RequestWrapper(localName = "getResult", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetResult")
    @ResponseWrapper(localName = "getResultResponse", targetNamespace = "http://nectar.plantcell.unimelb.edu.au", className = "au.edu.unimelb.plantcell.io.servers.nectar.estscan.GetResultResponse")
    public String getResult(
        @WebParam(name = "job_id", targetNamespace = "http://nectar.plantcell.unimelb.edu.au")
        String jobId);

}
