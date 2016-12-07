/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author OSORIO
 */
public class Controladores {

    private static int BUFFER_SIZE = 10000000;

    public static boolean descomprime(String destination) {

        //String filename,
        try {

            String filename;
            filename = "";

            // Create a ZipInputStream to read the zip file
            BufferedOutputStream dest = null;

            FileInputStream fis = new FileInputStream("C:\\BioMiniLib.zip");
            //InputStream fis = getClass().getResourceAsStream("BioMiniLib.zip");

            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

            // Loop over all of the entries in the zip file
            int count;
            byte data[] = new byte[BUFFER_SIZE];
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();

                    //prepareFileDirectories( destination, entryName );
                    String destFN = destination + File.separator + entry.getName();
                    System.err.println(destFN);
                    File fichero = new File(destFN);

                    if (fichero.exists()) {
                        System.out.println("El fichero " + destFN + " existe");
                    } else {

                        System.out.println("Pues va a ser que no");

                        // Write the file to the file system
                        FileOutputStream fos = new FileOutputStream(destFN);
                        dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                        while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                        dest.close();
                    }
                }
            }
            zis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void AgregarRuta(String ruta) throws IOException {
        try {
            Field campo = ClassLoader.class.getDeclaredField("usr_paths");
            campo.setAccessible(true);
            String[] arregloRutas = (String[]) campo.get(null);
            for (int i = 0; i < arregloRutas.length; i++) {
                if (ruta.equals(arregloRutas[i])) {
                    return;
                }
            }
            String[] tmp = new String[arregloRutas.length + 1];
            System.arraycopy(arregloRutas, 0, tmp, 0, arregloRutas.length);
            tmp[arregloRutas.length] = ruta;
            campo.set(null, tmp);
            System.setProperty("jna.library.path", System.getProperty("jna.library.path") + File.pathSeparator + ruta);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }
}
