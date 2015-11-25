import java.util.LinkedList;
class ParseFn
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
		getTerms();

	}
	private void getTerms() throws ParseException
	{
		String tempfn = fn;
		while (tempfn.length()>0)
		{
			int offset = 0;
			if (tempfn.indexOf("(")<tempfn.indexOf("+")||tempfn.indexOf("(")<tempfn.indexOf("-"))
			{
				offset = tempfn.indexOf(")");
			}
			boolean zeroTerm = true;
			int next = Math.max(tempfn.indexOf("+", offset),tempfn.indexOf("-", offset));
			next = next==-1?tempfn.length():next;
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
			if (tempfn.equals(""))
			{
				break;
			}
		}
		if(join.size()+1!=terms.size())
		{
			throw new ParseException("Extra terms!");
		}
	}

	public String defVar(char[] var, double[] value) throws ParseException
	{
		if (var.length!=value.length)
			throw new ParseException("Unmatched vars and values!");
		
			for(int i=0; i<terms.size(); i++)
			{
				String temp = terms.get(i);

				for (int k=0; k<var.length; k++)
					temp = temp.replace(var[k]+"", value[k]+"");

				defterms.add(temp);
			}
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
		
		for(int i=0; i<defterms.size(); i++)
		{
			for(int j=0; j<defterms.get(i).length(); j++)
			{
				if (vars.contains(defterms.get(i).charAt(j)+""))
				{
					return "";
				}
			}
		}
		
		String exp="";
		for(int i=0; i<defterms.size(); i++)
		{
			if (i<join.size())
				exp+=defterms.get(i)+join.get(i);
			else
				exp+=defterms.get(i);
		}
		System.out.println("exp: "+exp);
		return ParseExp.evaluate(exp)+"";
		
	}

}