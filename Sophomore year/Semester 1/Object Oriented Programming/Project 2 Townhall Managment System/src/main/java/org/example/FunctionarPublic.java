package org.example;

import org.example.users.Utilizator;

public class FunctionarPublic {
    public final String name;

    public FunctionarPublic(String name) {
        this.name = name;
    }

    public <T extends Utilizator> void solve(Cerere req, T user) {
        user.deleteRequest(req.getDate());
        user.addSolve(req);
    }
}
