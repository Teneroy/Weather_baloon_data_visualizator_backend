package org.passau.visualizor.exceptions;

public class PredictionResponseDataTransformationException extends RuntimeException {
    public PredictionResponseDataTransformationException(Exception e) {
        super("An error has occurred during the datetime transformation process", e);
    }
}
