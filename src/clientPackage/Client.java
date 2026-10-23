package clientPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        String serveur = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(serveur, port);
             Scanner clavier = new Scanner(System.in);
             PrintWriter sortie = new PrintWriter(
                     socket.getOutputStream(), true);
             BufferedReader entree = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

            System.out.print("Entrez une opération (exemple : 55 * 25) : ");
            String operation = clavier.nextLine();

            sortie.println(operation);

            String resultat = entree.readLine();
            System.out.println("Réponse du serveur : " + resultat);

        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
