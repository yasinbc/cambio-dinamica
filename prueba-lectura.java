import java.io.*;

public class PruebaLectura {

    public static void main(String[] args){
        System.err.println("imprime");
    }

    // Nuevo método para leer el archivo de entrada.
    public static DatosEntrada leerArchivoEntrada(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            int numeroDeMonedas = Integer.parseInt(br.readLine());
            int[] denominaciones = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int cantidad = Integer.parseInt(br.readLine());

            return new DatosEntrada(numeroDeMonedas, denominaciones, cantidad);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
