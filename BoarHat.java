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

  private double valorHeuristic, beneficis;


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
        if(prod < prodLeft[cent]*0.25) { //Posem el client a la central
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
        if(prod < prodLeft[cent]*0.5) { //Posem el client a la central
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
    valorHeuristic = getValue();
    beneficis = getBeneficis();
    } catch(java.lang.Exception e) {
        System.out.println(e);
    }

  }
  public double consumTotal(){
    double consum = 0;
    for(int i = 0; i < clientes.size(); i++){
      consum += clientes.get(i).getConsumo();
    }
    return consum;
  }

  public double produccioTotal(){
    double produccio = 0;
    for(int i = 0; i < centrals.size(); i++){
      produccio+= centrals.get(i).getProduccion();
    }
    return produccio;
  }

  public double ProduccioLeft(){
    double produccioLeft = 0;
    for(int i = 0; i < prodLeft.length; i++){
      produccioLeft += prodLeft[i];
    }
    return produccioLeft;
  }

  public BoarHat(int [] clients2, double [] prodLeft2, double beneficis2){ //Constructora per generar successors

      try{
      beneficis = beneficis2;
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
      double prodA = produccioReal(centPre, cli);
      prodLeft[centPre]+= prodA;
      prodLeft[cent]-= prod;
      clients[cli] = cent;
      beneficis = getBeneficis();
      return true;
    }
    else {
      if(prodLeft[cent] < prod) return false;
      prodLeft[cent]-= prod;
      clients[cli] = cent;
      beneficis = getBeneficis();
      //recalcBenficisCliNoAssig(cli, cent);
      return true;
    }
  }

  public boolean swap(int cli1, int cli2){
    int cent1 = clients[cli1];
    int cent2 = clients[cli2];
    if( clientes.get(cli1).getContrato() == 0 && !clientAssignat(cli2)) return false;
    if( clientes.get(cli2).getContrato() == 0 && !clientAssignat(cli1)) return false;
    return true;
  }

  public void recalcBenficisCliNoAssig(int cli, int cent){
    try{
      int tipo = clientes.get(cli).getTipo();
    int garant = clientes.get(cli).getContrato();
    if(garant == 0){
      beneficis+=clientes.get(cli).getConsumo()*VEnergia.getTarifaClienteGarantizada(tipo);
    }
    else {
      beneficis+=clientes.get(cli).getConsumo()*VEnergia.getTarifaClienteNoGarantizada(tipo);
    }
    }catch(java.lang.Exception e) {
        System.out.println(e);

      }

  }

  public double getBeneficis(){
    try{
    double sum = 0;
    for(int i = 0; i < clients.length; i++){
      int tipo = clientes.get(i).getTipo();
      int garant = clientes.get(i).getContrato();
      if(clientAssignat(i)){
        if(garant == 0){
          sum+=clientes.get(i).getConsumo()*VEnergia.getTarifaClienteGarantizada(tipo);
        }
        else {
          sum+=clientes.get(i).getConsumo()*VEnergia.getTarifaClienteNoGarantizada(tipo);
        }
      }
      else {
        sum-=clientes.get(i).getConsumo()*VEnergia.getTarifaClientePenalizacion(tipo);
      }
    }
    for(int i = 0; i < prodLeft.length; i++){
      int tipo = centrals.get(i).getTipo();
      if(prodLeft[i] != centrals.get(i).getProduccion()){
        sum-=(centrals.get(i).getProduccion()*VEnergia.getCosteProduccionMW(tipo) + VEnergia.getCosteMarcha(tipo) + VEnergia.getCosteParada(tipo));
      }
    }
    return sum;
  } catch(java.lang.Exception e) {
      System.out.println(e);
      return 0;
    }
  }

  public double beneficis(){
    return beneficis;
  }

  public int clientsNoAssignats(){
    int sum = 0;
    for(int i = 0; i < clients.length; i++){
      if(clients[i] == prodLeft.length + 1) ++sum;
    }
    return sum;
  }
  public int centralsNoObertes(){
    int sum = 0;
    for(int i = 0; i < prodLeft.length; i++){
      if(prodLeft[i] == centrals.get(i).getProduccion())++sum;
    }
    return sum;
  }

  public double getValue(){
  double sum = 0;
   for(int i = 0; i < getNCentrals(); i++){
       double aux = Math.log(prodOcupada(i)/prodTotal(i));
       sum-= aux*prodOcupada(i)/prodTotal(i);
       //sum+=Math.pow(board.prodTotal(i) - board.prodOcupada(i),2);
   }
   //sum = -board.getBeneficis();

   return sum;
   }




   public double getValueHeuristic(){
        return valorHeuristic;
   }

   public int getCentCliX(int x){
     return clients[x];
   }
  }
