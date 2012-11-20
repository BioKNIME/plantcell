package au.edu.unimelb.plantcell.proteomics.ppgroupxml;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FastaReader" Node.
 * This nodes reads sequences from the user-specified FASTA file and outputs three columns per sequence: * n1) Accession * n2) Description - often not accurate in practice * n3) Sequence data * n * nNo line breaks are preserved.
 *
 * @author Andrew Cassin
 */
public class GroupXMLReaderNodeFactory 
        extends NodeFactory<GroupXMLReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupXMLReaderNodeModel createNodeModel() {
        return new GroupXMLReaderNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<GroupXMLReaderNodeModel> createNodeView(final int viewIndex,
            final GroupXMLReaderNodeModel nodeModel) {
        return null;	// no view for this node
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new GroupXMLReaderNodeDialog();
    }

}

