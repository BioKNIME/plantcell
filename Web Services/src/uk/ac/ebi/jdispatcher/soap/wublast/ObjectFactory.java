
package uk.ac.ebi.jdispatcher.soap.wublast;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.ac.ebi.jdispatcher.soap.wublast package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetResultParameters_QNAME = new QName("", "parameters");
    private final static QName _RunTitle_QNAME = new QName("", "title");
    private final static QName _InputParametersSort_QNAME = new QName("", "sort");
    private final static QName _InputParametersSensitivity_QNAME = new QName("", "sensitivity");
    private final static QName _InputParametersStats_QNAME = new QName("", "stats");
    private final static QName _InputParametersAlign_QNAME = new QName("", "align");
    private final static QName _InputParametersMatrix_QNAME = new QName("", "matrix");
    private final static QName _InputParametersStrand_QNAME = new QName("", "strand");
    private final static QName _InputParametersTransltable_QNAME = new QName("", "transltable");
    private final static QName _InputParametersTopcombon_QNAME = new QName("", "topcombon");
    private final static QName _InputParametersExp_QNAME = new QName("", "exp");
    private final static QName _InputParametersScores_QNAME = new QName("", "scores");
    private final static QName _InputParametersSequence_QNAME = new QName("", "sequence");
    private final static QName _InputParametersAlignments_QNAME = new QName("", "alignments");
    private final static QName _InputParametersViewfilter_QNAME = new QName("", "viewfilter");
    private final static QName _InputParametersFilter_QNAME = new QName("", "filter");
    private final static QName _WsResultTypeDescription_QNAME = new QName("", "description");
    private final static QName _WsResultTypeLabel_QNAME = new QName("", "label");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.ac.ebi.jdispatcher.soap.wublast
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetResultTypesResponse }
     * 
     */
    public GetResultTypesResponse createGetResultTypesResponse() {
        return new GetResultTypesResponse();
    }

    /**
     * Create an instance of {@link WsProperty }
     * 
     */
    public WsProperty createWsProperty() {
        return new WsProperty();
    }

    /**
     * Create an instance of {@link Run }
     * 
     */
    public Run createRun() {
        return new Run();
    }

    /**
     * Create an instance of {@link WsParameters }
     * 
     */
    public WsParameters createWsParameters() {
        return new WsParameters();
    }

    /**
     * Create an instance of {@link WsRawOutputParameter }
     * 
     */
    public WsRawOutputParameter createWsRawOutputParameter() {
        return new WsRawOutputParameter();
    }

    /**
     * Create an instance of {@link GetParameters }
     * 
     */
    public GetParameters createGetParameters() {
        return new GetParameters();
    }

    /**
     * Create an instance of {@link WsResultType }
     * 
     */
    public WsResultType createWsResultType() {
        return new WsResultType();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link GetResultResponse }
     * 
     */
    public GetResultResponse createGetResultResponse() {
        return new GetResultResponse();
    }

    /**
     * Create an instance of {@link WsResultTypes }
     * 
     */
    public WsResultTypes createWsResultTypes() {
        return new WsResultTypes();
    }

    /**
     * Create an instance of {@link WsParameterValue }
     * 
     */
    public WsParameterValue createWsParameterValue() {
        return new WsParameterValue();
    }

    /**
     * Create an instance of {@link WsProperties }
     * 
     */
    public WsProperties createWsProperties() {
        return new WsProperties();
    }

    /**
     * Create an instance of {@link GetParameterDetailsResponse }
     * 
     */
    public GetParameterDetailsResponse createGetParameterDetailsResponse() {
        return new GetParameterDetailsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfString }
     * 
     */
    public ArrayOfString createArrayOfString() {
        return new ArrayOfString();
    }

    /**
     * Create an instance of {@link GetResult }
     * 
     */
    public GetResult createGetResult() {
        return new GetResult();
    }

    /**
     * Create an instance of {@link WsParameterDetails }
     * 
     */
    public WsParameterDetails createWsParameterDetails() {
        return new WsParameterDetails();
    }

    /**
     * Create an instance of {@link InputParameters }
     * 
     */
    public InputParameters createInputParameters() {
        return new InputParameters();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link GetParametersResponse }
     * 
     */
    public GetParametersResponse createGetParametersResponse() {
        return new GetParametersResponse();
    }

    /**
     * Create an instance of {@link GetResultTypes }
     * 
     */
    public GetResultTypes createGetResultTypes() {
        return new GetResultTypes();
    }

    /**
     * Create an instance of {@link RunResponse }
     * 
     */
    public RunResponse createRunResponse() {
        return new RunResponse();
    }

    /**
     * Create an instance of {@link WsRawOutputParameters }
     * 
     */
    public WsRawOutputParameters createWsRawOutputParameters() {
        return new WsRawOutputParameters();
    }

    /**
     * Create an instance of {@link WsParameterValues }
     * 
     */
    public WsParameterValues createWsParameterValues() {
        return new WsParameterValues();
    }

    /**
     * Create an instance of {@link GetParameterDetails }
     * 
     */
    public GetParameterDetails createGetParameterDetails() {
        return new GetParameterDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WsRawOutputParameters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "parameters", scope = GetResult.class)
    public JAXBElement<WsRawOutputParameters> createGetResultParameters(WsRawOutputParameters value) {
        return new JAXBElement<WsRawOutputParameters>(_GetResultParameters_QNAME, WsRawOutputParameters.class, GetResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "title", scope = Run.class)
    public JAXBElement<String> createRunTitle(String value) {
        return new JAXBElement<String>(_RunTitle_QNAME, String.class, Run.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sort", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersSort(String value) {
        return new JAXBElement<String>(_InputParametersSort_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sensitivity", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersSensitivity(String value) {
        return new JAXBElement<String>(_InputParametersSensitivity_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "stats", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersStats(String value) {
        return new JAXBElement<String>(_InputParametersStats_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "align", scope = InputParameters.class)
    public JAXBElement<Integer> createInputParametersAlign(Integer value) {
        return new JAXBElement<Integer>(_InputParametersAlign_QNAME, Integer.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "matrix", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersMatrix(String value) {
        return new JAXBElement<String>(_InputParametersMatrix_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "strand", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersStrand(String value) {
        return new JAXBElement<String>(_InputParametersStrand_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "transltable", scope = InputParameters.class)
    public JAXBElement<Integer> createInputParametersTransltable(Integer value) {
        return new JAXBElement<Integer>(_InputParametersTransltable_QNAME, Integer.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "topcombon", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersTopcombon(String value) {
        return new JAXBElement<String>(_InputParametersTopcombon_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "exp", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersExp(String value) {
        return new JAXBElement<String>(_InputParametersExp_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "scores", scope = InputParameters.class)
    public JAXBElement<Integer> createInputParametersScores(Integer value) {
        return new JAXBElement<Integer>(_InputParametersScores_QNAME, Integer.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sequence", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersSequence(String value) {
        return new JAXBElement<String>(_InputParametersSequence_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "alignments", scope = InputParameters.class)
    public JAXBElement<Integer> createInputParametersAlignments(Integer value) {
        return new JAXBElement<Integer>(_InputParametersAlignments_QNAME, Integer.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "viewfilter", scope = InputParameters.class)
    public JAXBElement<Boolean> createInputParametersViewfilter(Boolean value) {
        return new JAXBElement<Boolean>(_InputParametersViewfilter_QNAME, Boolean.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "filter", scope = InputParameters.class)
    public JAXBElement<String> createInputParametersFilter(String value) {
        return new JAXBElement<String>(_InputParametersFilter_QNAME, String.class, InputParameters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = WsResultType.class)
    public JAXBElement<String> createWsResultTypeDescription(String value) {
        return new JAXBElement<String>(_WsResultTypeDescription_QNAME, String.class, WsResultType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "label", scope = WsResultType.class)
    public JAXBElement<String> createWsResultTypeLabel(String value) {
        return new JAXBElement<String>(_WsResultTypeLabel_QNAME, String.class, WsResultType.class, value);
    }

}
