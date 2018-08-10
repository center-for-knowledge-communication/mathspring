package edu.umass.ckc.wo.woreports.js;

/**
 *
 * This represents one Javascript method that can be inserted in the javascript portion of an HTML page
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 9, 2011
 * Time: 2:37:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSFunction {
    private String methodBody;

    public JSFunction(String methodBody) {
        this.methodBody = methodBody;
    }

    public String getFunction () {
        return methodBody;
    }

    public static final JSFunction NAVIGATION_PULLDOWN_MENU = new JSFunction(
            "<!-- Original: Alex Tu <boudha1@hotmail.com> -->\n" +
                    "<!-- Web Site:  http://www.geocities.com/alex_2106 -->\n" +
                    "\n" +
                    "<!-- This script and many more are available free online at -->\n" +
                    "<!-- The JavaScript Source!! http://javascript.internet.com -->\n" +
                    "\n" +
                    "<!-- Begin\n" +
                    "function formHandler(form){\n" +
                    "var URL = document.form.site.options[document.form.site.selectedIndex].value;\n" +
//                    "window.location.href = URL;\n" +
                    "window.open( URL);\n" +
                    "}\n" +
                    "// End -->");
}
