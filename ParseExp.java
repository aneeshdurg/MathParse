import java.util.LinkedList;
class ParseExp
{
	private String exp;
	private String ops = "^*/+-";
	private int counter=0;
	private int numpointer=0;
	private int oppointer=0;
	private	LinkedList<Double> numbers = new LinkedList<Double>();
	private LinkedList<Character> operations = new LinkedList<Character>();
	public ParseExp(String exp) throws ParseException
	{
		if (left(exp)!=right(exp))
			throw new ParseException("Unmatched brackets!");
		exp = exp.replaceAll("\\s+", "").toLowerCase();
		this.exp = exp;
		parse();
	}
	private ParseExp(String exp, int counter) throws ParseException
	{
		if (left(exp)!=right(exp))
			throw new ParseException("Unmatched brackets!");		
		exp = exp.replaceAll("\\s+", "").toLowerCase();
		this.exp = exp;
		this.counter = counter;
		parse();
	}
	private void parse() throws ParseException
	{
		String exp = this.exp;
		int length = exp.length();
		double currentnum=0;
		boolean decimal=false;
		int decimalcount=0;
		boolean neg=false;
		while(exp.length()>0)
		{
			char now = exp.charAt(0);	
			char next;
			if (exp.length()>1) 
				next = exp.charAt(1);
			else
				next = 0;
			//normal number
			if (now<=57&&now>=48)
			{
				if (!decimal)
				{
					currentnum*=10;
					currentnum+=now-48;
				}
				else
				{
					currentnum+=(now-48)*Math.pow(10, 0-decimalcount);
					decimalcount++;
				}

				exp = exp.substring(1, exp.length());

				if (exp.length()==0)
				{
					if(neg)
					{
						currentnum=0-currentnum;
					}
					numbers.add(currentnum);
				}
				counter++;
			}
			//handles decimal numbers
			else if(now=='.')
			{
				decimal=true;
				decimalcount++;
				exp = exp.substring(1, exp.length());
			}
			//handles pi
			else if(now=='p')
			{
				counter++;
				if(next!='i')
					throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
				if(next<=57&&next>=48)
					throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
				currentnum = Math.PI;
				if (exp.equals("pi"))
					numbers.add(currentnum);
				exp = exp.substring(2, exp.length());
				counter++;
			}
			//Expression inside ()
			else if(now=='(')
			{
				//need to check for nested ) so that index of doesn't pick it up?
				if (exp.lastIndexOf(")")<1)
					throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
				if (exp.indexOf("-)")==1)
				{
					neg=true;
					counter+=3;
					exp = exp.substring(exp.lastIndexOf(")")+1, exp.length());
				}
				else
				{
					int closing=1;
					for(int i=1; i<exp.length(); i++)
					{
						if (exp.charAt(i)=='(') closing++;
						else if (exp.charAt(i)==')') closing--;
						if (closing==0) 
						{
							closing=i;
							i=exp.length();
						}
					}
					String subexp = exp.substring(exp.indexOf("(")+1, closing);
					ParseExp brackets = new ParseExp(subexp, counter);
					currentnum=brackets.evaluate();
					counter+=subexp.length()+2;
					exp = exp.substring(closing+1, exp.length());
					if (exp.length()==0)
						numbers.add(currentnum);
				}
			}
			//adds operators
			else if(ops.contains(now+""))
			{
				if(neg)
				{
					currentnum=0-currentnum;
					neg=false;
				}
				operations.add(now);
				numbers.add(currentnum);
				exp = exp.substring(1, exp.length());
				currentnum = 0;
				counter++;	
			}
			//Illegal characters
			if((next<48||next>57)&&!ops.contains(now+"")&&!ops.contains(next+"")&&next!='.'&&next!=0&&!(now=='p'&&next=='i'))
				throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
		}
		//Illegal operators
		if(numbers.size()!=operations.size()+1)
		{
			throw new ParseException("Operations ("+operations.size()+") and arguments ("+numbers.size()+") do not match! Please check and ensure that the expression is properly formatted!");	
		}
	}

	public double evaluate() throws ParseException
	{
		LinkedList<Double> numbers = new LinkedList<Double>();
		LinkedList<Character> operations = new LinkedList<Character>();
		for(int i=0; i<this.numbers.size()-1; i++)
		{
			numbers.add(this.numbers.get(i));
			operations.add(this.operations.get(i));
		}
		numbers.add(this.numbers.get(this.numbers.size()-1));
		//loops through operations in the order ^*/+-
		for(int o=0; o<ops.length(); o++)
		{
			while(operations.contains(ops.charAt(o)))
			{
				int i = operations.indexOf(ops.charAt(o));
				if (ops.charAt(o)=='^')
					numbers.add(i, Math.pow(numbers.get(i), numbers.get(i+1)));
				else if(ops.charAt(o)=='*')
					numbers.add(i, numbers.get(i)*numbers.get(i+1));
				else if(ops.charAt(o)=='/')
					numbers.add(i, numbers.get(i)/numbers.get(i+1));
				else if(ops.charAt(o)=='+')
					numbers.add(i, numbers.get(i)+numbers.get(i+1));
				else if(ops.charAt(o)=='-')
					numbers.add(i, numbers.get(i)-numbers.get(i+1));
				//updates the operators and numbers
				numbers.remove(i+1);
				numbers.remove(i+1);
				operations.remove(i);
			}
		}

		return numbers.get(0);
	}
	public static double evaluate(String exp) throws ParseException
	{
		return new ParseExp(exp).evaluate();
	}

	public double nextNum() throws ParseException
	{
		if (numpointer>=numbers.size()) 
			throw new ParseException("No doubles found!");
		numpointer++;
		return numbers.get(numpointer);
	}
	public boolean hasNextNum()
	{
		return numpointer<numbers.size();
	}

	public double nextOp() throws ParseException
	{
		if (oppointer>=operations.size()) 
			throw new ParseException("No doubles found!");
		oppointer++;
		return operations.get(oppointer);
	}
	public boolean hasNextOp()
	{
		return oppointer<operations.size();
	}

	public String getExp()
	{
		return exp;
	}
	//counts the number of (
	private int left(String s)
	{
		int counter = 0;
		for( int i=0; i<s.length(); i++ ) 
		{
    		if( s.charAt(i) == '(' ) 
    		{
        		counter++;
    		}	 
		}
		return counter;
	}
	//counts the number of )
	private int right(String s)
	{
		int counter = 0;
		for( int i=0; i<s.length(); i++ ) 
		{
    		if( s.charAt(i) == ')' ) 
    		{
        		counter++;
    		}	 
		}
		return counter;
	}
}
class ParseException extends Exception
{
	public ParseException(String msg)
	{
		super(msg);
	}
}