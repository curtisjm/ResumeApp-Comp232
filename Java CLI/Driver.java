import io.minio.*;
import io.minio.messages.Item;

import java.util.Scanner;

public class Driver {

    private Scanner scan = new Scanner(System.in);
    private String input;
    private MinioClient minioClient = MinioClient.builder()
            .endpoint("http://localhost:9000")
            .credentials("admin", "Nelsondog7")
            .build();

    private void uploadFile() {
        System.out.println();
        // sample values:
            // UnitCircle.png
        // \Users\curti\Desktop\Unit Circle.gif
        String object, filepath;
        System.out.println("--- Upload File ---");
        System.out.println("Enter the file name: ");
        object = scan.nextLine();
        System.out.println("Enter the path to the file: ");
        filepath = scan.nextLine();
        try {
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("test-bucket")
                    .object(object)
                    .filename(filepath)
                    .build());
            System.out.println("Uploaded " + object + "...");
        // super lazy exception handling:
        } catch(Exception e) {
            System.out.println("ERROR: File path may be incorrect. Or maybe other stuff went wrong... who knows");
        }
        mainMenu();
    }

    // download doesn't seem to go through until program ends
    private void downloadFile() {
        System.out.println();
        // sample value: UnitCircle.png
        String object;
        System.out.println("--- Download File ---");
        System.out.println("Enter the file name: ");
        object = scan.nextLine();
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket("test-bucket")
                    .object(object)
                    .filename(object)
                    .build());
            System.out.println("Downloaded " + object + "...");
        // more super lazy exception handling:
        } catch(Exception e) {
            System.out.println("ERROR: File name may be incorrect. Or maybe other stuff went wrong... who knows");
        }
        mainMenu();
    }

    private void listObjects() {
        System.out.println();
        System.out.println("--- File List ---");
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("test-bucket").recursive(true).build());
            for(Result<Item> i : results) {
                System.out.println(i.get().objectName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        mainMenu();
    }

    private void mainMenu() {
        System.out.println();
        System.out.println("--- Main Menu ---");
        System.out.println("Would you like to upload a file or download a file? (Enter \"up\" or \"down\") You can also list all files by typing \"list\". Type \"stop\" to stop the program.");
        input = scan.nextLine();

        while(!input.equalsIgnoreCase("up") && !input.equalsIgnoreCase("down") && !input.equalsIgnoreCase("end") && !input.equalsIgnoreCase("list")) {
            System.out.println("Invalid input. Enter \"up\", \"down\", or \"list\".");
            input = scan.nextLine();
        }

        switch(input.toLowerCase()) {
            case "up": uploadFile();
                break;
            case "down": downloadFile();
                break;
            case "list": listObjects();
                break;
            case "end":
                System.out.println("Ending program...");
                System.exit(0);
                break;
        }
    }

    public static void main(String[] args) throws Exception {
        Driver driver = new Driver();
        driver.mainMenu();
    }
}
