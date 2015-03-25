# contextnotifier
Automatically exported from code.google.com/p/contextnotifier

ContextNotifier is a rule based, framework for dinamic generation of ContextAware Adaptive MIDlets. 
The core of the framework is composed of three components, 
1)a DecisionEngine that enables loading and disposing Rules formed by trees of Contraints, 
2)a ContextHandlersManager that registers each Constraint to a specific ContextHandler 
and that guarantees that unused handlers are garbage collected, and 
3)a NotificationManager that activates specific application-dependent components, after the validation of a Rule. 
An application developed within this framework is composed of a set of XML rules and some high-level components 
that will be loaded by the NotificationManager.
