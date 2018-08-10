package edu.umass.ckc.wo.admin;

import edu.umass.ckc.wo.lc.LCRuleset;
import edu.umass.ckc.wo.tutor.DynamicPedagogy;
import edu.umass.ckc.wo.tutor.Pedagogy;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorParam;
import edu.umass.ckc.wo.tutor.intervSel2.InterventionSelectorSpec;
import edu.umass.ckc.wo.tutor.probSel.PedagogicalModelParameters;
import edu.umass.ckc.wo.xml.JDOMUtils;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;



/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Dec 19, 2007
 * Time: 4:22:53 PM    kk
 */
public class PedagogyParser {
    private String defaultClasspath;
    private List<Pedagogy> pedagogies;

    public static final String DEFAULT_PROBLEM_SELECTOR = "BaseTopicProblemSelector";
    public static final String DEFAULT_HINT_SELECTOR = "PercentageHintSelector";
    public static final String DEFAULT_PEDAGOGICAL_MODEL = "BasePedagogicalModel";
    public static final String DEFAULT_STUDENT_MODEL = "BaseStudentModel";
    public static final String DEFAULT_REVIEW_MODE_PROBLEM_SELECTOR = "ReviewModeProblemSelector";
    public static final String DEFAULT_CHALLENGE_MODE_PROBLEM_SELECTOR = "ChallengeModeProblemSelector";

    public PedagogyParser(InputStream str) throws ClassNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DataConversionException {
//        File f = new File(filename);
        Document d = makeDocument(str);
        pedagogies = readPedagogies(d);
    }

    public PedagogyParser () {}

    /**
     * Make a JDOM Document out of the file.
     * @param str
     * @return  JDOM Document
     */
    public Document makeDocument (InputStream str) {

        SAXBuilder parser = new SAXBuilder();
        try {
            Document doc = parser.build(str);
            Element root = doc.getRootElement();
            this.defaultClasspath = root.getAttribute("defaultClasspath").getValue();
            return doc;
        } catch (JDOMException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     *
     * @return  a List of Pedagogy objects created from the pedagogies.xml file
     */
    public List<Pedagogy> getPedagogies() {
        return pedagogies;
    }


    public List<Pedagogy> readPedagogies (Document xmlDoc) throws NoSuchMethodException, DataConversionException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Element root = xmlDoc.getRootElement();
        List<Pedagogy> result= new ArrayList<Pedagogy>();
        List children = root.getChildren("pedagogy");
        for (int i = 0; i < children.size(); i++) {
            Element element = (Element) children.get(i);
            result.add(readPed(element));
        }
        return result;
        }


    /**
     * Given the XML for a pedagogy (comes from DB now).  This will parse it into JDOM elements and return it.
     * @param pedXML
     * @return
     * @throws Exception
     */
    public Pedagogy parsePed (String pedXML) throws Exception {
        Element pedElt = JDOMUtils.getRoot(pedXML);
        Pedagogy p = readPed(pedElt);
        return p;
    }

    public Pedagogy readPed(Element pedElt) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, DataConversionException {
        Element e;

        e = pedElt.getChild("pedagogicalModelClass");
        // by default we build a BasePedagogicalModel unless a class is provided.
        String pedModClass = DEFAULT_PEDAGOGICAL_MODEL;
        if (e != null)
            pedModClass = e.getValue();

        if(pedModClass.equals("DynamicPedagogicalModel"))
            return initDynamicPedagogy(pedElt, pedModClass);
        else
            return initStaticPedagogy(pedElt, pedModClass);

    }

    private DynamicPedagogy initDynamicPedagogy(Element pedElt, String pedModClass) throws DataConversionException {
        Element e;
        DynamicPedagogy p = new DynamicPedagogy();

        setCommonElements(pedElt, pedModClass, p);

        e = pedElt.getChild("studentModels");
        if (e != null) {
            List<Element> studentModelClasses = e.getChildren("studentModelClass");
            for(Element elem : studentModelClasses){
                String smClass = elem.getValue();
                p.addStudentModelClass(smClass);
            }
        }
        else p.addStudentModelClass(DEFAULT_STUDENT_MODEL);

        e = pedElt.getChild("problemSelectors");
        if (e != null) {
            List<Element> problemSelectorClasses = e.getChildren("problemSelectorClass");
            for(Element elem : problemSelectorClasses){
                String smClass = elem.getValue();
                p.addProblemSelectorClass(smClass);
            }
        }
        else p.addProblemSelectorClass(DEFAULT_PROBLEM_SELECTOR);

        e = pedElt.getChild("reviewModeProblemSelectors");
        if (e != null) {
            List<Element> reviewModeProblemSelectorClasses = e.getChildren("reviewModeProblemSelectorClass");
            for(Element elem : reviewModeProblemSelectorClasses){
                String smClass = elem.getValue();
                p.addReviewModeProblemSelectorClass(smClass);
            }
        }
        else p.addReviewModeProblemSelectorClass(DEFAULT_REVIEW_MODE_PROBLEM_SELECTOR);

        e = pedElt.getChild("challengeModeProblemSelectors");
        if (e != null) {
            List<Element> challengeModeProblemSelectorClasses = e.getChildren("challengeModeProblemSelectorClass");
            for(Element elem : challengeModeProblemSelectorClasses){
                String smClass = elem.getValue();
                p.addChallengeModeProblemSelectorClass(smClass);
            }
        }
        else p.addChallengeModeProblemSelectorClass(DEFAULT_CHALLENGE_MODE_PROBLEM_SELECTOR);

        e = pedElt.getChild("hintSelectors");
        if (e != null) {
            List<Element> hintSelectorClasses = e.getChildren("hintSelectorClass");
            for(Element elem : hintSelectorClasses){
                String smClass = elem.getValue();
                p.addHintSelectorClass(smClass);
            }
        }
        else p.addHintSelectorClass(DEFAULT_HINT_SELECTOR);

        e = pedElt.getChild("learningCompanions");
        if ( e != null ) {
            List<Element> learningCompanionClasses = e.getChildren("learningCompanionClass");
            for(Element elem : learningCompanionClasses){
                String smClass = elem.getValue();
                p.addLearningCompanionClass(smClass);
            }
        }

        e = pedElt.getChild("switchers");
        if (e != null) {
            p.setSwitchersElement(e);
        }

        return p;

    }

    private Pedagogy initStaticPedagogy(Element pedElt, String pedModClass) throws DataConversionException {
        Element e;
        Pedagogy p = new Pedagogy();

        setCommonElements(pedElt, pedModClass, p);

        e = pedElt.getChild("studentModelClass");
        if (e != null) {
            String smClass = e.getValue();
            p.setStudentModelClass(smClass);
        }
        else p.setStudentModelClass(DEFAULT_STUDENT_MODEL);

        e = pedElt.getChild("problemSelectorClass");
        if (e != null) {
            String psClass = e.getValue();
            p.setProblemSelectorClass(psClass);
        }
        else p.setProblemSelectorClass(DEFAULT_PROBLEM_SELECTOR);

        e = pedElt.getChild("reviewModeProblemSelectorClass");
        if (e != null) {
            String psClass = e.getValue();
            p.setReviewModeProblemSelectorClass(psClass);
        }
//        else p.setReviewModeProblemSelectorClass(DEFAULT_REVIEW_MODE_PROBLEM_SELECTOR);

        e = pedElt.getChild("challengeModeProblemSelectorClass");
        if (e != null) {
            String psClass = e.getValue();
            p.setChallengeModeProblemSelectorClass(psClass);
        }
//        else p.setChallengeModeProblemSelectorClass(DEFAULT_CHALLENGE_MODE_PROBLEM_SELECTOR);

        e = pedElt.getChild("hintSelectorClass");
        if (e != null) {
            String hClass = e.getValue();
            p.setHintSelectorClass(hClass);
        }
        else p.setHintSelectorClass(DEFAULT_HINT_SELECTOR);

        e = pedElt.getChild("learningCompanionClass");
        if ( e != null )
            p.setLearningCompanionClass(e.getValue());

        e = pedElt.getChild("learningCompanionCharacter");
        if ( e != null )
            p.setLearningCompanionCharacter(e.getValue());
        List<Element> rulesets= pedElt.getChildren("learningCompanionRuleSet");
        if (rulesets != null)
            for (Element rs : rulesets) {
                String src = rs.getAttributeValue("source");
                String name = rs.getAttributeValue("name");
                String descr = rs.getAttributeValue("description");
                String notes = rs.getAttributeValue("notes");
                LCRuleset lcrs = new LCRuleset();
                lcrs.setName(name);
                lcrs.setSource(src);
                lcrs.setDescription(descr);
                lcrs.setNotes(notes);
                p.addLearningCompanionRuleSet(lcrs);
            }

        return p;
    }

    private void setCommonElements(Element pedElt, String pedModClass, Pedagogy p) throws DataConversionException {
        Element e;
        p.setPedagogicalModelClass(pedModClass);

        Attribute defaultAttr = pedElt.getAttribute("default");
        if (defaultAttr != null)
            p.setDefault(defaultAttr.getBooleanValue());
        // id shouldn't be necessary anymore since pedagogies are now coming from a table
        e = pedElt.getChild("id");
        if (e != null) {
            String id = e.getValue();
            p.setId(id);
        }


        e = pedElt.getChild("comment");
        if (e != null)  {
            String comment = e.getValue();
            p.setComment(comment);
        }
        e = pedElt.getChild("provideInSimpleConfig");
        if (e != null) {
            String n = e.getAttributeValue("name");
            p.setSimpleConfigName(n);
        }

        // Hoping we can eliminate the below since they are now coming from the fields of the pedagogy table rather
        // than XML

//        e = pedElt.getChild("login");
//        if (e != null) {
//            p.setLoginXMLName(e.getTextTrim());
//            p.setLoginXML(Settings.loginMap.get(p.getLoginXMLName()));
//        }
//        else p.setLoginXML(null);

//        e = pedElt.getChild("lesson");
//        if (e != null) {
//            p.setLessonName(e.getTextTrim());
//        }
//        p.setLessonXML(Settings.lessonMap.get(p.getLessonName()));

        e = pedElt.getChild("interventions");
        if (e != null) {
            p.setInterventionsElement(e);
        }

        e = pedElt.getChild("controlParameters");
        // get the default params
        PedagogicalModelParameters params = new PedagogicalModelParameters();
        // if user provided some, overwrite the individual settings
        if (e != null)
            params.setParameters(e);
//            readControlParams(params, e);
        p.setParams(params);
        e = pedElt.getChild("package");
        String packg = null ;
        if ( e != null )
            packg = e.getValue();
        p.setPackg(packg);

    }


    private void readAttemptInterventionSelectors(Pedagogy p, Element e) {
        InterventionSelectorSpec spec = parseSelector(e);
        p.setAttemptInterventionSelector(spec);
        List<Element> subSelectors = e.getChildren("attemptInterventionSelector");
        List<InterventionSelectorSpec> subs = new ArrayList<InterventionSelectorSpec>();
        for (Element elt : subSelectors) {
            subs.add(parseSelector(elt));
        }
        p.setSubAttemptInterventionSelectors(subs);

    }

    private void readNextProblemInterventionSelectors(Pedagogy p, Element e) {
        InterventionSelectorSpec spec = parseSelector(e);
        p.setNextProblemInterventionSelector(spec);
        List<Element> subSelectors = e.getChildren("nextProblemInterventionSelector");
        List<InterventionSelectorSpec> subs = new ArrayList<InterventionSelectorSpec>();
        for (Element elt : subSelectors) {
            subs.add(parseSelector(elt));
        }
        p.setSubNextProblemInterventionSelectors(subs);
    }

    private InterventionSelectorSpec parseSelector (Element elt) {
        String className = elt.getAttributeValue("class");
        String selectProblem = elt.getAttributeValue("selectProblem");
        List<InterventionSelectorParam> paramSpecs = new ArrayList<InterventionSelectorParam>();
        List<Element> params = elt.getChildren("param");
        for (Element param: params) {
            String name = param.getAttributeValue("name");
            String value = param.getValue();
            InterventionSelectorParam pSpec = new InterventionSelectorParam(name,value);
            paramSpecs.add(pSpec);
        }
        // We want this intervention to show at the same time as a problem, so the selectProblem flag tells the PM
        // to return a problem that contains the intervention
        InterventionSelectorParam selectParam = new InterventionSelectorParam("selectProblem", selectProblem);
        paramSpecs.add(selectParam);
        Element config = elt.getChild("config");
        InterventionSelectorSpec spec  = new  InterventionSelectorSpec(className,paramSpecs, config, false);
        return spec;
    }




}
