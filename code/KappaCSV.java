import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.zip.GZIPInputStream;



public class KappaCSV {
	  public static final boolean DEBUG = true ;
		 public static boolean headerline= true; // <- Dies the file have a header ?
		 public static String line_sep= ","; // seperator, otherwise whitespace, tab... \\s+
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		if(args.length!= 1) {
			System.out.println("1 Argument required: <inputfile (txt/csv or .gz)>");
		}
		String inputfile=args[0];
		
		System.out.println("Start reading:" +inputfile);
		BufferedReader in = null;
		if(inputfile.endsWith(".gz")) {
			 GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(inputfile));
			   in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
		}
		else {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(inputfile), "UTF-8");  
			 in = new BufferedReader(reader);
		}
	
		

	
			 String zeile="";			
			 int no_annot =0; // <- number of annots
			 ArrayList<String> Mem = new ArrayList<String>();
			 HashMap<String,Integer> cat2id = new  HashMap<String,Integer>();
				while ((zeile = in.readLine()) != null) {
					String[] tmp = zeile.split(line_sep);
					if(headerline) {
						headerline = false;
						no_annot = tmp.length;
						continue;
					}
					for(int q=0;q<tmp.length;q++) {
	
						if(cat2id.containsKey(tmp[q])==false) {
							cat2id.put(tmp[q], cat2id.size());
						}
					}
					Mem.add(zeile);
					
				}
			   
			   int subjects = Mem.size();
			   int categories = cat2id.size();
				in.close();
				 short[][] mat = new short[subjects][categories];
				    for(int i=0;i<Mem.size();i++) {
				    	String[] line = Mem.get(i).split(",");
				    	// set all val to zero first
				    	for(String pos : cat2id.keySet()) {
				    		int id = cat2id.get(pos);
				    		mat[i][id]=0;
				    	}
				    	
				    	for(int annotx=0;annotx<line.length;annotx++) {
				    		String an = line[annotx];
				    		int id = cat2id.get(an);
				    	
				    		mat[i][id]+=1;
				    	}
				    }
				 
				float kappa = computeKappa(mat) ;
		        System.out.println(inputfile+"\tKappa: "+kappa);

		
	}
	
	
	
	/**
     * Computes the Kappa value
     * @param n Number of rating per subjects (number of human raters)
     * @param mat Matrix[subjects][categories]
     * @return The Kappa value
     */
    public static float computeKappa(short[][] mat)
    {
        final int n = checkEachLineCount(mat) ;  // PRE : every line count must be equal to n
        final int N = mat.length ;          
        final int k = mat[0].length ;       
 
        if(DEBUG) System.out.println(n+" raters.") ;
        if(DEBUG) System.out.println(N+" subjects.") ;
        if(DEBUG) System.out.println(k+" categories.") ;
 
        // Computing p[]
        float[] p = new float[k] ;
        for(int j=0 ; j<k ; j++)
        {
            p[j] = 0 ;
            for(int i=0 ; i<N ; i++)
                p[j] += mat[i][j] ;
            p[j] /= N*n ;
        }
        if(DEBUG) System.out.println("p = "+Arrays.toString(p)) ;
 
        // Computing P[]    
        float[] P = new float[N] ;
        for(int i=0 ; i<N ; i++)
        {
            P[i] = 0 ;
            for(int j=0 ; j<k ; j++)
                P[i] += mat[i][j] * mat[i][j] ;
            P[i] = (P[i] - n) / (n * (n - 1)) ;
        }
        if(DEBUG) System.out.println("P = "+Arrays.toString(P)) ;
 
        // Computing Pbar
        float Pbar = 0 ;
        for(float Pi : P)
            Pbar += Pi ;
        Pbar /= N ;
        if(DEBUG) System.out.println("Pbar = "+Pbar) ;
 
        // Computing PbarE
        float PbarE = 0 ;
        for(float pj : p)
            PbarE += pj * pj ;
        if(DEBUG) System.out.println("PbarE = "+PbarE) ;
 
         float kappa = (Pbar - PbarE)/(1 - PbarE) ;
        if(Double.isNaN(kappa)){ 
        	kappa = 1; 
        	}
        if(DEBUG) System.out.println("kappa = "+kappa) ;
 
        return kappa ;
    }
 
    /**
     * Assert that each line has a constant number of ratings
     * @param mat The matrix checked
     * @return The number of ratings
     * @throws IllegalArgumentException If lines contain different number of ratings
     */
    private static int checkEachLineCount(short[][] mat)
    {
        int n = 0 ;
        boolean firstLine = true ;
 
        for(short[] line : mat)
        {
            int count = 0 ;
            for(short cell : line)
                count += cell ;
            if(firstLine)
            {
                n = count ;
                firstLine = false ;
            }
            if(n != count)
                throw new IllegalArgumentException("Line count != "+n+" (n value).") ;
        }
        return n ;
    }

}
