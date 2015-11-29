package MathParse;

import java.util.LinkedList;
public class ParseFn
{
	public LinkedList<String> terms = new LinkedList<String>();
	public LinkedList<String> defterms = new LinkedList<String>();
	public LinkedList<Character> join = new LinkedList<Character>();
	private String vars = "abcdefghijklmnopqrstuvwxyz";
	private String fn;

	public ParseFn(String fn) throws ParseException
	{
		//Check and make sure first and last chars are not ops
		this.fn = fn.replaceAll("\\s+", "").toLowerCase();
		if (fn.charAt(0)=='+'||fn.charAt(0)=='-'||fn.charAt(fn.length()-1)=='+'||fn.charAt(fn.length()-1)=='-')
			throw new ParseException("First and last characters cannot be + or -!");
		getTerms();

	}

	private void getTerms() throws ParseException
	{
		//copies fn to temporary variable to avoid changing fn
		String tempfn = fn;
		while (tempfn.length()>0)
		{
			//counts () as 1 term by setting offset to the index of ')'
			int offset = 0;
			if (tempfn.indexOf("(")<tempfn.indexOf("+")||tempfn.indexOf("(")<tempfn.indexOf("-"))
			{
				offset = tempfn.indexOf(")");
			}
			//finds index of next +/-, is set to tempfn.length() if no next +/-
			int next = Math.max(tempfn.indexOf("+", offset),tempfn.indexOf("-", offset));
			next = next==-1?tempfn.length():next;
			//Current term is appended to terms
			String term = tempfn.substring(0, next);
			if (next!=tempfn.length())
			{
				join.add(tempfn.charAt(next));

				tempfn = tempfn.substring(next+1, tempfn.length());
			}
			else
			{
				tempfn = "";
			}
			//Calulates values of terms without variables
			boolean zeroTerm = true;
			for (int i=0; i<term.length(); i++)
			{
				if (vars.contains(term.charAt(i)+""))
				{
					zeroTerm = false;
				}
			}
			if (zeroTerm)
			{
				terms.add(ParseExp.evaluate(term)+"");
			}
			else
			{
				terms.add(term);
			}
			//breaks when the temporary variable is empty
			if (tempfn.equals(""))
			{
				break;
			}
		}
		//Unmatched terms and +/-
		if(join.size()+1!=terms.size())
		{
			throw new ParseException("Extra terms!");
		}
	}

	public String defVar(char[] var, double[] value) throws ParseException
	{
		//checks var for illegal chars
		for (int i=0; i<var.length; i++)
		{	
			if (!vars.contains(var[i]+""))
				throw new ParseException("Illegal varaible names!");			
		}
		//checks for unmatched variables and values
		if (var.length!=value.length)
			throw new ParseException("Unmatched vars and values!");
		//if defterms is empty, uses terms
		if (defterms.size()==0)
		{
			for(int i=0; i<terms.size(); i++)
			{
				String temp = terms.get(i);

				for (int k=0; k<var.length; k++)
					temp = temp.replace(var[k]+"", value[k]+"");

				defterms.add(temp);
			}
		}	
		else
		{
			for(int i=0; i<defterms.size(); i++)
			{
				String temp = defterms.get(i);

				for (int k=0; k<var.length; k++)
					temp = temp.replace(var[k]+"", value[k]+"");

				defterms.add(i, temp);
				defterms.remove(i+1);
			}
		}
		//find the value of zeroTerms
		boolean zeroTerm = true;
		for(int i=0; i<defterms.size(); i++)
		{
			for(int j=0; j<defterms.get(i).length(); j++)
			{
				if (vars.contains(defterms.get(i).charAt(j)+""))
				{
					zeroTerm = false;
				}
			}
			if (zeroTerm)
			{
				defterms.add(i, ParseExp.evaluate(defterms.get(i))+"");
				defterms.remove(i+1);
			}
		}
		//find undefined vars
		boolean flag = false;
		for(int i=0; i<defterms.size(); i++)
		{
			for(int j=0; j<defterms.get(i).length(); j++)
			{
				if (vars.contains(defterms.get(i).charAt(j)+""))
				{
					flag = true;
				}
			}
		}
		//if some vars are undefined, print the string representation
		if (flag)
		{
			String res="";
			for(int i=0; i<defterms.size(); i++)
			{
				if(i!=defterms.size()-1)
					res+=defterms.get(i)+join.get(i);
				else
					res+=defterms.get(i);
			}
			return res;
		}
		//creates String to be parsed with ParseExp
		String exp="";
		for(int i=0; i<defterms.size(); i++)
		{
			if (i<join.size())
				exp+=defterms.get(i)+join.get(i);
			else
				exp+=defterms.get(i);
		}
		//returns String with value of function
		return ParseExp.evaluate(exp)+"";
		
	}

}