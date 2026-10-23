
package serverPackage;

import clientPackage.Operation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {

    public static void main(String[] args) {

        int port = 5001;

        try (ServerSocket serveur = new ServerSocket(port)) {

            System.out.println("Serveur objet en attente...");

            try (Socket socket = serveur.accept();
                 ObjectOutputStream sortie =
                         new ObjectOutputStream(socket.getOutputStream())) {

                sortie.flush();

                try (ObjectInputStream entree =
                             new ObjectInputStream(socket.getInputStream())) {

                    System.out.println("Client connecté.");

                    Object objet = entree.readObject();

                    if (!(objet instanceof Operation)) {
                        sortie.writeObject("Erreur : objet invalide.");
                        sortie.flush();
                        return;
                    }

                    Operation operation = (Operation) objet;

                    double a = operation.getOperande1();
                    double b = operation.getOperande2();
                    String op = operation.getOperateur();

                    double resultat;

                    switch (op) {
                        case "+":
                            resultat = a + b;
                            break;
                        case "-":
                            resultat = a - b;
                            break;
                        case "*":
                            resultat = a * b;
                            break;
                        case "/":
                            if (b == 0) {
                                sortie.writeObject(
                                        "Erreur : division par zéro.");
                                sortie.flush();
                                return;
                            }
                            resultat = a / b;
                            break;
                        default:
                            sortie.writeObject("Erreur : opérateur inconnu.");
                            sortie.flush();
                            return;
                    }

                    sortie.writeObject("Résultat = " + resultat);
                    sortie.flush();

                    System.out.println("Calcul effectué : "
                            + a + " " + op + " " + b);
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
