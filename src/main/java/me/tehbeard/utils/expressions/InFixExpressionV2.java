package me.tehbeard.utils.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


import me.tehbeard.utils.expressions.functions.FunctionProvider;
/**
 * infix expression parser
 * Supported operators: + - / * ^
 * Variables ($ prefixed)
 * Functions (@ prefixed)
 * @author James
 *
 */
public class InFixExpressionV2 {


    private ArrayList<String> rules;

    /**
     * construct a new expression
     * @param expr expression to construct
     */
    public InFixExpressionV2(String expr){
        process(toTokens(expr));
    }


    /**
     * Tokenize input string
     * @param expr
     * @return
     */
    private static String[] toTokens(String expr){
        String w = new String(expr);
        w = w.replaceAll(" ","");
        w = w.replaceAll("\\+","\\;\\+\\;");
        w = w.replaceAll("\\-","\\;\\-\\;");
        w = w.replaceAll("\\*","\\;\\*\\;");
        w = w.replaceAll("\\/","\\;\\/\\;");
        w = w.replaceAll("\\^","\\;\\^\\;");
        w = w.replaceAll("\\,","\\;\\,\\;");
        w = w.replaceAll("\\(","\\;\\(\\;");
        w = w.replaceAll("\\)","\\;\\)\\;");
        w = w.replaceAll("\\;\\;","\\;");

        return w.split("\\;");       
    }


    /**
     * Process tokens into expression
     * @param tokens
     */
    private void process(String[] tokens){
        Stack<String> stack = new Stack<String>();
        rules = new ArrayList<String>();

        int parameterCount = 1;
        Stack<Integer> paramCountStack = new Stack<Integer>();

        for(String token : tokens){
            //if variable or token add to the pile
            if(isNumber(token) || isVariable(token)){
                rules.add(token);
            }else if(isFunction(token)|| token.charAt(0) =='('){
                if(isFunction(token)){
                    paramCountStack.push(parameterCount);
                    parameterCount = 1;
                }
                stack.push(token);

            }else if(token.equals(",")){
                parameterCount++;
                //if it's a separator
                while ((stack.size() > 0) && (stack.peek().charAt(0) != '('))
                {
                    rules.add(stack.pop());
                }



            }else if(isOperator(token.charAt(0)) ){
                while ((stack.size() > 0) && (stack.peek().charAt(0) != '('))
                {
                    if (comparePrecedence(stack.peek().charAt(0), token.charAt(0)))
                    {
                        rules.add(stack.pop());
                    }
                    else
                    {
                        break;
                    }
                }
                stack.push(token);

            }else if(token.equals(")")){
                while ((stack.size() > 0) && (stack.peek().charAt(0) != '('))
                {
                    rules.add(stack.pop());
                }

                if (stack.size() > 0){
                    stack.pop(); // popping out the left brace '('
                    if(isFunction(stack.peek())){
                        rules.add("" + parameterCount);
                        if(paramCountStack.size()>0){
                            parameterCount=  paramCountStack.pop();  
                        }else{
                            parameterCount=1;
                        }
                        rules.add(stack.pop());
                    }
                }
            }
        }
        while (stack.size() > 0)
        {
            rules.add(stack.pop());
        }

    }

    private boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    private boolean isOperator(char c){
        return (c=='+' || c=='-' || c=='*' || c=='/' || c=='^');
    }

    private boolean isVariable(String s){
        return s.startsWith("$");
    }

    private boolean isFunction(String s){
        return s.startsWith("@");
    }

    private static boolean comparePrecedence(char top, char p_2)
    {
        return getOpVal(top) > getOpVal(p_2);
    }

    private static int getOpVal(char op){
        switch(op){

        case '(': return 4;
        case '^': return 3;
        case '*':
        case '/': return 2;

        case '+':
        case '-': return 1;
        default : return 0;
        }
    }


    /**
     * resolve the value of the expression
     * @param varProvider variable resolver to use
     * @param funcProvider function resolver to use
     * @return
     */
    public int getValue(VariableProvider varProvider,FunctionProvider funcProvider){
        Stack<Integer> stack = new Stack<Integer>();  

        Iterator<String> it = rules.iterator();
        while(it.hasNext()){
            String exp = it.next();
            int i = 0;
            switch(exp.charAt(0)){
            case '+':i = stack.pop(); stack.push(stack.pop() + i);break;
            case '-':i = stack.pop(); stack.push(stack.pop() - i);break;
            case '*':i = stack.pop(); stack.push(stack.pop() * i);break;
            case '/':i = stack.pop(); stack.push(stack.pop() / i);break;
            case '^':i = stack.pop(); stack.push((int) Math.pow(stack.pop() , i));break;

            case '$':
                String var = exp.substring(1);
                if(varProvider==null){
                    stack.push(0);
                }else{
                    stack.push(varProvider.resolveVariable(var));    
                } 
                ;break;
            case '@':{
                String func = exp.substring(1);
                int pc = stack.pop();
                if(stack.size() < pc){throw new IllegalStateException("Invalid number of parameters left on stack");}
                int[] params = new int[pc];
                for(int ii =0;ii<pc;ii++){
                    params[ii] = stack.pop();
                }
                try {
                    stack.push(funcProvider.resolveFunction(func, params));
                } catch (UnresolvedFunctionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return 0;
                }
                break;
            }

            default:stack.push(Integer.parseInt(exp));break;
            }
        }
        if(stack.size() == 0){throw new IllegalArgumentException("Expression resulted in a empty stack!");}
        return stack.pop();
    }



    

    public static void l(String n){
        //System.out.println(n);
    }

}
