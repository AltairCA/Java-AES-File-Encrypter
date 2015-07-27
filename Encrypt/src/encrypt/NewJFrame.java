/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package encrypt;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.*;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JPasswordField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author Alex
 */
public class NewJFrame extends javax.swing.JFrame {
    //public static boolean checks = true;
    private static final int IV_LENGTH=16;
    public boolean checks;
    /**
     * Creates new form NewJFrame
     */
    public static void music() 
    {       


        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;

        ContinuousAudioDataStream loop = null;

        try
        {
            InputStream test = new FileInputStream("sound.wav");
            
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);
            //MD = BGM.getData();
            //loop = new ContinuousAudioDataStream(MD);

        }
        catch(FileNotFoundException e){
            
            System.out.print(e.toString());
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
        MGP.start(loop);

    }
    
    public  void outzip(String zipFile,String outputFolder){
        
        try{
            byte[] buffer = new byte[1024];
             
            
             ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
             ZipEntry ze = zis.getNextEntry();
             if(ze==null){
                 checks=false;
                 
             }else{
                 checks=true;
                 
                  
             }
             while(ze!=null){
 
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
 
           //System.out.println("file unzip : "+ newFile.getAbsoluteFile());
 
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            //new File(newFile.getParent()).mkdirs();
 
            FileOutputStream fos = new FileOutputStream(newFile);             
               
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
 
            fos.close();   
            ze = zis.getNextEntry();
    	}
 
        zis.closeEntry();
    	zis.close();
        
    	System.out.println("Done");
        //File newFile1 = new File(outputFolder + File.separator + fileName1);     
             
//        if(newFile1.exists()){
//            JOptionPane.showMessageDialog(null, "Done");
//            
//        }else{
//            JOptionPane.showMessageDialog(null, "Fail");
//        }
        }catch(Exception ex){
            
             JOptionPane.showMessageDialog(null, ex);
        }
        
        
        
        
        
    }
    public static void intozip(String filepath,String fname){
        try{
            FileInputStream fis = new FileInputStream(filepath);
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filepath+".zip"));
            zos.putNextEntry(new ZipEntry(fname));
            byte[] Buffer = new byte[1024];
            int bytesRead;
            while((bytesRead=fis.read(Buffer))>0){
                zos.write(Buffer,0,bytesRead);
                
                
            }
            zos.closeEntry();
            zos.close();
            fis.close();
            
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public static String md5(String input) {
		
		String md5 = null;
		
		if(null == input) return null;
		
		try {
			
		//Create MessageDigest object for MD5
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		//Update input string in message digest
		digest.update(input.getBytes(), 0, input.length());

		//Converts message digest value in base 16 (hex) 
		md5 = new BigInteger(1, digest.digest()).toString(8);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
                //System.out.println(md5);
		return md5;
	}
    public static void encrypt(InputStream in,OutputStream out,String password,String file){
        try{
           /**********************************************/
            Path path = Paths.get(file);
            byte[] data = Files.readAllBytes(path);
            //JOptionPane.showMessageDialog(null,data.toString());
           // byte [] temp = new byte[32];
             byte[] iv = new byte[IV_LENGTH];
                byte [] temp = new byte[16];
        
            temp = md5(password).getBytes();
            byte [] keys = new byte[32];
        
           
                for(int x=113;x<=144;x++){
                    keys[x-113]=data[x];
                    //System.out.print(keys[x-5]+" ");
                 }
            
            //System.out.println("Key length is : " + keys.length);
           /**********************************************/
        
       // SecureRandom r = new SecureRandom();
        
       
        System.out.println(md5(password).getBytes().length);
        //temp = md5(temp.toString()).getBytes();
        for(int x=0;x<=15;x++){
            iv[x]=temp[x];
            
            
        }
        //System.out.println(iv);
        //System.out.println("IV length is " +iv.length);
        /*r.nextBytes(iv);
        out.write(iv); //write IV as a prefix
	out.flush();
    */     
        
        Cipher cipher= Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keys, "AES");
        //System.out.println("KeySpec length is "+keySpec.getEncoded().length);
        IvParameterSpec IvSpec = new IvParameterSpec(iv);
        //System.out.println("IVSpec length is "+IvSpec.getIV().length);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec,IvSpec);
        
        out = new CipherOutputStream(out,cipher);
        int numRead = 0;
        byte[] buf = new byte[1024];
        while((numRead=in.read(buf))>=0){
            out.write(buf,0,numRead);
        }
        out.close();
        
        }catch(Exception ex){
            //ex.getMessage();
            
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Erro",0);
            //out.close();
        }
    }
    
    
    public static int decrypt(InputStream in, OutputStream out, String password,String file){
                try{
                    /**********************************************/
                    Path path = Paths.get(file);
                    byte[] data = Files.readAllBytes(path);
                    //JOptionPane.showMessageDialog(null,data.toString());
                    // byte [] temp = new byte[32];
                     byte [] keys = new byte[32];
                     byte[] iv = new byte[IV_LENGTH];
                //in.read(iv);
                     byte [] temp = new byte[16];
                     temp = md5(password).getBytes();
                     
                     
                    for(int x=113;x<=144;x++){
                    keys[x-113]=data[x];
                    //System.out.print(keys[x-5]+" ");
                 }
            //JOptionPane.showMessageDialog(null,keys.length);
            
           /**********************************************/
                
		
                for(int x=0;x<=15;x++){
                    iv[x]=temp[x];
            
                }
		//System.out.println(">>>>>>>>red"+Arrays.toString(iv));
                
                
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //"DES/ECB/PKCS5Padding";"AES/CBC/PKCS5Padding"
		SecretKeySpec keySpec = new SecretKeySpec(keys, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		in = new CipherInputStream(in, cipher);
		byte[] buf = new byte[1024];
		int numRead = 0;
		while ((numRead = in.read(buf)) >= 0) {
			out.write(buf, 0, numRead);
		}
		out.close();
                
                return 1;
                }catch(Exception ex){
                    //ex.getMessage();
                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Erro",0);
                    return -1;
                   
                    
                }
	}
    
    
    public static void copy(int mode, String inputFile, String outputFile, String password,String file) throws Exception {
                int x=0;
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inputFile));
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile));
		if(mode==Cipher.ENCRYPT_MODE){
			encrypt(is, os, password,file);
                        x=2;
		}
		else if(mode==Cipher.DECRYPT_MODE){
                        x=0;
			x=decrypt(is, os, password,file);
		}
		else throw new Exception("unknown mode");
                if(x==-1||x==0){
                    JOptionPane.showMessageDialog(null,"Decryption Failed","Failed",0);
                }else {
                    if(x!=2){
                        //JOptionPane.showMessageDialog(null,"Decryption Done","Done",1);
                    }
                }
		is.close();
		os.close();
	}
    
    
    
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FileOpen1 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Encryptor");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButton1.setText("Encrypt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Decrypt");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jProgressBar1.setName(""); // NOI18N
        jProgressBar1.setStringPainted(true);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/encrypt/newpackage/f78c.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/encrypt/newpackage/Mary-Mother-of-God.png"))); // NOI18N
        jLabel2.setText("jLabel2");

        jLabel3.setText("Altair");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(77, 77, 77)))
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(55, 55, 55)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void input1(){
   JPasswordField pf = new JPasswordField();
   JPasswordField pf_1 = new JPasswordField();
   
   //JFileChooser fileopen = new JFileChooser();
   int fileinput_1;
   
   JFileChooser inputfile = new JFileChooser();
   inputfile.setDialogTitle("Select the File");
   fileinput_1 = inputfile.showOpenDialog(null);
   
   if(fileinput_1 != JFileChooser.CANCEL_OPTION){
       jProgressBar1.setValue(5);
       
       File filepath_1 = inputfile.getSelectedFile();
       String filefullpaths = filepath_1.getAbsolutePath();
       
   
   int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
   
    if (okCxl == JOptionPane.OK_OPTION && new String(pf.getPassword()).isEmpty()==false) {
        jProgressBar1.setValue(10);
        String password_1 = new String(pf.getPassword());
       // System.err.println("You entered: " + password_1);
        
        int okCx2 = JOptionPane.showConfirmDialog(null, pf_1, "Enter Password Again", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        String password_2 = new String(pf_1.getPassword());
        if(okCx2==JOptionPane.OK_OPTION&& new String(pf_1.getPassword()).isEmpty() ==false&&password_1.equals(password_2)){
            jProgressBar1.setValue(15);
            int Fileinput;
          //  System.err.println("You entered: " + password_2);
            JFileChooser inputcert = new JFileChooser();
            
            FileNameExtensionFilter cerfilter = new FileNameExtensionFilter(
            "certificate files (*.cer)", "cer");
            FileNameExtensionFilter certfilter = new FileNameExtensionFilter(
            "certificate files (*.cert)", "cert");
            inputcert.addChoosableFileFilter(cerfilter);
            inputcert.addChoosableFileFilter(certfilter);
            inputcert.setFileFilter(cerfilter);
            inputcert.setDialogTitle("Select the Key");
            
            Fileinput = inputcert.showOpenDialog(null);
            if(Fileinput!=JFileChooser.CANCEL_OPTION){
                jProgressBar1.setValue(20);
               File certpath = inputcert.getSelectedFile();
               String certpaths = certpath.getAbsolutePath();
               
                //System.err.println("You entered: " + password_2 +" "+certpaths+" "+filefullpaths);
                try {
                    String fname =  filepath_1.getName();
                    intozip(filefullpaths,fname);
                    jProgressBar1.setValue(50);
                    copy(Cipher.ENCRYPT_MODE,filefullpaths+".zip",filefullpaths+".enc",password_2,certpaths);
                    jProgressBar1.setValue(60);
                    File delfile = new File(filepath_1.getAbsolutePath()+".zip");
                    delfile.delete();
                    jProgressBar1.setValue(100);
                    JOptionPane.showMessageDialog(null,"Encryption Done","Done",1);
                    //copy(Cipher.ENCRYPT_MODE,"K:\\testing\\test.txt","K:\\testing\\testenc.txt","1234567812345678","C:\\Users\\Alex\\Desktop 3\\Copy.cer");
                } catch (Exception ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                jProgressBar1.setValue(0);
                JOptionPane.showMessageDialog(null,"Encryption cancelled","Canceled",1);
            }
        }else{
            jProgressBar1.setValue(0);
            JOptionPane.showMessageDialog(null,"Password Not match","Erro",0);
        }
    }else{
        if(okCxl != JOptionPane.CANCEL_OPTION){
            jProgressBar1.setValue(0);
        JOptionPane.showMessageDialog(null,"Password Field Can't be Empty","Erro",0);
        }else{
            jProgressBar1.setValue(0);
            JOptionPane.showMessageDialog(null,"Encryption cancelled","Canceled",1);
        }
    }
   }else{
       jProgressBar1.setValue(0);
       JOptionPane.showMessageDialog(null,"Encryption cancelled","Canceled",1);
   }   
    
    
}
    

public void output1(){
   JPasswordField pf = new JPasswordField();
   JPasswordField pf_1 = new JPasswordField();
   
   //JFileChooser fileopen = new JFileChooser();
   int fileinput_1;
   
   JFileChooser inputfile = new JFileChooser();
   FileNameExtensionFilter filefilters = new FileNameExtensionFilter(
            "Encrypted files (*.enc)", "enc");
   inputfile.setFileFilter(filefilters);
   inputfile.setDialogTitle("Select the File");
   fileinput_1 = inputfile.showOpenDialog(null);
   if(fileinput_1 != JFileChooser.CANCEL_OPTION){
       jProgressBar1.setValue(10);
       
       File filepath_1 = inputfile.getSelectedFile();
       String filefullpaths = filepath_1.getAbsolutePath();
   
   int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
   
    if (okCxl == JOptionPane.OK_OPTION && new String(pf.getPassword()).isEmpty()==false) {
        jProgressBar1.setValue(20);
        String password_1 = new String(pf.getPassword());
        //System.err.println("You entered: " + password_1);
        
        
            int Fileinput;
            
            JFileChooser inputcert = new JFileChooser();
            
            FileNameExtensionFilter cerfilter = new FileNameExtensionFilter(
            "certificate files (*.cer)", "cer");
            FileNameExtensionFilter certfilter = new FileNameExtensionFilter(
            "certificate files (*.cert)", "cert");
            inputcert.addChoosableFileFilter(cerfilter);
            inputcert.addChoosableFileFilter(certfilter);
            inputcert.setFileFilter(cerfilter);
            inputcert.setDialogTitle("Select the Key");
            
            Fileinput = inputcert.showOpenDialog(null);
            if(Fileinput!=JFileChooser.CANCEL_OPTION){
               jProgressBar1.setValue(30);
               File certpath = inputcert.getSelectedFile();
               String certpaths = certpath.getAbsolutePath();
               
        //        System.err.println("You entered: " + password_1 +" "+certpaths+" "+filefullpaths);
                try {
                    copy(Cipher.DECRYPT_MODE,filefullpaths,filefullpaths.replace(".enc", ".zip"),password_1,certpaths);
                    jProgressBar1.setValue(60);
                    //copy(Cipher.ENCRYPT_MODE,"K:\\testing\\test.txt","K:\\testing\\testenc.txt","1234567812345678","C:\\Users\\Alex\\Desktop 3\\Copy.cer");
                    String folder = filepath_1.getParent();
                    outzip(filefullpaths.replace(".enc", ".zip"),folder);
                    File delfile = new File(filefullpaths.replace(".enc", ".zip"));
                    delfile.delete();
                    if(checks){
                        jProgressBar1.setValue(100);
                        JOptionPane.showMessageDialog(null,"Decryption Done","Successful",1);
                        
                    }else{
                        jProgressBar1.setValue(0);
                        JOptionPane.showMessageDialog(null,"Decryption Fail","Fail",0);
                     }
                    //JOptionPane.showMessageDialog(null,filefullpaths.replace(".enc", ".zip"),"Canceled",1);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,ex,"Canceled",1);
                    JOptionPane.showMessageDialog(null,"fail","Canceled",1);
                }
            }else{
                jProgressBar1.setValue(0);
                JOptionPane.showMessageDialog(null,"Decryption cancelled","Canceled",1);
            }
        
    }else{
        if(okCxl != JOptionPane.CANCEL_OPTION){
        jProgressBar1.setValue(0);
        JOptionPane.showMessageDialog(null,"Password Field Can't be Empty","Erro",0);
        }else{
            jProgressBar1.setValue(0);
            JOptionPane.showMessageDialog(null,"Decryption cancelled","Canceled",1);
        }
    }
   }else{
       jProgressBar1.setValue(0);
       JOptionPane.showMessageDialog(null,"Decryption cancelled","Canceled",1);
   }   
    
    
}

    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            jProgressBar1.setValue(0);
            input1();
            
            //copy(Cipher.ENCRYPT_MODE,"C:\\Users\\Alex\\Desktop 3\\Shihan_Mihiranga_ft_ThilinaR_Sandarenu_Wehena_Re-Make.mp3","C:\\Users\\Alex\\Desktop 3\\Shihan_Mihiranga_ft_ThilinaR_Sandarenu_Wehena_Re-Make.mp3.enc","12345678","C:\\Users\\Alex\\Desktop 3\\Copy.cer");
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
                jProgressBar1.setValue(0);
                output1();
                
           //copy(Cipher.DECRYPT_MODE,"C:\\Users\\Alex\\Desktop 3\\Shihan_Mihiranga_ft_ThilinaR_Sandarenu_Wehena_Re-Make.mp3.enc","C:\\Users\\Alex\\Desktop 3\\Shihan_Mihiranga_ft_ThilinaR_Sandarenu_Wehena_Re-Make.mp3","456","C:\\Users\\Alex\\Desktop 3\\Copy.cer");
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
        music();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JFileChooser FileOpen1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
