package edu.umass.ckc.wo.login;


import edu.umass.ckc.wo.tutormeta.LearningCompanion;

public class LoginResult {
    int sessId;
    String message;
    LearningCompanion learningCompanion;
    int status;
    boolean forwardedToJSP=true;

    public static final int  NEW_SESSION = 1;
    public static final int  PRE_LOGIN = 0;
    public static final int  ALREADY_LOGGED_IN = 2;
    public static final int  ERROR = -1;
    public static final boolean  FORWARDED_TO_JSP = true;
    public static final boolean  NOT_FORWARDED_TO_JSP = false;

    public LoginResult(int sessId, String loginView) {
        this.sessId = sessId;
        this.message = loginView;
        status = NEW_SESSION;
    }

    public LoginResult(int sessId, String loginView, int status) {
        this(sessId,loginView);
        this.status = status;
    }

    public LoginResult(int sessId, String loginView, int status, boolean forwardedToJSP) {
        this(sessId,loginView);
        this.status = status;
        this.forwardedToJSP = forwardedToJSP;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // If an error message is created during login, it is placed in message.
    public boolean isFailed() {
        return sessId == -1 && message != null;
    }

    public boolean hasExistingSession () {
        return sessId != -1 && message != null;
    }

    public LearningCompanion getLearningCompanion() {
        return learningCompanion;
    }

    public void setLearningCompanion(LearningCompanion learningCompanion) {
        this.learningCompanion = learningCompanion;
    }

    public String getMessage() {
        return message;
    }

    public int getSessId() {
        return sessId;
    }

    public boolean isForwardedToJSP() {
        return forwardedToJSP;
    }

    public void setForwardedToJSP(boolean forwardedToJSP) {
        this.forwardedToJSP = forwardedToJSP;
    }

    public boolean isNewSession () {
        return status == NEW_SESSION;
    }
}