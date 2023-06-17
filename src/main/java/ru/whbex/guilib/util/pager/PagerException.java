package ru.whbex.guilib.util.pager;

public class PagerException extends Exception {
    public enum Reason {
        INVALID_NUMBER("Page must be a positive number!"),
        MAX_PAGE_EXCEEDED("Provided page number is bigger than max pages count!"),
        OTHER("Unknown pager exception!");
        private final String msg;

        Reason(String message){
            this.msg = message;
        }

        public String getMessage() {
            return msg;
        }
    }
    private final Reason reason;
    private final Throwable throwable;
    public PagerException(Reason reason){
        super(reason.getMessage());
        this.reason = reason;
        this.throwable = null;
    }
    public PagerException(Throwable throwable){
        super(Reason.OTHER.getMessage(), throwable);
        this.throwable = throwable;
        this.reason = Reason.OTHER;
    }

    public Reason getReason() {
        return reason;
    }
}
