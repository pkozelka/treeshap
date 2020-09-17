package ai.h2o.treeshap.tree;

import java.io.PrintWriter;

public class PkTree {
    public final PkNode root;

    public PkTree(final PkNode root) {
        this.root = root;
    }

    private static final String INDENT_STEP = "    ";

    /**
     * Prints the decision tree using java "if" tree as its notation, for convenient reading
     */
    public void printTree(final PrintWriter pw) {
        printNode(pw, "", root);
        pw.flush();
    }

    private static void printNode(final PrintWriter pw, final String indent, final PkNode node) {
        if (node.isSplit()) {
            pw.printf("%sif (data[%d] <= %f) {  // dc = %f%n", indent, node.splitFeatureIndex, node.splitValue, node.dataCount);
            printNode(pw, indent + INDENT_STEP, node.yes);
            pw.printf("%s} else {%n", indent);
            printNode(pw, indent + INDENT_STEP, node.no);
            pw.printf("%s}%n", indent);
        } else {
            pw.printf("%sreturn %f;  // dc = %f%n", indent, node.leafValue, node.dataCount);
        }
    }



    /**
     * Prints the tree in form of java builder - useful for importing more test trees from other formats
     */
    public void printTreeConstructor(final PrintWriter pw) {
        pw.print("new PkTree(");
        printNodeConstructor(pw, INDENT_STEP, root);
        pw.printf("%n)%n");
        pw.flush();
    }

    private static void printNodeConstructor(final PrintWriter pw, final String indent, final PkNode node) {
        if (node.isSplit()) {
            pw.printf("%n%sPkNode.split(/*dc=*/%f, /*col=*/%d, %f,", indent, node.dataCount, node.splitFeatureIndex, node.splitValue);
            printNodeConstructor(pw, indent + INDENT_STEP, node.yes);
            pw.print(",");
            printNodeConstructor(pw, indent + INDENT_STEP, node.no);
            pw.print(")");
        } else {
            pw.printf("%n%sPkNode.leaf(/*dc=*/%f, %f)", indent, node.dataCount, node.leafValue);
        }
    }

}

