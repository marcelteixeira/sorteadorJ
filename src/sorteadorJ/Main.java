package sorteadorJ;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {

    Sorteio sorteio;


    @Override
    public void start(Stage primaryStage) throws Exception {
        CheckMenuItem cmiPermiteRepeticao = new CheckMenuItem("Permitir números repetidos");
        CheckMenuItem cmiTelaCheia = new CheckMenuItem("Tela Cheia");
        MenuItem miExportar = new MenuItem("Exportar Log");
        MenuItem miSair = new MenuItem("Sair");
        MenuItem miSobre = new MenuItem("Sobre...");

        Menu mnArquivo = new Menu("Arquivo");
        Menu mnConfiguracoes = new Menu("Configurações");
        Menu mnSobre = new Menu("Ajuda");

        MenuBar mbMenuPrincipal = new MenuBar();

        Label lbNumeros = new Label("Números do Sorteio");
        Label lbNumeroMin = new Label("Minimo: ");
        Label lbNumeroMax = new Label("Máximo: ");
        Label lbNumeroSorteado = new Label("?");
        Label lbUltimosSorteados = new Label("Últimos Números:");

        TextField tfNumeroMin = new TextField();
        TextField tfNumeroMax = new TextField();

        TextArea taNumeroSorteados = new TextArea();

        Button btOk = new Button("Ok!");
        Button btSortear = new Button("Sortear");
        Button btReiniciar = new Button("Reiniciar");
        Button btFinalizar = new Button("Finalizar");

        HBox hbMenuPrincipal = new HBox();
        HBox hbConfigSorteio = new HBox();
        HBox hbExibicaoBotoes = new HBox();
        VBox vbNumeroSorteado = new VBox();
        VBox vbExibicaoNumerosAnteriores = new VBox();
        VBox vbBotoes = new VBox();
        VBox vbPrincipal = new VBox();

        Scene cenaPrincipal;







        miExportar.setDisable(true);
        miExportar.setOnAction(e -> {
            sorteio.exportarNumerosSorteados();
        });

        miSair.setOnAction(e -> {
            if (confirmarAcao()) {
                System.exit(0);
            }
        });
        miSobre.setOnAction(e -> {



            Alert alSobre = new Alert(Alert.AlertType.INFORMATION);
            alSobre.setTitle("Sobre...");
            alSobre.setHeaderText("Sorteador_J");

            alSobre.setContentText("Desenvolvido por Marcel M. Teixeira.");
            alSobre.show();

        });

        mnArquivo.getItems().addAll(miExportar, miSair);

        cmiTelaCheia.setSelected(false);
        cmiTelaCheia.setOnAction(e -> {
            if(primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(false);
                cmiTelaCheia.setSelected(false);
            }else {
                primaryStage.setFullScreen(true);
                cmiTelaCheia.setSelected(true);
            }
        });
        mnConfiguracoes.getItems().addAll(cmiPermiteRepeticao, cmiTelaCheia);

        mnSobre.getItems().addAll(miSobre);

        mbMenuPrincipal.prefWidthProperty().bind(primaryStage.widthProperty());
        mbMenuPrincipal.getMenus().addAll(mnArquivo, mnConfiguracoes, mnSobre);


        hbMenuPrincipal.getChildren().add(mbMenuPrincipal);


        //Intervalo de números do sorteio
        tfNumeroMin.setPrefWidth(70);
        tfNumeroMax.setPrefWidth(70);

        btOk.setOnAction(e -> {

            if(verificarIntervaloDeNumeros(tfNumeroMin.getText(), tfNumeroMax.getText())) {

                int numeroMinimo = Integer.parseInt(tfNumeroMin.getText());
                int numeroMaximo = Integer.parseInt(tfNumeroMax.getText());
                sorteio = new Sorteio(numeroMinimo, numeroMaximo, cmiPermiteRepeticao.isSelected());
                btOk.setDisable(true);
                miExportar.setDisable(false);
                cmiPermiteRepeticao.setDisable(true);
                tfNumeroMin.setDisable(true);
                tfNumeroMax.setDisable(true);
                btSortear.setDisable(false);
                btFinalizar.setDisable(false);
                btReiniciar.setDisable(false);


            }else {

                tfNumeroMin.setText("");

                tfNumeroMax.setText("");

            }





        });

        hbConfigSorteio.setPrefHeight(50);

        hbConfigSorteio.setAlignment(Pos.CENTER);
        hbConfigSorteio.setPadding(new Insets(15));
        hbConfigSorteio.setSpacing(10);

        hbConfigSorteio.getChildren().addAll(lbNumeros, lbNumeroMin, tfNumeroMin, lbNumeroMax, tfNumeroMax, btOk);


        // Exibir número sorteado


        lbNumeroSorteado.setFont(Font.font(280));

        btSortear.setDisable(true);
        btSortear.setOnAction(e -> {

            if(sorteio.possibilidadesRestantes == 0) {
                // Janela de aviso link: https://aprendendo-javafx.blogspot.com/2015/03/as-janelas-de-dialogos-do-javafx.html
                Alert alSemPossibilidades = new Alert(Alert.AlertType.WARNING);
                alSemPossibilidades.setTitle("Aviso!");
                alSemPossibilidades.setHeaderText("Todos os números foram sorteados!");
                alSemPossibilidades.setContentText("Não existem mais números para sortear.");
                alSemPossibilidades.show();
                System.err.println("Sem numeros restantes");
            }else {
                int n = sorteio.sortearNumero();
                lbNumeroSorteado.setText(String.valueOf(n));
                taNumeroSorteados.setText( String.valueOf(n) + "    " + taNumeroSorteados.getText()) ;

            }

        });

        btFinalizar.setDisable(true);
        btFinalizar.setOnAction(e -> {
            if(confirmarAcao()) {

                sorteio.exportarNumerosSorteados();

                sorteio = null;
                tfNumeroMin.setDisable(false);
                tfNumeroMin.setText("");

                tfNumeroMax.setDisable(false);
                tfNumeroMax.setText("");

                lbNumeroSorteado.setText("?");

                btOk.setDisable(false);
                btSortear.setDisable(true);
                btReiniciar.setDisable(true);
                cmiPermiteRepeticao.setDisable(false);
                taNumeroSorteados.setText("");

            }
        });

        btReiniciar.setDisable(true);
        btReiniciar.setOnAction(e -> {



            if(confirmarAcao()) {
                sorteio = null;
                tfNumeroMin.setDisable(false);
                tfNumeroMin.setText("");

                tfNumeroMax.setDisable(false);
                tfNumeroMax.setText("");

                lbNumeroSorteado.setText("?");
                taNumeroSorteados.setText("");

                btOk.setDisable(false);
                btSortear.setDisable(true);
                btReiniciar.setDisable(true);
                cmiPermiteRepeticao.setDisable(false);
            }

        });


        Hyperlink hpLink = new Hyperlink("Desenvolvido por Marcel M. Teixeira - GitHub");
        hpLink.setOnAction(e ->{
            try {
                URI uri = new URI("https://github.com/marcelteixeira");

                if(Desktop.isDesktopSupported()) {

                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(uri);


                }

            }catch(URISyntaxException error) {
                System.err.println(error.getMessage());
            }catch( IOException error) {
                System.err.println(error.getMessage());
            }

        });


        vbNumeroSorteado.setAlignment(Pos.CENTER);
        vbNumeroSorteado.getChildren().addAll(lbNumeroSorteado,btSortear);



        taNumeroSorteados.setPrefSize(400, 100);
        taNumeroSorteados.maxWidth(200);
        taNumeroSorteados.setEditable(false);
        taNumeroSorteados.setWrapText(true);
        taNumeroSorteados.setFont(Font.font(18));

        vbExibicaoNumerosAnteriores.getChildren().addAll(lbUltimosSorteados, taNumeroSorteados);






        vbBotoes.setSpacing(15);
        vbBotoes.setAlignment(Pos.BOTTOM_RIGHT);
        vbBotoes.getChildren().addAll(btReiniciar, btFinalizar, hpLink);

        hbExibicaoBotoes.setPadding(new Insets(20));
        hbExibicaoBotoes.setSpacing(40);
        hbExibicaoBotoes.setAlignment(Pos.BOTTOM_CENTER);
        hbExibicaoBotoes.getChildren().addAll(vbExibicaoNumerosAnteriores, vbBotoes);



        ToolBar tbRodape = new ToolBar();


        tbRodape.prefWidthProperty().bind(primaryStage.widthProperty());
        tbRodape.getItems().add(new Label("teste"));



        vbPrincipal.setSpacing(20);
        vbPrincipal.prefHeightProperty().bind(primaryStage.heightProperty());
        vbPrincipal.getChildren().add(hbMenuPrincipal);
        vbPrincipal.getChildren().add(hbConfigSorteio);
        vbPrincipal.getChildren().add(vbNumeroSorteado);
        vbPrincipal.getChildren().add(hbExibicaoBotoes);





        //Scene cenaPrincipal = new Scene(vbPrincipal);
        //Scene cenaPrincipal;
        cenaPrincipal = new Scene(vbPrincipal);

        primaryStage.setTitle("Sorteador_J");;
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.setFullScreen(false);
        primaryStage.setScene(cenaPrincipal);
        primaryStage.show();
    }


    private boolean verificarIntervaloDeNumeros(String tfNumeroMin, String tfNumeroMax) {

        int min;
        int max;
        Alert alError = new Alert(Alert.AlertType.ERROR);
        alError.setTitle("Erro!");

        try {
            min = Integer.parseInt(tfNumeroMin);
            max = Integer.parseInt(tfNumeroMax);

            if (min > max) {
                alError.setHeaderText("Valores digitados inválidos");
                alError.setContentText("Corrija os valores Min e Max.(Valor Max deve ser igual ou maior que o Min.)");
                alError.showAndWait();

                return false;
            }

            if (min < 0 || max <0) {
                alError.setHeaderText("Valores digitados inválidos");
                alError.setContentText("Corrija os valores Min e Max.(Apenas valores positivos são válidos)");
                alError.showAndWait();

                return false;
            }

            return true;

        }catch(NumberFormatException error) {
            alError.setHeaderText("Valores digitados inválidos");
            alError.setContentText("Corrija os valores Min e Max.(Apenas números são aceitos)");
            alError.showAndWait();
            System.err.println("Valor inválido:" + error.getMessage());

        }



        return false;
    }

    private boolean confirmarAcao() {

        Alert alConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        alConfirmacao.setTitle("Confirmar");
        alConfirmacao.setHeaderText("Você tem certeza disso?");
        alConfirmacao.setContentText("Esta ação não poderá ser revertida.");

        Optional<ButtonType> opcao = alConfirmacao.showAndWait();

        if(opcao.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            return true;
        }else {
            return false;
        }

    }


    public static void main(String[] args) {
        launch(args);


        }



}
