package com.example.ResumeService;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    private ArrayList<String> objectNames = new ArrayList<>();

    private MinioClient minioClient = MinioClient.builder()
            .endpoint("http://localhost:9000")
            .credentials("admin", "Nelsondog7")
            .build();

    private void storeObjects() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("test-bucket").recursive(true).build());
            for(Result<Item> i : results) {
                objectNames.add(i.get().objectName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        storeObjects();
        message = "Resume Files:\n";
        for(String s : objectNames) {
            message += s + ", ";
        }
        message = message.substring(0, message.length() - 2);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}