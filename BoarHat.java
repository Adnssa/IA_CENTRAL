/*!
 * REpresentacion del estado del problema del viajante de comercio
 */

import java.util.Random;
import java.util.ArrayList;

import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Clientes;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;

public class BoarHat {

  /// String que describe el operador
  public static String INTERCAMBIO = "Movimiento";


  // [id] -> Central
  private static Centrales centrals;

  // [id] -> Client
  private static Clientes clientes;

  // [clientId] -> central|null
  private int[] clients;

  // [centralId] -> prod|null
  private double[] prodLeft;

  /// Distancias entre las ciudades
  //private int [][] dist;

  /*!\brief Genera una instancia del problema del TSP
   *
   * Crea una nueva instancia del problema del viajante de comercion con nc ciudades
   *
   * @param [in] nc Numero de ciudades
   */
  public BoarHat(int nc) {

      int[] percent = {8, 1, 1};
      int ncli = 3;
      double[] propc = {0.3, 0.3, 0.4};
      double propg = 0.1;
      int seed = 10;

      try {
          // INIT CENTRALS
          centrals = new Centrales(percent, seed);
          prodLeft = new double[centrals.size()];
          for(int i = 0; i < centrals.size(); i++){ //prodLeft[i] == produccio total central
            prodLeft[i] = centrals.get(i).getProduccion();
          }
          System.out.println(centrals);
          System.out.println(centrals.get(0).getTipo());
          // INIT CLIENTS
          clientes = new Clientes(ncli, propc, propg, seed);
          System.out.println(clientes);
      } catch(java.lang.Exception e) {
          System.out.println("Too Little Centrals");
          int[] centralssss = {1};
      }

      // if (centrals == null) {
      //     System.out.println("Empty");
      // } else {
      //     System.out.println(centrals);
      // }
      return;
  }

  /*private int[] initialSol() {
      int [] solution;
      int iCent = 0;
      int iCli = 0;
      double prodLeft, prodInit = centrals.get(0).getProduccion();
      while (true) {
          if (prodLeft >= (prodInit*0.75)) {
              if (iCent < centrals.size()-1) {
                  iCent++;
                  prodLeft = prodInit = centrals.get(iCent).getProduccion();
              } else {
                  // TODO: WHAT IF WE CANT SUPPLY ALL THE CUSTOMERS
                  break;
              }
          } else {
              if (clientes.get(iCli).getConsumo() <= prodLeft) {
                  solution.add(iCent);
                  prodLeft -= clientes.get(iCli).getConsumo();
                  if (iCli < clientes.size()-1) {
                      iCli++;
                  } else {
                      //end
                      break;
                  }
              } else {
                  if (iCent < centrals.size()-1) {
                      iCent++;
                      prodLeft = prodInit = centrals.get(iCent).getProduccion();
                  } else {
                      // TODO: WHAT IF WE CANT SUPPLY ALL THE CUSTOMERS
                      break;
                  }
              }
          }

      }

      return solution;
  }
*/
  public int getNCentrals(){
    return centrals.size();
  }

  public int getNClients(){
    return clientes.size();
  }

  public double prodTotal(int i){
    return centrals.get(i).getProduccion();
  }

  public double prodOcupada(int i){
    return centrals.get(i).getProduccion() - prodLeft[i];
  }

  public double getDistancia(int cent, int cli){
      int centX, centY, cliX, cliY;
      centX = centrals.get(cent).getCoordX();
      centY = centrals.get(cent).getCoordY();
      cliX = clientes.get(cli).getCoordX();
      cliY = clientes.get(cli).getCoordY();
      double aux = Math.pow(cliX - centX, 2) + Math.pow(cliY - centY, 2);
      aux = Math.sqrt(aux);
      return aux;
  }

  public double produccioReal(int cent, int cli){
      double demanda = clientes.get(cli).getConsumo();
      double dist = getDistancia(cent, cli);
      demanda += VEnergia.getPerdida(dist)*demanda;
      return demanda;
  }

}
