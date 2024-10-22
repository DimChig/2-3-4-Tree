package TwoThreeFourTree;

import java.util.ArrayList;

public class TwoFourTree {

    TwoFourTreeItem root = null;

    public class TwoFourTreeItem {

        TwoFourTreeItem parent = null;

        ArrayList<Integer> keys; // all keys
        ArrayList<TwoFourTreeItem> children; // all children (len = keys.size() + 1)

        public boolean isRoot() {
            return this.parent == null;
        }

        public boolean isLeaf() {
            for (TwoFourTreeItem c : children) {
                if (c != null) return false;
            }
            return true;
        }

        public Integer getFirstKey() {
            return keys.get(0);
        }

        public Integer getLastKey() {
            return keys.get(keys.size() - 1);
        }

        public TwoFourTreeItem getFirstChild() {
            return children.get(0);
        }

        public TwoFourTreeItem getLastChild() {
            return children.get(children.size() - 1);
        }

        /**
         * START OF SHITTY CODE
         */
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        /* TRANSLATOR FOR SHITTY CODE */
        public void shittyfyMyself() {
            // We just need to reassign these variables:
            // isLeaf, leftChild, centerChild, centerLeftChild, centerRightChild, rightChild, value1, value2, value3

            // default values
            value1 = 0;
            value2 = 0;
            value3 = 0;
            isLeaf = isLeaf();
            leftChild = null;
            rightChild = null;
            centerChild = null;
            centerLeftChild = null;
            centerRightChild = null;

            if (children.size() == 2) {
                value1 = keys.get(0);

                leftChild = children.get(0);
                rightChild = children.get(1);
            } else if (children.size() == 3) {

                value1 = keys.get(0);
                value2 = keys.get(1);

                leftChild = children.get(0);
                centerChild = children.get(1);
                rightChild = children.get(2);
            } else if (children.size() == 4) {

                value1 = keys.get(0);
                value2 = keys.get(1);
                value3 = keys.get(2);

                leftChild = children.get(0);
                centerLeftChild = children.get(1);
                centerRightChild = children.get(2);
                rightChild = children.get(3);
            }
        }

        public boolean isThreeNode() {
            return this.children.size() == 3;
        }

        public boolean isFourNode() {
            return this.children.size() == 4;
        }

        private void printIndents(int indent) {
            for (int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if (!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if (isThreeNode()) {
                if (!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if (isFourNode()) {
                if (!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if (!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if (!isLeaf) rightChild.printInOrder(indent + 1);
        }

        /** END OF SHITTY CODE **/
    }


    public boolean addValue(int value) {
        // If the root is null, create a new root node
        if (root == null) {
            root = new TwoFourTreeItem();
            root.parent = null;
            root.keys = new ArrayList<>();
            root.keys.add(value);
            root.children = new ArrayList<>();
            root.children.add(null);  // Initialize children with 2 null values
            root.children.add(null);
            root.shittyfyMyself();
            return true;
        }


        // Start at the root and traverse to find the appropriate position to insert
        TwoFourTreeItem currentNode = null;
        TwoFourTreeItem nextNode = null;

        while (true) {

            if (currentNode == null) nextNode = root;
            else {
                int i;
                for (i = 0; i < currentNode.keys.size(); i++) {
                    if (value < currentNode.keys.get(i)) {
                        break;  // The value should go into the i-th child
                    }
                }
                nextNode = currentNode.children.get(i);
            }

            // Check if we need to split the node
            if (nextNode.keys.size() == 3) {  // 4-node (3 keys)
                nextNode = splitNode(nextNode);
            }

            if (nextNode.isLeaf()) break;

            // Traverse the node's keys to find the right child
            currentNode = nextNode;
        }
        // Insert the value into the leaf node
        insertIntoNode(nextNode, value);

        return true;
    }

    private void insertIntoNode(TwoFourTreeItem node, int value) {
        // Insert the value into the node's keys list, maintaining sorted order
        int i = 0;
        while (i < node.keys.size() && value > node.keys.get(i)) {
            i++;
        }
        node.keys.add(i, value);  // Insert value in sorted order
        node.children.add(i + 1, null);  // Update children for the new key
        node.shittyfyMyself();
    }

    private TwoFourTreeItem splitNode(TwoFourTreeItem node) {
        // Create a new node to the right and push the middle key up to the parent
        int midIndex = 1;  // The middle key in a 3-key (4-node) structure is at index 1

        int midKey = node.keys.remove(midIndex);
        TwoFourTreeItem newRightNode = new TwoFourTreeItem();

        // Move the right half of keys and children to the new right node
        newRightNode.keys = new ArrayList<>(node.keys.subList(midIndex, node.keys.size()));
        node.keys.subList(midIndex, node.keys.size()).clear();

        newRightNode.children = new ArrayList<>(node.children.subList(midIndex + 1, node.children.size()));
        for (TwoFourTreeItem i : newRightNode.children) if (i != null) i.parent = newRightNode;
        node.children.subList(midIndex + 1, node.children.size()).clear();

        // If node is root, create a new root and assign parent
        if (node.isRoot()) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem();
            newRoot.keys = new ArrayList<>();
            newRoot.keys.add(midKey);

            newRoot.children = new ArrayList<>();
            newRoot.children.add(node);
            newRoot.children.add(newRightNode);

            node.parent = newRoot;
            newRightNode.parent = newRoot;

            root = newRoot;  // Update the tree's root
            root.parent = null;
        } else {
            // Insert the middle key into the parent node
            insertIntoNode(node.parent, midKey);
            // Insert the new right node as a child of the parent
            int index = node.parent.children.indexOf(node);
            if (node.parent.children.size() > index + 1 && node.parent.children.get(index + 1) == null)
                node.parent.children.set(index + 1, newRightNode);
            else node.parent.children.add(index + 1, newRightNode);
            newRightNode.parent = node.parent;
        }

        node.shittyfyMyself();
        newRightNode.shittyfyMyself();
        node.parent.shittyfyMyself();

//        for (TwoFourTreeItem child: node.children) if (child != null) child.shittyfyMyself();
//        for (TwoFourTreeItem child: newRightNode.children) if (child != null) child.shittyfyMyself();
//        for (TwoFourTreeItem child: node.parent.children) if (child != null) child.shittyfyMyself();

        return node.parent;
    }

    public TwoFourTreeItem searchNode(int value) {
        if (root == null) return null;

        TwoFourTreeItem currentNode = root;
        int v;
        while (currentNode != null) {
            int i;
            for (i = 0; i < currentNode.keys.size(); i++) {
                v = currentNode.keys.get(i);
                if (v == value) return currentNode;
                if (value < v) break;
            }
            currentNode = currentNode.children.get(i);
        }
        return null;
    }

    public boolean hasValue(int value) {
        return searchNode(value) != null;
    }

    public boolean deleteFromLeafNode(TwoFourTreeItem node, int value) {
        // simply remove the key and child
        node.keys.remove((Integer) value);
        node.children.remove(0); // doesn't matter which one to remove as they are all null

        node.shittyfyMyself();
        if (node.isRoot() && node.keys.isEmpty()) root = null;
        return true;
    }

    public boolean performLeftRotation(TwoFourTreeItem parentNode, int parent_key_idx, TwoFourTreeItem targetNode, TwoFourTreeItem sharingNode) {
        // 1. Add parent node to the end of targetNode.keys
        int value = parentNode.keys.remove(parent_key_idx);
        targetNode.keys.add(value);
        // 2. Reassign left most child of sharing node to the targetNode.keys[last] children
        TwoFourTreeItem child = sharingNode.children.remove(0);
        targetNode.children.add(child);
        if (child != null) child.parent = targetNode;
        // 3. Move first child of sharing node to the root end
        value = sharingNode.keys.remove(0);
        parentNode.keys.add(parent_key_idx, value);
        // 4. Update parent
        targetNode.parent = parentNode;
        sharingNode.parent = parentNode;

        parentNode.shittyfyMyself();
        targetNode.shittyfyMyself();
        sharingNode.shittyfyMyself();

        return true;
    }

    public boolean performRightRotation(TwoFourTreeItem parentNode, int parent_key_idx, TwoFourTreeItem targetNode, TwoFourTreeItem sharingNode) {
        // 1. Add parent node to the start of targetNode.keys
        int value = parentNode.keys.remove(parent_key_idx);
        targetNode.keys.add(0, value);
        // 2. Reassign right most child of sharing node to the targetNode.keys[0] children
        TwoFourTreeItem child = sharingNode.children.remove(sharingNode.children.size() - 1);
        targetNode.children.add(0, child);
        if (child != null) child.parent = targetNode;
        // 3. Move last child of sharing node to the root start
        value = sharingNode.keys.remove(sharingNode.keys.size() - 1);
        parentNode.keys.add(parent_key_idx, value);
        // 4. Update parent
        targetNode.parent = parentNode;
        sharingNode.parent = parentNode;

        parentNode.shittyfyMyself();
        targetNode.shittyfyMyself();
        sharingNode.shittyfyMyself();

        return true;
    }

    public TwoFourTreeItem performMerge(TwoFourTreeItem parentNode, int parent_key_idx) {
        // 1. Remove key from parent
        int middle_value = parentNode.keys.remove(parent_key_idx);
        // 2. Remove the right child of the key that is being pulled down
        TwoFourTreeItem right_tree = parentNode.children.remove(parent_key_idx + 1);
        // 3. Add the root to the left child
        TwoFourTreeItem left_tree = parentNode.children.get(parent_key_idx);
        left_tree.keys.add(middle_value);
        // 4. Add the keys of right tree to the keys of the left tree
        left_tree.keys.addAll(right_tree.keys);
        // 5. Add the children of right tree to the children of the left tree
        left_tree.children.addAll(right_tree.children);
        // 6. Repoint right_tree children back to parent
        for (TwoFourTreeItem child: right_tree.children) {
            if (child != null) child.parent = left_tree;
        }
        // 7. Check if root has any keys. If not, reassign pointer
        if (root.keys.isEmpty()) {
            root = left_tree;
            root.parent = null;
        }

        parentNode.shittyfyMyself();
        left_tree.shittyfyMyself();

        return left_tree;
    }

    public TwoFourTreeItem getLeftSibling(TwoFourTreeItem parentNode, int child_idx) {
        if (child_idx == 0 || parentNode.children.size() < 2) return null;
        return parentNode.children.get(child_idx - 1);
    }

    public TwoFourTreeItem getRightSibling(TwoFourTreeItem parentNode, int child_idx) {
        if (child_idx >= parentNode.children.size() - 1) return null;
        return parentNode.children.get(child_idx + 1);
    }

    public TwoFourTreeItem resolveSmallNodeForDeletion(TwoFourTreeItem parentNode, TwoFourTreeItem node, int child_idx) {
        // "Node" has 1 key only rn
        // "ParentNode" has access to siblings children
        // "child_idx" is to find siblings quicker

        TwoFourTreeItem left_sibling = getLeftSibling(parentNode, child_idx);
        TwoFourTreeItem right_sibling = getRightSibling(parentNode, child_idx);
        int parent_key_idx = -1;
        TwoFourTreeItem mergingSibling = null;

        // try borrowing from the left sibling
        if (left_sibling != null) {
            parent_key_idx = child_idx - 1;
            if (left_sibling.keys.size() > 1) {
                if (performRightRotation(parentNode, parent_key_idx, node, left_sibling)) return node;
            }
        }
        // try borrowing from the right sibling
        if (right_sibling != null) {
            parent_key_idx = child_idx;
            if (right_sibling.keys.size() > 1) {
                if (performLeftRotation(parentNode, parent_key_idx, node, right_sibling)) return node;
            }
        }
        // if both subtrees can't resolve, merge node
        // finding a merging sibling
        node = performMerge(parentNode, parent_key_idx);
        return node;
    }

    public TwoFourTreeItem searchPredecessor(TwoFourTreeItem currentNode) {
        // "currentNode" is the root of the subtree
        while (currentNode != null && !currentNode.isLeaf()) {
            currentNode = currentNode.getLastChild();
        }
        return currentNode;
    }

    public TwoFourTreeItem searchSuccessor(TwoFourTreeItem currentNode) {
        // "currentNode" is the root of the subtree
        while (currentNode != null && !currentNode.isLeaf()) {
            currentNode = currentNode.getFirstChild();
        }
        return currentNode;
    }

    public boolean deletionSwap(TwoFourTreeItem containerNode, int value_in_container, TwoFourTreeItem leafNode, int value_in_leaf) {
        int idx = containerNode.keys.indexOf(value_in_container);
        containerNode.keys.remove((Integer) value_in_container);
        containerNode.keys.add(idx, value_in_leaf);
        if (!deleteFromLeafNode(leafNode, value_in_leaf)) return false;

        containerNode.shittyfyMyself();
        return true;
    }

    public boolean deleteValue(int value) {
        if (root == null) return false;
        TwoFourTreeItem currentNode = root;
        TwoFourTreeItem nextNode = null;
        TwoFourTreeItem deleteNode = null;
        int v;

        while (currentNode != null) {
            // Check if node has this key
            if (currentNode.isLeaf()) {
                if (deleteNode == null || deleteNode.keys.isEmpty() || currentNode.keys.contains(value)) {
                    if (!currentNode.keys.contains(value)) return false;
                    return deleteFromLeafNode(currentNode, value); // Delete from leaf simple
                } else {
                    // Swap values
                    int value_to_delete = currentNode.getFirstKey(); // predecessor
                    if (deleteNode.children.get(deleteNode.keys.indexOf((Integer) value)) == currentNode) {
                        value_to_delete = currentNode.getLastKey(); //successor
                    }
                    return deletionSwap(deleteNode, value, currentNode, value_to_delete);
                }
            } else {
                // Check if we found the key
                int key_idx = currentNode.keys.indexOf(value);
                if (key_idx >= 0) { // Key is inside this node
                    // find successor and predecessor
                    TwoFourTreeItem predecessor = searchPredecessor(currentNode.children.get(key_idx));
                    if (predecessor != null && predecessor.keys.size() > 1) {
                        if (deletionSwap(currentNode, value, predecessor, predecessor.getLastKey())) {
                            return true;
                        }
                    }
                    // if predecessor has only 1 key, try successor
                    TwoFourTreeItem successor = searchSuccessor(currentNode.children.get(key_idx + 1));
                    if (successor != null && successor.keys.size() > 1) {
                        if (deletionSwap(currentNode, value, successor, successor.getFirstKey())) {
                            return true;
                        }
                    }

                    // if both successor and predecessor are 2-node,
                    // then continue traversing to predecessor and rotate on the way

                    deleteNode = currentNode; // remember the child and continue
                } //else traverse down
            }

            // If key is not in the node, traverse down. Finding appropriate children
            int i;
            for (i = 0; i < currentNode.keys.size(); i++) {
                v = currentNode.keys.get(i);
                if (value < v) break;
            }
            nextNode = currentNode.children.get(i);
            if (nextNode == null) return false; // Node is not in the tree

            // Make sure that the node has at least 2 keys (and doesn't contain value)
            if (nextNode.keys.size() < 2) {
                // Perform a left/right rotation or merge
                nextNode = resolveSmallNodeForDeletion(currentNode, nextNode, i);
                if (nextNode == null) return false; // Something happened
            }
            currentNode = nextNode;
        }
        return false; //End. Node is not found
    }

    public TwoFourTreeItem highlightedAddedNode = null; //just for visualizer
    public TwoFourTreeItem highlightedDeletedNode = null; //just for visualizer

    // CAN'T TOUCH THIS FUNCTION
    public void printInOrder() {
        if (root != null) root.printInOrder(0);
    }
}
