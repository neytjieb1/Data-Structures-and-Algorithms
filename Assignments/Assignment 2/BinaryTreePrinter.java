import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BinaryTreePrinter {

    public static <T extends Comparable<? super T>> void printNode(ThreadedAVLNode<T> root) {
        int maxLevel = BinaryTreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<? super T>> void printNodeInternal(List<ThreadedAVLNode<T>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || BinaryTreePrinter.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BinaryTreePrinter.printWhitespaces(firstSpaces);

        List<ThreadedAVLNode<T>> newNodes = new ArrayList<ThreadedAVLNode<T>>();
        for (ThreadedAVLNode<T> node : nodes) {
            if (node != null) {
                System.out.print(node.data);
                newNodes.add(node.left);

                if(!node.hasThread){//augmented
                    newNodes.add(node.right);
                }
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BinaryTreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BinaryTreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BinaryTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).left != null)
                    System.out.print("/");
                else
                    BinaryTreePrinter.printWhitespaces(1);

                BinaryTreePrinter.printWhitespaces(i + i - 1);

                if (!nodes.get(j).hasThread && nodes.get(j).right != null)//augmented
                    System.out.print("\\");
                else
                    BinaryTreePrinter.printWhitespaces(1);

                BinaryTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<? super T>> int maxLevel(ThreadedAVLNode<T> node) {
        if (node == null)
            return 0;

        int rightmaxLevel = (!node.hasThread) ? BinaryTreePrinter.maxLevel(node.right) : 0;//augmented

        return Math.max(BinaryTreePrinter.maxLevel(node.left), rightmaxLevel) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }

}