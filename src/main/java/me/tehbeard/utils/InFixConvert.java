package me.tehbeard.utils;

import java.util.Stack;

public class InFixConvert {

    public static String[] ConvertInfixToPostfix(char[] infix)
    {
        int length = infix.length;
        Stack<Character> stack = new Stack<Character>();
        StringBuilder postfix = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            if ((infix[i] >= '0') && (infix[i] <= '9'))
            {
                postfix.append(infix[i]);
            }
            else if (infix[i] == '(')
            {
                stack.push(infix[i]);
            }
            else if ((infix[i] == '*') || (infix[i] == '+') || (infix[i] == '-') || (infix[i] == '/'))
            {
                while ((stack.size() > 0) && (stack.peek() != '('))
                {
                    if (ComparePrecedence(stack.peek(), infix[i]))
                    {
                        postfix.append(',');
                        postfix.append(stack.pop());
                    }
                    else
                    {
                        break;
                    }
                }
                postfix.append(',');
                stack.push(infix[i]);
            }
            else if (infix[i] == ')')
            {
                while ((stack.size() > 0) && (stack.peek() != '('))
                {
                    postfix.append(',');
                    postfix.append(stack.pop());
                }
                if (stack.size() > 0)
                    stack.pop(); // popping out the left brace '('
            }
            else
            {
                postfix.append(infix[i]);
            }
        }
        while (stack.size() > 0)
        {
            postfix.append(',');
            postfix.append(stack.pop());
        }
        return postfix.toString().split("\\,");
    }

    private static boolean ComparePrecedence(char top, char p_2)
    {
        if (top == '+' && p_2 == '*') // + has lower precedence than *
            return false;

        if (top == '*' && p_2 == '-') // * has higher precedence over -
            return true;

        if (top == '+' && p_2 == '-') // + has same precedence over +
            return true;

        return true;
    }


    private static int ApplyOperator(int p, int p_2, char p_3)
    {
        switch (p_3)
        {
        case '+':
            return p_2 + p;
        case '-':
            return p_2 - p;
        case '*':
            return p_2 * p;
        case '/':
            return p_2 / p;
        default:
            return -1;
        }
    }

    public static void main(String[] args){
        System.out.println(ConvertInfixToPostfix("stats.playedfor+stats.lastlogin-kills.total".toCharArray()));
    }
}
