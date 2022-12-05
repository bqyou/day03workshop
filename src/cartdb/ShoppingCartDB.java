package cartdb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ShoppingCartDB {

    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String SAVE = "save";
    public static final String USERS = "users";
    public static final String LIST = "list";
    public static final String EXIT = "exit";
    public static final List<String> VALIDCOMMANDS = Arrays.asList(LOGIN, ADD, SAVE, USERS, LIST, EXIT);
    private ShoppingCartMemory db;
    private String currentUser;
    private String folderName;

    public ShoppingCartDB() {

        this.folderName = "db"; // default
        this.db = new ShoppingCartMemory(folderName);
    }

    public ShoppingCartDB(String folderName) {

        this.folderName = folderName;
        this.db = new ShoppingCartMemory(folderName);
    }

    public void setUp() {
        Path p = Paths.get(this.folderName);
        File f = p.toFile();
        if (!f.isDirectory()) {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void startShell() {

        System.out.println("welcome to multi user cart");

        Scanner sc = new Scanner(System.in);
        String line;
        Boolean flag = true;
        while (flag) {
            line = sc.nextLine();
            line = line.trim();

            System.out.println("=> " + line);

            if (this.validateInput(line)) {
                System.out.println("Processing: " + line);
                processInput(line);

            } else {
                System.out.println("Invalid command");
            }

            if (line.equalsIgnoreCase("exit")) {
                flag = false;
                System.out.println("Exiting program!");
            }

        }

        sc.close();

    }

    public Boolean validateInput(String input) {
        String[] parts = input.split(" ");
        String command = parts[0].trim();
        return VALIDCOMMANDS.contains(command);
    }

    public void processInput(String input) {
        Scanner sc = new Scanner(input);
        String command = sc.next().trim();

        switch (command) {
            case LOGIN:
                String username = sc.nextLine().trim();
                this.loginAction(username);
                System.out.println("Hi " + this.currentUser + ", welcome to your shopping cart.");
                break;

            case LIST:
                this.listItems();
                break;

            case ADD:
                String[] items = sc.nextLine().trim().split(",");
                this.addItems(items);
                break;

            case SAVE:
                this.save();
                break;

            case USERS:
                this.listUsers();
                break;

            default:
                break;

        }

        sc.close();
    }

    public void loginAction(String username) {
        if (!this.db.cartDb.containsKey(username)) {
            this.db.cartDb.put(username, new ArrayList<String>());
        }
        this.currentUser = username;
    }

    public void addItems(String[] items) {
        for (String i : items) {
            this.db.cartDb.get(this.currentUser).add(i.trim());
            System.out.printf("%s added to cart\n", i.trim());
        }
    }

    public void listItems() {
        if (this.db.cartDb.get(this.currentUser).size() == 0) {
            System.out.println("You have an empty shopping cart, starting add items!\n");
        } else {
            for (Integer i = 0; i < this.db.cartDb.get(this.currentUser).size(); i++) {
                System.out.printf("%d. %s \n", i + 1, this.db.cartDb.get(this.currentUser).get(i));
            }
        }
    }

    public void save() {

        for (String key : this.db.cartDb.keySet()) {
            String tmpUser = key;
            ArrayList<String> tmpArray = this.db.cartDb.get(key);
            String path = this.folderName + "/" + tmpUser + ".db";
            try {
                FileWriter fw = new FileWriter(path, false);
                fw.write("cartdb/" + tmpUser + ".db" + "\n");
                for (String item : tmpArray) {
                    fw.write(item + "\n");
                }
                fw.flush();
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void listUsers() {
        for (String key : this.db.cartDb.keySet()) {
            System.out.println(key);
        }
    }
}