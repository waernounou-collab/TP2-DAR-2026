
package clientPackage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client2 {

    public static void main(String[] args) {

        String serveur = "localhost";
        int port = 5001;

        try (Socket socket = new Socket(serveur, port);
             ObjectOutputStream sortie =
                     new ObjectOutputStream(socket.getOutputStream())) {

            sortie.flush();

            try (ObjectInputStream entree =
                         new ObjectInputStream(socket.getInputStream());
                 Scanner clavier = new Scanner(System.in)) {

                System.out.print("Premier nombre : ");
                double a = clavier.nextDouble();

                System.out.print("Opérateur (+, -, *, /) : ");
                String operateur = clavier.next();

                System.out.print("Deuxième nombre : ");
                double b = clavier.nextDouble();

                if (!operateur.matches("[+*/-]")) {
                    System.out.println("Opérateur invalide.");
                    return;
                }

                if (operateur.equals("/") && b == 0) {
                    System.out.println("Division par zéro impossible.");
                    return;
                }

                Operation operation =
                        new Operation(a, operateur, b);

                sortie.writeObject(operation);
                sortie.flush();

                Object reponse = entree.readObject();
                System.out.println("Réponse du serveur : " + reponse);
            }

        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
