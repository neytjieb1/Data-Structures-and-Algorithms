public class Main
{
    public static void main(String[] args) throws Exception
    {
        ThreadedAVLTree<Integer> b=new ThreadedAVLTree();
        Integer [] array={10,20,30,40,50,25,35,45,70,100,46,37,67,51,90,34,23,24,68,928,19,15,77,88,12,4,8,9,7,5,3,1};
        for(int i=0;i<32;i++){
            b.insert(array[i]);
            System.out.println("InOrder after insertion of "+array[i]+"\n----------------------------------------------------------------------\n"+b.inorder());
            System.out.println("");
            System.out.println("InOrderDetailed after insertion of "+array[i]+"\n----------------------------------------------------------------------\n"+b.inorderDetailed());
            System.out.println("");
            System.out.println("PreOrder after insertion of "+array[i]+"\n----------------------------------------------------------------------\n"+b.preorder());
            System.out.println("");
            System.out.println("PreOrderDetailed after insertion of "+array[i]+"\n----------------------------------------------------------------------\n"+b.preorderDetailed());
            System.out.println("");
		/*System.out.println("The new OrderDetailed after insertion of "+array[i]+"\n----------------------------------------------------------------------\n"+b.newOrder());
		System.out.println("");*/
        }
        System.out.println(b.inorderDetailed());
        for(int i=0;i<32;i++){
            if (array[i]==928) {
                BinaryTreePrinter p = new BinaryTreePrinter();
                p.printNode(b.getRoot());
                System.out.println(i);
            }
            b.delete(array[i]);

            System.out.println("InOrder after deletion of "+array[i]+" num: " + b.getNumberOfNodes()+ "\n----------------------------------------------------------------------\n"+b.inorder());
            System.out.println("");
            System.out.println("InOrderDetailed after deletion of "+array[i]+" num: " + b.getNumberOfNodes()+ "\n----------------------------------------------------------------------\n"+b.inorderDetailed());
            System.out.println("");

            System.out.println("PreOrder after deletion of "+array[i]+" num: " + b.getNumberOfNodes()+ "\n----------------------------------------------------------------------\n"+b.preorder());
            System.out.println("");
            System.out.println("PreOrderDetailed after deletion of "+array[i]+" num: " + b.getNumberOfNodes()+ "\n----------------------------------------------------------------------\n"+b.preorderDetailed());
            System.out.println("");
		/*System.out.println("The new OrderDetailed after deletion of "+array[i]+"\n----------------------------------------------------------------------\n");
		b.newOrder();*/
            System.out.println("");
        }
	/*
      TODO: Write code to thoroughly test your implementation here.

      Note that this file will be overwritten for marking purposes.
      */
    }
}