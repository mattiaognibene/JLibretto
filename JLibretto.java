import static java.lang.System.*;
import java.util.*;
import java.io.*;
import java.sql.Date;

public class Libretto {

/**
 * nel libretto l'esame è identificato dal nome esame, ed è composto da nome voto e cfu
 * se viene inserito un esame con voto deve essere aggiornata la media e il numero di crediti
 * se vine inserito un esame senza voto viene solo memorizzato (ma forse non è corretto)
 */
	private Map <String, Esame> libretto; 
	private int cfuTot;
	private double votoPon;
	private int contatorelodi;
	
	public Libretto(){
		libretto = new TreeMap<String, Esame>();
		cfuTot = 0;
		votoPon = 0;
		contatorelodi = 0;
	}
	public boolean addEsame(String nome, int voto, int cfu, boolean l, Date d) throws Exception{
		if(libretto.containsKey(nome)) return false;
		libretto.put(nome, new Esame(nome, voto, cfu,l, d));
		if(l) contatorelodi++;
		double tmp = cfuTot*votoPon + voto*cfu;
		cfuTot += cfu;
		votoPon = tmp/cfuTot;
		return true;
	}
	
	public boolean addEsame(String nome, int cfu, Date d) throws Exception{
		if(libretto.containsKey(nome)) return false;
		libretto.put(nome, new Esame(nome, cfu, d));
		return true;
	}
	
	/** rimuove un esame dall'elenco ripristinando l'inveriante strutturale
	 */
	public boolean delEsame(String nome){
		Esame mod = libretto.remove(nome); 	// se esiste lo elimina
		if(mod == null) return false; 		// oppure restituisce null
		else if (mod.voto != 0){
				// ristabilire invariante di struttura
				double tmp = votoPon*cfuTot - (mod.voto * mod.cfu);
				cfuTot -= mod.cfu;
				votoPon = tmp/cfuTot;
				if(mod.lode) contatorelodi--;
		}
		return true;
	}
	
	public boolean modEsame(String nome, int voto, int cfu, boolean lode, Date d)throws Exception{
		if(delEsame(nome)) return addEsame(nome, voto, cfu, lode, d);
		else return false;
	}
	
	public boolean modEsame(String nome, int cfu, Date d)throws Exception{
		if(delEsame(nome)) return addEsame(nome, cfu, d);
		else return false;
	}
	
	public void save() throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter("save.txt"));
		Esame e;
		String lib = "";
		String lode = "";
		Collection<Esame> col = libretto.values();
		Iterator<Esame> ite = col.iterator();
		while (ite.hasNext()) {
			e = ite.next();
			String str = e. nome + "\n"+e.voto+"\n"+e.cfu+"\n"+e.lode+"\n"+e.data+"\n";
			out.write(str);
		}
		out.close();
	}

	public void load() throws Exception{
		BufferedReader in = new BufferedReader(new FileReader("save.txt"));
		String s, tmp;
		int voto, cfu;
		boolean l;
		Date d;
		while ((s=in.readLine())!= null){
			voto = Integer.parseInt(in.readLine());
			cfu = Integer.parseInt(in.readLine());
			if (in.readLine().equals("true")) l = true;
			else l = false;
			d = Date.valueOf(in.readLine());
			if (voto !=0 )addEsame(s , voto, cfu, l, d);
			else addEsame(s, cfu, d);
		}
		in.close();
	}

	public Esame[] toArrays(boolean ord){
		Esame e;
		Collection<Esame> col = libretto.values();
		int leng = col.size();
		Esame [] a = new Esame[leng];
		col.toArray(a);
		if(ord)Arrays.sort(a);
		return a;
	}
	
	public String toString(boolean ord){
		Esame [] a = toArrays(ord);
		Esame e;
		String lib = "";
		String lod = "";
		for(int i = 0; i < a.length; i++){
			e = a[i];
			if (e.voto !=0){
				if(e.lode) lod = "L";
				else lod = "";
				lib = lib + e.nome + " voto: " + e.voto + lod +" da: " + e.cfu + " cfu " +e.data+"\n";
			}
			else lib = lib + e.nome + " OK da: " + e.cfu + " cfu " +e.data+"\n";
		}
		lib = lib + "\n\nmedia: " + votoPon;
		return lib;
	}

	public static void main (String [] args) throws Exception{
		Libretto mio = new Libretto();
		Scanner input = new Scanner(System.in);
		out.println("Main di Test");
		do{
			out.println("Digitare:\n1 per inserire un esame");
			out.println("2 per modificare un esame esistente");
			out.println("3 per eliminare un esame esistente");
			out.println("4 per stampare il libretto in ordine di data");
			out.println("5 per stampare il libretto in ordine alfabetico");
			out.println("6 per salvare il libretto");
			out.println("7 per caricare il libretto");
			out.println("8 per creare un nuovo libretto");
			out.println("0 per terminare");
			String index = input.nextLine();
			
			int voto, cfu;
			String tmp;
			Date d;
			boolean l = false;
	
			if(index.equals("1")) {
				out.print("\nInserire nome esame: ");
				tmp = input.nextLine();
				out.print("\nInserire cfu esame: ");
				cfu = input.nextInt();
				input.nextLine();
				System.out.print("\nInserire la data (formato aaaa-mm-gg): ");
				d = Date.valueOf(input.nextLine());
				out.print("\ncon voto? ");
				String dom = input.nextLine();
				if(dom.equals("si")) {
					out.print("\nInserire voto esame: ");
					voto = input.nextInt();
					input.nextLine();
					if(voto == 30){
						out.print("\ncon lode? ");
						if(input.nextLine().equals("si")) l = true;
					}
					try{
						mio.addEsame(tmp, voto, cfu, l, d);
					}catch(Exception e) {out.println("Esame non inserito");}
				}
				else if(dom.equals("no")){
					try{
						mio.addEsame(tmp, cfu, d);
					}catch(Exception e) {out.println("Esame non inserito");}
				}
				
			}
			
			else if(index.equals("2")) {
				out.print("\nInserire nome esame: ");
				tmp = input.nextLine();
				out.print("\nInserire cfu esame: ");
				cfu = input.nextInt();
				input.nextLine();
				out.print("\nInserire la data(formato aaaa-mm-gg): ");
				d = Date.valueOf(input.nextLine());
				System.out.print("\ncon voto? ");
				if(input.next().equals("si")) {
					out.print("\nInserire voto esame: ");
					voto = input.nextInt();
					input.nextLine();
					if(voto == 30){
						out.print("\ncon lode? ");
						if(input.nextLine().equals("si")) l = true;
					}
					try{
						mio.modEsame(tmp, voto, cfu, l, d);
					}catch(Exception e) {out.println("Esame non modificato");}
					
				}
				
				else {
					try{
						mio.modEsame(tmp, cfu, d);
					}catch(Exception e) {out.println("Esame non modificato");}
					}
			}
			
			else if(index.equals("3")) {
				out.print("\nInserire la stringa: ");
				tmp = input.nextLine();
				mio.delEsame(tmp);
			}
			else if(index.equals("4")) out.println(mio.toString(false));
			else if(index.equals("5")) out.println(mio.toString(true));
			else if(index.equals("6")) {
				try{
					mio.save();
				  }catch (Exception e) {out.println("Libretto non salvato");}
			}
			else if(index.equals("7"))  {
				try{
					mio.load();
				  }catch (Exception e) {out.println("Libretto non caricato");}
			}
			else if(index.equals("8")) mio =  new Libretto();
			else if(index.equals("0")) break;
			else out.println("Comando errato");
			}while(true);
	}
}
