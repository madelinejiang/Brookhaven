/*
 */

/**
 *
 * @author student
 */
public class tree {

    private abstract class Node {

        abstract float calc();

    }

    private abstract class BinaryNode extends Node {

        Node left, right;

    }
    Node oldroot;
    public void save(){
        oldroot=root;
       
    }
    public void restore(){
        root=oldroot;
    }

    Node root;

    char ch;

    int pos;

    String eqn;

    private char getChar() {

        if (pos < eqn.length()) {
            return ch = eqn.charAt(pos++);
        }

        return ch = '\0';

    }

    void parse(String eqn) {

        this.eqn = eqn;

        pos = 0;

        getChar();

        root = findExpr();

    }

    float x;

    float calc(float x) {

        this.x = x;

        return root.calc();

    }

    Node findExpr() {

        Node tmp = findProd();

        while (ch == '+' || ch == '-') {

            BinaryNode tmproot;
            if (ch == '+') {
                tmproot = new BinaryNode() {

                    float calc() {

                        return left.calc() + right.calc();

                    }
                };
            } else {
                tmproot = new BinaryNode() {

                    float calc() {

                        return left.calc() - right.calc();
                    }

                };
            }
            getChar();
            tmproot.left = tmp;
            tmproot.right = findProd();
            tmp = tmproot;
        }
        return tmp;
    }

    Node findProd() {
        Node tmproot = findTerm();
        while (ch == '*' || ch == '/') {
            BinaryNode tmp;
            if (ch == '*') {
                tmp = new BinaryNode() {
                    float calc() {
                        return left.calc() * right.calc();
                    }
                };
            } else {
                tmp = new BinaryNode() {
                    float calc() {
                        return left.calc() / right.calc();
                    }
                };
            }
            getChar();
            tmp.left = tmproot;
            tmp.right = findTerm();
            tmproot = tmp;
        }
        return tmproot;
    }

    Node findTerm() {
        if (ch == 'x') {
            Node tmp = new Node() {
                float calc() {
                    return x;
                }
            };
            getChar();
            return tmp;
        }
        if (ch >= '0' && ch <= '9') {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(ch);
            } while (getChar() >= '0' && ch <= '9');
            
            if(ch=='.'){
                do{
                sb.append(ch);
                }while(getChar()>='0'&&ch<='9');
            }
            float z = Float.parseFloat(sb.toString());
            Node tmp = new Node() {
                float calc() {
                    return z;
                }
            };
            return tmp;
        
        }
        
        if(ch=='('){
            getChar();//move to the next character
            Node tmp=this.findExpr();
            getChar();
            return tmp;
        }
        if(ch=='s'&&eqn.substring(pos).startsWith("in(")){
            pos+=2;
            getChar();
            BinaryNode tmp=new BinaryNode(){
                public float calc(){
                    return (float)Math.sin(right.calc());
                }
            };
           tmp.right=findExpr();
           getChar();
           return tmp;
        }
        else if(ch=='c'&&eqn.substring(pos).startsWith("os(")){
            pos+=2;
            getChar();
            BinaryNode tmp=new BinaryNode(){
                public float calc(){
                    return (float)Math.cos(right.calc());
                }
            };
           tmp.right=findExpr();
           getChar();
           return tmp;
        }
        else if(ch=='t'&&eqn.substring(pos).startsWith("an(")){
            pos+=2;
            getChar();
            BinaryNode tmp=new BinaryNode(){
                public float calc(){
                    return (float)Math.tan(right.calc());
                }
            };
           tmp.right=findExpr();
           getChar();
           return tmp;
        }
       if(ch=='-'){
        getChar();
        BinaryNode tmp=new BinaryNode(){
            public float calc(){
                return -right.calc();
            }
        };
        tmp.right=findTerm();
        return tmp;
}   
        return null;
    }
}
