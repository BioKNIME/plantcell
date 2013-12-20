
package au.edu.unimelb.plantcell.io.ws.biomart.soap;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PortalServiceImpl", targetNamespace = "http://soap.api.biomart.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PortalServiceImpl {


    /**
     * 
     * @param container
     * @param datasets
     * @param config
     * @param allowPartialList
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.Attribute>
     */
    @WebMethod
    @WebResult(name = "attribute", targetNamespace = "")
    @RequestWrapper(localName = "getAttributes", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetAttributes")
    @ResponseWrapper(localName = "getAttributesResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetAttributesResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getAttributesRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getAttributesResponse")
    public List<Attribute> getAttributes(
        @WebParam(name = "datasets", targetNamespace = "")
        String datasets,
        @WebParam(name = "config", targetNamespace = "")
        String config,
        @WebParam(name = "container", targetNamespace = "")
        String container,
        @WebParam(name = "allowPartialList", targetNamespace = "")
        Boolean allowPartialList);

    /**
     * 
     * @param container
     * @param datasets
     * @param config
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.Filter>
     */
    @WebMethod
    @WebResult(name = "filter", targetNamespace = "")
    @RequestWrapper(localName = "getFilters", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetFilters")
    @ResponseWrapper(localName = "getFiltersResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetFiltersResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getFiltersRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getFiltersResponse")
    public List<Filter> getFilters(
        @WebParam(name = "datasets", targetNamespace = "")
        String datasets,
        @WebParam(name = "config", targetNamespace = "")
        String config,
        @WebParam(name = "container", targetNamespace = "")
        String container);

    /**
     * 
     * @param guitype
     * @return
     *     returns au.edu.unimelb.plantcell.io.ws.biomart.soap.GuiContainer
     */
    @WebMethod
    @WebResult(name = "guicontainer", targetNamespace = "")
    @RequestWrapper(localName = "getRootGuiContainer", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetRootGuiContainer")
    @ResponseWrapper(localName = "getRootGuiContainerResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetRootGuiContainerResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getRootGuiContainerRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getRootGuiContainerResponse")
    public GuiContainer getRootGuiContainer(
        @WebParam(name = "guitype", targetNamespace = "")
        String guitype);

    /**
     * 
     * @param guicontainer
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.Mart>
     */
    @WebMethod
    @WebResult(name = "mart", targetNamespace = "")
    @RequestWrapper(localName = "getMarts", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetMarts")
    @ResponseWrapper(localName = "getMartsResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetMartsResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getMartsRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getMartsResponse")
    public List<Mart> getMarts(
        @WebParam(name = "guicontainer", targetNamespace = "")
        String guicontainer);

    /**
     * 
     * @param mart
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.Dataset>
     */
    @WebMethod
    @WebResult(name = "dataset", targetNamespace = "")
    @RequestWrapper(localName = "getDatasets", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetDatasets")
    @ResponseWrapper(localName = "getDatasetsResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetDatasetsResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getDatasetsRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getDatasetsResponse")
    public List<Dataset> getDatasets(
        @WebParam(name = "mart", targetNamespace = "")
        String mart);

    /**
     * 
     * @param datasets
     * @param value
     * @param config
     * @param filter
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.FilterData>
     */
    @WebMethod
    @WebResult(name = "filtervalue", targetNamespace = "")
    @RequestWrapper(localName = "getFilterValues", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetFilterValues")
    @ResponseWrapper(localName = "getFilterValuesResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetFilterValuesResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getFilterValuesRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getFilterValuesResponse")
    public List<FilterData> getFilterValues(
        @WebParam(name = "filter", targetNamespace = "")
        String filter,
        @WebParam(name = "value", targetNamespace = "")
        String value,
        @WebParam(name = "datasets", targetNamespace = "")
        String datasets,
        @WebParam(name = "config", targetNamespace = "")
        String config);

    /**
     * 
     * @param withFilters
     * @param withAttributes
     * @param datasets
     * @param config
     * @param allowPartialList
     * @return
     *     returns au.edu.unimelb.plantcell.io.ws.biomart.soap.Container
     */
    @WebMethod
    @WebResult(name = "container", targetNamespace = "")
    @RequestWrapper(localName = "getContainers", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetContainers")
    @ResponseWrapper(localName = "getContainersResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetContainersResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getContainersRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getContainersResponse")
    public Container getContainers(
        @WebParam(name = "datasets", targetNamespace = "")
        String datasets,
        @WebParam(name = "config", targetNamespace = "")
        String config,
        @WebParam(name = "withAttributes", targetNamespace = "")
        Boolean withAttributes,
        @WebParam(name = "withFilters", targetNamespace = "")
        Boolean withFilters,
        @WebParam(name = "allowPartialList", targetNamespace = "")
        Boolean allowPartialList);

    /**
     * 
     * @param datasets
     * @return
     *     returns java.util.List<au.edu.unimelb.plantcell.io.ws.biomart.soap.Dataset>
     */
    @WebMethod
    @WebResult(name = "dataset", targetNamespace = "")
    @RequestWrapper(localName = "getLinkables", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetLinkables")
    @ResponseWrapper(localName = "getLinkablesResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetLinkablesResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getLinkablesRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getLinkablesResponse")
    public List<Dataset> getLinkables(
        @WebParam(name = "datasets", targetNamespace = "")
        String datasets);

    /**
     * 
     * @param query
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "result", targetNamespace = "")
    @RequestWrapper(localName = "getResults", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetResults")
    @ResponseWrapper(localName = "getResultsResponse", targetNamespace = "http://soap.api.biomart.org/", className = "au.edu.unimelb.plantcell.io.ws.biomart.soap.GetResultsResponse")
    @Action(input = "http://soap.api.biomart.org/PortalServiceImpl/getResultsRequest", output = "http://soap.api.biomart.org/PortalServiceImpl/getResultsResponse")
    public String getResults(
        @WebParam(name = "query", targetNamespace = "")
        String query);

}