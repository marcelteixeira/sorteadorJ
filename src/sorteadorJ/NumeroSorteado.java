package sorteadorJ;


public class NumeroSorteado {

    private int numero;
    private String timeStamp = new java.util.Date().toString();

    public NumeroSorteado(int numero) {

        setNumero(numero);

    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public int getNumero() {
        return this.numero;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

}
