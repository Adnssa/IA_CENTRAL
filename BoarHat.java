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
  /*public BoarHat(int nc) {

      int [] percent = {8, 1, 1};
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
  */

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

  public BoarHat(int [] percent, int seedCent, int ncli, double [] propc, double propg, int seedCli){
    try{
    //INIT Centrals
    centrals = new Centrales(percent, seedCent);
    prodLeft = new double[centrals.size()];
    for(int i = 0; i < centrals.size(); i++){
      prodLeft[i] = centrals.get(i).getProduccion();
    }
    //INIT CLIENTS
    clientes = new Clientes(ncli, propc, propg, seedCli);
    clients = new int[clientes.size()];
    int cent = 0;
    int cli = 0;
    boolean run = true;
    while (run){ //Assignem tots els garantitzats
        double prod = produccioReal(cent, cli);
        if(prod < prodLeft[cent]) { //Posem el client a la central
          if(clientes.get(cli).getContrato() == 0){
          clients[cli] = cent;
          prodLeft[cent]-=prod;
          }
          else {
            clients[cli] = prodLeft.length + 1; //Als que no podem servir (de moment els no garantitzats els hi assignem una central que no existeix)
          }
          cli++;
        }
        else cent ++;
        if(cli >= clients.length) run = false;
        else if (cent >= prodLeft.length) run = false;
        else if (cent >= centrals.size()) run = false;

    }
    cent = cli = 0;
    run = true;
    while (run){ //Assignem tots els possibles no garantitzats
        double prod = produccioReal(cent, cli);
        if(prod < prodLeft[cent]) { //Posem el client a la central
          if(!clientAssignat(cli)) {
          clients[cli] = cent;
          prodLeft[cent]-=prod;
          }
          cli++;
        }
        else cent ++;
        if(cli >= clients.length) run = false;
        else if (cent >= prodLeft.length) run = false;
        else if (cent >= centrals.size()) run = false;

    }
    } catch(java.lang.Exception e) {
        System.out.println(e);
    }

  }


  public BoarHat(int [] clients2, double [] prodLeft2){ //Constructora per generar successors

      try{
      clients = new int[clients2.length];
      prodLeft = new double[prodLeft2.length];
      for(int i = 0; i < clients2.length; i++){
        clients[i] = clients2[i];
      }
      for(int i = 0; i < prodLeft2.length; i++){
        prodLeft[i] = prodLeft2[i];
      }
    }catch(java.lang.Exception e) {
        System.out.println("Excepcio Successor");

    }

  }

  public boolean clientAssignat(int cli){
    if(clients[cli] == prodLeft.length + 1) return false;
    else return true;
  }

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
  public int [] getClients(){
    return clients;
  }

  public double [] getProdLeft(){
    return prodLeft;
  }

  public boolean move (int cli, int cent){
    double prod = produccioReal(cent, cli);
    if(clientAssignat(cli)){
      int centPre = clients[cli];
      if(prodLeft[cent] < prod) return false;
      prodLeft[centPre]+= prod;
      prodLeft[cent]-= prod;
      clients[cli] = cent;
      return true;
    }
    else {
      if(prodLeft[cent] < prod) return false;
      prodLeft[cent]-= prod;
      clients[cli] = cent;
      return true;
    }
  }

}
