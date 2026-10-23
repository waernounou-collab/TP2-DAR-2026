
package serverPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serveur = new ServerSocket(port)) {

            System.out.println("Serveur en attente du client...");

            try (Socket socket = serveur.accept();
                 BufferedReader entree = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()));
                 PrintWriter sortie = new PrintWriter(
                         socket.getOutputStream(), true)) {

                System.out.println("Client connecté.");

                String operation = entree.readLine();
                System.out.println("Opération reçue : " + operation);

                try {
                    double resultat = calculer(operation);
                    sortie.println("Résultat = " + resultat);
                } catch (IllegalArgumentException e) {
                    sortie.println("Erreur : " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }

    private static double calculer(String operation) {

        if (operation == null) {
            throw new IllegalArgumentException("Aucune opération reçue.");
        }

        String regex =
                "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+*/-])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$";

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile(regex).matcher(operation);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Syntaxe incorrecte. Exemple : 55 * 25");
        }

        double a = Double.parseDouble(matcher.group(1));
        String operateur = matcher.group(2);
        double b = Double.parseDouble(matcher.group(3));

        switch (operateur) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new IllegalArgumentException(
                            "Division par zéro impossible.");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Opérateur inconnu.");
        }
    }
}
