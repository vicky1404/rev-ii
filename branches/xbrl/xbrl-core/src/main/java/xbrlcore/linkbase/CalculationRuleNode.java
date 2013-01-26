package xbrlcore.linkbase;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xbrlcore.taxonomy.Concept;

/**
 * Defines the calculation rules for a {@link Concept} from {@link CalculationLinkbase}
 * with the extension to {@link CalculationRule} of tree node information (children).
 * 
 * @author Marvin Froehlich (INFOLOG GmbH)
 */
public class CalculationRuleNode extends CalculationRule implements Iterable<CalculationRuleNode> {

    private static final long serialVersionUID = 5942748944839492341L;

    public static interface DirtyNodeNotifier {
        public void notify(CalculationRuleNode node);
    }

    private final boolean isWeighted;

    private double value = 0.0;
    private boolean isUserValue = false;
    private boolean dirty = true;

    private Object id;

    private CalculationRuleNode parent = null;
    private final List<CalculationRuleNode> children = new ArrayList<CalculationRuleNode>();

    protected CalculationRuleNode(Object id, Concept concept, float weight, boolean isWeighted) {
        super(concept, weight);

        this.isWeighted = isWeighted;
        this.id = id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public final Object getId() {
        return id;
    }

    public final boolean isWeighted() {
        return isWeighted;
    }

    public final boolean hasUserValue() {
        return isUserValue;
    }

    protected void setParent(CalculationRuleNode parent) {
        this.parent = parent;
    }

    public final CalculationRuleNode getParent() {
        return parent;
    }

    protected void addChild(CalculationRuleNode child) {
        children.add(child);
    }

    public final int getNumChildren() {
        return children.size();
    }

    public final boolean hasChildren() {
        return (children.size() > 0);
    }

    public final CalculationRuleNode getChild(int index) {
        return children.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<CalculationRuleNode> iterator() {
        return children.iterator();
    }

    private void setDirty(DirtyNodeNotifier notifier) {
        this.dirty = true;

        if (notifier != null) {
            notifier.notify(this);
        }

        if (parent != null) {
            parent.setDirty(notifier);
        }
    }

    public void resetValue(DirtyNodeNotifier notifier) {
        this.value = 0.0;
        this.isUserValue = false;
        this.dirty = hasChildren();

        if (parent != null) {
            parent.setDirty(notifier);
        }
    }

    public final void resetValue() {
        resetValue(null);
    }

    public void setValue(double value, DirtyNodeNotifier notifier) {
        this.value = (value == -0.0 ? 0.0 : value);
        if (hasChildren()) {
            this.isUserValue = true;
        }
        this.dirty = false;

        if (parent != null) {
            parent.setDirty(notifier);
        }
    }

    public final void setValue(double value) {
        setValue(value, null);
    }

    public final double getValue() {
        return value;
    }

    public double calculate(boolean force) {
        if (isUserValue) {
            return value;
        }

        if (hasChildren() && !isUserValue && (dirty || force)) {
            this.value = 0.0;

            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).isWeighted())
                    this.value += children.get(i).calculate(force);
            }

            if (isWeighted()) {
                this.value *= getWeight();
            }

            if (this.value == -0.0)
                this.value = 0.0;

            this.dirty = false;
        }

        return this.value;
    }

    public final double getCalculatedValue() {
        return calculate(false);
    }

    public void dump(int indent, PrintStream ps) {
        for (int i = 0; i < indent; i++)
            ps.print("  ");

        ps.println(getId() + ", " + isWeighted() + ", " + dirty + ", " + getValue() + ", " + getCalculatedValue() + ", " + (isUserValue ? "USER_VALUE" : "CALCULATED_VALUE"));
        
        if (hasChildren()) {
            for (int i = 0; i < children.size(); i++) {
                children.get(i).dump(indent + 1, ps);
            }
        }
    }

    public final void dump() {
        dump(0, System.out);
    }
}
