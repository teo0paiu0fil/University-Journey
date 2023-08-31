package com.example.project;

import java.util.ArrayList;

public class Question {
    private final String type;
    private final String body;
    private final ArrayList<Answer> answers;
    private final int id;

    Question(String body, String type, ArrayList<Answer> answers, int id) {
        this.body = body;
        this.type = type;
        this.answers = answers;
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Answer a: answers) {
            sb.append(a.toString()).append(" ");
        }
        sb.append("*");
        return type + "," +body + "," + sb.toString().replace(" *", "");
    }

    public double calculatePoints(Answer answer) {
        double numberOfRight = 0;
        for (Answer ans: answers) {
            if(ans.isRightAnswer())
                numberOfRight++;

        }

        if(answer.isRightAnswer())
            return 1d / numberOfRight;
        return 1d / (answers.size() - numberOfRight);
    }
}
