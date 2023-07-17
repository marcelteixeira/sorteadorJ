package sorteadorJ;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Sorteio {


    int numeroMinimo;
    int numeroMaximo;
    int possibilidadesRestantes;
    boolean permiteRepetido;
    ArrayList<NumeroSorteado> numerosSorteados = new ArrayList<NumeroSorteado>();


    public Sorteio(int numeroMinimo, int numeroMaximo, boolean permiteRepetido){
        this.numeroMinimo = numeroMinimo;
        this.numeroMaximo = numeroMaximo;
        this.permiteRepetido = permiteRepetido;
        this.possibilidadesRestantes = (numeroMaximo - numeroMinimo) + 1;

        System.out.println(toString());
    }

    public int sortearNumero() {
        int novoNumero;
        boolean isNumeroValido = false;

        if(permiteRepetido) {
            novoNumero = new Random().nextInt(numeroMinimo, numeroMaximo + 1);
            System.out.println("Novo Numero: " + novoNumero + " ");
            return novoNumero;
        }else {

            while(isNumeroValido == false && possibilidadesRestantes > 0) {
                isNumeroValido = true;
                novoNumero = new Random().nextInt(numeroMinimo, numeroMaximo + 1);
                System.out.println("Novo Numero: " + novoNumero + " ");


                for(NumeroSorteado numeroSorteado : numerosSorteados) {

                    if(numeroSorteado.getNumero() == novoNumero) {
                        isNumeroValido = false;

                    }

                }

                if(isNumeroValido) {
                    numerosSorteados.add(new NumeroSorteado(novoNumero));
                    System.out.println("*** " + novoNumero + " Adicionado. ***");
                    possibilidadesRestantes--;
                    return novoNumero;

                }

            }

        }

        return -1;

    }


    @Override
    public String toString() {
        return String.format("Sorteio - Min: %s Max: %s Repetidos: %s - Possibilidades: %d", numeroMinimo, numeroMaximo, permiteRepetido, possibilidadesRestantes);
    }

    public void exibirNumerosSorteados() {
        if(numerosSorteados.isEmpty()) {
            System.out.println("Nenhum número sorteado anteriormente.");
        }else {
            System.out.println("\nNúmeros sorteados");
            for(NumeroSorteado numero : numerosSorteados) {
                System.out.printf("Número: %d | TimeStamp: %s\n", numero.getNumero(), numero.getTimeStamp());
            }
            System.out.println();
        }

    }

    public void exportarNumerosSorteados() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));


        String local = directoryChooser.showDialog(new Stage()).toString() + "\\Sorteador.log";

        System.out.println(local);

        File arquivoLog = new File(local);


        try {
            FileWriter fileWriter = new FileWriter(arquivoLog);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Núm | Hora");
            printWriter.println("=========");

            int cont = 0;
            for(NumeroSorteado numeroSorteado : numerosSorteados) {

                printWriter.append("\n" + numeroSorteado.getNumero() + " | " + numeroSorteado.getTimeStamp());
            }

            printWriter.close();


            Alert alAviso = new Alert(AlertType.INFORMATION);
            alAviso.setTitle("Processo Concluído");
            alAviso.setHeaderText("Processo finalizado com sucesso");
            //alAviso.setContentText("Este processo foi finalizado com sucesso.");
            alAviso.showAndWait();


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }



    }

}
