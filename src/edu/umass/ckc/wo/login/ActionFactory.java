package edu.umass.ckc.wo.login;

import ckc.servlet.servbase.ServletAction;
import ckc.servlet.servbase.ServletParams;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 12, 2012
 * Time: 3:20:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionFactory {

    public static LoginServletAction buildAction(ServletParams params) throws Exception {
        String action = params.getMandatoryString("action");
        Class c = ActionFactory.class;
        String packageName = c.getPackage().getName();
        // assumption is that the action class lives in the same package as this factory.
        String actionClassName = packageName + "." + action;
        try {
            c = Class.forName(actionClassName);
            return (LoginServletAction) c.newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        }



    }
}
