package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private final String name;
    private final String password;
    private final ArrayList<Question> questions;
    private final ArrayList<Integer> submittedQuizzes;
    private static int numberOfQuestions = 1;
    private static int numberOfAnswers = 1;

    public static void setNumberOfQuestions(int numberOfQuestions) {
        User.numberOfQuestions = numberOfQuestions;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void addSubmittedQuiz(int id) {
        submittedQuizzes.add(id);
    }

    User(String name, String password) {
        this.name = name;
        this.password = password;
        this.questions = new ArrayList<>();
        this.submittedQuizzes = new ArrayList<>();
    }

    public static void setNumberOfAnswers(int i) {
        numberOfAnswers = i;
    }

    public String getName() {
        return name;
    }

    public boolean login(String password) {
        return password.compareTo(this.password) == 0;
    }

    public String addQuestion(String questionBody, String type, ArrayList<String> answersString) {
       if ( answersString.size() > 10)
           return "Single correct answer question has more than one correct answer";

        if (answersString.size() == 0)
           return "No answer provided";

       if (answersString.size() == 1)
            return "Answer 1 has no answer description";

        if (answersString.size() == 2)
            return "Only one answer provided";

       String[] answersArray = answersString.toArray(new String[0]);
       ArrayList<Answer> answers = new ArrayList<>();
       int numberOfWriteAnswers = 0;

       for (int i = 0; i < answersArray.length - 1; i += 2) {
           String answerDescription = null;
           Boolean isCorrect = null;
           if (answersArray[i].contains("-answer-" + (i/2 + 1) + " ")) {
               answerDescription = answersArray[i].split("'")[1];
           }
           if (answersArray[i + 1].contains("-answer-" + (i/2 + 1) + "-is-correct")) {
               isCorrect = answersArray[i + 1].split("'")[1].contains("1");
           }
           if (answerDescription == null)
                return "Answer " + (i /2 + 1) + " has no answer description";
           if (isCorrect == null)
                return "Answer " + (i /2 + 1) + " has no answer correct flag";
           if (type.compareTo("single") == 0 && Boolean.TRUE.equals(isCorrect))
               numberOfWriteAnswers++;
           if (numberOfWriteAnswers > 1)
               return "Single correct answer question has more than one correct answer";
           for (Answer a: answers) {
               if(a.getBody().compareTo(answerDescription) == 0)
                   return "Same answer provided more than once";
           }
           answers.add(new Answer(answerDescription, isCorrect, numberOfAnswers));
           numberOfAnswers++;
       }

        this.questions.add(new Question(questionBody, type, answers, numberOfQuestions));
        numberOfQuestions++;
        return "Question added successfully";
    }

    public Question findQuestion(String body) {
        for (Question question: questions) {
            if (question.getBody().compareTo(body) == 0) {
                return question;
            }
        }
        return null;
    }

    public void loadQuestion(String type, String body, String id, ArrayList<String> answersString) {
        ArrayList<Answer> answers = new ArrayList<>();
        for (String str: answersString) {
            String[] info = str.split(":");
            answers.add(new Answer(info[0], info[1].contains("1"), Integer.parseInt(info[2])));
            setNumberOfAnswers(Integer.parseInt(info[2]) + 1);
        }
        questions.add(new Question(type, body, answers, Integer.parseInt(id)));
        setNumberOfQuestions(Integer.parseInt(id) + 1);
    }

    public Question getQuestionsByID(int id) {
        for (Question q: questions) {
            if (q.getId() == id)
                return q;
        }
        return null;
    }

    public String getScore(HashMap<Integer, Quiz> quizzes) {
        StringBuilder sb = new StringBuilder();
        int positions = 1;
        for ( Integer id : submittedQuizzes) {
            Quiz q = quizzes.get(id);
            double score = q.getScore().get(this);
            sb.append("{\"quiz-id\" : \"").append(id).append("\", ");
            sb.append("\"quiz-name\" : \"").append(q.getName()).append("\", ");
            sb.append("\"score\" : \"").append((int)score).append("\", ");
            sb.append("\"index_in_list\" : \"").append(positions).append("\"}, ");
            positions++;
        }
        sb.append("*");

        return sb.toString().replace(", *", "");
    }

    public String toDatabase(HashMap<Integer, Quiz> quizzes) {
        StringBuilder sb = new StringBuilder(name + "," + password);
        for (int id : submittedQuizzes) {
            Quiz q = quizzes.get(id);
            double dub = q.getScore().get(this);
            int number = (int)dub;
            sb.append(",").append(id).append(":").append(dub);
        }

         return sb.toString();
    }
}
