package TwoThreeFourTree;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class AppNew {

    // SET IS TO TRUE TO MAKE COOL ANIMATION
    public static boolean isRandomValues = true;
    public static int number_of_random_values = 40;
    public static int total_time = 10; // higher = slower animation

    public static void main(String[] args) throws InterruptedException {
        TwoFourTree tree = new TwoFourTree();
        ArrayList<Integer> valueList = new ArrayList<>();


        if (!isRandomValues) {
            // Hardcoded array of values
            int[] values = {2, 3, 5, 7, 1, 10, 35, 38, 49, 40, 28, 30, 29};
            valueList = new ArrayList<>(values.length);
            for (int value : values) {
                valueList.add(value);
            }
            // Loop to add the values to the tree
            for (int value : values) {
                tree.addValue(value);
            }
            tree.printInOrder();

            JFrame frame = TreeVisualizer.createAndShowGUI(tree);
            TreeVisualizer visualizer = (TreeVisualizer) frame.getContentPane().getComponent(0);

        } else {
            valueList.clear();

            Random rnd = new Random();

            int time_interval = total_time * 1000 / number_of_random_values;

            // Create the JFrame and the visualizer

            JFrame frame = TreeVisualizer.createAndShowGUI(tree);
            TreeVisualizer visualizer = (TreeVisualizer) frame.getContentPane().getComponent(0);


            ArrayList<Integer> random_numbers = new ArrayList<>();
//            for (int i = 0; i < number_of_random_values; i++) {
//                random_numbers.add(i + 1);
//            }
//            valueList.addAll(random_numbers);
//
//
//            // Add values and refresh the tree visualization in a loop
//            while (!random_numbers.isEmpty()) {
//
//                int x = random_numbers.remove(rnd.nextInt(random_numbers.size()));
//                //System.out.println("Add " + x);
//                tree.addValue(x);
//
//                SwingUtilities.invokeLater(visualizer::refresh); // Refresh the tree
//                Thread.sleep(time_interval);  // Pause to simulate real-time updates
//            }
//
//            // Add values and refresh the tree visualization in a loop
//            while (valueList.size() > 0) {
//                int x = valueList.remove(rnd.nextInt(valueList.size()));
//                //System.out.println("Delete " + x);
//                Thread.sleep(time_interval);  // Pause to simulate real-time updates
//
//                if (!tree.deleteValue(x)) {
//                    System.out.println("ERROR: COULDN'T DELETE");
//                }
//                SwingUtilities.invokeLater(visualizer::refresh); // Refresh the tree
//
//                // detect where it got wrong
//                // tree.mycheck(tree.root);
//                // tree.mycheck2(tree.root);
//            }
            tree.root = null;


            // Make add/delete animation




            random_numbers = new ArrayList<>();
            for (int i = 0; i < number_of_random_values; i++) {
                random_numbers.add(i + 1);
            }
            valueList = new ArrayList<>();
            valueList.addAll(random_numbers);


            while (!random_numbers.isEmpty()) {

                int x = random_numbers.remove(rnd.nextInt(random_numbers.size()));
                tree.addValue(x);
                tree.highlightedAddedNode = tree.searchNode(x);

                SwingUtilities.invokeLater(visualizer::refresh); // Refresh the tree
                Thread.sleep(time_interval);  // Pause to simulate real-time updates
            }

            // delete and add randomly
            int random_delete_value = -1;
            while (true) {
                tree.highlightedAddedNode = null;
                tree.highlightedDeletedNode = null;

                if (rnd.nextInt(20) == 0) {
                    time_interval = (int)Math.max(5, time_interval * 0.7);
                    System.out.println(time_interval);
                }

                if (rnd.nextInt(2) ==  0) {
                    // add
                    // get random value that is not in the tree
                    int random_value = -1;
                    int attemps = 0;
                    while (attemps < 1000 || random_value == -1) {
                        attemps += 1;
                        random_value = 1 + rnd.nextInt(number_of_random_values * 2);
                        if (!tree.hasValue(random_value)) break;
                    }
                    //System.out.println("add " + random_value + " (" + tree.hasValue(random_value) + ")");

                    if (!tree.hasValue(random_value)) {
                        tree.addValue(random_value);
                        valueList.add(random_value);
                        tree.highlightedAddedNode = tree.searchNode(random_value);
                    }


                } else if (!valueList.isEmpty()) {
                    // delete
                    // get random value that is in the tree
                    if (random_delete_value > 0) {
                        tree.deleteValue(random_delete_value);
                        valueList.remove((Integer) random_delete_value);
                    }

                    if (!valueList.isEmpty()) {
                        random_delete_value = valueList.get(rnd.nextInt(valueList.size()));
                        tree.highlightedDeletedNode = tree.searchNode(random_delete_value);
                    }
                }

//                tree.mycheck(tree.root);
//                tree.mycheck2(tree.root);
//                tree.mycheck3(tree.root);

                SwingUtilities.invokeLater(visualizer::refresh); // Refresh the tree
                Thread.sleep(time_interval);  // Pause to simulate real-time updates
            }
        }

    }
}



/* ADD THIS TO "TwoFourTree.java" to debug
public void mycheck(TwoFourTreeItem node) {
        if (node == null) return;
        if (node.keys.size() + 1 != node.children.size()) {
            System.out.println("ERROR (" + node.keys.size() + "/" + node.children.size() + ") NODE \"" + node.keys.toString() + "\"");
        }
        for (TwoFourTreeItem child : node.children) {
            mycheck(child);
        }
    }

    public void mycheck2(TwoFourTreeItem node) {
        if (node == null) return;
        for (int i = 0; i < node.children.size(); i++) {
            TwoFourTreeItem child = node.children.get(i);
            if (child == null) continue;
            for (int j = 0; j < child.keys.size(); j++) {
                int key = child.keys.get(j);
                if (i < node.keys.size() && key > node.keys.get(i)) {
                    System.out.println("ERROR VALUES. NODE " + node.keys.toString());
                    return;
                } else if (i == node.children.size() - 1 && key < node.keys.getLast()) {
                    System.out.println("ERROR VALUES! NODE " + node.keys.toString());
                    return;
                }
            }
            mycheck2(child);
        }
    }

    public void mycheck3(TwoFourTreeItem node) {
        if (node == null) return;
        for (int i = 0; i < node.children.size(); i++) {
            TwoFourTreeItem child = node.children.get(i);
            if (child == null) continue;
            if (child.parent != node) {
                System.out.println("ERROR BAD PARENT FOR NODE \"" + node.keys.toString() + "\": " + child.keys.toString() + " _> " + (child.parent == null ? "null" : child.parent.keys.toString()));
                return;
            }
            mycheck3(child);
        }
    }

 */