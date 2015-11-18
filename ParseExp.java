import java.util.LinkedList;
class ParseExp
{
	private String exp;
	private String ops = "^*/+-";
	private int counter=0;
	public ParseExp(String exp)
	{
		exp = exp.replaceAll("\\s+", "").toLowerCase();
		this.exp = exp;
	}
	private ParseExp(String exp, int counter)
	{
		exp = exp.replaceAll("\\s+", "").toLowerCase();
		this.exp = exp;
		this.counter = counter;
	}
	public double parse() throws ParseException
	{
		int length = exp.length();
		LinkedList<Double> numbers = new LinkedList<Double>();
		LinkedList<Character> operations = new LinkedList<Character>();
		double currentnum=0;
		boolean decimal=false;
		int decimalcount=0;
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
					numbers.add(currentnum);
				counter++;
			}
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
				exp = exp.substring(2, exp.length());
				counter++;
			}

			//Expression inside ()
			else if(now=='(')
			{
				if (exp.indexOf(")")<1)
					throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
				String subexp = exp.substring(exp.indexOf("(")+1, exp.indexOf(")"));
				ParseExp brackets = new ParseExp(subexp, counter);
				currentnum=brackets.parse();
				counter+=subexp.length()+2;
				exp = exp.substring(exp.indexOf(")")+1, exp.length());
			}

			else if(ops.contains(now+""))
			{
				operations.add(now);
				numbers.add(currentnum);
				exp = exp.substring(1, exp.length());
				currentnum = 0;
				counter++;

				
			}

			if((next<48||next>57)&&!ops.contains(now+"")&&!ops.contains(next+"")&&next!='.'&&next!=0&&!(now=='p'&&next=='i'))
				throw new ParseException("Error at "+counter+":"+length+"! Please check and ensure that the expression is properly formatted!");
		}
		
		if(numbers.size()!=operations.size()+1)
		{
			throw new ParseException("Operations ("+operations.size()+") and arguments ("+numbers.size()+") do not match! Please check and ensure that the expression is properly formatted!");	
		}

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

				numbers.remove(i+1);
				numbers.remove(i+1);
				operations.remove(i);
			}
		}

		return numbers.get(0);
	}
	public static double parse(String exp) throws ParseException
	{
		return new ParseExp(exp).parse();
	}
}
class ParseException extends Exception
{
	public ParseException(String msg)
	{
		super(msg);
	}
}