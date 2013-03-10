package au.edu.unimelb.plantcell.io.write.spectra;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SpectraWriter" Node.
 * Writes a spectra column out to disk for processing with other Mass Spec. software. Supports MGF format but does not guarantee that all input data will be preserved in the created file.
 *
 * @author Andrew Cassin
 */
public class SpectraWriterNodeView extends NodeView<SpectraWriterNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link SpectraWriterNodeModel})
     */
    protected SpectraWriterNodeView(final SpectraWriterNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        SpectraWriterNodeModel nodeModel = 
            (SpectraWriterNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

