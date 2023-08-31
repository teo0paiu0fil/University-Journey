package com.example.project;

public class Answer {
    private final String body;
    private final boolean isRightAnswer;
    private final int id;
    public String getBody() {
        return body;
    }

    public boolean isRightAnswer() {
        return isRightAnswer;
    }

    Answer(String body, boolean goodOrBad, int id) {
        this.body = body;
        this.isRightAnswer = goodOrBad;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return body + ":" + (isRightAnswer ? 1 : 0) + ":" + id;
    }
}
