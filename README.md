Cuke2Customer
=============
Cucumber is great for collaboratively specifying a software system using natural language that both your development team and your customers can understand. Not so great however is presenting raw .feature files to your clients as they tend to look a bit cryptic/technical at first glance. Manually converting those .feature files into a more client friendly format can be a struggle and once you start passing around different versions of your specification, you are bound to run into version conflicts sooner or later.

So facing this situation I wrote a simple tool that regards your .feature files under version control as single source of truth and makes them readily accessible for your clients: Cuke2Customer is a simple Grails application that retrieves Cucumber features from a version control system and makes them available and searchable through a web browser. 

See the wiki for further documentation:
https://github.com/Behrwind/Cuke2Customer/wiki
