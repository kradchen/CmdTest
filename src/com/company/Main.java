package com.company;


import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) {
//        try {
//            ThreadStateTester.threadStateTest();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        switchFunc();
    }
    private static void switchFunc()
    {
        for (int i = 0; i< 3 ; i++) {
            switch (i) {
                case 0:
                    System.out.println("case 0");
                    continue;//直接会跳到for循环
                case 1:
                    System.out.println("case 1");
                    break;//跳出switch
                case 2:
                    System.out.println("case 2");//没有break直接往下执行
                case 3://本循环中i到不了3
                    System.out.println("case 3");
            }
            System.out.println("switch is end!");
        }
    }
    static public  class Solution {
        ArrayList<Integer> deepCopy(ArrayList<Integer> olist)
        {
            ArrayList<Integer> nlist =  new ArrayList<Integer>();
            for(Integer t : olist)
            {
                nlist.add(t.intValue());
            }
            return nlist;
        }
        public boolean exist(char[][] board, String word) {
            // write your code here
            char[] wc = word.toCharArray();
            List<Point> pary = getStartPostion(board, wc[0]);
            for (Point p : pary) {
                if (CheckW(board, wc, 1, p)) return true;
            }
            return false;
        }

        char[][] Copy(char[][] board) {
            char[][] newary = new char[board.length][board[0].length];
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    newary[i][j] = board[i][j];
                }
            }
            return newary;
        }

        List<Point> getStartPostion(char[][] board, char key) {
            List<Point> pointAry = new ArrayList<>();
            int i, j;
            for (i = 0; i < board.length; i++) {
                for (j = 0; j < board[0].length; j++) {
                    if (board[i][j] == key) pointAry.add(new Point(i, j));
                }
            }
            return pointAry;

        }

        boolean CheckW(char[][] board, char[] word, int index, Point p) {
            char[][] newboard =Copy(board);
            newboard[p.X][p.Y] = ' ';
            printCharAry(board);
            if (index == (word.length)) return true;
            boolean Result= false;
            if (p.X != 0 && newboard[p.X - 1][p.Y] == word[index])
                Result = CheckW(newboard, word, index+1, new Point(p.X - 1, p.Y));
            if (p.X != (newboard.length - 1) && newboard[p.X + 1][p.Y] == word[index])
                Result= Result || CheckW(newboard, word, index+1, new Point(p.X + 1, p.Y));
            if (p.Y != 0 && newboard[p.X][p.Y - 1] == word[index])
                Result = Result ||CheckW(newboard, word, index+1, new Point(p.X, p.Y - 1));
            if (p.Y != (newboard[0].length - 1) && newboard[p.X][p.Y + 1] == word[index])
                Result = Result || CheckW(newboard, word, index+1, new Point(p.X, p.Y + 1));
            return Result;
        }

        void printCharAry(char[][] board)
        {
            for (int i = 0;i<board.length; i++)
            {
                for ( int j= 0; j< board[0].length; j++)
                {
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
        }
        static class Point {
            int X, Y;

            Point(int val1, int val2) {
                X = val1;
                Y = val2;
            }
        }
    }
    static public class Solution2{
        public List<List<Integer>> combine(int n, int k) {
            // write your code here
            _k=k;
            List<Integer> list = new ArrayList<Integer>(k);
            getList(n,k,1,list);
            return ll;
        }
        int _k;
        List<List<Integer>> ll= new LinkedList<>();
        public void getList(int n,int k, int begin,List<Integer> olist)
        {
            if(k == 0) ll.add(deepCopy(olist));
            for(int i = begin; i <= n - k + 1 ; i++)
            {
                olist.add(i);
                getList(n, k-1, i+1, olist);
                olist.remove(_k-k);
            }
        }
        List<Integer> deepCopy(List<Integer> olist)
        {
            ArrayList<Integer> nlist =  new ArrayList<Integer>();
            for(Integer t : olist)
            {
                nlist.add(t.intValue());
            }
            return nlist;
        }
    }
    static public class Solution3{
        int[] quickSort(int[] nums,int begin,int end)
        {
            if(end>=begin) {
                int j = Patition(nums, begin, end);
                quickSort(nums, begin, j-1 );
                quickSort(nums, j+1 , end);
            }
            return nums;
        }

        static void me(){};

        private int Patition(int[] nums, int begin, int end) {
            int compareKey=nums[begin];
            int j=end,i=begin+1;
            while (j>i)
            {
                if(nums[j]>compareKey){ j-- ;break;}
                if(nums[i]<compareKey){ i++; break;}
                if(nums[i]>compareKey && nums[j]<compareKey)
                {
                    swap(nums,j,i);
                    j--;
                    i++;
                }
            }
            if(nums[begin]>nums[j]) swap(nums,begin,j);
            return j;
        }

        private void swap(int[] nums, int j, int i) {
            int temp=nums[j];
            nums[j]=nums[i];
            nums[i]=temp;
        }
    }
    static public class Solution4 {
        /*
         * @param board: board a 2D board containing 'X' and 'O'
         * @return: nothing
         */
        public void surroundedRegions(char[][] board) {
            // write your code here
            setX(board);
        }
        void setX(char[][] board)
        {
            int X = board.length;
            if(X<3) return;
            int Y = board[0].length;
            if(Y<3) return;
            board2=board;
            for(int i=1; i< X-1; i++)
            {
                for(int j=1; j<Y-1;j++)
                {
                    if(board[i][j]=='O' && !Node.oList.contains(new Node(i,j)))//有O，开始寻找周边的O
                    {
                        Node node=new Node(i,j);
                        Node.nodeList.add(node);
                        node.findAroundNodeCycleWithStack();
                        if(!Node.checkBoder()) {Node.setNodeX(board);}
                        else {
                            Node.oList.addAll(Node.nodeList);
                            Node.nodeList.clear();
                        }
                    }
                }
            }
        }
        static char[][] board2;
        static public class Node
        {
            static List<Node> nodeList = new ArrayList<>();
            static List<Node> oList = new LinkedList<>();
            static Boolean checkBoder()
            {
                for(Node n : nodeList)
                {
                    if(n.X==0||n.Y==0
                            ||n.X==board2.length-1
                            ||n.Y==board2[0].length-1)
                        return true;
                }
                return false;
            }
            int X,Y;

            Node Left = null;
            Node Right =null;
            Node Up=null;
            Node Down=null;

            Node(int x,int y)
            {
                X=x;
                Y=y;
            }

            void findAroundNode()
            {
                if(Left==null && X>0&& board2[X-1][Y]=='O')
                {
                    Left = new Node(X-1,Y);
                    Left.Right= this;
                    if(!nodeList.contains(Left))
                    {
                        nodeList.add(Left);
                        Left.findAroundNode();
                    }
                }
                if(Right==null && X<board2.length-1 && board2[X+1][Y]=='O')
                {
                    Right = new Node(X+1,Y);
                    Right.Left = this;
                    if(!nodeList.contains(Right))
                    {
                        nodeList.add(Right);
                        Right.findAroundNode();
                    }
                }
                if(Up==null && Y>0  && board2[X][Y-1]=='O')
                {
                    Up = new Node(X,Y-1);
                    Up.Down =this;
                    if(!nodeList.contains(Up))
                    {
                        nodeList.add(Up);
                        Up.findAroundNode();
                    }
                }
                if(Down==null && Y<board2[0].length-1 && board2[X][Y+1]=='O')
                {
                    Down = new Node(X,Y+1);
                    Down.Up = this;
                    if(!nodeList.contains(Down))
                    {
                        nodeList.add(Down);
                        Down.findAroundNode();
                    }
                }
            }
            void findAroundNodeCycleWithStack()
            {
                Stack<Node> stack = new Stack<>();
                stack.push(this);
                while(!stack.isEmpty())
                {
                    Node current= stack.pop();
                    if(current.Left==null && current.X>0&& board2[current.X-1][current.Y]=='O')
                    {
                        current.Left = new Node(current.X-1,current.Y);
                        current.Left.Right= current;
                        if(!nodeList.contains(current.Left))
                        {
                            nodeList.add(current.Left);
                            stack.push(current.Left);
                        }
                    }
                    if(current.Right==null && current.X<board2.length-1 && board2[current.X+1][current.Y]=='O')
                    {
                        current.Right = new Node(current.X+1,current.Y);
                        current.Right.Left = current;
                        if(!nodeList.contains(current.Right))
                        {
                            nodeList.add(current.Right);
                            stack.push(current.Right);
                        }
                    }
                    if(current.Up==null && current.Y>0  && board2[current.X][current.Y-1]=='O')
                    {
                        current.Up = new Node(current.X,current.Y-1);
                        current.Up.Down =current;
                        if(!nodeList.contains(current.Up))
                        {
                            nodeList.add(current.Up);
                            stack.push(current.Up);
                        }
                    }
                    if(current.Down==null && current.Y<board2[0].length-1 && board2[current.X][current.Y+1]=='O')
                    {
                        current.Down = new Node(current.X,current.Y+1);
                        current.Down.Up = current;
                        if(!nodeList.contains(current.Down))
                        {
                            nodeList.add(current.Down);
                            stack.push(current.Down);
                        }
                    }
                }
            }
            static void setNodeX(char[][] board)
            {
                for(Node n : nodeList)
                {
                    board[n.X][n.Y]='X';
                }
                nodeList.clear();
            }
            @Override
            public boolean equals(Object obj) {
                Node nnode =(Node)obj;
                return nnode.X==this.X && nnode.Y==this.Y;
            }
        }
    }
}
