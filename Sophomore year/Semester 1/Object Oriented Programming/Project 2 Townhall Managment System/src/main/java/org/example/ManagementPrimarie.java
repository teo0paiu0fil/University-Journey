package org.example;

import org.example.users.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManagementPrimarie {

    private HashMap<String, Utilizator> users;
    private TreeSet<Eveniment> events;
    private Birou<Persoana> persoanaBirou;
    private Birou<Pensionar> pensionarBirou;
    private Birou<Elev> elevBirou;
    private Birou<Angajat> angajatBirou;
    private Birou<EntitateJuridica> entitateJuridicaBirou;
    private static String filename;

    public static SimpleDateFormat dateFormat;

    public ManagementPrimarie() {
        this.users = new HashMap<>();
        this.events = new TreeSet<>();
        this.persoanaBirou = new Birou<>();
        this.pensionarBirou = new Birou<>();
        this.elevBirou = new Birou<>();
        this.angajatBirou = new Birou<>();
        this.entitateJuridicaBirou = new Birou<>();

        dateFormat = new SimpleDateFormat( "dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
    }

    public static void main(String[] args) {
        ManagementPrimarie PM = new ManagementPrimarie();
        filename = args[0];
        try {
            File file = new File("./src/main/resources/input/" + filename);
            Scanner in = new Scanner(file);
            PM.manageRequest(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            filename = null;
        }
    }

    private void manageRequest(Scanner fileScanner) {
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parameters = line.split("; ");
            if (parameters[0].equals("adauga_utilizator")) {
                addUser(parameters);
            } else if (parameters[0].equals("adauga_functionar")) {
                addFunctinar(parameters);
            } else if (parameters[0].equals("cerere_noua")) {
                addRequest(parameters);
            } else if (parameters[0].equals("afiseaza_cereri_in_asteptare")) {
                displayRequestInWaiting(parameters);
            } else if (parameters[0].equals("retrage_cerere")) {
                removeRequest(parameters);
            } else if (parameters[0].equals("afiseaza_cereri_finalizate")) {
                displayFinishedRequest(parameters);
            } else if (parameters[0].equals("afiseaza_cereri")) {
                displayRequest(parameters);
            } else if (parameters[0].equals("rezolva_cerere")) {
                solveRequest(parameters);
            } else if (parameters[0].equals("adauga_eveniment")) {
                addEvent(parameters);
            } else if (parameters[0].equals("afiseaza_evenimente")) {
                displayAllEvents();
            } else if (parameters[0].equals("sterge_eveniment")) {
                deleteEvent(parameters[1]);
            } else if (parameters[0].equals("afiseaza_evenimente_luna")) {
                displayEventsMonth(parameters[1]);
            } else if (parameters[0].equals("schimba_data")) {
                changeEvent(parameters);
            }

        }
    }

    private void changeEvent(String[] parameters) {
        try {
            Date start = dateFormat.parse(parameters[2]);
            Date end = dateFormat.parse(parameters[3]);
            events.forEach((e) -> {
                if (e.getName().equals(parameters[1])) {
                    e.change(start, end);
                }
            });

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayEventsMonth(String parameter) {
        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            String log = Eveniment.displayEventMonth(events, dateFormat , parameter);
            fw.write(log);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void displayAllEvents() {
        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            String log = Eveniment.displayAll(events, dateFormat);
            fw.write(log);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteEvent(String parameter) {
        events.removeIf((e) -> {
           return e.getName().equals(parameter);
        });
    }

    private void addEvent(String[] parameters) {
        try {
            events.add(new Eveniment(parameters[4], parameters[1], dateFormat.parse(parameters[2]), dateFormat.parse(parameters[3])));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void addFunctinar(String[] parameters) {
        if ("entitate juridica".equals(parameters[1])) {
            entitateJuridicaBirou.addFunctionar(parameters[2]);
        } else if ("pensionar".equals(parameters[1])) {
            pensionarBirou.addFunctionar(parameters[2]);
        } else if ("elev".equals(parameters[1])) {
            elevBirou.addFunctionar(parameters[2]);
        } else if ("persoana".equals(parameters[1])) {
            persoanaBirou.addFunctionar(parameters[2]);
        } else if ("angajat".equals(parameters[1])) {
            angajatBirou.addFunctionar(parameters[2]);
        }
    }

    private void solveRequest(String[] parameters) {
        String log = null;
        if ("entitate juridica".equals(parameters[1])) {
            log = entitateJuridicaBirou.solve(parameters[2]);
        } else if ("pensionar".equals(parameters[1])) {
            log = pensionarBirou.solve(parameters[2]);
        } else if ("elev".equals(parameters[1])) {
            log = elevBirou.solve(parameters[2]);
        } else if ("persoana".equals(parameters[1])) {
            log = persoanaBirou.solve(parameters[2]);
        } else if ("angajat".equals(parameters[1])) {
            log = angajatBirou.solve(parameters[2]);
        }

        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + "functionar_" + parameters[2] + ".txt", true);
            assert log != null;
            fw.write(log);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayFinishedRequest(String[] parameters) {
        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            Utilizator user = users.get(parameters[1]);
            if (user == null)
                return;
            ArrayList<Cerere> cereri = user.getRequestSolve();

            fw.write(parameters[1] + " - cereri in finalizate:\n");

            for (Cerere req: cereri) {
                fw.write(dateFormat.format(req.getDate()) + " - ");
                fw.write(user.writeRequest(req.getReq()));
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRequest(String[] parameters) {
        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            if ("angajat".equals(parameters[1])) {
                fw.write( "angajat" + " - cereri in birou:\n");
                angajatBirou.displayRequest(fw);
            } else if ("pensionar".equals(parameters[1])) {
                fw.write( "pensionar" + " - cereri in birou:\n");
                pensionarBirou.displayRequest(fw);
            } else if ("elev".equals(parameters[1])) {
                fw.write( "elev" + " - cereri in birou:\n");
                elevBirou.displayRequest(fw);
            } else if ("persoana".equals(parameters[1])) {
                fw.write( "persoana" + " - cereri in birou:\n");
                persoanaBirou.displayRequest(fw);
            } else if ("entitate juridica".equals(parameters[1])) {
                fw.write( "entitate juridica" + " - cereri in birou:\n");
                entitateJuridicaBirou.displayRequest(fw);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRequestInWaiting(String[] parameters) {
        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            Utilizator user = users.get(parameters[1]);
            if (user == null)
                return;
            ArrayList<Cerere> cereri = user.getRequestInWaiting();

            fw.write(parameters[1] + " - cereri in asteptare:\n");

            for (Cerere req: cereri) {
                fw.write(dateFormat.format(req.getDate()) + " - ");
                fw.write(user.writeRequest(req.getReq()));
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRequest(String[] parameters) {
        Utilizator user = users.get(parameters[1]);
        if (user == null)
            return;
        Cerere.RequestType requestType = null;
        if (parameters[2].equals("inlocuire buletin")) {
            requestType = Cerere.RequestType.inlocuire_buletin;
        } else if (parameters[2].equals("inregistrare venit salarial")) {
            requestType = Cerere.RequestType.inregistrare_venit_salarial;
        } else if (parameters[2].equals("inlocuire carnet de elev")) {
            requestType = Cerere.RequestType.inlocuire_carnet_de_elev;
        } else if (parameters[2].equals("creare act constitutiv")) {
            requestType = Cerere.RequestType.creare_act_constitutiv;
        } else if (parameters[2].equals("reinnoire autorizatie")) {
            requestType = Cerere.RequestType.reinnoire_autorizatie;
        } else if (parameters[2].equals("inlocuire carnet de sofer")) {
            requestType = Cerere.RequestType.inlocuire_carnet_de_sofer;
        } else if (parameters[2].equals("inregistrare cupoane de pensie")) {
            requestType = Cerere.RequestType.inregistrare_cupoane_de_pensie;
        }

        if (!checkPermision(user, requestType))
            return;
        try {
            Cerere cerere = new Cerere(Integer.parseInt(parameters[4]), requestType, dateFormat.parse(parameters[3]));
            user.addToWaiting(cerere);
            String userType = user.getClas();

            if ("entitate juridica".equals(userType)) {
               entitateJuridicaBirou.add(((EntitateJuridica)user), cerere);
            } else if ("pensionar".equals(userType)) {
                pensionarBirou.add(((Pensionar)user), cerere);
            } else if ("elev".equals(userType)) {
                elevBirou.add(((Elev)user), cerere);
            } else if ("persoana".equals(userType)) {
                persoanaBirou.add(((Persoana)user), cerere);
            } else if ("angajat".equals(userType)) {
                angajatBirou.add(((Angajat)user), cerere);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPermision(Utilizator user, Cerere.RequestType requestType) {
        String value = null;

        if (requestType == Cerere.RequestType.inlocuire_buletin && user instanceof EntitateJuridica) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip inlocuire buletin\n";
        } if (requestType == Cerere.RequestType.inregistrare_cupoane_de_pensie && !(user instanceof Pensionar)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip inregistrare cupoane de pensie\n";
        } if (requestType == Cerere.RequestType.inlocuire_carnet_de_elev && !(user instanceof Elev)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip inlocuire carnet de elev\n";
        } if (requestType == Cerere.RequestType.inregistrare_venit_salarial && !(user instanceof Angajat)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip inregistrare venit salarial\n";
        } if (requestType == Cerere.RequestType.creare_act_constitutiv && !(user instanceof EntitateJuridica)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip creare act constitutiv\n";
        } if (requestType == Cerere.RequestType.reinnoire_autorizatie && !(user instanceof EntitateJuridica)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip reinnoire autorizatie\n";
        } if (requestType == Cerere.RequestType.inlocuire_carnet_de_sofer && (user instanceof Elev || user instanceof EntitateJuridica)) {
            value = "Utilizatorul de tip " + user.getClas() + " nu poate inainta o cerere de tip inlocuire carnet de sofer\n";
        }

        if (value == null)
            return true;

        try {
            FileWriter fw = new FileWriter("./src/main/resources/output/" + filename, true);
            fw.write(value);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void removeRequest(String[] parameters) {
        Utilizator user = users.get(parameters[1]);
        try {
            Date date = dateFormat.parse(parameters[2]);
            user.deleteRequest(date);
            String userType = user.getClas();

            if ("entitate juridica".equals(userType)) {
                entitateJuridicaBirou.removeReq(date);
            } else if ("pensionar".equals(userType)) {
                pensionarBirou.removeReq(date);
            } else if ("elev".equals(userType)) {
                elevBirou.removeReq(date);
            } else if ("persoana".equals(userType)) {
                persoanaBirou.removeReq(date);
            } else if ("angajat".equals(userType)) {
                angajatBirou.removeReq(date);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void addUser(String[] parameters) {
        Utilizator user = null;

        if (parameters[1].equals("angajat")) {
            user = new Angajat(parameters[2], parameters[3]);
        } else if (parameters[1].equals("pensionar")) {
            user = new Pensionar(parameters[2]);
        } else if (parameters[1].equals("elev")) {
            user = new Elev(parameters[2], parameters[3]);
        } else if (parameters[1].equals("entitate juridica")) {
            user = new EntitateJuridica(parameters[2], parameters[3]);
        } else {
            user = new Persoana(parameters[2]);
        }

        users.put(parameters[2], user);
    }

}
