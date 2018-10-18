/*!
 * REpresentacion del estado del problema del viajante de comercio
 */

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Clientes;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;

public class BoarHat {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

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
	private int cliNoAssig;


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
			Arrays.fill(clients, centrals.size()+1);
			int cent = 0;
			int cli = 0;
			boolean run = true;

			getInitialSolution();

			/*cent = cli = 0;
			  run = true;
			  while (run){ //Assignem tots els possibles no garantitzats
			  double prod = produccioReal(cent, cli);
			  double prodCent = prodLeft[cent];
			  if(prod < prodCent*0.5) { //Posem el client a la central
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
			  }*/
			//valorHeuristic = getValue();
			beneficis = getBeneficis();
			cliNoAssig = clientsNoAssignats();
		} catch(java.lang.Exception e) {
			System.out.println(e);
		}

	}

	private ArrayList<Integer> getSortedGntd() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < clients.length; i++) {
			if (clientes.get(i).getContrato() == 0) {
				result.add(i);
			}
		}

		result = sortByProd(result);

		System.out.println("Size: "+result.size());
		//System.out.println(result.toString());
		return result;
	}

	public ArrayList<Integer> sortByProd(ArrayList<Integer> result) {
		int n = result.size();
		for (int i=1; i<n; ++i)
		{
			int key = result.get(i);
			int j = i-1;

			/* Move elements of arr[0..i-1], that are
			   greater than key, to one position ahead
			   of their current position */
			while (j>=0 && clientes.get(result.get(j)).getConsumo() < clientes.get(key).getConsumo())
			{
				result.set(j+1, result.get(j));
				j = j-1;
			}
			result.set(j+1, key);
		}

		return result;
	}

	private void getInitialSolution() {
		// Init Vars
		int cent = 0;
		int cli = 0;
		boolean run = true;

		ArrayList<Integer> garantits = getSortedGntd();

		while (run){ //Assignem tots els garantitzats
			double prod = produccioReal(cent, garantits.get(cli));
			double prodCent = prodLeft[cent];
			if(prod < prodCent) { //Posem el client a la central
				clients[garantits.get(cli)] = cent;
				prodLeft[cent]-=prod;
				cent++;
				cli++;
			} else {
				cent ++;
			}

			if(cli >= garantits.size()) run = false;
			else if (cent >= prodLeft.length) {
				cent = 0;
				//run = false;
			}
			else if (cent >= centrals.size()) run = false;
		}
		
		cent = cli = 0;
		run = true;
		while (run){ //Assignem tots els possibles no garantitzats
			double prod = produccioReal(cent, cli);
			double prodCent = prodLeft[cent];
			if(prod < prodCent*0.5) { //Posem el client a la central
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

		printState();
	}

	public void printState() {
		int[] centt = new int[42];

		for(int i = 0; i < clients.length; i++) {
			centt[clients[i]] += 1;
		}


		for(int i = 0; i < centt.length; i++) {
			if (i < centrals.size()) {
				System.out.println("Central "+i+": "+ANSI_GREEN+centt[i]+ANSI_RESET+" Produccio: "+
				ANSI_PURPLE+centrals.get(i).getProduccion()+ANSI_RESET+
				" Usada: "+ANSI_RED+(centrals.get(i).getProduccion()-prodLeft[i])+ANSI_RESET);
				//" Usada: "+Math.round(prodLeft[i]));
			} else {
				System.out.println("Central NONE: "+ANSI_GREEN+centt[i]+ANSI_RESET);
			}
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
			produccioLeft += prodOcupada(i);
		}
		return produccioLeft;
	}

	public BoarHat(int [] clients2, double [] prodLeft2, double beneficis2, int cliNoAssig2){ //Constructora per generar successors

		try{
			beneficis = beneficis2;
			cliNoAssig = cliNoAssig2;
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

	public int getNoAssig(){
		return cliNoAssig;
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
		double centX, centY, cliX, cliY;
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
		demanda = demanda + VEnergia.getPerdida(dist)*demanda;
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
			recalcBenficisCliAssig(cli, cent, centPre);
			//beneficis = getBeneficis();
			return true;
		}
		else {
			if(prodLeft[cent] < prod) return false;
			prodLeft[cent]-= prod;
			clients[cli] = cent;
			//beneficis = getBeneficis();
			--cliNoAssig;
			recalcBenficisCliNoAssig(cli, cent);
			return true;
		}
	}

	public boolean swap(int cli1, int cli2){
		int cent1 = clients[cli1];
		int cent2 = clients[cli2];
		if( !clientAssignat(cli1) && !clientAssignat(cli2)) return false;
		if( clientes.get(cli1).getContrato() == 0 && !clientAssignat(cli2)) return false;
		if( clientes.get(cli2).getContrato() == 0 && !clientAssignat(cli1)) return false;
		if(!clientAssignat(cli1)){
			double prodPre1 = produccioReal(cent2, cli2);
			double prodR1 = produccioReal(cent2, cli1);
			prodLeft[cent2] = prodLeft[cent2] - prodR1 + prodPre1;
			clients[cli1] = cent2;
			clients[cli2] = cent1;
			beneficis = getBeneficis();
			return true;
		}
		if(!clientAssignat(cli2)){
			double prodPre1 = produccioReal(cent1, cli1);
			double prodR1 = produccioReal(cent1, cli2);
			prodLeft[cent1] = prodLeft[cent1] - prodR1 + prodPre1;
			clients[cli1] = cent2;
			clients[cli2] = cent1;
			beneficis = getBeneficis();
			return true;
		}
		double prodR1 = produccioReal(cent2, cli1);
		double prodR2 = produccioReal(cent1, cli2);
		double prodPre1 = produccioReal(cent1, cli1);
		double prodPre2 = produccioReal(cent2, cli2);
		if(prodR1 > prodLeft[cent2] + prodPre2) return false;
		if(prodR2 > prodLeft[cent1] + prodPre1) return false;
		clients[cli1] = cent2;
		clients[cli2] = cent1;
		prodLeft[cent1] = prodLeft[cent1] - prodR2 + prodPre1;
		prodLeft[cent2] = prodLeft[cent2] - prodR1 + prodPre2;
		beneficis = getBeneficis();
		return true;
	}

	public void recalcBenficisCliAssig(int cli, int cent, int centPre){
		try{
			int tipo = clientes.get(cli).getTipo();
			int garant = clientes.get(cli).getContrato();
			int  tipo2 = centrals.get(cent).getTipo();
			int  tipo3 = centrals.get(centPre).getTipo();
			double prod = produccioReal(cent, cli);
			if(prodLeft[centPre] == centrals.get(centPre).getProduccion()) beneficis+=(centrals.get(centPre).getProduccion()*VEnergia.getCosteProduccionMW(tipo3) + VEnergia.getCosteMarcha(tipo3) + VEnergia.getCosteParada(tipo3));
			if(centrals.get(cent).getProduccion() == (prod + prodLeft[cent])){
				beneficis-=(centrals.get(cent).getProduccion()*VEnergia.getCosteProduccionMW(tipo2) + VEnergia.getCosteMarcha(tipo2) + VEnergia.getCosteParada(tipo2));
			}



		}catch(java.lang.Exception e) {
			System.out.println(e);
		}
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
			double prod = produccioReal(cent, cli);
			if(centrals.get(cent).getProduccion() == (prod + prodLeft[cent])){
				tipo = centrals.get(cent).getTipo();
				beneficis-=(centrals.get(cent).getProduccion()*VEnergia.getCosteProduccionMW(tipo) + VEnergia.getCosteMarcha(tipo) + VEnergia.getCosteParada(tipo));
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

	public double getHeurIndex() {
		//double val = getValue()*10;
		int nAcli = getNoAssig()*100;
		int nCent = 0;
		double centInd = 0;
		for(int i = 0; i < prodLeft.length; i++){
			Central cent = centrals.get(i);
			// Central No Oberta
			if(prodLeft[i] == cent.getProduccion()) {
				nCent++;
			} else {
				double aux = cent.getProduccion()
				*((prodLeft[i]*1)/cent.getProduccion());
				centInd += aux;
			}
		}
		//System.out.println("Value = " + val + " No Assignats = " + nAcli + " Centrals Tancades " + nCent + " Index " + centInd+ " Total " + (val-nAcli+nCent+centInd));
		//return val-nAcli+nCent+centInd*10+beneficis/10;
		//return val-nAcli+nCent+centInd*10;
		return -nAcli+nCent*10000+centInd*1;
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
			if(prodOcupada(i) > 0){
			double aux = Math.log(prodOcupada(i)/prodTotal(i));
			sum-= aux*prodOcupada(i)/prodTotal(i);
			}
			//sum+=Math.pow(board.prodTotal(i) - board.prodOcupada(i),2);
		}
		//sum = -board.getBeneficis();

		return sum;
	}

	public int CliACent(int cli){
		return clients[cli];
	}



	public double getValueHeuristic(){
		return valorHeuristic;
	}

	public int getCentCliX(int x){
		return clients[x];
	}
}
