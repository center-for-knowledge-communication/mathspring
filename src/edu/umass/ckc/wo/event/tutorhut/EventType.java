package edu.umass.ckc.wo.event.tutorhut;

/**
 * These are the event types that the learning companion rules pay attention to.
 * User: david
 * Date: 5/24/16
 * Time: 8:33 AM
 * To change this template use File | Settings | File Templates.
 */
public enum EventType {
    ATTEMPT ("Attempt"),
    NEXT_PROBLEM ("NextProblem"),
    BEGIN_PROBLEM ("BeginProblem"),
    END_PROBLEM ("EndProblem"),
    HINT ("Hint"),
    END_EXAMPLE ("EndExample)");

    private String text;

    EventType(String text) {
        this.text = text;
    }



    public static EventType getType (TutorHutEvent ev) {
        if (ev instanceof AttemptEvent)
            return ATTEMPT;
        else if (ev instanceof NextProblemEvent)
            return NEXT_PROBLEM;
        else if (ev instanceof BeginProblemEvent)
            return BEGIN_PROBLEM;
        else if (ev instanceof EndProblemEvent)
            return END_PROBLEM;
        else if (ev instanceof  HintEvent)
            return HINT;
        else
            return END_EXAMPLE;
    }


    public String getText() {
        return this.text;
    }

    public static EventType fromString(String text) {
        if (text != null) {
            for (EventType b : EventType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }

    public int ordinalFromString (String x) {
        return fromString(x).ordinal();
    }

}
