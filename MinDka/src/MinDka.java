import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MinDka {

	 public static void main(String[] args) {
	        //System.out.println("program je krenuo");

	        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String line;
	        int brojac = 1;
	        List<String> stanja =new ArrayList<>();
	        List<String> simboli = new ArrayList<>();
	        List<String> prihvatljiva_stanja =new ArrayList<>();
	         String poc_stanje = null;
	        List<String> list=new ArrayList<>();

	        HashMap<String, HashMap<String, String>> prijelazi = null;


	        try {
	            while ( (line = br.readLine()) != null && !line.isEmpty())
	            {
	               /* line = br.readLine();
	                if(line.isEmpty()  || line.isBlank()) {break;}*/


	                //svaki skup nizova je stavljen u listu
	            	//skup stanja
	                if(brojac ==1) {
	                    String[] stanja_pom = line.split(",");

	                    for(String niz:stanja_pom) {
	                		stanja.add(niz);
	                	}
	                    brojac++;
	                }
	                //simboli
	                else if(brojac ==2) {
                		String[] simboli_pom = line.split(",");

                		for(String niz:simboli_pom) {
	                		simboli.add(niz);
	                	}
	                    //simboli = line.split(",");
	                    brojac++;
	                }
	                //prihvatljiva stanja
	                else if(brojac ==3) {
	                    String [] pom_prihvatljiva_stanja = line.split(",");
	                    for(String niz:pom_prihvatljiva_stanja ) {
	                		prihvatljiva_stanja.add(niz);
	                	}
	                    brojac++;
	                }
	                //pocetno stanje
	                else if(brojac ==4) {
	                    poc_stanje = line;
	                    brojac++;
	                }

	                //prijelazi
	                else {
	                    String funk_prijelaz = line;

	                    list.add(funk_prijelaz);
	                    brojac++;
	                }

	            }

	            prijelazi = parsiranje(list,brojac);
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        //fja za pronalazak dohvatljivih stanja
	        List<String> dohvatljiva_stanja = new ArrayList<>();
	        dohvatljiva_stanja = dohvat_stan(simboli,poc_stanje,prijelazi);

	        //fja za minimizaciju
	        HashMap<String, HashMap<String, String>> mini =
	        		minimiziraj(dohvatljiva_stanja,simboli,prihvatljiva_stanja,poc_stanje,prijelazi);
	        //nuzno je da se prvo provede postupak uklanjanja nedohvatljivih stanja, a tek onda postupak uklanjanja istovjetnih stanja.



	        //ispis minimiziranog automata
	        //dohvatljiva stanja
	        
	        for(String stanje : stanja) {
	        	if(dohvatljiva_stanja.contains(stanje) && stanja.indexOf(stanje) == 0) {
	        		System.out.print(stanje);
	        	}
	        	else if(dohvatljiva_stanja.contains(stanje)) {
	        		System.out.print(","+stanje);
	        	}
	        }
	        System.out.println();

	        //simboli
	        Collections.sort(simboli);
	        for(int i=0;i<simboli.size();i++) {
	        	if(i == 0) {
	        		System.out.print(simboli.get(i));
	        	}
	        	else {
	        		System.out.print(","+simboli.get(i));
	        		}
	        }
	        System.out.println();

	        //prihvatljiva stanja
	        for(int i=0;i<prihvatljiva_stanja.size();i++) {
	        	if(i == 0) {
	        		if(dohvatljiva_stanja.contains(prihvatljiva_stanja.get(i))) {
	        		System.out.print(prihvatljiva_stanja.get(i));}
	        	}
	        	else {
	        		if(dohvatljiva_stanja.contains(prihvatljiva_stanja.get(i))) {
	        		System.out.print(","+prihvatljiva_stanja.get(i));
	        		}}
	        }
	        System.out.println();

	        //pocetno stanje
	        System.out.println(poc_stanje);


	        //mini prijelazi
	        Collections.sort(dohvatljiva_stanja);  
	        for (String element : dohvatljiva_stanja) {
	        	for (String element2 : simboli) {

	        		if(mini.get(element) != null &&
	        				mini.get(element).get(element2) != null) {

	        		System.out.print(element+",");
	        		System.out.print(element2+"->");
	        		System.out.println(mini.get(element).get(element2));
	        	}}
	        }
	        
	        
	 }

	    private static List<String> dohvat_stan( List<String> simboli, String poc_stanje,
			HashMap<String, HashMap<String, String>> prijelazi) {

	    	List<String> dohvatljiva_stanja = new ArrayList<>();

	    	dfs(poc_stanje,simboli,prijelazi,dohvatljiva_stanja);
	    	

			return dohvatljiva_stanja;
	}

		private static void dfs(String stanje, List<String> simboli, HashMap<String, HashMap<String, String>> prijelazi, List<String> dohvatljiva_stanja) {

			dohvatljiva_stanja.add(stanje);
			for(String simbol : simboli) {
				if(!dohvatljiva_stanja.contains( prijelazi.get(stanje).get(simbol)) ) {

					dfs(prijelazi.get(stanje).get(simbol), simboli,prijelazi,dohvatljiva_stanja);
				}
			}
		}


		//koristenjem 3. algoritma
		private static HashMap<String, HashMap<String, String>> minimiziraj
		(List<String> dohvatljiva_stanja,List<String> simboli, List<String> prihvatljiva_stanja, String poc_stanje,HashMap<String, HashMap<String, String>> prijelazi) {

			HashMap<String, HashMap<String, String>> mini = prijelazi ;
			boolean ekv=false;

			for(int i=1;i<dohvatljiva_stanja.size();i++) {
				for(int j=0;j<dohvatljiva_stanja.size() - 1; j++) {
					if(j<i) {
					if(prihvatljiva_stanja.contains(dohvatljiva_stanja.get(i)) != prihvatljiva_stanja.contains(dohvatljiva_stanja.get(j))) 
					{}
					else {
						boolean jednaki = true;
						for(String simbol : simboli) {
							if(prijelazi.get(dohvatljiva_stanja.get(i)) != null
									&& prijelazi.get(dohvatljiva_stanja.get(i)).get(simbol) != null
										&& prijelazi.get(dohvatljiva_stanja.get(j)) != null
											&& prijelazi.get(dohvatljiva_stanja.get(j)).get(simbol) != null) {
								//provjera nullPointerException
							if(prijelazi.get(dohvatljiva_stanja.get(i)).get(simbol) != prijelazi.get(dohvatljiva_stanja.get(j)).get(simbol)) {
								jednaki = false;
							}}
						}
						if(jednaki) {
							//j je uvijek manjeg broja od i(tj uvijek se iz liste brise i)
							izmjeni_prijelaze(dohvatljiva_stanja.get(i),dohvatljiva_stanja.get(j),prijelazi);
							
							for(String simbol : simboli) {
								if(mini.get(dohvatljiva_stanja.get(i)) != null
										&& mini.get(dohvatljiva_stanja.get(i)).get(simbol) != null) {
									//provjera NullPointerException
									mini.remove(mini.get(dohvatljiva_stanja.get(i)).get(simbol));
									if(prihvatljiva_stanja.contains(dohvatljiva_stanja.get(i))){
										prihvatljiva_stanja.remove(dohvatljiva_stanja.get(i));
										dohvatljiva_stanja.remove(dohvatljiva_stanja.get(i));
									}
								}
							}
						}
						else {
							List<String> visited = new ArrayList<>();
							ekv = compare(dohvatljiva_stanja.get(i), dohvatljiva_stanja.get(j),prijelazi,prihvatljiva_stanja,simboli,visited);
						}
						if(ekv) {
							for(String simbol : simboli) {
								if( mini.get(dohvatljiva_stanja.get(i)) != null
										&& mini.get(dohvatljiva_stanja.get(i)).get(simbol) != null) {
								mini.remove(mini.get(dohvatljiva_stanja.get(i)).get(simbol));
									if(prihvatljiva_stanja.contains(dohvatljiva_stanja.get(i))){
										prihvatljiva_stanja.remove(dohvatljiva_stanja.get(i));
										dohvatljiva_stanja.remove(dohvatljiva_stanja.get(i));
									}
								}
							}
						}
					}
				}
			}
		}

		return mini;
	}

		
		//fja ne radi
		private static void izmjeni_prijelaze(String stanje, String novostanje, HashMap<String, HashMap<String, String>> prijelazi) {
		    //za svaki prijelaz koji ulazi u stanje, promijeni ciljno stanje na novostanje
		    for (String s : prijelazi.keySet()) {
		        for (String simbol : prijelazi.get(s).keySet()) {
		            if (prijelazi.get(s).get(simbol).equals(stanje)) {
		                prijelazi.get(s).put(simbol, novostanje);
		            }
		        }
		    }

		    //obriši sve prijelaze koji izlaze iz stanja
		    prijelazi.remove(stanje);

		    //izbaci stanje iz svih prijelaza koji vode do nekog drugog stanja
		    for (String s : prijelazi.keySet()) {
		        for (String simbol : prijelazi.get(s).keySet()) {
		            if (prijelazi.get(s).get(simbol).equals(stanje)) {
		                prijelazi.get(s).put(simbol, novostanje);
		            }
		        }
		    }
		}


		private static boolean compare(String a, String b, HashMap<String, HashMap<String, String>> prijelazi,
                List<String> prihvatljiva_stanja, List<String> simboli, List<String> visited) {

			// Check if the states have already been compared
					if (visited.contains(a) && visited.contains(b)) {
							return true;
					}
	
					// Mark the current states as visited
					visited.add(a);
					visited.add(b);

					// Check if the states have different acceptance status
					if (prihvatljiva_stanja.contains(a) != prihvatljiva_stanja.contains(b)) {
						return false;
					}

					// Check if the states have different number of outgoing transitions
					if(prijelazi.get(a) != null && prijelazi.get(b) != null) {
					if (prijelazi.get(a).size() != prijelazi.get(b).size()) {
						return false;
					}}

					// Check if the states have the same transitions for each symbol
					for (String simbol : simboli) {
						String x =null;
						String y = null;
						if(prijelazi.get(a) != null && prijelazi.get(b) != null) {
							x = prijelazi.get(a).get(simbol);
							y = prijelazi.get(b).get(simbol);
						}
						if (x == null && y == null) {
							continue;
						}

						if (x == null || y == null || !compare(x, y, prijelazi, prihvatljiva_stanja, simboli, visited)) {
							return false;
						}
					}
					
					// The states are equivalent
					return true;
		}


		static HashMap<String, HashMap<String, String>> parsiranje(List<String> list, int brojac) {

	        HashMap<String, HashMap<String,String > > prijelazi = new HashMap<>();

	        for(String linija : list) {
	            String trenutnoStanje = linija.substring(0, linija.indexOf(','));
	            String simbolAbecede = linija.substring(linija.indexOf(',')+1, linija.indexOf('-'));
	            String skupIducihStanja = linija.substring(linija.indexOf('>')+1) ;

	            //var stanja_var = skupIducihStanja.split(",");//odvajanje stanja
	            //List<String> stanja = List.of(stanja_var);//stavljanje pojedinih stanja u listu stringova


	            if(prijelazi.get(trenutnoStanje) == null) {
	                HashMap<String,String> mapa = new HashMap<>();
	                mapa.put(simbolAbecede, skupIducihStanja/*stanja*/);
	                prijelazi.put(trenutnoStanje, mapa);
	            }
	            else {
	                HashMap<String,String> mapa2 = prijelazi.get(trenutnoStanje);
	                mapa2.put(simbolAbecede, skupIducihStanja/*stanja*/);
	                prijelazi.put(trenutnoStanje, mapa2);
	            }
	        }

	        return prijelazi;
	    }

}

