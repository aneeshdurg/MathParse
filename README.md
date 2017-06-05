# MathParse
	To use, add the folder MathParse to your project directory and add the line:
		> import MathParse.*;
	to import all classes in MathParse

	Classes contained in MathParse:	

# ParseExp
	A class used to parse a String into a mathematical expression.

	ParseExp(String exp) throws ParseException
		Initializes a ParseExp object with the string exp.

	double evaluate() throws ParseException
		Parses the string which the ParseExp object was initiallized 
		with. If the string contains "=", calls equate.

	double evaluate(String exp) throws ParseException
		Parses the string passed to the function. If the string contains "=", calls equate.

	double equate() throws ParseException
		Parses the equation with which the ParseExp object was initialized with.
		Return 1 if true and 0 if false.	

	double equate(String exp) throws ParseException
		Parses the equation passed to the function.
		Return 1 if true and 0 if false.
			
	double nextNum() throws ParseException
		Returns the next number in the expression. Throws ParseException
		if no number is found. Use hasNextNum to avoid this.

	boolean hasNextNum() throws ParseException
		Returns true if there is another number in the expression, false
		otherwise.

	double nextOp() throws ParseException throws ParseException
		Returns the next operation in the experssion. Throws ParseException
		if no operation is found. Use hasNextOp to avoid this.	

	boolean hasNextOp() throws ParseException
		Returns true if there is another operation in the expression, false
		otherwise.

	String getExp() throws ParseException
		Returns the original expression as a String.	

# ParseFn
	A class used to parse a String into a mathematical function.
	Character in the range a..z are acceptable variable names.

	ParseFn(String fn) throws ParseExption
		Initializes a ParseFn object with the string fn

	String defVar(char var, String value) throws ParseException
		Replaces var with value.
		See below for more details

	String defVar(char[] var, String[] values) throws ParseException
		Assigns the vars to specified values. The values are Strings and 
		can be used to change one variable to another.
		returns a String displaying var replaced with values if some vars aren't 
		assigned yet. Otherwise returns the value of the function.
		If some vars are not yet assigned, the assigned variables will be saved.
		Calling defvars again will use the saved values.
		To reset all variables use:
			object.defVar();
		Note that defVar does not edit the original function

	void defVar()
		Resets saved variables.	If variables have not be assigned values via the 
		above defVar methods, this has no effect.
