package daimaroo.bash;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Mostafa on 15/12/12.
 */
enum HashType{
    MD5,
    SHA1
}
public class FileHandler  implements java.io.Serializable{

    public String os_seprator;
    private String filepath;
    private String wanted_separator;
    private File fhfile;

    /**Set Values
     *
     * @param path
     * @param wanted_separator
     */
    public void setValues(String path,String wanted_separator){
        if (wanted_separator==null){
            wanted_separator="/";       //Default separator of java
        }
        this.filepath=path;
        this.setWantedSeparator(wanted_separator);
    }

    /**Get separatorChar() Of My os and save it in
     * Os Separator String
     */
    public void setSeparator(){
        Character separator= File.separatorChar;
        os_seprator=separator.toString();
    }

    /**Replace Our Seprator
     * With os_seprator
     * @param s
     */
    public void setFilePath(String s){
        filepath=filepath.replace(os_seprator, wanted_separator);
    }

    /**Set Wanted Seprator
     * @param w_separator
     */
    public void setWantedSeparator(String w_separator){
        this.wanted_separator =w_separator;
    }

    /**return boolean of file is directory or not
     * @return
     */
    public boolean fileIsDirectory(File fname){
        if(fname.isDirectory()){
            return true;
        }
        return false;
    }


    /**make New File with set path
     *
     */
    public void  newFile(){
        this.fhfile=new File(filepath);
    }


    /**Make New File With specific path
     *
     * @param path
     */
    public static Writer makeNewFile(String path){
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("filename.txt"), "utf-8"));
            return writer;
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
        return writer;
    }

    /**Get Name Of A File
     *
     * @return
     */
    public String getFileName(){
        String name=null;
        if(fhfile!=null)
            name= fhfile.getName();
        else{
            this.newFile();
            name=fhfile.getName();
        }
        return name;
    }


    /**Put FileReader for Reading From File
     * Please Close your variable end of complete process
     * @return
     */
    public Reader getReader(){
        FileReader fr;
        BufferedReader br;
        try {
            fr=new FileReader(this.fhfile);
            br=new BufferedReader(fr);
            return br;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**Get Extension Of file by type File
     *
     * @param file
     * @return
     */
    public static String extension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return name.substring(dotIndex + 1);
    }

    /**Give Extension Of File By type String
     *
     * @param fname
     * @return
     */
    public static String extension(String fname) {
        int dotIndex = fname.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return fname.substring(dotIndex + 1);
    }


    /** Get FileWriter writer in file input
     * *Warning ! In the end of function and when
     * function call ended you must close bw ==> bw.close or caller variable
     * @return
     */
    public Writer getWriter(){
        FileWriter fw;
        BufferedWriter bw;
        try{
            fw=new FileWriter(this.fhfile);
            bw=new BufferedWriter(fw);
            return bw;
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }


    /**Make A Directory By Given Path
     *
     * @param file
     */
    public static void makeDir(File file){
        if(!file.exists()){
            file.mkdir();
            System.out.println("Directory Was Created");
        }
    }


    /**Be Aware! and Be Careful ! This Function Will Delete Hole Of Directory!
     * Please Use This Less!  :)
     * @param file
     */
    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }


    public static Boolean removeFile(File Path){
        if(Path.exists()){
            Path.setExecutable(true);
            Path.setReadable(true);
            Path.setWritable(true);
            Path.delete();
            return true;
        }
        return false;
    }


    /**Get A File And Hash Type and Return String Of Hash
     *
     * @param file
     * @param type
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHashOfFile(File file,HashType type) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = null;
        if(type.equals(HashType.MD5)){
            md= MessageDigest.getInstance("MD5");
        }
        if(type.equals(HashType.SHA1)){
            md= MessageDigest.getInstance("SHA1");
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] dataBytes = new byte[2048];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        };

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        if(md==null){
            return null;
        }

        return sb.toString();
    }


    /**Make A Directory From Given List Of Pathes
     *
     * @param folderTree
     * @throws IOException
     */
    public static void makeDirByTree(List<String> folderTree) throws IOException {
        for(String filepath:folderTree){
            File file=new File(filepath);
            mkFileWithAbsolutePath(file);
        }
    }


    /**Make Whole Of A repository By It's --HashMap--
     *
     * @param files
     * @throws IOException
     */
    public static void mkRepository(HashMap<File,String> files) throws IOException {
        for(File file:files.keySet()){
            mkFileWithAbsolutePath(file);
        }
    }


    /**Make A Repository By It's --List-- Of Absolute file's Path String
     *
     * @param files
     * @throws IOException
     */
    public static void mkRepository(List<String> files) throws IOException {
        File file;
        for(String filepath:files){
            file=new File(filepath);
            mkFileWithAbsolutePath(file);
        }
    }


    /**Get File Path And Make File With Absolute Path
     *Be Carefule That The Last Path Must BE A File
     * @param file
     * @throws IOException
     */
    public static void mkFileWithAbsolutePath(File file){
        file.getParentFile().mkdirs();

        try {
            FileWriter writer = new FileWriter(file);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error:Cannot make this file!");
            e.printStackTrace();
        }
    }


    /**Get A File And Return List Of Parent Directories Of A File
     *
     * @param file
     * @return
     */
    public static HashMap<File,Integer> getParentsList(File file){
        HashMap<File,Integer> ParentDirectories=new HashMap<File,Integer>();
        Integer i=new Integer(0);
        while(file!=null){
            i++;
            file=file.getParentFile();
            if(file!=null){
                ParentDirectories.put(file,i);
            }
        }
        return ParentDirectories;
    }

    public static void findMasterFolder(){

    }


    /**Serialize An Object An Write To Given File Pass
     *
     * @param obj
     * @param file
     * @return
     */
    public static boolean writeObjectInFile(Object obj,File file){
        String Path=file.getAbsolutePath();
        if(file.exists()){
            System.out.println("Object File Was Deleted!!");
            file.delete();
        }
        try
        {

            FileOutputStream fileOut = new FileOutputStream(Path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.write(Integer.parseInt(""));
            out.writeObject(obj);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved To :"+file.getPath());
            return true;
        }catch(IOException i)
        {
            i.printStackTrace();
        }

        return false;

    }


    /**DeSerialize An Object From
     *
     * @param file
     * @return
     */
    public static Object readObjectInFile(File file) {
        Object obj=null;
        if(file.isFile()){
            try
            {
                System.out.println(file.getAbsolutePath());
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                System.out.println(file.getAbsoluteFile());
                obj=(Object) in.readObject();
                fileIn.close();
                in.close();
                System.out.printf("DeSerialized data is Done From:"+file.getPath());
                return obj;
            }catch(IOException i)
            {
                i.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("returned null :\\");
            System.out.println("Enter Address Of A File Not Folder Or Another! :\\");
        }
        return obj;

    }


    /**Clear Content Of A File
     *
     * @param file
     * @return
     */
    public static boolean clearContentOfAFile(File file){
        try{
            FileOutputStream fin=new FileOutputStream(file);
            fin.write(Integer.parseInt(""));
            fin.close();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }




    public static void copyFile(File afile,String Bfile) {
        InputStream inStream = null;
        OutputStream outStream = null;
        File bfile=new File(Bfile);
        try{
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            //delete the original file
            afile.delete();

            System.out.println("File is Moved successful!");

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void copyFile(File afile,File bfile) {
        if(afile.isDirectory()){
            bfile.mkdirs();
            return ;
        }
        InputStream inStream = null;
        OutputStream outStream = null;
        try{
            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            //delete the original file
            afile.delete();

            System.out.println("File is Moved successful!");

        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void moveFile(File src,String des) {
        try{
            if(src.renameTo(new File(des + src.getName()))){
                System.out.println("File is moved successful!");
            }else{
                System.out.println("File is failed to move!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void replaceNewFile(File oldFile,File newFile){
        String OldFileAddress=oldFile.getAbsolutePath();
        //In This Part YOu can Move Your File To A Directory
        FileHandler.removeFile(oldFile);
        File nfile=new File(OldFileAddress);
        FileHandler.copyFile(newFile,nfile);
    }


    public static void printHashMap(HashMap<File,String> hm,String HashMapName){
        System.out.println("\nName Of HashMap:" + HashMapName + "_____________________________Start\n");
        for(File file:hm.keySet()){
            System.out.println("File::"+file.getAbsoluteFile()+"##"+"MD5--> "+hm.get(file));
        }
        System.out.println("\nName Of HashMap:" + HashMapName + "_____________________________End\n");
    }

    public static void printArrayList(ArrayList<File> files,String Name){
        System.out.println("ArrayListName:"+Name+"_____________________________Starts\n");
        for(File file:files){
            System.out.println(file.getAbsoluteFile());
        }
        System.out.println("ArrayListName:"+Name+"_____________________________Ends\n");
    }



    /**HashMap Read Object
     *
     * @param file
     * @return
     */
    public static HashMap<String,Object> readHashMapInFile(File file) {
        HashMap<String,Object> hashmap = null;
        if (file.isFile()) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                hashmap =(HashMap<String,Object>) in.readObject();
                fileIn.close();
                in.close();
                System.out.printf("DeSerialized data is Done From:" + file.getPath());
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Enter Address Of A File Not Folder Or Another! :\\");
        }
        return hashmap;
    }
//    public static void uploadFile(File file,String url) throws IOException {
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost httpPostRequest = new HttpPost();
//        HttpPost httppost = new HttpPost(url);
//
//        try {
//            FileBody bin = new FileBody(new File("/home/ubuntu/cd.png"));
//            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
//            reqEntity.addTextBody("Filename",file.getCanonicalPath());
//            reqEntity.addPart("file-content", (ContentBody) file);
//            System.out.println("Requesting : " + httppost.getRequestLine());
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            String responseBody = httpClient.execute(httppost, responseHandler);
//            System.out.println("responseBody : " + responseBody);
//
//        } catch (ClientProtocolException e) {
//
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//        }
//    }
    public static String convertFileToString(File file) throws IOException {
        StringBuilder sb=new StringBuilder();
        FileInputStream fin=new FileInputStream(file);
        int c;
        while((c=fin.read())!=-1){
            sb.append((char)c);
        }
        String filest=sb.toString();
        fin.close();
//        System.out.println(filest);
        return filest;
    }
}
