package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Quiz {

    private final String name;
    private final User author;
    private final ArrayList<Question> questions;
    private final HashMap<User,Double> score;
    private final int id;
    private final double pondere;

    public Quiz(User author, String name, ArrayList<Question> questions, int id) {
        this.name = name;
        this.author = author;
        this.questions = questions;
        this.score = new HashMap<>();
        this.id = id;
        this.pondere = 100d / questions.size();
    }

    public User getAuthor() {
        return author;
    }
    public int getId() {
        return id;
    }

    public boolean isCompleted(User user) {
        return score.containsKey(user);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public HashMap<User, Double> getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder ret =  new StringBuilder();
        ret.append(name).append("-");
        for (Question q: questions)
            ret.append(q.toString()).append("/");
        return ret.append("*").toString().replace("/*", "");
    }

    public String solve(User login, ArrayList<String> answersString) {
        double dub = 0d;
        if (login.equals(author))
            return "You cannot answer your own quizz";
        if (score.containsKey(login))
            return "You already submitted this quizz";

        for (String param : answersString) {
            if (param.contains("-answer-id-")) {
                String ans = param.split("'")[1];
                for (Question q : questions) {
                    for (Answer answer : q.getAnswers()) {
                        if (Integer.toString(answer.getId()).contains(ans) && q.getType().compareTo("single") == 0) {
                            double points = 1d;
                            if (answer.isRightAnswer())
                                dub += points * pondere;
                            else
                                dub -= points * pondere;
                        } else if(Integer.toString(answer.getId()).contains(ans)) {
                            double points = q.calculatePoints(answer);
                            if (answer.isRightAnswer())
                                dub += points * pondere;
                            else
                                dub -= points * pondere;
                        }
                    }
                }
            }
        }

        if (dub < 0)
            dub = 0d;

        score.put(login, dub);
        login.addSubmittedQuiz(this.id);
        if ( (int) dub + 0.5d <= dub)
            return ((int) dub + 1) + " points";
        return (int) dub + " points";
    }
}
