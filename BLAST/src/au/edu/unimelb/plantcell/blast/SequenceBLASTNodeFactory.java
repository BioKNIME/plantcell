package au.edu.unimelb.plantcell.blast;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SequenceBLAST" Node.
 * Supports local execution of NCBI BLAST+ executables (which must be  installed separately)
 *
 * @author http://www.plantcell.unimelb.edu.au/
 */
public class SequenceBLASTNodeFactory 
        extends NodeFactory<SequenceBLASTNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SequenceBLASTNodeModel createNodeModel() {
        return new SequenceBLASTNodeModel();
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
    public NodeView<SequenceBLASTNodeModel> createNodeView(final int viewIndex,
            final SequenceBLASTNodeModel nodeModel) {
        return null;
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
        return new SequenceBLASTNodeDialog();
    }

}

