package TwoThreeFourTree;

import javax.swing.*;
import java.awt.*;

public class TreeVisualizer extends JPanel {
    private TwoFourTree tree;
    private static final int VERTICAL_MARGIN = 100;  // Vertical margin between levels
    private static final int HORIZONTAL_MARGIN = 20; // Horizontal margin between siblings
    private static final int HORIZONTAL_PADDING = 5; // Horizontal padding from screen edges
    private double scale = 1.0;  // Initial scale set to 100%

    public TreeVisualizer(TwoFourTree tree) {
        this.tree = tree;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (tree.root != null) {
            int totalTreeWidth = calculateSubtreeWidth(g, tree.root);
            int windowWidth = getWidth() - 2 * HORIZONTAL_PADDING; // Adjust for horizontal padding
            int windowHeight = getHeight();

            // Calculate scaling if the tree doesn't fit in the current window
            if (totalTreeWidth > windowWidth || getTotalTreeHeight() > windowHeight) {
                scale = Math.min((double) windowWidth / totalTreeWidth, (double) windowHeight / getTotalTreeHeight());
            } else {
                scale = 1.0;  // No scaling if it fits
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.scale(scale, scale);  // Apply scaling to the Graphics context

            // Start drawing from the root, centered at the width of the panel, accounting for padding
            drawNode(g2d, tree.root, (int) ((getWidth() - HORIZONTAL_PADDING) / (2 * scale)), 50);
        }
    }

    private void drawNode(Graphics g, TwoFourTree.TwoFourTreeItem node, int x, int y) {
        // Check if this node should be highlighted
        Color originalColor = g.getColor();
        Color backgroundColor = Color.WHITE; // Default background color

        // Highlighting using custom RGB background color
        if (node.equals(tree.highlightedAddedNode)) {
            backgroundColor = new Color(0, 200, 0);  // Greenish background for added node
        } else if (node.equals(tree.highlightedDeletedNode)) {
            backgroundColor = new Color(255, 105, 105);  // Red background for deleted node
        }

        // Draw the background box
        String keysStr = node.keys.toString().replace("[", "").replace("]", "");
        int nodeWidth = g.getFontMetrics().stringWidth(keysStr) + 20;
        g.setColor(backgroundColor);
        g.fillRect(x - nodeWidth / 2, y, nodeWidth, 30);

        // Draw the border and text of the node
        g.setColor(Color.BLACK);
        g.drawRect(x - nodeWidth / 2, y, nodeWidth, 30);
        g.drawString(keysStr, x - nodeWidth / 2 + 10, y + 20);

        // Reset color after drawing
        g.setColor(originalColor);

        // Draw lines to children
        if (node.children != null && !node.isLeaf()) {
            // Get total width required by the subtree to dynamically adjust spacing
            int subtreeWidth = calculateSubtreeWidth(g, node);
            int startX = x - subtreeWidth / 2;

            // Calculate line start points for each child (spread lines evenly from the parent's width)
            int parentStartX = x - nodeWidth / 2;
            int step = nodeWidth / (node.children.size() + 1);

            for (int i = 0; i < node.children.size(); i++) {
                TwoFourTree.TwoFourTreeItem child = node.children.get(i);
                if (child != null) {
                    int childWidth = calculateSubtreeWidth(g, child);
                    int childX = startX + childWidth / 2;

                    // Draw line from the appropriate point on the parent's node to the child
                    g.drawLine(parentStartX + (i + 1) * step, y + 30, childX, y + VERTICAL_MARGIN);

                    // Recursively draw the child node
                    drawNode(g, child, childX, y + VERTICAL_MARGIN);

                    // Move the starting point to the next child's space, adding horizontal margin
                    startX += childWidth + HORIZONTAL_MARGIN;
                }
            }
        }
    }


    // Function to calculate the total width required by a subtree
    private int calculateSubtreeWidth(Graphics g, TwoFourTree.TwoFourTreeItem node) {
        if (node.isLeaf()) {
            // Leaf nodes only need space for their own width
            String keysStr = node.keys.toString().replace("[", "").replace("]", "");
            return g.getFontMetrics().stringWidth(keysStr) + 20;
        } else {
            // For non-leaf nodes, sum the widths of all children
            int totalWidth = 0;
            for (TwoFourTree.TwoFourTreeItem child : node.children) {
                if (child != null) {
                    totalWidth += calculateSubtreeWidth(g, child) + HORIZONTAL_MARGIN;
                }
            }
            return totalWidth - HORIZONTAL_MARGIN;  // Subtract the last extra margin
        }
    }

    // Function to calculate the total height of the tree
    private int getTotalTreeHeight() {
        return getTreeHeight(tree.root) * VERTICAL_MARGIN;
    }

    // Recursive function to get the height of the tree (number of levels)
    private int getTreeHeight(TwoFourTree.TwoFourTreeItem node) {
        if (node.isLeaf()) {
            return 1;
        } else {
            int maxHeight = 0;
            for (TwoFourTree.TwoFourTreeItem child : node.children) {
                if (child != null) {
                    int childHeight = getTreeHeight(child);
                    if (childHeight > maxHeight) {
                        maxHeight = childHeight;
                    }
                }
            }
            return maxHeight + 1;  // Include the current level
        }
    }

    // Function to repaint the tree when updated
    public void refresh() {
        repaint();  // Causes the panel to be redrawn with updated tree structure
    }

    public static JFrame createAndShowGUI(TwoFourTree tree) {
        JFrame frame = new JFrame("2-3-4 Tree Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TreeVisualizer visualizer = new TreeVisualizer(tree);
        frame.add(visualizer);
        frame.setSize(1000, 800);
        frame.setVisible(true);
        return frame; // Return the frame so we can access the visualizer later
    }
}
