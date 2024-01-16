package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerFormController {
    @FXML
    private Button btnRunClient;

    @FXML
    private TextField txtGetMassage;

    @FXML
    private TextArea txtShowMessage;

    String message = "";
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    private boolean clientIsConnected = false;

    public void initialize() {
        txtShowMessage.setEditable(false);

        new Thread(() ->{
            try {
                ServerSocket serverSocket = new ServerSocket(3001);
                txtShowMessage.appendText("Server Stared");
                Socket socket = serverSocket.accept();
                txtShowMessage.appendText("\nClient Connected...");
                if (!clientIsConnected) {
                    btnRunClient.setDisable(true);
                }
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!message.equals("END")) {
                    message = dataInputStream.readUTF();
                    txtShowMessage.appendText("\nClient : " + message);
                }
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();

            } catch (IOException e) {
                new Alert(Alert.AlertType.INFORMATION,"Connection Closed").show();
            }
        }).start();
    }


    @FXML
    void btnSendONAction(ActionEvent event) throws IOException {
        dataOutputStream.writeUTF(txtGetMassage.getText().trim());
        dataOutputStream.flush();
    }

    @FXML
    public void btnRunClientOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/client_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Client");
        stage.show();

        /*AnchorPane anchorPane2 = FXMLLoader.load(this.getClass().getResource("/view/two_form.fxml"));
        Scene scene1 = new Scene(anchorPane2);
        Stage stage1 = new Stage();
        stage1.setScene(scene1);
        stage1.centerOnScreen();
        stage1.setResizable(false);
        stage1.setTitle("Client Two");
        stage1.show();*/
    }
}
