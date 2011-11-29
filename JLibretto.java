import static java.lang.System.*;
import java.util.*;
import java.io.*;
import java.sql.Date;

public class JLibretto implements Serializable {

/**
 * nel libretto l'esame è identificato dal nome esame, ed è composto da nome voto e cfu
 * se viene inserito un esame con voto deve essere aggiornata la media e il numero di crediti
 * se vine inserito un esame senza voto viene solo memorizzato (ma forse non è corretto)
 */
	private Map <String, Esame> libretto; 
	private int cfuTot;
	private double votoPon;
	private int contatorelodi;
	
	public JLibretto(){
		libretto = new TreeMap<String, Esame>();
		cfuTot = 0;
		votoPon = 0;
		contatorelodi = 0;
	}
	
	public Esame addEsame(Esame e) throws NumberFormatException, IllegalArgumentException{
		if(libretto.containsKey(e.getNome())) return null;
		libretto.put(e.getNome(), e);
		if(e.faMedia()){
			EsameConVoto ecv = (EsameConVoto) e;
			double tmp = cfuTot*votoPon + ecv.getVoto() * ecv.getCFU();
			cfuTot += e.getCFU();
			votoPon = tmp/cfuTot;
			if(ecv.getLode()) contatorelodi++;
			
		}
		return e;
	}
	
	
	/** rimuove un esame dall'elenco ripristinando l'inveriante strutturale
	 */
	public Esame delEsame(Esame e){
		Esame mod = libretto.remove(e.getNome()); 	// se esiste lo elimina
		if (mod == null) return null;
		if (mod.faMedia()){
				// ristabilire invariante di struttura
				EsameConVoto ecv = (EsameConVoto) mod;
				double tmp = cfuTot*votoPon - ecv.getVoto() * ecv.getCFU();
				cfuTot -= mod.cfu;
				votoPon = tmp/cfuTot;
				if(ecv.getLode()) contatorelodi--;
		}
		return mod;
	}
	
	public Esame modEsame(Esame e)throws NumberFormatException, IllegalArgumentException{
		if(delEsame(e) != null) return addEsame(e);
		else return null;
	}
	
	public Esame findEsame(String nome){
		Esame e = (Esame)libretto.get(nome);
		return e;
	}
/*	
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
*/

	public void salva()throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("salva.dat"));
		out.writeObject(this);
	}
	
	public JLibretto carica()throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("salva.dat"));
		JLibretto lib = (JLibretto)in.readObject();
		return lib;
	}
	
	public void load() throws Exception{
		BufferedReader in = new BufferedReader(new FileReader("save.txt"));
		String s, tmp;
		int voto, cfu;
		boolean l;
		Date d;
		while ((s=in.readLine())!= null){
			StringTokenizer st = new StringTokenizer(s, "|");
			s = st.nextToken();
			voto = Integer.parseInt(st.nextToken());
			cfu = Integer.parseInt(st.nextToken());
			if (st.nextToken().equals("true")) l = true;
			else l = false;
			d = Date.valueOf(st.nextToken());
			if (voto !=0 )addEsame(new EsameConVoto(s , voto, cfu, l, d));
			else addEsame(new Esame(s, cfu, d));
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
		for(int i = 0; i < a.length; i++){
			lib = lib + a[i].toString() +"\n"; 
		}
		lib = lib + "\n\nmedia: " + votoPon;
		return lib;
	}

	public static void main (String [] args) throws Exception{
		JLibretto mio = new JLibretto();
		Scanner input = new Scanner(System.in);
		out.println("Main di Test");
		out.println("Digitare:\n1 per inserire un esame");
		out.println("2 per modificare un esame esistente");
		out.println("3 per eliminare un esame esistente");
		out.println("4 per stampare il libretto in ordine di data");
		out.println("5 per stampare il libretto in ordine alfabetico");
		out.println("6 per salvare il libretto");
		out.println("7 per caricare il libretto");
		out.println("8 per caricare il libretto da file");
		out.println("9 per creare un nuovo libretto");
		out.println("0 per terminare");
		do{
			
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
				out.print("\ncon voto? (inserire il voto oppure 'no'): ");
				String dom = input.nextLine();
				if(dom.equals("no")){
					try{
						mio.addEsame(new Esame(tmp, cfu, d));
					}catch(Exception e) {out.println("Esame non inserito");}
				}
				else{
					voto = Integer.parseInt(dom);
					if(voto == 30){
						out.print("\ncon lode? ");
						if(input.nextLine().equals("si")) l = true;
					}
					try{
						mio.addEsame(new EsameConVoto(tmp, voto, cfu, l, d));
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
				out.print("\ncon voto? (inserire il voto oppure 'no'): ");
				String dom = input.nextLine();
				if(dom.equals("no")){
					try{
						mio.modEsame(new Esame(tmp, cfu, d));
					}catch(Exception e) {out.println("Esame non modificato");}
				}
				else{
					voto = Integer.parseInt(dom);
					if(voto == 30){
						out.print("\ncon lode? ");
						if(input.nextLine().equals("si")) l = true;
					}
					try{
						mio.modEsame(new EsameConVoto(tmp, voto, cfu, l, d));
					}catch(Exception e) {out.println("Esame non modificato");}
				}
				
			}
			else if(index.equals("3")) {
				out.print("\nInserire la stringa: ");
				tmp = input.nextLine();
				mio.delEsame(mio.findEsame(tmp));
			}
			else if(index.equals("4")) out.println(mio.toString(false));
			else if(index.equals("5")) out.println(mio.toString(true));
			else if(index.equals("6")) {
				mio.salva();
			}
			else if(index.equals("7"))  {
				
					mio.load();
				  
			}
			else if(index.equals("8"))  {
				try{
					mio = mio.carica();
				  }catch (Exception e) {out.println("Libretto non caricato");}
			}
			else if(index.equals("9")) mio =  new JLibretto();
			else if(index.equals("10")){
				out.println("Digitare:\n1 per inserire un esame");
				out.println("2 per modificare un esame esistente");
				out.println("3 per eliminare un esame esistente");
				out.println("4 per stampare il libretto in ordine di data");
				out.println("5 per stampare il libretto in ordine alfabetico");
				out.println("6 per salvare il libretto");
				out.println("7 per caricare il libretto");
				out.println("8 per caricare il libretto da file");
				out.println("9 per creare un nuovo libretto");
				out.println("0 per terminare");
			}
			else if(index.equals("0")) break;
			else out.println("Comando errato");
			out.println("Digitare comando: (10 per stamapare menu)");
			}while(true);
	}
}
