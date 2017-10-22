import java.util.Stack;
import java.lang.Math;

/**
 * Interpreter interprets strings and tries to make sense out of them. Basically
 * it takes strings (from Main) and determines if they're valid. If they are,
 * the calculation can occur. Otherwise an Error message is returned to the
 * display.
 *
 * @author Michael Ranciglio
 */
public class Interpreter
{
	double result;

	public static final double e = Math.E; //e should be lowercase if you ask me

	/**
	 * interpret is the function that's called from Main. From here everything
	 * else, all the other functions, are called and chained. This is the source
	 * of those calls.
	 *
	 * Once checkString is done, interpret converts the string into post fix (a
	 * much easier representation of math for computers to understand, compared
	 * to infix.)
	 *
	 * Once the post fix conversion is complete with no issues, the actual
	 * calculatiosn are done. If nothing goes wrong, the result gets returned
	 * back to the display and we do it all again.
	 *
	 * @author Michael Ranciglio
	 */
	public String interpret(String str)
	{
		try
		{
			Stack<Character> stack = new Stack<Character>();
			String postFix = new String();

			str = checkString(str);

			if(str.charAt(0) == 'E') //if first char is E then it's an error
				return str; //return the error message

			System.out.print("str after check in interpret: ");
			System.out.println(str);

			int toPushPriority, stackPriority;

			for(int i = 0; i < str.length(); i++)
			{
				switch(str.charAt(i))
				{ //to postfix
					case '-':
						if(i > 0 && str.charAt(i-1) == ' ' && i != str.length() && str.charAt(i+1) == ' ')
						{
							//do nothing
						}
						else
						{
							postFix += '-';
							break;
						}
					case '+':
					case '*':
					case '/':
					case '%':
						postFix += " "; //add space to make the numbers separate
						toPushPriority = setPriority(str.charAt(i));

						if(stack.empty())
							stack.push(str.charAt(i));
						else
						{ //begin initial else (not empty)
							stackPriority = setPriority(stack.peek());
							if(toPushPriority >= stackPriority)
								stack.push(str.charAt(i));
							else
							{ //begin inner else (not empty and lower priority)
								while(stackPriority > toPushPriority && !stack.empty())
								{
									postFix += stack.peek() + " ";
									stack.pop();

									if(!stack.empty())
										stackPriority = setPriority(stack.peek());
								}

								stack.push(str.charAt(i));
							} //end inner else (not empty and lower priority)
						} //end initial else (not empty)

						break;
					case '(':
						stack.push('(');
						break;
					case ')':
						while(stack.peek() != '(')
						{
							postFix += " " + stack.peek();
							stack.pop();
						}

						stack.pop(); //pop the '('
						break;
					case ' ':
						break;
					default:
						postFix += str.charAt(i);
				}
			}

			while(!stack.empty())
			{
				postFix += " " + stack.peek();
				stack.pop();
			}

			//calculate
			String num = new String("");
			Stack<Double> dStack = new Stack<Double>();
			double first, second;

			for(int i = 0; i < postFix.length(); i++)
			{
				switch(postFix.charAt(i))
				{
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					case '0':
					case '.':
						num += postFix.charAt(i);
						break;
					case ' ':
						if(num != null && !num.equals(""))
						{
							dStack.push(Double.parseDouble(num));
							num = "";
						}
						break;
					case '+':
						second = dStack.peek();
						dStack.pop();
						first = dStack.peek();
						dStack.pop();
						dStack.push(first + second);
						break;
					case '-':
						if(i > 0 && str.charAt(i-1) == ' ' && i != str.length() && str.charAt(i+1) == ' ')
						{
							//do nothing
						}
						else
						{
							num += '-';
							break;
						}
						second = dStack.peek();
						dStack.pop();
						first = dStack.peek();
						dStack.pop();
						dStack.push(first - second);
						break;
					case '/':
						second = dStack.peek();
						dStack.pop();
						first = dStack.peek();
						dStack.pop();
						if(second == 0)
							return "Error: Cannot divide by zero.";
						dStack.push(first / second);
						break;
					case '*':
						second = dStack.peek();
						dStack.pop();
						first = dStack.peek();
						dStack.pop();
						dStack.push(first * second);
						break;
					case '%':
						second = dStack.peek();
						dStack.pop();
						first = dStack.peek();
						dStack.pop();
						dStack.push(first % second);
						break;
					default:
						System.out.println("Reached default in the calculation stage.");
				}
			}

			System.out.println(postFix);

			if(!dStack.empty())
				return Double.toString(dStack.peek());
			else //if there's no operators (only entered one number)
				return postFix;
		}
		catch(Exception e)
		{
			return "Error: Unknown.";
		}
	}

	/**
	 * checkString does exactly what it sounds like: it checks the string and
	 * ensures it is valid. It also takes care of the more advanced calculations
	 * (like sin and cos) and changes the string accordingly so the post fix
	 * calculations can go smoothly.
	 *
	 * Once checkString is done, it passes all sorts of information to interpret
	 * and interpret decides what to do with it from there.
	 *
	 * @author Michael Ranciglio
	 */
	public String checkString(String str)
	{ //to check if the string follows all the syntax rules
		try
		{
			//flags
			boolean operatorCheck = false;
			boolean dotCheck = false;
			int lasti;

			if(!checkParenthesis(str))
				return "Error: Parenthesis unbalanced.";

			char c = str.charAt(0); //readability
			if(c == '+' || c == '-' || c == '*' || c == '/') //first char is an operator
				operatorCheck = true;
			//System.out.println("First character is operator confirmed.");
			//still not sure what I'd do here yet

			final char sqr = (char)8730;

			for(int i = 0; i < str.length(); i++)
			{
				System.out.println(i);

				switch(str.charAt(i))
				{
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
					case '0':
						operatorCheck = false;
						break;
					case ')':
						if(i != str.length()-1 && Character.isDigit(str.charAt(i+1)))
						{
							//insert *
							str = new StringBuilder(str).insert(i+1, " * ").toString();
							operatorCheck = false;
						}
						if(i > 0 && str.charAt(i-1) == ' ')
							System.out.println("Error: Parenthesis cannot end with a space.");
						break;
					case '(':
						if(i > 0 && Character.isDigit(str.charAt(i-1)))
						{
							//insert *
							str = new StringBuilder(str).insert(i, " * ").toString();
							operatorCheck = true;
							i++;
						}
						if(i != str.length()-1 && str.charAt(i+1) == ' ')
							System.out.println("Error: Parenthesis cannot begin with a space.");
						break;
					case 'π':
						System.out.print("Str before parse: ");
						System.out.println(str.substring(0, i));
						System.out.print("Str after parse: ");
						System.out.println(str.substring(i+1));

						if(i == 0)
							str = Double.toString(Math.PI) + str.substring(i+1);
						else
							str = str.substring(0, i) + Double.toString(Math.PI) + str.substring(i+1);
						System.out.print("Str after pi: ");
						System.out.println(str);
						break;
					case 'e':
						if(i == 0)
							str = Double.toString(Math.E) + str.substring(i+1);
						else
							str = str.substring(0, i) + Double.toString(Math.E) + str.substring(i+1);
						System.out.print(str);
						break;
					case '+':
					case '-':
					case '*':
					case '/':
					case '%':
						if(operatorCheck)
							return "Error: Operators cannot operate on each other.";

						dotCheck = false;
						operatorCheck = true;
						break;
					case '.':
						if(dotCheck)
							return "Error: Decimal cannot have more than one period.";
						dotCheck = true;
						break;
					case 'f':
					case 'l':
					case 's':
					case 'c':
					case 't':
					case sqr: //have them all do the following * insertion
						if(i > 0 && Character.isDigit(str.charAt(i-1)))
						{
							//insert *
							str = new StringBuilder(str).insert(i, " * ").toString();
							operatorCheck = true;
							i++;
						}

						switch(str.charAt(i))
						{
							case 'f':
								str = calcFunc(str, "fact(", i);
								break;
							case 'l':
								switch(str.charAt(i+1))
								{
									case 'o':
										str = calcFunc(str, "log(", i);
										break;
									case 'n':
										str = calcFunc(str, "ln(", i);
										break;
									default:
										return "Error: Unknown.";
								}
								break;
							case 's':
								str = calcFunc(str, "sin(", i);
								break;
							case 'c':
								str = calcFunc(str, "cos(", i);
								break;
							case 't':
								str = calcFunc(str, "tan(", i);
								break;
							case sqr:
								str = calcFunc(str, "q(", i);
								break;
						}
						break;
					case '^': //write the entire exp handle here
						if(operatorCheck)
							return "Error: Cannot have ^ after an operator.";

						if(i != str.length()-1 && !Character.isDigit(str.charAt(i+1)) && str.charAt(i+1) != '(')
							return "Error: Must have number after ^ operator.";

						if(i == 0)
							return "Error: First item cannot be ^ operator.";

						//checking before the ^, or the number to be raised
						double ansF, ansL;
						int firsti;

						if(i > 0 && str.charAt(i-1) == ')')
						{
							firsti = beginOfFunc(str, i-2);
							System.out.print("firsti: ");
							System.out.println(firsti);

							System.out.print("String to be parsed: ");
							System.out.println(str.substring(firsti, i));
							ansF = Double.parseDouble(calcThis(str, firsti, i));

							System.out.print("ansF: ");
							System.out.println(ansF);
						}
						else
						{
							firsti = i-1;

							while((Character.isDigit(str.charAt(firsti)) || str.charAt(firsti) == '.') && firsti > 1)
							{
								if(dotCheck)
									return "Error: Decimal cannot have more than one period.";
								if(str.charAt(firsti) == '.')
									dotCheck = true;

								firsti--;
							}

							System.out.print("What the firsti points to: ");
							System.out.println(str.charAt(firsti));

							ansF = Double.parseDouble(str.substring(firsti, i));
							System.out.print("ansF: ");
							System.out.println(ansF);
						}

						dotCheck = false;

						//checking after the ^, or the number to be raised by
						if(i != str.length()-1 && str.charAt(i+1) == '(')
						{
							lasti = endOfFunc(str, i+2);
							//System.out.print("What the lasti points to: ");
							//System.out.println(str.charAt(lasti));

							System.out.print("String to be parsed: ");
							System.out.println(str.substring(i+1, lasti));
							ansL = Double.parseDouble(calcThis(str, i+1, lasti));
							System.out.print("ansL: ");
							System.out.println(ansL);
						}
						else
						{
							lasti = i+1;

							while((Character.isDigit(str.charAt(lasti)) || str.charAt(lasti) == '.') && lasti < str.length()-1)
							{
								if(dotCheck)
									return "Error: Decimal cannot have more than one period.";
								if(str.charAt(lasti) == '.')
									dotCheck = true;

								lasti++;
							}

							lasti += 1; //gonna be real here, don't know how this
										//prevents an error, but it does..

							ansL = Double.parseDouble(str.substring(i+1, lasti));
							System.out.print("ansL: ");
							System.out.println(ansL);
						}

						System.out.print("The actual power of ansF and ansL: ");
						System.out.println(Math.pow(ansF, ansL));

						String ans = new String();
						ans = Double.toString(Math.pow(ansF, ansL));
						System.out.print("ans: ");
						System.out.println(ans);

						if(ans.equals("Infinity"))
							return "Error: Divide by zero.";

						if(firsti <= 0)
						{
							System.out.print("String to be parsed: ");
							System.out.println(str.substring(lasti));
							str = ans + str.substring(lasti);
							i = 0;
						}
						else
						{
							System.out.print("String to be parsed: ");
							System.out.println(str.substring(0, firsti-1) + ans + str.substring(lasti));
							str = str.substring(0, firsti-1) + ans + str.substring(lasti);
							i = firsti;
						}

						System.out.println(str);

						break; //end exp (^) operator
					case ' ':
						break;
					case 'E':
						if(Character.isDigit(str.charAt(i))) //big number
							return "Error: Number too big/small.";
						else //error message, which ends in a '.'
						{
							int j = i;

							while(str.charAt(j) != '.')
								j++;

							return str.substring(i, j);
						}
					default: //if we got here somehow just return false
						System.out.println("Somehow you got to default in the checking function");
						return "Error: Unknown.";
				}
				System.out.println(str);
			}

			if(operatorCheck)
				return "Error: Ends in an operator.";

			System.out.print("str after check: ");
			System.out.println(str);

			return str;
		}
		catch(Exception e)
		{
			return "Error: Unkown.";
		}
	}

	/**
	 * checkParenthesis checks the parentheses with a stack to ensure the right
	 * amont of parenthesis were used. if it is successful it will pass a true,
	 * otherwise a false!
	 *
	 * @author Michael Ranciglio
	 */
	public boolean checkParenthesis(String str)
	{
		try
		{
			Stack<Character> stack = new Stack<Character>();

			for(int i = 0; i < str.length(); i++)
			{
				if(str.charAt(i) == '(')
					stack.push('(');
				else if(str.charAt(i) == ')')
				{
					if(stack.empty()) //if a closing parenthesis is w/o it's opening companion
						return false; //bad syntax, return false
					stack.pop();
				}
			}

			return(stack.empty()); //at this point, as long as the stack is empty we good
		}
		catch(Exception e)
		{
			return false;
		}
	}

	/**
	 * setPriority returns the priority for the given operator.
	 * + and - = 1,
	 * *, % and / = 2,
	 * all others = 0
	 *
	 * @author Michael Ranciglio
	 */
	public static int setPriority(char op)
	{
		switch(op)
		{
			case '+':
			case '-':
				return 1;
			case '*':
			case '/':
			case '%':
				return 2;
			default:
				System.out.println("Reached default in setPriority.");
				return 0;
		}
	}

	/**
	 * Returns the position of the end of the function.
	 *
	 * @author Michael Ranciglio
	 */
	public int endOfFunc(String str, int i)
	{
		try
		{
			Stack<Character> stack = new Stack<Character>();
			stack.push('(');

			while(!stack.empty())
			{
				if(str.charAt(i) == '(')
					stack.push('(');
				else if(str.charAt(i) == ')')
					stack.pop();
				i++;
			}

			return i;
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	/**
	 * Returns the position of the beginning of the function.
	 *
	 * @author Michael Ranciglio
	 */
	public int beginOfFunc(String str, int i)
	{
		try
		{
			Stack<Character> stack = new Stack<Character>();
			stack.push(')');

			while(!stack.empty())
			{
				if(str.charAt(i) == ')')
					stack.push(')');
				else if(str.charAt(i) == '(')
					stack.pop();
				i--;
			}

			return i+1; //goes to right before the func, +1 offsets that
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	/**
	 * calcThis attempts to calculate the substring of a given string with
	 * given start and end points.
	 *
	 * @author Michael Ranciglio
	 */
	public String calcThis(String str, int start, int end)
	{
		try
		{
			return interpret(str.substring(start, end));
		}
		catch(Exception e)
		{
			return "Error: Unknown.";
		}
	}

	/**
		For the gamma function

		@author Princeton's CS department
	*/
	static double logGamma(double x)
	{
		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		double ser = 1.0 + 76.18009173   / (x + 0) - 86.50532033   / (x + 1)
		                 + 24.01409822   / (x + 2) - 1.231739516   / (x + 3)
		                 + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);

		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	}

	/**
		For the fact function to give proper factorials in the case of decimals

		@author Princeton's CS department
	*/
	static double gamma(double x)
	{
		return Math.exp(logGamma(x));
	}

	/**
	 * fact gives the factorial of a number, which is that number * the
	 * gamma of that number.
	 *
	 * @author Michael Ranciglio
	*/
	public double fact(String str)
	{
		double n = Double.parseDouble(str);
		System.out.print("n = ");
		System.out.println(n);

		if(n <= 1)
			return 1;
		else
		{
			n *= gamma(n);

			return n;
		}
	}

	/**
	 * calcFunc takes the original string, a string representing the function,
	 * and the current index of the for loop. It then determines what function
	 * to perform based on the first letter and some other things too.
	 *
	 * @author Michael Ranciglio
	 */
	public String calcFunc(String str, String func, int i)
	{
		try
		{
			int lasti = endOfFunc(str, i+func.length());
			String newStr = calcThis(str, i+func.length()-1, lasti);
			double d;

			switch(func.charAt(0))
			{
				case 'l':
					switch(func.charAt(1))
					{
						case 'o':
							d = Math.log10(Double.parseDouble(newStr));
							break;
						case 'n':
							d = Math.log(Double.parseDouble(newStr));
							break;
						default:
							d = 0;
							break;
					}
					break;
				case 'f':
					d = fact(newStr);
					break;
				case 's':
					d = Math.sin(Double.parseDouble(newStr));
					break;
				case 'c':
					d = Math.cos(Double.parseDouble(newStr));
					break;
				case 't':
					d = Math.tan(Double.parseDouble(newStr));
					break;
				case 'q':
					d = Math.sqrt(Double.parseDouble(newStr));
					break;
				default:
					d = 0;
					break;
				}

			if(i == 0)
				str = Double.toString(d) + str.substring(lasti);
			else
				str = str.substring(0, i) + Double.toString(d) + str.substring(lasti);

			return str;
		}
		catch(Exception e)
		{
			return "Error: Unknown.";
		}
	}
}
