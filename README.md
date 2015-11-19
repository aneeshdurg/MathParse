# ParseExp
	A class used to parse a String into a mathematical expression.

	double evaluate()
		Parses the string which the ParseExp object was initiallized 
		with.

	double evaluate(String exp)
		Parses the string passed to the function

	double nextNum()
		Returns the next number in the expression. Throws ParseException
		if no number is found. Use hasNextNum to avoid this.

	boolean hasNextNum()
		Returns true if there is another number in the expression, false
		otherwise.

	double nextOp()
		Returns the next operation in the experssion. Throws ParseException
		if no operation is found. Use hasNextOp to avoid this.	

	boolean hasNextOp()
		Returns true if there is another operation in the expression, false
		otherwise.

	String getExp()
		Returns the original expression as a String.	