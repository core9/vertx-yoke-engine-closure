Google closure templates Yoke engine
====================================

This is a simple Google Closure templates Yoke engine.  
Add Google Closure templates to your classpath and create templates in a src/main/resources/templates folder.  

Call the engine with:  
yoke.engine("soy", new io.core9.yoke.engine.ClosureTemplateEngine());  

The renderer is a bit different than the standard Yoke renderer, as a closure template contains a namespace and a template name. A file can contain multiple templates.  
The renderer is called with request.response().render('namespace.template.soy');

