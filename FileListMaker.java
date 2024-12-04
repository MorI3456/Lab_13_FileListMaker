import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileListMaker {

    private static List<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item");
            System.out.println("D - Delete an item");
            System.out.println("I - Insert an item");
            System.out.println("M - Move an item");
            System.out.println("V - View the list");
            System.out.println("O - Open a list file");
            System.out.println("S - Save the current list");
            System.out.println("C - Clear the list");
            System.out.println("Q - Quit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A":
                        addItem(scanner);
                        break;
                    case "D":
                        deleteItem(scanner);
                        break;
                    case "I":
                        insertItem(scanner);
                        break;
                    case "M":
                        moveItem(scanner);
                        break;
                    case "V":
                        viewList();
                        break;
                    case "O":
                        openFile(scanner);
                        break;
                    case "S":
                        saveFile();
                        break;
                    case "C":
                        clearList(scanner);
                        break;
                    case "Q":
                        exit = quitProgram(scanner);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter item to add: ");
        itemList.add(scanner.nextLine());
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner scanner) {
        viewList();
        System.out.print("Enter index to delete: ");
        int index = Integer.parseInt(scanner.nextLine());
        if (index >= 0 && index < itemList.size()) {
            itemList.remove(index);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void insertItem(Scanner scanner) {
        viewList();
        System.out.print("Enter index to insert at: ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter item to insert: ");
        String item = scanner.nextLine();
        if (index >= 0 && index <= itemList.size()) {
            itemList.add(index, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid index.");
        }
    }

    private static void moveItem(Scanner scanner) {
        viewList();
        System.out.print("Enter index to move from: ");
        int fromIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter index to move to: ");
        int toIndex = Integer.parseInt(scanner.nextLine());

        if (fromIndex >= 0 && fromIndex < itemList.size() && toIndex >= 0 && toIndex <= itemList.size()) {
            String item = itemList.remove(fromIndex);
            itemList.add(toIndex, item);
            needsToBeSaved = true;
        } else {
            System.out.println("Invalid indices.");
        }
    }

    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < itemList.size(); i++) {
            System.out.println(i + ": " + itemList.get(i));
        }
    }

    private static void openFile(Scanner scanner) throws IOException {
        if (needsToBeSaved) {
            System.out.print("Unsaved changes detected. Do you want to save first? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                saveFile();
            }
        }

        System.out.print("Enter filename to open: ");
        String fileName = scanner.nextLine();
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            itemList = Files.readAllLines(path);
            currentFileName = fileName;
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void saveFile() throws IOException {
        if (currentFileName == null) {
            System.out.print("Enter filename to save as: ");
            Scanner scanner = new Scanner(System.in);
            currentFileName = scanner.nextLine();
        }
        Files.write(Paths.get(currentFileName), itemList);
        needsToBeSaved = false;
        System.out.println("File saved successfully.");
    }

    private static void clearList(Scanner scanner) {
        System.out.print("Are you sure you want to clear the list? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            itemList.clear();
            needsToBeSaved = true;
        }
    }

    private static boolean quitProgram(Scanner scanner) throws IOException {
        if (needsToBeSaved) {
            System.out.print("Unsaved changes detected. Do you want to save before quitting? (y/n): ");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                saveFile();
            }
        }
        return true;
    }
}
