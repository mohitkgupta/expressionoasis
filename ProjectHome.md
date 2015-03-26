**Notes:**
  * Latest version **3.2** can be downloaded from  [here](https://code.google.com/p/expressionoasis/wiki/Downloads). Release notes are placed [here](https://code.google.com/p/expressionoasis/wiki/ReleaseNotes).
  * If you face any issue or find any new requirement, please submit it in [Issues](https://code.google.com/p/expressionoasis/issues/list) section. Inputs are welcome.
  * Please consider to contribute any improvement you have done in code. This will help in improving the framework for whole community.
  * Please let us know if your Project uses 'ExpressionOasis', we shall add it to references. [Contact Us.](mailto:mohit.gupta@vedantatree.com)
  * This project was earlier known as Xpressionengine (http://code.google.com/p/xpressionengine - **Deleted**). However due to an existing trademark, project has been renamed to 'ExpressionOasis' now.

ExpressionOasis is an extensible framework to evaluate the expressions. It can be extended easily to evaluate any new type of expression. The extensiblility comes from the definition of grammar, parsing rules and expression implementations in the configuration files (XML based), which can be extended easily. It supports evaluation of mathematical, boolean, logical, object property, XML, and function kind of expressions.

System does have support for evaluating the functions and variables. User just need to provide the helping classes having implementation for functions and variables. Moreover user can provide new method implementation in XML file, and system picks the function implementation at runtime. Hence any new implementation can be plugged-in. With this, system can evaluate any expression having variable or function as part of it. Currently, various math functions are supported.

System is also capable to return the possible return type for any expression without actually evaluating the expression (in most of the cases). It is not possible in cases, when it is not possible to extract the type without getting actual value, like if we are getting value form any XML document. Another capability is to get the possible operand types for any expression. This is very usable features while implementing the UI for any tool which is based on expression evaluation for its features. These features i.e. type of return value and the type for operand adds a lot of value while using this framework with any other framework or application. It can help to showthe right type of UI or variables or functions to the user using some UI.

System is capable to work in multi-threaded environment.

**Release Notes:** [Wiki > ReleaseNotes](https://code.google.com/p/expressionoasis/wiki/ReleaseNotes)
**Downloads:** [Wiki > Downloads](https://code.google.com/p/expressionoasis/wiki/Downloads)

**Features:**
  * Capable to evaluate Mathematical, Boolean, Logical, Object property, XML, and Function Expressions
  * Supports dynamic functions, where implementation can be provided in XML file
  * Supports variables
  * Capable to suggest the return type of expression (in most of the caess)
  * Capable to suggest possible operand type for a operator
  * Grammer and Parsing rules can be customized or extended easily using XML configuration files
  * New expressions can be introduced easily using XML configuration file
  * Provides in memory configurable cache support
  * Works in multi-threaded environment
  * Maven based build system

**What purpose it will serve:**
The 'expressionoasis' can be used as a plug-in component with other framework components like
  * With a UI framework to evaluate the properties of various data objects and fill these in UI components,
  * Or by any scientific or mathematics research program,
  * Or with any financial or general software which need to evaluate the expressions
  * Or with Workflow Engine to evaluate the runtime workflow expressions

**What problem will it solve:**
As define with the purpose, many of the software may need this component. It helps the developer to avoid reinventing the code and help them to easily plug-in the expression evaluation within their Application. It is very simple in use and is dependent on a few other standard libraries, which are downloadable from Maven Repository. As defined above, the framework configuration is XML based and is highly extensible; hence it can be extended easily for new modification as per application requirement.

**Examples:**

  * Arithmetic Expressions - 2+5-7\*8 20-(5\*3)+10
  * Logical Expressions - a && b (2-1) > 0
  * Expressions with variables - principle rate 100 / time, where principle, rate and time are the variables
  * Expressions with Functions - CurrentBalance? + SimpleInterest? (principle, rate, time)
  * Object property expression - person.city.name, where person is the identifier which points to java object of Person class and city is the property of person and further name is the property of city

_More samples can be found with [TestEvaluator.java](https://expressionoasis.googlecode.com/svn/trunk/src/test/java/org/vedantatree/expressionoasis/TestEvaluator.java)_

**For more information, please refer to** http://www.vedantatree.com/

## Price ##
It is a free software, however you can do favor:
  * By using the software and report back any issue, or any enhancement required
  * By submitting back any enhancements, or documentation done by you. Contributor's name will be retained with contributed code or document etc.
  * By Providing a back link to  http://www.vedantatree.com/ with the reference of 'ExpressionOasis' on your website.
  * By connecting with us on Facebook at https://www.facebook.com/VedantaTree and on Twitter at [@VedantaTree](https://twitter.com/VedantaTree)

Thank  you!


---

### ~A product developed by [VedantaTree](http://www.vedantatree.com) ###