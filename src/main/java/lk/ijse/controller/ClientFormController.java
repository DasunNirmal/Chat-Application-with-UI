package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

public class ClientFormController {
    @FXML
    private TextField txtGetMassage;

    @FXML
    private TextArea txtShowMessage;

    String message = "";
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;

    public void initialize() {
        txtShowMessage.setEditable(false);

        new Thread(()->{
            try {
                Socket socket = new Socket("localhost",3001);

                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (!message.equals("END")) {
                    message = dataInputStream.readUTF();
                    txtShowMessage.appendText("\nServer :" + message);
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
}
