package cn.cy.springbatch.config.p22;

public class CustomRetryException extends Exception{

    public CustomRetryException() {
    }

    public CustomRetryException(String message) {
        super(message);
    }
}
