package com.example.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Application {

    private final IDatabase database;
    private final String[] collections = new String[] {"./users.csv", "./questions.csv" ,"./quizzes.csv"};
    private final HashMap<String,User> users;
    private final HashMap<Integer,Quiz> quizzes;
    private boolean isLoaded;
    private static int numberOfQuizzes = 1;

    Application() {
        this.database = new MyDatabase();
        this.users = new HashMap<>();
        this.quizzes = new HashMap<>();
        this.isLoaded = false;
    }

    private void load() {
        ArrayList<String> StrUsers = database.read(collections[0]);
        ArrayList<String> StrQuestions = database.read(collections[1]);
        ArrayList<String> StrQuizzes = database.read(collections[2]);

        for (String line: StrUsers) {
            String[] info = line.split(",");
            this.users.put(info[0], new User(info[0], info[1]));
        }

        for (String line: StrQuestions) {
            String[] info = line.split("-");
            String[] infoQuestion = info[1].split(",");
            String[] infoAnswers = infoQuestion[2].split(" ");
            this.users.get(info[0]).loadQuestion(infoQuestion[1], infoQuestion[0], info[2],new ArrayList<>(Arrays.asList(infoAnswers)));
        }

        for (String line: StrQuizzes) {
            String[] info = line.split("-");
            String[] infoQuiz = info[2].split(",");
            loadQuiz(info[1], info[3], new ArrayList<>(List.of(infoQuiz)), users.get(info[0]));
        }

        for (String line: StrUsers) {
            String[] info = line.split(",");
            for (int i = 2; i < info.length ; i++) {
                int id = Integer.parseInt(info[i].split(":")[0]);
                double score = Double.parseDouble(info[i].split(":")[1]);
                this.users.get(info[0]).addSubmittedQuiz(id);
                quizzes.get(id).getScore().put(users.get(info[0]), score);
            }
        }

        this.isLoaded = true;
    }

    private void close() {
        ArrayList<String> usersString = new ArrayList<>();
        ArrayList<String> questionString = new ArrayList<>();
        ArrayList<String> quizString = new ArrayList<>();


        for (User user: users.values()) {
            usersString.add(user.toDatabase(quizzes));
            for (Question q : user.getQuestions()) {
                String sb = user.getName() + "-" +
                        q.toString() + "-" + q.getId();
                questionString.add(sb);
            }
        }

        for (Quiz quiz : quizzes.values()) {
            StringBuilder sb = new StringBuilder(quiz.getAuthor().getName() + "-" + quiz.getName() + "-");
            for (Question q : quiz.getQuestions()) {
                if ( q != null)
                    sb.append(q.getId()).append(",");
            }
            sb.append("*");
            sb.append("-").append(quiz.getId());
            quizString.add(sb.toString().replace(",*",""));
        }

        database.write(usersString, collections[0]);
        database.write(questionString, collections[1]);
        database.write(quizString, collections[2]);
        numberOfQuizzes = 1;
        User.setNumberOfQuestions(1);
        User.setNumberOfAnswers(1);
        this.isLoaded = false;
    }

    public void run(String[] commands) {
        if (!this.isLoaded) {
            load();
        }

        if(commands[0].compareTo("-create-user") == 0) {
            String var = userCreate(commands);
            if(var.compareTo("User created successfully") == 0)
                System.out.print("{ 'status' : 'ok', 'message' : 'User created successfully'}");
            else System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-create-question") == 0) {
            String var = questionCreate(commands);
            if (var.compareTo("Question added successfully") == 0)
                System.out.print("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
            else System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-question-id-by-text") == 0) {
            String var = getQuestionIdByInt(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Question does not exist") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-all-questions") == 0) {
            String var = getAllQuestion(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-create-quizz") == 0) {
            String var = quizzCreate(commands);
            if (var.compareTo("Quizz added succesfully") == 0)
                System.out.print("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
            else System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-quizz-by-name") == 0) {
            String var = getQuizzByName(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Quizz does not exist") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);

            else System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-all-quizzes") == 0) {
            String var = getAllQuizzes(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-quizz-details-by-id") == 0) {
            String var = getQuizDetails(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '[%s]'}", var);
        }

        if(commands[0].compareTo("-delete-quizz-by-id") == 0) {
            String var = deleteQuiz(commands);
            if (var.compareTo("Quizz deleted successfully") == 0)
                System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);

        }

        if(commands[0].compareTo("-submit-quizz") == 0) {
            String var = submitQuiz(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("No quizz identifier was provided") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("No quiz was found") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '%s'}", var);
        }

        if(commands[0].compareTo("-get-my-solutions") == 0) {
            String var = getMyScore(commands);
            if (var.compareTo("You need to be authenticated") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else if (var.compareTo("Login failed") == 0)
                System.out.printf("{ 'status' : 'error', 'message' : '%s'}", var);
            else System.out.printf("{ 'status' : 'ok', 'message' : '[%s]'}", var);
        }

        if(commands[0].compareTo("-cleanup-all") == 0) {
            database.reset(collections);
            System.out.print("{ 'status': 'ok', 'message' : 'Cleanup finished successfully'}");
            return;
        }

        close();
    }

    private String userCreate(String[] parameters) {
        String name = null;
        String password = null;

        for (String param: parameters) {
            if (param.contains("-u "))
                name = param.split("'")[1];
            else if (param.contains("-p "))
                password = param.split("'")[1];
        }

        if (name == null)
            return "Please provide username";
        if (password == null)
            return "Please provide password";

        if(users.containsKey(name))
            return "User already exists";


        users.put(name,new User(name, password));
        return "User created successfully";
    }

    private String questionCreate(String[] parameters) {
        String name = null;
        String password = null;
        User login = null;
        String questionBody = null;
        String type = null;
        ArrayList<String> answersString = new ArrayList<>();

        for (String param: parameters) {
            if (param.contains("-u"))
                name = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-text"))
                questionBody = param.split("'")[1];
            else if (param.contains("-type"))
                type = param.split("'")[1];
            else if (!param.contains("-create-question"))
                answersString.add(param);
        }

        if (password == null || name == null)
                return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(name) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        if (questionBody == null)
            return "No question text provided";

        if(login.findQuestion(questionBody) != null)
            return "Question already exists";

        return login.addQuestion(questionBody, type, answersString);
    }

    private String getQuestionIdByInt(String[] parameters) {
        String username = null;
        String password = null;
        String body = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-text "))
                body = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        Question q = login.findQuestion(body);
        if(q == null)
            return  "Question does not exist";

        return Integer.toString(q.getId());
    }

    private String getAllQuestion(String[] parameters) {
        String username = null;
        String password = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        StringBuilder sb = new StringBuilder("[");
        for (Question q: login.getQuestions()) {
            sb.append("{\"question_id\" : \"").append(q.getId()).append("\"");
            sb.append(", \"question_name\" : \"").append(q.getBody()).append("\"}, ");
        }
        return  sb.append("*").toString().replace(", *", "]");
    }

    private String quizzCreate(String[] parameters) {
        String username = null;
        String password = null;
        String name = null;
        User login = null;
        Quiz quizFound = null;
        ArrayList<String> questionsStr = new ArrayList<>();

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-name "))
                name = param.split("'")[1];
            else if (!param.contains("-create-quizz"))
                questionsStr.add(param);

        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        for (Quiz quiz: quizzes.values()) {
            assert name != null;
            if(quiz.getName().compareTo(name) == 0)
                quizFound = quiz;
        }

        if (quizFound != null)
            return "Quizz name already exists";

        
        if (questionsStr.size() > 10)
            return "Quizz has more than 10 questions'";

        return addQuizz(name, questionsStr, login);
    }

    private String getQuizzByName(String[] parameters) {
        String username = null;
        String password = null;
        String body = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-name "))
                body = param.split("'")[1];
        }

//            case  -> {
//                int var = ;
//                switch (var) {
//                    case -2 -> System.out.print("{ 'status' : 'error', 'message' : ''}");
//                    case -1 -> System.out.print("{ 'status' : 'error', 'message' : '}");
//                    case 0 -> System.out.print("{ 'status' : 'error', 'message' : ''}");
//                    default ->  System.out.printf("{ 'status' : 'ok', 'message' : '%d'}", var);
//                }
//            }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        Quiz q = findQuizz(body);
        if (q == null)
            return "Quizz does not exist";

        return Integer.toString(q.getId());
    }

    private String getAllQuizzes(String[] parameters) {
        String username = null;
        String password = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        StringBuilder sb = new StringBuilder("[");
        for (Quiz q: quizzes.values()) {
            String isCompleted = "False";
            sb.append("{\"quizz_id\" : \"").append(q.getId()).append("\"");
            sb.append(", \"quizz_name\" : \"").append(q.getName()).append("\"");
            if (q.isCompleted(login))
                isCompleted = "True";
            sb.append(", \"is_completed\" : \"").append(isCompleted).append("\"}, ");

        }
        return  sb.append("*").toString().replace(", *", "]").replace("*","]");
    }

    private String submitQuiz(String[] parameters) {
        String username = null;
        String password = null;
        String body = null;
        User login = null;
        ArrayList<String> answersString = new ArrayList<>();

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-quiz-id"))
                body = param.split("'")[1];
            else if (!param.contains("-submit-quizz"))
                answersString.add(param);
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        if(body == null)
            return "No quizz identifier was provided";

        Quiz q = quizzes.get(Integer.parseInt(body));
        if(q == null)
            return "No quiz was found";

        return q.solve(login, answersString);
    }

    private String addQuizz(String name, ArrayList <String> questions, User user) {

        ArrayList<Question> questionsForQuizzes = new ArrayList<>();

        for (String str: questions) {
            String id;
            if (str.contains("-question-")) {
                id = str.split("'")[1];
                Question q = user.getQuestionsByID(Integer.parseInt(id));
                if (q == null) {
                    return "Question ID for question " + id + " does not exist";
                }
                questionsForQuizzes.add(q);
            }
        }
        quizzes.put(numberOfQuizzes ,new Quiz(user, name, questionsForQuizzes, numberOfQuizzes));
        numberOfQuizzes++;

        return "Quizz added succesfully";
    }

    private Quiz findQuizz(String body) {
        if (body != null) {
            for (Quiz quiz : quizzes.values()) {
                if (quiz.getName().compareTo(body) == 0) {
                    return quiz;
                }
            }
        }
        return null;
    }

    private void loadQuiz(String name, String id, ArrayList<String> strings, User user) {
        ArrayList<Question> quiz = new ArrayList<>();
        for (String str: strings) {
            quiz.add(user.getQuestionsByID(Integer.parseInt(str)));
        }
        quizzes.put(Integer.parseInt(id) ,new Quiz(user, name, quiz, Integer.parseInt(id)));
        numberOfQuizzes = Integer.parseInt(id) + 1;
    }

    private String getQuizDetails(String[] parameters) {
        String username = null;
        String password = null;
        String body = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains("-id "))
                body = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        if (body == null)
            return "No id provided";
        Quiz q = quizzes.get(Integer.parseInt(body));

        String sb_aux = "";
        for (Question question: q.getQuestions()) {
            StringBuilder sb = new StringBuilder(sb_aux);
            sb.append("{\"question-name\":\"").append(question.getBody());
            sb.append("\", \"question_index\":\"").append(question.getId());
            sb.append("\", \"question_type\":\"").append(question.getType());
            sb.append("\", \"answers\":\"[");
            for (Answer a: question.getAnswers()) {
                sb.append("{\"answer_name\":\"").append(a.getBody()).append("\", ");
                sb.append("\"answer_id\":\"").append(a.getId()).append("\"}, ");
            }
            sb.append("*");
            sb_aux = sb.toString().replace(", *", "");
            sb_aux += "]\"}, ";
        }

        sb_aux += "*";
        sb_aux = sb_aux.replace(", *", "");

        return sb_aux;
    }

    private String deleteQuiz(String[] parameters) {
        String username = null;
        String password = null;
        String body = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p"))
                password = param.split("'")[1];
            else if (param.contains(" "))
                body = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        if(body == null)
            return "No quizz identifier was provided";

        Quiz q = quizzes.get(Integer.parseInt(body));
        if(q == null)
            return "No quiz was found";

        quizzes.remove(Integer.parseInt(body));

        return "Quizz deleted successfully";
    }

    private String getMyScore(String[] parameters) {
        String username = null;
        String password = null;
        User login = null;

        for (String param: parameters) {
            if (param.contains("-u"))
                username = param.split("'")[1];
            else if (param.contains("-p "))
                password = param.split("'")[1];
        }

        if (username == null || password == null)
            return "You need to be authenticated";

        for (User user: users.values()) {
            if (user.getName().compareTo(username) == 0 && user.login(password))
                login = user;
        }

        if (login == null)
            return "Login failed";

        return login.getScore(quizzes);
    }

}
