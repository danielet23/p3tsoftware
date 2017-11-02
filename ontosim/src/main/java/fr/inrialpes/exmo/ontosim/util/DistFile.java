package fr.inrialpes.exmo.ontosim.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * class DistFile
 */
public class DistFile {
	final static Logger logger = LoggerFactory.getLogger(DistFile.class);


    public static void testDistFile(String[] args) throws IOException {
		File f1 = new File(args[0]);

		int[] dist = new int[200];

		BufferedReader br = null;

		String line=null;

		try {

			br = new BufferedReader(new FileReader(f1));
			line = br.readLine();
			while (line!=null) {
				String nb = line.substring(line.lastIndexOf(';')+1);
				double val = Double.parseDouble(nb);
				int idx = (int) (val/0.005);
				//System.out.println(idx);
				if (idx==dist.length) idx--;
				dist[idx]++;
				line = br.readLine();
			}
		} catch (Exception ex) {logger.error("FATAL error", ex);}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (java.io.IOException e3) {
					System.out.println("I/O Exception");
				}
			}
		}

		for (int i=0 ; i< dist.length ; i++) {
			System.out.println(i+"\t"+dist[i]);
		}
	}

}
